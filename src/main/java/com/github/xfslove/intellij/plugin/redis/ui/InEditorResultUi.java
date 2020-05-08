package com.github.xfslove.intellij.plugin.redis.ui;

import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.execution.ui.RunnerLayoutUi.Factory;
import com.intellij.icons.AllIcons.Actions;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.BasicUndoableAction;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager;
import com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager.Properties;
import com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager.ResizePolicy;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.impl.view.FontLayoutService;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.border.CustomLineBorder;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import com.intellij.util.BooleanFunction;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import com.intellij.util.ui.EmptyIcon;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.MouseEventAdapter;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class InEditorResultUi {

  private final MultiMap<Editor, ContainerWeakRef> myContainers = new MultiMap<>();

  @Nullable
  public ResultConstructor getOrCreateResult(@NotNull Editor editor, int offset) {

    if (!editor.isDisposed() && EditorUtil.isRealFileEditor(editor)) {
      return this.getContainer(editor, offset);
    } else {
      return null;
    }
  }

  @NotNull
  private ResultConstructor getContainer(@NotNull Editor editor, int offset) {

    ResultConstructor found = this.findContainer(editor, offset);
    if (found != null) {

      return found;
    } else {
      final Disposable parent = Disposer.newDisposable();

      BooleanFunction<EditorResultsContainer> aliveChecker = c -> this.getUncollectedResults().contains(c);
      EditorResultsContainer container = new EditorResultsContainer(parent, (EditorEx) editor, aliveChecker, offset);
      final RunnerLayoutUi ui = container.getUi();
      ui.addListener(new ContentManagerListener() {


        @Override
        public void contentAdded(@NotNull ContentManagerEvent event) {
          
        }

        @Override
        public void contentRemoved(@NotNull ContentManagerEvent event) {

          if (ui.getContents().length == 0) {
            Disposer.dispose(parent);
          }

        }

        @Override
        public void contentRemoveQuery(@NotNull ContentManagerEvent event) {

        }

        @Override
        public void selectionChanged(@NotNull ContentManagerEvent event) {

        }
      }, parent);
      ContainerWeakRef reference = container.getWeakRef();
      this.myContainers.putValue(editor, reference);
      EditorUtil.disposeWithEditor(editor, parent);
      Disposer.register(parent, () -> {
        this.myContainers.remove(editor, reference);
        EditorResultsContainer c = reference.get();
        if (c != null) {
          c.clearStrongReferences();
        }

      });

      return container;
    }
  }

  @NotNull
  public Collection<? extends Result> getResults(@NotNull Editor editor) {

    return this.getUncollectedResults(editor);
  }

  @Nullable
  private ResultConstructor findContainer(@NotNull Editor editor, int offset) {

    Collection<ResultConstructor> containers = this.getUncollectedResults(editor);
    Iterator<ResultConstructor> iterator = containers.iterator();

    ResultConstructor container;
    do {
      if (!iterator.hasNext()) {
        return null;
      }

      container = iterator.next();
    } while (!container.isShown() || offset != container.getOffset());

    return container;
  }

  @NotNull
  private Collection<ResultConstructor> getUncollectedResults(@NotNull Editor editor) {

    List<ContainerWeakRef> refs = new ArrayList<>(this.myContainers.get(editor));

    return ContainerUtil.filter(ContainerUtil.mapNotNull(refs, InEditorResultUi::asResult), Result::isShown);
  }

  @NotNull
  private Collection<ResultConstructor> getUncollectedResults() {

    return ContainerUtil.mapNotNull(new ArrayList<>(this.myContainers.values()), InEditorResultUi::asResult);
  }

  @Nullable
  private static ResultConstructor asResult(@NotNull ContainerWeakRef ref) {
    EditorResultsContainer container = ref.get();
    if (container == null) {
      ref.dispose();
      return null;
    }

    if (ref.isOutdated()) {
      ref.dispose();
      return null;
    }

    if (!container.isShown()) {
      ref.dispose();
      return null;
    }

    return container;
  }

  private static int getEditorTextWidth(@NotNull EditorImpl editor) {

    FontMetrics metrics = editor.getFontMetrics(0);
    float spaceWidth = FontLayoutService.getInstance().charWidth2D(metrics, 32);
    return (int) spaceWidth * editor.getSettings().getRightMargin(editor.getProject());
  }

  private static int getLineEnd(@NotNull Editor editor, int offset) {

    return editor.getDocument().getLineEndOffset(editor.getDocument().getLineNumber(offset));
  }

  private static class ContainerWeakRef extends WeakReference<EditorResultsContainer> {
    private static final long LIFETIME = TimeUnit.MINUTES.toMillis(5L);
    private final Disposable myParent;
    private long myHiddenTime;

    ContainerWeakRef(@NotNull EditorResultsContainer referent, @NotNull Disposable parent) {

      super(referent);
      this.myHiddenTime = -1L;
      this.myParent = parent;
    }

    boolean isOutdated() {
      return this.myHiddenTime != -1L && System.currentTimeMillis() - this.myHiddenTime > LIFETIME;
    }

    void hidden() {
      this.myHiddenTime = System.currentTimeMillis();
    }

    void shown() {
      this.myHiddenTime = -1L;
    }

    void dispose() {
      Disposer.dispose(this.myParent);
    }

  }

  private static class RemoveCellAction extends AnAction {
    private final ResultConstructor myResult;

    RemoveCellAction(@NotNull ResultConstructor result) {

      super("Close Result");
      this.myResult = result;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {

      e.getPresentation().setEnabledAndVisible(this.myResult.isStable());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

      this.myResult.destroy();
    }
  }

  private static class CloseRenderer extends GutterIconRenderer {
    private final ResultConstructor myResult;
    private final RemoveCellAction myAction;
    private final Icon myIcon;

    CloseRenderer(@NotNull ResultConstructor result, int height) {
      this.myResult = result;
      this.myAction = new RemoveCellAction(this.myResult);
      this.myIcon = new CloseRenderer.MyIcon(height);
    }

    @Nullable
    @Override
    public AnAction getClickAction() {
      return this.myAction;
    }

    @Nullable
    @Override
    public String getTooltipText() {
      return this.myAction.getTemplateText();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      } else if (o != null && this.getClass() == o.getClass()) {
        CloseRenderer renderer = (CloseRenderer) o;
        return this.myResult.equals(renderer.myResult);
      } else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return this.myResult.hashCode();
    }

    @NotNull
    @Override
    public Icon getIcon() {

      return this.myResult.isStable() ? this.myIcon : EmptyIcon.ICON_0;
    }

    private static class MyIcon implements Icon {
      private static final Icon CLOSE = Actions.Close;
      private final int myOffset;

      MyIcon(int offset) {
        this.myOffset = offset;
      }

      @Override
      public int getIconHeight() {
        return CLOSE.getIconHeight() + this.myOffset;
      }

      @Override
      public int getIconWidth() {
        return CLOSE.getIconWidth();
      }

      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        CLOSE.paintIcon(c, g, x, y + this.myOffset);
      }
    }
  }

  private static class PanelBorder extends CustomLineBorder {
    private final Editor myEditor;

    PanelBorder(@NotNull Editor editor) {

      super(JBUI.insets(1, 0, 1, 1));
      this.myEditor = editor;
    }

    @Override
    protected Color getColor() {
      return this.myEditor.getColorsScheme().getColor(EditorColors.RIGHT_MARGIN_COLOR);
    }
  }

  private static class EditorResultsContainer implements ResultConstructor {
    private final Disposable myParent;
    private final BooleanFunction<EditorResultsContainer> myIsAlive;
    private final ContainerWeakRef myWeakRef;
    private final AtomicLong myLastScrollEvent;
    private final EditorResultsContainer.MyScrollBarListener myScrollBarListener;
    private EditorEx myEditor;
    private RunnerLayoutUi myUi;
    private boolean myShown;
    private Inlay<?> myInlay;
    private int myInitialOffset;
    private EditorResultsContainer.ParentDisposalController myParentController;
    private boolean myInProgress;

    EditorResultsContainer(@NotNull Disposable parent, @NotNull EditorEx editor, @NotNull BooleanFunction<EditorResultsContainer> isAlive, int initialOffset) {
      this.myParent = parent;
      this.myEditor = editor;
      this.myIsAlive = isAlive;
      this.myInitialOffset = initialOffset;
      this.myLastScrollEvent = new AtomicLong();
      this.myScrollBarListener = new EditorResultsContainer.MyScrollBarListener(this.myLastScrollEvent);
      this.myUi = Factory.getInstance(Objects.requireNonNull(editor.getProject())).create("not_persistent_id", "Results", "Results", parent);
      this.myUi.getDefaults().initTabDefaults(0, "not_persistent_id", (Icon) null);
      this.myUi.getOptions().setMoveToGridActionEnabled(false).setMinimizeActionEnabled(false);
      this.myShown = true;
      this.myWeakRef = new ContainerWeakRef(this, parent);
    }

    @Override
    public int getOffset() {
      return this.myInlay != null ? this.myInlay.getOffset() : this.myInitialOffset;
    }

    @Override
    public void setInProgress(boolean progress) {
      this.myInProgress = progress;
    }

    @Override
    public void hide() {
      if (this.myInlay != null) {
        this.justHide();
        this.perform(new BasicUndoableAction(this.myEditor.getDocument()) {
          @Override
          public void undo() {
            EditorResultsContainer.this.justShow();
          }

          @Override
          public void redo() {
            EditorResultsContainer.this.justHide();
          }
        }, "Hide Results");
      }
    }

    @Override
    public void show() {
      if (this.myInlay == null) {
        this.justShow();
        this.perform(new BasicUndoableAction(this.myEditor.getDocument()) {
          @Override
          public void redo() {
            EditorResultsContainer.this.justShow();
          }

          @Override
          public void undo() {
            EditorResultsContainer.this.justHide();
          }
        }, "Show Results");
      }
    }

    @Override
    public void saveBeforeChanges() {
      this.justHide();
    }

    @Override
    public void restoreAfterChanges() {
      this.justShow();
    }

    @Override
    public void destroy() {
      this.myParentController.run(() -> {
        Disposer.dispose(this.myParent);
      });
    }

    @NotNull
    ContainerWeakRef getWeakRef() {

      return this.myWeakRef;
    }

    private void perform(@NotNull UndoableAction action, @Nls @NotNull String name) {

      CommandProcessor instance = CommandProcessor.getInstance();
      Project project = this.myEditor.getProject();
      instance.executeCommand(project, () -> UndoManager.getInstance(Objects.requireNonNull(project)).undoableActionPerformed(action), name, null);
    }

    private void justHide() {
      if (this.myIsAlive.fun(this) && this.myInlay != null) {
        this.myEditor.getScrollPane().getVerticalScrollBar().removeAdjustmentListener(this.myScrollBarListener);
        this.myInitialOffset = this.myInlay.getOffset();
        this.myParentController.run(() -> Disposer.dispose(this.myInlay));
        this.myParentController = null;
        this.myInlay = null;
        this.myShown = false;
        this.myWeakRef.hidden();
      }
    }

    private void justShow() {
      if (this.myIsAlive.fun(this) && this.myInlay == null) {
        JComponent component = this.myUi.getComponent();
        component.setBorder(new PanelBorder(this.myEditor));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        JLayeredPane pane = new EditorResultsContainer.MyJLayeredPane();
        if (this.myEditor instanceof EditorImpl) {
          pane.setPreferredSize(new Dimension(getEditorTextWidth((EditorImpl) this.myEditor) + 1, 100));
        }

        pane.add(panel);
        pane.setLayer(panel, JLayeredPane.DEFAULT_LAYER);
        JPanel transparentPanel = new EditorResultsContainer.EventTransparentPanel(this.myLastScrollEvent);
        pane.add(transparentPanel);
        pane.setLayer(transparentPanel, JLayeredPane.DEFAULT_LAYER + 1);
        int rowHeight = (int) ((double) this.myEditor.getLineHeight() * 0.35D);
        panel.add(Box.createVerticalStrut(rowHeight), BorderLayout.NORTH);
        panel.setOpaque(false);
        transparentPanel.addMouseWheelListener(new EditorResultsContainer.MyMouseWheelListener(transparentPanel, panel, this.myEditor, this.myLastScrollEvent));
        this.myEditor.getScrollPane().getVerticalScrollBar().addAdjustmentListener(this.myScrollBarListener);
        int leOffset = getLineEnd(this.myEditor, this.myInitialOffset);
        Properties properties = new Properties(
            ResizePolicy.any(),
            r -> new CloseRenderer(this, rowHeight),
            true, false, 0, leOffset);
        Inlay<?> inlay = (Inlay<?>) Objects.requireNonNull(EditorEmbeddedComponentManager.getInstance().addComponent(this.myEditor, pane, properties));
        this.setInlay(inlay);
      }
    }

    @Override
    public boolean isStable() {
      return !this.myInProgress;
    }

    @Override
    public boolean isShown() {
      return this.myShown;
    }

    @Override
    @NotNull
    public RunnerLayoutUi getUi() {
      return this.myUi;
    }

    @Override
    @Nullable
    public Inlay<?> getInlay() {
      return this.myInlay;
    }

    private void setInlay(@NotNull Inlay<?> inlay) {

      this.myInlay = inlay;
      this.myShown = true;
      this.myParentController = new EditorResultsContainer.ParentDisposalController(this.myParent);
      Disposer.register(this.myParent, this.myInlay);
      Disposer.register(this.myInlay, this.myParentController);
      this.myWeakRef.shown();
    }

    void clearStrongReferences() {
      this.myUi = null;
      this.myInlay = null;
      this.myEditor = null;
    }

    private static class EventTransparentPanel extends JPanel {
      private final AtomicLong myTime;

      EventTransparentPanel(@NotNull AtomicLong time) {
        this.myTime = time;
        this.setOpaque(false);
      }

      @Override
      public boolean contains(int x, int y) {
        return System.currentTimeMillis() - this.myTime.get() < 100L && super.contains(x, y);
      }
    }

    private static class MyMouseWheelListener implements MouseWheelListener {
      private final JPanel myGlassPane;
      private final JPanel myPanel;
      private final Editor myEditor;
      private final AtomicLong myTime;

      MyMouseWheelListener(@NotNull JPanel glassPane, @NotNull JPanel panel, @NotNull Editor editor, @NotNull AtomicLong time) {
        this.myGlassPane = glassPane;
        this.myPanel = panel;
        this.myEditor = editor;
        this.myTime = time;
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (System.currentTimeMillis() - this.myTime.get() <= 100L || !redispatch(e, this.myGlassPane, this.myPanel)) {
          MouseEventAdapter.redispatch(e, this.myEditor.getContentComponent());
        }
      }

      static boolean redispatch(@NotNull MouseEvent e, @NotNull JComponent from, @NotNull JComponent to) {

        Point converted = SwingUtilities.convertPoint(from, e.getPoint(), to);
        Component deepest = UIUtil.getDeepestComponentAt(to, converted.x, converted.y);
        if (deepest == null) {
          return false;
        } else {
          MouseEventAdapter.redispatch(e, deepest);
          return true;
        }
      }
    }

    private static class MyScrollBarListener implements AdjustmentListener {
      private final AtomicLong myValue;

      MyScrollBarListener(@NotNull AtomicLong value) {
        this.myValue = value;
      }

      @Override
      public void adjustmentValueChanged(AdjustmentEvent e) {
        if (!e.getValueIsAdjusting()) {
          this.myValue.set(System.currentTimeMillis());
        }

      }
    }

    private static class MyJLayeredPane extends JLayeredPane {

      @Override
      public void doLayout() {
        Component[] components = this.getComponents();
        for (Component component : components) {
          component.setBounds(this.getBounds());
        }

      }
    }

    private static class ParentDisposalController implements Disposable {
      private final Disposable myParent;
      private boolean myDisposalInProgress;

      ParentDisposalController(@NotNull Disposable parent) {
        this.myParent = parent;
      }

      void run(@NotNull Runnable runnable) {

        if (!this.myDisposalInProgress) {
          this.myDisposalInProgress = true;

          try {
            runnable.run();
          } finally {
            this.myDisposalInProgress = false;
          }

        }
      }

      @Override
      public void dispose() {
        if (!this.myDisposalInProgress) {
          Disposer.dispose(this.myParent);
        }

      }
    }
  }

  public interface ResultConstructor extends Result {

    void setInProgress(boolean inProgress);
  }

  public interface Result {

    void hide();

    void show();

    void destroy();

    boolean isShown();

    boolean isStable();

    int getOffset();

    void saveBeforeChanges();

    void restoreAfterChanges();

    @NotNull
    RunnerLayoutUi getUi();

    @Nullable
    Inlay<?> getInlay();
  }
}
