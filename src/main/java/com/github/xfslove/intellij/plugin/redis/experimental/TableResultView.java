///*
// * Decompiled with CFR 0.149.
// *
// * Could not load the following classes:
// *  com.intellij.application.options.EditorFontsConstants
// *  com.intellij.database.model.DasColumn
// *  com.intellij.database.model.DasIndex
// *  com.intellij.database.model.DasObject
// *  com.intellij.database.model.DasTable
// *  com.intellij.database.model.MultiRef
// *  com.intellij.database.model.ObjectKind
// *  com.intellij.database.psi.DbDataSource
// *  com.intellij.database.util.DasUtil
// *  com.intellij.find.FindModel
// *  com.intellij.find.SearchSession
// *  com.intellij.icons.AllIcons$General
// *  com.intellij.ide.ui.UISettings
// *  com.intellij.ide.ui.UISettingsListener
// *  com.intellij.lang.Language
// *  com.intellij.openapi.Disposable
// *  com.intellij.openapi.actionSystem.ActionGroup
// *  com.intellij.openapi.actionSystem.ActionManager
// *  com.intellij.openapi.actionSystem.ActionPopupMenu
// *  com.intellij.openapi.application.ApplicationManager
// *  com.intellij.openapi.editor.colors.EditorColorsListener
// *  com.intellij.openapi.editor.colors.EditorColorsScheme
// *  com.intellij.openapi.editor.colors.EditorFontType
// *  com.intellij.openapi.util.ActionCallback
// *  com.intellij.openapi.util.Comparing
// *  com.intellij.openapi.util.Disposer
// *  com.intellij.openapi.util.Key
// *  com.intellij.openapi.util.Ref
// *  com.intellij.openapi.util.UserDataHolder
// *  com.intellij.openapi.util.UserDataHolderBase
// *  com.intellij.openapi.util.registry.Registry
// *  com.intellij.openapi.util.text.StringUtil
// *  com.intellij.openapi.wm.IdeFocusManager
// *  com.intellij.ui.CellRendererPanel
// *  com.intellij.ui.ComponentUtil
// *  com.intellij.ui.IdeBorderFactory
// *  com.intellij.ui.PopupMenuListenerAdapter
// *  com.intellij.ui.TableUtil
// *  com.intellij.ui.border.CustomLineBorder
// *  com.intellij.ui.components.JBLabel
// *  com.intellij.ui.components.Magnificator
// *  com.intellij.ui.popup.HintUpdateSupply
// *  com.intellij.ui.scale.JBUIScale
// *  com.intellij.ui.table.JBTable
// *  com.intellij.ui.table.JBTable$JBTableHeader
// *  com.intellij.util.Function
// *  com.intellij.util.IntIntFunction
// *  com.intellij.util.ObjectUtils
// *  com.intellij.util.containers.ContainerUtil
// *  com.intellij.util.containers.IntArrayList
// *  com.intellij.util.containers.JBIterable
// *  com.intellij.util.ui.UIUtil
// *  gnu.trove.TIntObjectHashMap
// *  gnu.trove.TIntObjectIterator
// *  gnu.trove.TIntObjectProcedure
// *  org.jetbrains.annotations.NotNull
// *  org.jetbrains.annotations.Nullable
// */
//package com.github.xfslove.intellij.plugin.redis;
//
//import com.github.xfslove.intellij.plugin.redis.experimental.JBTableWithResizableCells;
//import com.intellij.application.options.EditorFontsConstants;
//import com.intellij.find.FindModel;
//import com.intellij.find.SearchSession;
//import com.intellij.icons.AllIcons;
//import com.intellij.ide.ui.UISettings;
//import com.intellij.ide.ui.UISettingsListener;
//import com.intellij.lang.Language;
//import com.intellij.openapi.Disposable;
//import com.intellij.openapi.actionSystem.ActionGroup;
//import com.intellij.openapi.actionSystem.ActionManager;
//import com.intellij.openapi.actionSystem.ActionPopupMenu;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.editor.colors.EditorColorsListener;
//import com.intellij.openapi.editor.colors.EditorColorsScheme;
//import com.intellij.openapi.editor.colors.EditorFontType;
//import com.intellij.openapi.util.*;
//import com.intellij.openapi.util.registry.Registry;
//import com.intellij.openapi.util.text.StringUtil;
//import com.intellij.openapi.wm.IdeFocusManager;
//import com.intellij.ui.*;
//import com.intellij.ui.border.CustomLineBorder;
//import com.intellij.ui.components.JBLabel;
//import com.intellij.ui.components.Magnificator;
//import com.intellij.ui.popup.HintUpdateSupply;
//import com.intellij.ui.scale.JBUIScale;
//import com.intellij.ui.table.JBTable;
//import com.intellij.util.Function;
//import com.intellij.util.IntIntFunction;
//import com.intellij.util.ObjectUtils;
//import com.intellij.util.containers.ContainerUtil;
//import com.intellij.util.containers.IntArrayList;
//import com.intellij.util.containers.JBIterable;
//import com.intellij.util.ui.UIUtil;
//import gnu.trove.TIntObjectHashMap;
//import gnu.trove.TIntObjectIterator;
//import gnu.trove.TIntObjectProcedure;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.event.*;
//import javax.swing.table.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.List;
//import java.util.*;
//
//public class TableResultView
//extends JBTableWithResizableCells
//implements
//EditorColorsListener,
//UISettingsListener {
//    public static final Key<EventObject> EDITING_STARTER_CLIENT_PROPERTY_KEY = Key.create((String)"EventThatCausedEditingToStart");
//    private final TableResultPanel myResultPanel;
//    private final MyTableColumnCache myColumnCache;
//    private final Renderers myRenderers;
//    private final TableCellImageCache myCellImageCache;
//    private final GridCellEditorFactoryProvider myEditorProvider;
//    private final TableRawIndexConverter myRawIndexConverter;
//    private final ActionGroup myColumnHeaderPopupActions;
//    private final ActionGroup myRowHeaderPopupActions;
//    private final GridColumnLayout<DataConsumer.Row, DataConsumer.Column> myColumnLayout;
//    private final GridSelectionGrower myGrower;
//    private ModelIndex<DataConsumer.Column> myClickedHeaderColumnIdx;
//    private double myFontSizeIncrement;
//    private double myFontSizeScale;
//    private int myColumnsHashCode;
//    private PaintingSession myPaintingSession;
//    private Ref<Object> myCommonValue;
//
//    public TableResultView(TableResultPanel resultPanel, GridCellEditorFactoryProvider editorProvider, @NotNull ActionGroup columnHeaderPopupActions, @NotNull ActionGroup rowHeaderPopupActions) {
//        super(new GridTableModel.Regular(resultPanel), new MyTableColumnModel());
//        this.myFontSizeIncrement = 0.0;
//        this.myFontSizeScale = 1.0;
//        this.myResultPanel = resultPanel;
//        this.myCellImageCache = new TableCellImageCache(this, this.myResultPanel);
//        this.myEditorProvider = editorProvider;
//        this.myColumnHeaderPopupActions = columnHeaderPopupActions;
//        this.myRowHeaderPopupActions = rowHeaderPopupActions;
//        this.myRawIndexConverter = new TableRawIndexConverter(this);
//        this.myColumnCache = new MyTableColumnCache();
//        this.myRenderers = new Renderers(this.myResultPanel, this);
//        this.myClickedHeaderColumnIdx = ModelIndex.forColumn(this.myResultPanel, -1);
//        this.myColumnLayout = ApplicationManager.getApplication().isUnitTestMode() ? new DummyGridColumnLayout() : new DefaultGridColumnLayout(this, resultPanel);
//        this.myGrower = new GridSelectionGrower(this.myResultPanel, this);
//        this.getTableHeader().setDefaultRenderer(this.createHeaderRenderer());
//        new ResizableCellEditorsSupport(this);
//        this.setupFocusListener();
//        this.setupMagnificator();
//        this.setEnableAntialiasing(true);
//        HintUpdateSupply.installDataContextHintUpdateSupply((JComponent)((Object)this));
//        this.setFont(this.getColorsScheme().getFont(EditorFontType.PLAIN));
//        this.updateFonts();
//        this.adjustDefaultActions();
//        this.addPropertyChangeListener("tableCellEditor", e -> DataGridUtil.updateWidgets(resultPanel));
//        new TableSelectionModel(this, this.myResultPanel);
//        new TableGoToRowHelper(this, this.myResultPanel);
//        new TablePositionWidgetHelper(this, this.myResultPanel);
//        new TableScrollPositionManager(this, this.myResultPanel);
//        this.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
//
//            @Override
//            public void valueChanged(@NotNull ListSelectionEvent e) {
//                TableResultView.this.myGrower.reset();
//            }
//
//        });
//    }
//
//    @Override
//    @NotNull
//    public JComponent getComponent() {
//        TableResultView tableResultView = this;
//        return tableResultView;
//    }
//
//    @Override
//    public void setTransposed(boolean transposed) {
//        if (this.isTransposed() == transposed) {
//            return;
//        }
//        this.doTranspose();
//        this.createDefaultColumnsFromModel();
//    }
//
//    @Override
//    @NotNull
//    public JScrollBar getVerticalScrollBar() {
//        JScrollBar jScrollBar = ((JScrollPane)Objects.requireNonNull(ComponentUtil.getParentOfType(JScrollPane.class, (Component)((Object)this)))).getVerticalScrollBar();
//        return jScrollBar;
//    }
//
//    @Override
//    @NotNull
//    public JScrollBar getHorizontalScrollBar() {
//        JScrollBar jScrollBar = ((JScrollPane)Objects.requireNonNull(ComponentUtil.getParentOfType(JScrollPane.class, (Component)((Object)this)))).getHorizontalScrollBar();
//        return jScrollBar;
//    }
//
//    @Override
//    public void resetLayout() {
//        this.myColumnLayout.resetLayout();
//    }
//
//    @Override
//    public void growSelection() {
//        this.myGrower.growSelection();
//    }
//
//    @Override
//    public void addSelectionChangedListener(@NotNull Runnable listener2) {
//        this.getColumnModel().getSelectionModel().addListSelectionListener(e -> listener2.run());
//        this.getSelectionModel().addListSelectionListener(e -> listener2.run());
//    }
//
//    @Override
//    public void restoreColumnsOrder(Map<Integer, ModelIndex<DataConsumer.Column>> expectedToModel) {
//        if (this.isTransposed()) {
//            return;
//        }
//        IntIntFunction column2View = this.getRawIndexConverter().column2View();
//        int columnsCount = this.myResultPanel.getDataModel().getColumnCount();
//        for (int expectedPos = 0; expectedPos < columnsCount; ++expectedPos) {
//            int actualPos;
//            ModelIndex<DataConsumer.Column> modelIndex = expectedToModel.get(expectedPos);
//            if (modelIndex == null || (actualPos = column2View.fun(modelIndex.value)) == -1 || actualPos == expectedPos) continue;
//            this.moveColumn(actualPos, expectedPos);
//        }
//    }
//
//    public void setViewColumnVisible(ModelIndex<?> modelColumnIdx, boolean visible) {
//        ViewIndex<?> viewColumnIdx = modelColumnIdx.toView(this.myResultPanel);
//        if (visible && viewColumnIdx.asInteger() < 0) {
//            boolean firstTimeShown = !this.getColumnCache().hasCachedColumn(modelColumnIdx.asInteger());
//            this.addColumn(this.getColumnCache().getOrCreateColumn(modelColumnIdx.asInteger()));
//            int lastColumnIndex = this.getColumnCount() - 1;
//            this.myResultPanel.runWithIgnoreSelectionChanges(() -> {
//                for (int viewTargetColumnIdx = 0; viewTargetColumnIdx < lastColumnIndex; ++viewTargetColumnIdx) {
//                    if (this.getColumnModel().getColumn(viewTargetColumnIdx).getModelIndex() <= modelColumnIdx.asInteger()) continue;
//                    this.moveColumn(lastColumnIndex, viewTargetColumnIdx);
//                    break;
//                }
//            });
//            if (firstTimeShown) {
//                this.myColumnLayout.columnsShown(this.isTransposed() ? ModelIndexSet.forRows(this.myResultPanel, modelColumnIdx.asInteger()) : ModelIndexSet.forColumns(this.myResultPanel, modelColumnIdx.asInteger()));
//            }
//        } else if (!visible && viewColumnIdx.asInteger() >= 0) {
//            this.getTableHeader().setDraggedColumn(null);
//            this.myResultPanel.runWithIgnoreSelectionChanges(() -> this.getColumnModel().removeColumn(this.getColumnModel().getColumn(viewColumnIdx.asInteger())));
//        }
//    }
//
//    @NotNull
//    public ModelIndex<DataConsumer.Column> uiColumn(int uiColumn) {
//        int modelIndex = uiColumn < 1 || uiColumn >= this.myResultPanel.getVisibleColumnCount() + 1 ? -1 : this.getRawIndexConverter().column2Model().fun(uiColumn - 1);
//        ModelIndex<DataConsumer.Column> modelIndex2 = ModelIndex.forColumn(this.myResultPanel, modelIndex);
//        return modelIndex2;
//    }
//
//    @NotNull
//    public ModelIndex<DataConsumer.Row> uiRow(int uiRow) {
//        ModelIndex<DataConsumer.Row> modelIndex = ModelIndex.forRow(this.myResultPanel, uiRow < 1 ? -1 : uiRow - 1);
//        return modelIndex;
//    }
//
//    @Override
//    public void showFirstCell(int rowNumOnCurrentPage) {
//        this.myResultPanel.showCell(this.uiRow(rowNumOnCurrentPage), this.uiColumn(0));
//    }
//
//    @NotNull
//    private GridColorsScheme getColorsScheme() {
//        GridColorsScheme gridColorsScheme = this.myResultPanel.getColorsScheme();
//        return gridColorsScheme;
//    }
//
//    @Override
//    public void searchSessionUpdated() {
//        this.updateRowFilter();
//        this.getComponent().repaint();
//    }
//
//    @Override
//    public boolean isEditingCellMaximized() {
//        ResizableCellEditorsSupport resizableCellEditorsSupport = ResizableCellEditorsSupport.get((JTable)((Object)this));
//        return resizableCellEditorsSupport != null && resizableCellEditorsSupport.isEditingCellMaximized();
//    }
//
//    @Override
//    public boolean canMaximizeEditingCell() {
//        ResizableCellEditorsSupport resizableCellEditorsSupport = ResizableCellEditorsSupport.get((JTable)((Object)this));
//        return resizableCellEditorsSupport != null && resizableCellEditorsSupport.canMaximizeEditingCell();
//    }
//
//    @Override
//    public void maximizeEditingCell() {
//        ResizableCellEditorsSupport resizableCellEditorsSupport = ResizableCellEditorsSupport.get((JTable)((Object)this));
//        if (resizableCellEditorsSupport != null) {
//            resizableCellEditorsSupport.maximizeEditingCell();
//        }
//    }
//
//    @Nullable
//    public JComponent getCellRendererComponent(@NotNull ViewIndex<DataConsumer.Row> viewRow, @NotNull ViewIndex<DataConsumer.Column> viewColumn, boolean forDisplay) {
//        int column;
//        if (!viewRow.isValid(this.myResultPanel) || !viewColumn.isValid(this.myResultPanel)) {
//            return null;
//        }
//        int row = (this.isTransposed() ? viewColumn : viewRow).asInteger();
//        TableCellRenderer renderer = this.getCellRenderer(row, column = (this.isTransposed() ? viewRow : viewColumn).asInteger());
//        if (renderer == null) {
//            return null;
//        }
//        return (JComponent)this.prepareRenderer(renderer, row, column, forDisplay);
//    }
//
//    @Override
//    public void setColumnEnabled(@NotNull ModelIndex<DataConsumer.Column> columnIdx, boolean state) {
//        if (this.isTransposed()) {
//            this.getModel().fireTableDataChanged();
//        } else {
//            this.setViewColumnVisible(columnIdx, state);
//        }
//    }
//
//    @Override
//    public void setRowEnabled(@NotNull ModelIndex<DataConsumer.Row> rowIdx, boolean state) {
//        if (this.isTransposed()) {
//            this.setViewColumnVisible(rowIdx, state);
//        } else {
//            this.getModel().fireTableDataChanged();
//        }
//    }
//
//    @NotNull
//    protected MyCellRenderer createHeaderRenderer() {
//        return new MyCellRenderer(this);
//    }
//
//    public void startPaintingSession() {
//        this.myPaintingSession = new PaintingSession();
//    }
//
//    public void endPaintingSession() {
//        this.myPaintingSession = null;
//    }
//
//    private void dropCaches() {
//        this.myCellImageCache.reset();
//    }
//
//    public MyTableColumnCache getColumnCache() {
//        return this.myColumnCache;
//    }
//
//    @Override
//    @NotNull
//    public ModelIndex<DataConsumer.Column> getContextColumn() {
//        ModelIndex<DataConsumer.Column> modelIndex = this.myClickedHeaderColumnIdx;
//        return modelIndex;
//    }
//
//    @Override
//    public void updateSortKeysFromColumnAttributes() {
//        RowSorter rowSorter = this.getRowSorter();
//        if (rowSorter != null) {
//            rowSorter.setSortKeys(this.isTransposed() || this.myResultPanel.isSortViaOrderBy() ? null : this.createSortKeys());
//        }
//    }
//
//    @Override
//    public void orderingAndVisibilityChanged() {
//        this.getModel().fireTableDataChanged();
//    }
//
//    @NotNull
//    private List<RowSorter.SortKey> createSortKeys() {
//        if (this.isTransposed()) {
//            List list = ContainerUtil.emptyList();
//            return list;
//        }
//        TreeMap<Integer, DataConsumer.Column> sortOrderMap = this.myResultPanel.getSortOrderMap();
//        int orderIdx = 0;
//        Object[] keys = new RowSorter.SortKey[sortOrderMap.size()];
//        for (DataConsumer.Column column : sortOrderMap.values()) {
//            RowSorter.SortKey key = new RowSorter.SortKey(column.columnNum, this.myResultPanel.getSortOrder(column) < 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING);
//            keys[orderIdx++] = key;
//        }
//        ArrayList arrayList = ContainerUtil.newArrayList((Object[])keys);
//        return arrayList;
//    }
//
//    @Override
//    @NotNull
//    public RawIndexConverter getRawIndexConverter() {
//        TableRawIndexConverter tableRawIndexConverter = this.myRawIndexConverter;
//        return tableRawIndexConverter;
//    }
//
//    @Override
//    @NotNull
//    public DataGridSearchSession createSearchSession(@Nullable FindModel findModel) {
//        FindModel newFindModel;
//        FindModel findModel2 = newFindModel = findModel == null ? null : new FindModel();
//        if (findModel != null) {
//            newFindModel.copyFrom(findModel);
//        }
//        return new DataGridSearchSession(this.myResultPanel.getProject(), this.myResultPanel, newFindModel);
//    }
//
//    @Override
//    public void searchSessionStarted(@NotNull SearchSession searchSession) {
//        if (!(searchSession instanceof GridSearchSession)) {
//            return;
//        }
//        ((GridSearchSession)searchSession).addListener(this, this);
//    }
//
//    public void doTranspose() {
//        this.myColumnCache.retainColumns(ContainerUtil.emptyList());
//        this.setModel(this.isTransposed() ? new GridTableModel.Regular(this.myResultPanel) : new GridTableModel.Transposed(this.myResultPanel));
//        this.myRawIndexConverter.transpose();
//        this.myColumnLayout.setTransposed(this.isTransposed());
//        this.myResultPanel.updateSortKeysFromColumnAttributes();
//        this.getModel().fireTableDataChanged();
//    }
//
//    public void addNotify() {
//        super.addNotify();
//        int hash = this.computeColumnsHashCode();
//        if (hash == this.myColumnsHashCode) {
//            return;
//        }
//        this.layoutColumns().doWhenDone(() -> {
//            this.myColumnsHashCode = hash;
//        });
//    }
//
//    protected void paintComponent(@NotNull Graphics g) {
//        this.adjustCacheSize();
//        super.paintComponent(g);
//        this.paintCellsEffects(g);
//    }
//
//    private void adjustCacheSize() {
//        if (!this.myCellImageCache.isCacheEnabled()) {
//            return;
//        }
//        TableColumnModel columnModel = this.getColumnModel();
//        int columnCount = columnModel == null ? 0 : columnModel.getColumnCount();
//        int rowCount = this.getRowCount();
//        Rectangle visibleRect = this.getVisibleRect();
//        if (columnCount == 0 || rowCount == 0 || visibleRect.isEmpty()) {
//            return;
//        }
//        int minColumnWidth = Integer.MAX_VALUE;
//        for (int i2 = 0; i2 < columnCount; ++i2) {
//            TableColumn column = columnModel.getColumn(i2);
//            minColumnWidth = Math.min(minColumnWidth, column.getMinWidth());
//        }
//        int rowHeight = this.getRowHeight();
//        int rowsMax = rowHeight == 0 ? rowCount : Math.min(rowCount, (int)Math.ceil((float)visibleRect.height / (float)rowHeight));
//        rowsMax = Math.min(100, rowsMax);
//        int colsMax = minColumnWidth == 0 ? columnCount : Math.min(columnCount, (int)Math.ceil((float)visibleRect.width / (float)minColumnWidth));
//        colsMax = Math.min(30, colsMax);
//        int factor = Math.max(1, Registry.intValue((String)"database.grid.cache.factor"));
//        this.myCellImageCache.adjustCacheSize(rowsMax * colsMax * factor);
//    }
//
//    private void paintCellsEffects(Graphics g) {
//        Rectangle visibleArea = g.getClipBounds();
//        Point at = new Point((int)visibleArea.getMinX(), (int)visibleArea.getMinY());
//        int fromRow = this.rowAtPoint(at);
//        int fromColumn = this.columnAtPoint(at);
//        at.setLocation(visibleArea.getMaxX(), visibleArea.getMaxY());
//        int toRow = this.rowAtPoint(at);
//        int toColumn = this.columnAtPoint(at);
//        if (fromColumn == -1) {
//            fromColumn = 0;
//        }
//        if (toRow == -1) {
//            toRow = 0;
//        }
//        if (toColumn == -1) {
//            toColumn = this.getColumnCount() - 1;
//        }
//        if (fromRow == -1) {
//            toRow = this.getRowCount() - 1;
//        }
//        for (int row = fromRow; row <= toRow; ++row) {
//            for (int column = fromColumn; column <= toColumn; ++column) {
//                this.paintCellEffects(g, row, column);
//            }
//        }
//    }
//
//    private void paintCellEffects(Graphics g, int row, int column) {
//        CellAttributes attributes2 = this.myResultPanel.getMarkupModel().getCellAttributes(ViewIndex.forRow(this.myResultPanel, this.isTransposed() ? column : row).toModel(this.myResultPanel), ViewIndex.forColumn(this.myResultPanel, this.isTransposed() ? row : column).toModel(this.myResultPanel), this.getColorsScheme());
//        if (attributes2 != null) {
//            CellRenderingUtils.paintCellEffect(g, this.getCellRect(row, column, true), attributes2);
//        }
//    }
//
//    private int computeColumnsHashCode() {
//        int hash = 0;
//        List<DataConsumer.Column> columns = this.myResultPanel.getDataModel().getColumns();
//        for (DataConsumer.Column column : columns) {
//            hash = System.identityHashCode(column) + 31 * hash;
//        }
//        return hash;
//    }
//
//    @Override
//    public boolean isViewModified() {
//        for (int viewColumnIdx = 0; viewColumnIdx < this.getColumnCount(); ++viewColumnIdx) {
//            ViewIndex<Object> viewIndex = this.isTransposed() ? ViewIndex.forRow(this.myResultPanel, viewColumnIdx) : ViewIndex.forColumn(this.myResultPanel, viewColumnIdx);
//            ModelIndex<Object> modelIndex = viewIndex.toModel(this.myResultPanel);
//            if (!modelIndex.isValid(this.myResultPanel) || !viewIndex.isValid(this.myResultPanel) || modelIndex.asInteger() == viewIndex.asInteger()) continue;
//            return true;
//        }
//        for (MyTableColumn tableColumn : this.myColumnCache) {
//            if (tableColumn.isWidthSetByLayout()) continue;
//            return true;
//        }
//        int defaultRowHeight = this.getRowHeight();
//        for (int i2 = 0; i2 < this.getRowCount(); ++i2) {
//            if (this.getRowHeight(i2) == defaultRowHeight) continue;
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void contentLanguageUpdated(@NotNull ModelIndex<DataConsumer.Column> columnIdx, @NotNull Language language) {
//        int viewColumn;
//        if (this.isTransposed() ? this.getColumnCount() == 0 : this.getRowCount() == 0) {
//            return;
//        }
//        int viewRow = this.isTransposed() ? columnIdx.toView(this.myResultPanel).asInteger() : 0;
//        TableCellRenderer renderer = this.getCellRenderer(viewRow, viewColumn = this.isTransposed() ? 0 : columnIdx.toView(this.myResultPanel).asInteger());
//        if (renderer instanceof TableCellImageCache.CachingCellRendererWrapper) {
//            ((TableCellImageCache.CachingCellRendererWrapper)renderer).clearCache();
//        }
//        this.revalidate();
//        this.repaint();
//    }
//
//    private void setupFocusListener() {
//        this.addFocusListener(new FocusAdapter(){
//
//            @Override
//            public void focusGained(@NotNull FocusEvent e) {
//                Component component;
//                if (TableResultView.this.isEditing() && (component = TableResultView.this.getEditorComponent()) != null) {
//                    IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> IdeFocusManager.getGlobalInstance().requestFocus(component, true));
//                }
//            }
//
//        });
//    }
//
//    private void setupMagnificator() {
//        this.putClientProperty((Object)Magnificator.CLIENT_PROPERTY_KEY, (Object)new Magnificator(){
//
//            public Point magnify(double scale, Point at) {
//                Rectangle r1;
//                int column = TableResultView.this.columnAtPoint(at);
//                int row = TableResultView.this.rowAtPoint(at);
//                Rectangle rectangle = r1 = column < 0 || row < 0 ? TableResultView.this.getBounds() : TableResultView.this.getCellRect(row, column, true);
//                if (r1.width == 0 || r1.height == 0) {
//                    return at;
//                }
//                double xPerc = (double)(at.x - r1.x) / (double)r1.width;
//                double yPerc = (double)(at.y - r1.y) / (double)r1.height;
//                TableResultView.this.changeFontSize(0, scale);
//                Rectangle r2 = column < 0 || row < 0 ? TableResultView.this.getBounds() : TableResultView.this.getCellRect(row, column, true);
//                return new Point((int)((double)r2.x + (double)r2.width * xPerc), (int)((double)r2.y + (double)r2.height * yPerc));
//            }
//        });
//    }
//
//    private void adjustDefaultActions() {
//        AbstractAction disabledAction = new AbstractAction(){
//
//            @Override
//            public boolean isEnabled() {
//                return false;
//            }
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            }
//        };
//        ActionMap actionMap = this.getActionMap();
//        actionMap.put("selectPreviousRowCell", disabledAction);
//        actionMap.put("startEditing", disabledAction);
//    }
//
//    public void columnMarginChanged(ChangeEvent e) {
//        TableColumn resizingColumn;
//        JTableHeader tableHeader = this.getTableHeader();
//        TableColumn tableColumn = resizingColumn = tableHeader != null ? tableHeader.getResizingColumn() : null;
//        if (resizingColumn != null && this.autoResizeMode == 0) {
//            resizingColumn.setPreferredWidth(resizingColumn.getWidth());
//        }
//        this.resizeAndRepaint();
//    }
//
//    public String getToolTipText(@NotNull MouseEvent event) {
//        return "";
//    }
//
//    public void setRowHeight(int row, int rowHeight) {
//        JViewport rowHeader;
//        Container grandParent;
//        if (row < 0 || row >= this.getRowCount()) {
//            return;
//        }
//        super.setRowHeight(row, rowHeight);
//        Container parent2 = this.getParent();
//        if (parent2 instanceof JViewport && (grandParent = parent2.getParent()) instanceof JScrollPane && (rowHeader = ((JScrollPane)grandParent).getRowHeader()) != null) {
//            rowHeader.revalidate();
//            rowHeader.repaint();
//        }
//    }
//
//    @NotNull
//    protected JTableHeader createDefaultTableHeader() {
//        return new MyTableHeader();
//    }
//
//    @Override
//    public void columnsAdded(ModelIndexSet<DataConsumer.Column> columnIndices) {
//        List<DataConsumer.Column> columns = this.myResultPanel.getDataModel().getColumns();
//        if (!this.isTransposed()) {
//            this.getColumnCache().retainColumns(columns);
//            this.createDefaultColumnsFromModel();
//        }
//        this.getModel().columnsAdded(columnIndices);
//        this.dropCaches();
//        this.myColumnLayout.newColumnsAdded(columnIndices);
//    }
//
//    @Override
//    public void columnsRemoved(ModelIndexSet<DataConsumer.Column> columns) {
//        this.getModel().columnsRemoved(columns);
//        if (!this.isTransposed()) {
//            this.createDefaultColumnsFromModel();
//        }
//        this.dropCaches();
//    }
//
//    @Override
//    public void rowsAdded(ModelIndexSet<DataConsumer.Row> rows) {
//        this.getModel().rowsAdded(rows);
//        if (this.isTransposed()) {
//            this.createDefaultColumnsFromModel();
//        }
//        this.myColumnLayout.newRowsAdded(rows);
//    }
//
//    @Override
//    public void rowsRemoved(ModelIndexSet<DataConsumer.Row> rows) {
//        this.getModel().rowsRemoved(rows);
//        if (this.isTransposed()) {
//            this.createDefaultColumnsFromModel();
//        }
//    }
//
//    @Override
//    public void cellsUpdated(ModelIndexSet<DataConsumer.Row> rows, ModelIndexSet<DataConsumer.Column> columns) {
//        this.getModel().cellsUpdated(rows, columns);
//        this.myColumnLayout.newRowsAdded(rows);
//    }
//
//    public void dispose() {
//        this.removeEditor();
//    }
//
//    @Override
//    public void changeSelectedColumnsWidth(int delta) {
//        int[] columns = this.getSelectedColumns();
//        TableColumnModel columnModel = this.getColumnModel();
//        for (int column : columns) {
//            if (column < 0) continue;
//            GridColumn tableColumn = (GridColumn)((Object)columnModel.getColumn(column));
//            int width = tableColumn.getColumnWidth();
//            tableColumn.setColumnWidth(Math.max(0, width + delta));
//        }
//    }
//
//    @Override
//    public void resetRowHeights() {
//        int defaultRowHeight = this.getRowHeight();
//        for (int i2 = 0; i2 < this.getRowCount(); ++i2) {
//            if (this.getRowHeight(i2) == defaultRowHeight) continue;
//            this.setRowHeight(i2, defaultRowHeight);
//        }
//    }
//
//    public void setModel(@NotNull TableModel model) {
//        super.setModel(model);
//        TableRowSorter<TableModel> rowSorter = this.createRowSorter(model);
//        rowSorter.setMaxSortKeys(1);
//        rowSorter.setSortsOnUpdates(this.isSortOnUpdates());
//        DbUIUtil.invokeLater(() -> {
//            if (this.getRowSorter() == rowSorter) {
//                this.updateRowFilter();
//            }
//        });
//        this.setRowSorter(rowSorter);
//    }
//
//    public void updateRowFilter() {
//        DefaultRowSorter sorter = (DefaultRowSorter)this.getRowSorter();
//        sorter.setRowFilter(this.createFilter());
//    }
//
//    @NotNull
//    private RowFilter<TableModel, Integer> createFilter() {
//        final RowFilter baseFilter = this.isTransposed() ? new MyTransposedViewColumnFilter(this.myResultPanel) : new MySearchRowFilter(this.myResultPanel);
//        return new RowFilter<TableModel, Integer>(){
//
//            @Override
//            public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
//                if (!baseFilter.include(entry)) {
//                    return false;
//                }
//                int intIdx = entry.getIdentifier();
//                ModelIndex<Object> rowIdx = TableResultView.this.isTransposed() ? ModelIndex.forColumn(TableResultView.this.myResultPanel, intIdx) : ModelIndex.forRow(TableResultView.this.myResultPanel, intIdx);
//                return !TableResultView.this.myResultPanel.isRowFilteredOut(rowIdx);
//            }
//        };
//    }
//
//    @NotNull
//    public Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int column) {
//        return component;
//    }
//
//    @NotNull
//    public Component prepareRenderer(@NotNull TableCellRenderer renderer, int row, int column, boolean forDisplay) {
//        Component component = super.prepareRenderer(renderer, row, column);
//        ViewIndex<DataConsumer.Row> rowIdx = ViewIndex.forRow(this.myResultPanel, this.isTransposed() ? column : row);
//        ViewIndex<DataConsumer.Column> columnIdx = ViewIndex.forColumn(this.myResultPanel, this.isTransposed() ? row : column);
//        Component component2 = ResultViewWithCells.prepareComponent(component, this.myResultPanel, this, rowIdx, columnIdx, forDisplay);
//        return component2;
//    }
//
//    @Override
//    @NotNull
//    public Color getCellBackground(@NotNull ViewIndex<DataConsumer.Row> row, @NotNull ViewIndex<DataConsumer.Column> column, boolean selected) {
//        Color color = selected ? this.getSelectionBackground() : this.myResultPanel.getColorModel().getCellBackground(row.toModel(this.myResultPanel), column.toModel(this.myResultPanel));
//        return color;
//    }
//
//    @Override
//    @NotNull
//    public Color getCellForeground(boolean selected) {
//        Color color = selected ? this.getSelectionForeground() : this.getForeground();
//        return color;
//    }
//
//    public boolean isCellEditable(int row, int column) {
//        return this.myResultPanel.isCellEditingAllowed();
//    }
//
//    /*
//     * WARNING - Removed try catching itself - possible behaviour change.
//     */
//    public boolean editCellAt(int row, int column, EventObject e) {
//        ComponentUtil.putClientProperty((JComponent)((Object)this), EDITING_STARTER_CLIENT_PROPERTY_KEY, (Object)e);
//        try {
//            boolean bl = super.editCellAt(row, column, e);
//            return bl;
//        }
//        finally {
//            ComponentUtil.putClientProperty((JComponent)((Object)this), EDITING_STARTER_CLIENT_PROPERTY_KEY, null);
//        }
//    }
//
//    @Override
//    public void createDefaultColumnsFromModel() {
//        GridTableModel model = this.getModel();
//        if (model == null) {
//            return;
//        }
//        this.getTableHeader().setDraggedColumn(null);
//        ((MyTableColumnModel)this.getColumnModel()).removeAllColumns();
//        IntArrayList newColumnIndices = new IntArrayList();
//        for (int columnDataIdx = 0; columnDataIdx < model.getColumnCount(); ++columnDataIdx) {
//            boolean notShownEarlier = !this.myColumnCache.hasCachedColumn(columnDataIdx);
//            this.myColumnCache.getOrCreateColumn(columnDataIdx);
//            boolean enabled = this.isTransposed() || this.myResultPanel.isColumnEnabled(this.getColumn(columnDataIdx));
//            ModelIndex<Object> modelColumnIdx = this.isTransposed() ? ModelIndex.forRow(this.myResultPanel, columnDataIdx) : ModelIndex.forColumn(this.myResultPanel, columnDataIdx);
//            this.setViewColumnVisible(modelColumnIdx, enabled);
//            if (!notShownEarlier || !enabled) continue;
//            newColumnIndices.add(columnDataIdx);
//        }
//        if (!newColumnIndices.isEmpty()) {
//            ModelIndexSet<Object> dataIndices = this.isTransposed() ? ModelIndexSet.forRows(this.myResultPanel, newColumnIndices.toArray()) : ModelIndexSet.forColumns(this.myResultPanel, newColumnIndices.toArray());
//            this.myColumnLayout.columnsShown(dataIndices);
//        }
//    }
//
//    public int getScrollableUnitIncrement(@NotNull Rectangle visibleRect, int orientation, int direction) {
//        if (orientation == 1) {
//            return this.getColorsScheme().getEditorFontSize();
//        }
//        return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
//    }
//
//    public Font getFont() {
//        return this.myPaintingSession != null ? this.myPaintingSession.getFont() : (this.myResultPanel == null ? super.getFont() : this.doGetFont());
//    }
//
//    public void globalSchemeChange(@Nullable EditorColorsScheme scheme) {
//        this.updateFonts();
//    }
//
//    public void changeFontSize(int increment, double scale) {
//        int oldFontSize;
//        double newIncrement = this.myFontSizeIncrement * scale + (double)increment;
//        double newScale = this.myFontSizeScale * scale;
//        int newFontSize = this.fontSize(newIncrement, newScale);
//        if (newFontSize == (oldFontSize = this.getFont().getSize())) {
//            return;
//        }
//        this.myFontSizeIncrement = newIncrement;
//        this.myFontSizeScale = newScale;
//        this.updateFonts();
//    }
//
//    private void updateFonts() {
//        Font font = this.getFont();
//        this.setFont(font);
//        this.setRowHeight(this.getRowHeight());
//        JTableHeader tableHeader = this.getTableHeader();
//        if (tableHeader != null) {
//            tableHeader.setFont(this.getScaledFont(font));
//        }
//        this.layoutColumns();
//    }
//
//    public void uiSettingsChanged(@NotNull UISettings uiSettings) {
//        if (this.changeHeaderFont(uiSettings)) {
//            this.layoutColumns();
//        }
//    }
//
//    private boolean changeHeaderFont(UISettings settings) {
//        boolean normalMode;
//        boolean presentationMode;
//        JTableHeader header = this.getTableHeader();
//        if (settings == null || header == null || header.getFont() == null) {
//            return false;
//        }
//        int fontSize = header.getFont().getSize();
//        boolean bl = presentationMode = settings.getPresentationMode() && fontSize != settings.getPresentationModeFontSize();
//        if (presentationMode) {
//            header.setFont(header.getFont().deriveFont((float)settings.getPresentationModeFontSize()));
//        }
//        boolean bl2 = normalMode = !settings.getPresentationMode() && fontSize == settings.getPresentationModeFontSize();
//        if (normalMode) {
//            header.setFont(this.getScaledFont(header.getFont()));
//        }
//        return presentationMode || normalMode;
//    }
//
//    private int fontSize(double fontSizeIncrement, double fontSizeScale) {
//        int baseFontSize = this.getColorsScheme().getEditorFontSize();
//        int newFontSize = (int)Math.max(fontSizeScale * (double)baseFontSize + fontSizeIncrement, 8.0);
//        return Math.min(Math.max(EditorFontsConstants.getMaxEditorFontSize(), baseFontSize), newFontSize);
//    }
//
//    @Override
//    public int getRowHeight() {
//        return this.myPaintingSession != null ? this.myPaintingSession.getRowHeight() : this.doGetRowHeight();
//    }
//
//    public int getTextLineHeight() {
//        return (int)((float)this.getFontMetrics(this.getFont()).getHeight() * this.getColorsScheme().getLineSpacing());
//    }
//
//    public Color getSelectionForeground() {
//        return this.myPaintingSession != null ? this.myPaintingSession.getSelectionForeground() : (this.myResultPanel == null ? super.getSelectionForeground() : GridColorSchemeUtil.doGetSelectionForeground(this.getColorsScheme()));
//    }
//
//    public Color getSelectionBackground() {
//        return this.myPaintingSession != null ? this.myPaintingSession.getSelectionBackground() : (this.myResultPanel == null ? super.getSelectionBackground() : GridColorSchemeUtil.doGetSelectionBackground(this.getColorsScheme()));
//    }
//
//    public Color getForeground() {
//        return this.myPaintingSession != null ? this.myPaintingSession.getForeground() : (this.myResultPanel == null ? super.getBackground() : GridColorSchemeUtil.doGetForeground(this.getColorsScheme()));
//    }
//
//    public Color getBackground() {
//        return this.myPaintingSession != null ? this.myPaintingSession.getBackground() : (this.myResultPanel == null ? super.getForeground() : GridColorSchemeUtil.doGetBackground(this.getColorsScheme()));
//    }
//
//    public void setBackground(@NotNull Color bg) {
//    }
//
//    public Color getGridColor() {
//        return this.myPaintingSession != null ? this.myPaintingSession.getGridColor() : (this.myResultPanel == null ? super.getGridColor() : GridColorSchemeUtil.doGetGridColor(this.getColorsScheme()));
//    }
//
//    public void tableChanged(TableModelEvent e) {
//        if (this.myResultPanel == null) {
//            super.tableChanged(e);
//            return;
//        }
//        this.myResultPanel.getAutoscrollLocker().runWithLock(() -> super.tableChanged(e));
//        this.myResultPanel.fireContentChanged();
//    }
//
//    public GridTableModel getModel() {
//        return (GridTableModel)super.getModel();
//    }
//
//    public Object getValueAt(int row, int column) {
//        boolean commonValue = this.isEditing() && this.isCellSelected(row, column) && this.isMultiEditingAllowed();
//        return commonValue ? this.myCommonValue.get() : super.getValueAt(row, column);
//    }
//
//    @Override
//    public void setCommonEditorValue(@Nullable Object object) {
//        this.myCommonValue = Ref.create((Object)object);
//    }
//
//    public void removeEditor() {
//        try {
//            super.removeEditor();
//        }
//        finally {
//            this.myCommonValue = null;
//        }
//    }
//
//    @Override
//    public boolean isTransposed() {
//        return this.getModel() instanceof GridTableModel.Transposed;
//    }
//
//    private void onRowHeaderClicked(ModelIndex<DataConsumer.Row> rowIdx, MouseEvent e) {
//        if (e.getID() != 501) {
//            return;
//        }
//        if (this.myResultPanel.isHeaderSelecting() && this.isTransposed()) {
//            int tableViewColumnIdx = this.myRawIndexConverter.row2View().fun(rowIdx.value);
//            this.selectViewColumnInterval(tableViewColumnIdx, e);
//        }
//        if (e.isPopupTrigger() && this.myRowHeaderPopupActions != ActionGroup.EMPTY_GROUP) {
//            ActionPopupMenu popupMenu = ActionManager.getInstance().createActionPopupMenu("unknown", this.myRowHeaderPopupActions);
//            popupMenu.getComponent().show(e.getComponent(), e.getX(), e.getY());
//        }
//    }
//
//    private void onColumnHeaderClicked(ModelIndex<DataConsumer.Column> columnIdx, MouseEvent e) {
//        if (e.getID() != 500 && e.getButton() == 2 && e.getClickCount() == 1) {
//            if (columnIdx.value >= 0) {
//                this.myResultPanel.setColumnEnabled(columnIdx, false);
//            }
//            return;
//        }
//        if (e.isPopupTrigger()) {
//            if (this.myResultPanel.isHeaderSelecting() && !this.isTransposed()) {
//                int tableViewColumnIdx = this.myRawIndexConverter.column2View().fun(columnIdx.value);
//                this.selectViewColumnInterval(tableViewColumnIdx, e);
//            }
//            if (this.myColumnHeaderPopupActions != ActionGroup.EMPTY_GROUP) {
//                this.myClickedHeaderColumnIdx = columnIdx;
//                ActionPopupMenu popupMenu = ActionManager.getInstance().createActionPopupMenu("unknown", this.myColumnHeaderPopupActions);
//                popupMenu.getComponent().addPopupMenuListener((PopupMenuListener)new PopupMenuListenerAdapter(){
//
//                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//                        DbUIUtil.invokeLater(() -> TableResultView.this.myClickedHeaderColumnIdx = ModelIndex.forColumn(TableResultView.this.myResultPanel, -1));
//                    }
//                });
//                popupMenu.getComponent().show(e.getComponent(), e.getX(), e.getY());
//            }
//        }
//    }
//
//    private void selectViewColumnInterval(int viewColumn, @NotNull MouseEvent e) {
//        boolean interval = DbUIUtil.isIntervalModifierSet(e);
//        boolean exclusive = DbUIUtil.isExclusiveModifierSet(e);
//        TableSelectionModel selectionModel = (TableSelectionModel)ObjectUtils.tryCast(SelectionModel.get(this.myResultPanel, this), TableSelectionModel.class);
//        if (selectionModel == null) {
//            return;
//        }
//        if (interval) {
//            int lead = this.getColumnModel().getSelectionModel().getLeadSelectionIndex();
//            if (exclusive) {
//                selectionModel.addRowSelectionInterval(this.getRowCount() - 1, 0);
//                selectionModel.addColumnSelectionInterval(viewColumn, lead);
//            } else {
//                selectionModel.setRowSelectionInterval(this.getRowCount() - 1, 0);
//                selectionModel.setColumnSelectionInterval(viewColumn, lead);
//            }
//        } else if (exclusive) {
//            this.removeColumnSelectionInterval(viewColumn, viewColumn);
//        } else {
//            selectionModel.setRowSelectionInterval(this.getRowCount() - 1, 0);
//            selectionModel.setColumnSelectionInterval(viewColumn, viewColumn);
//        }
//    }
//
//    @Override
//    @Nullable
//    public GridColumn getLayoutColumn(@NotNull ModelIndex<?> column) {
//        return this.getLayoutColumn(column, column.toView(this.myResultPanel));
//    }
//
//    @Nullable
//    public GridColumn getLayoutColumn(@NotNull ModelIndex<?> column, @NotNull ViewIndex<?> viewColumnIdx) {
//        return viewColumnIdx.asInteger() != -1 ? this.getColumnCache().getOrCreateColumn(column.asInteger()) : null;
//    }
//
//    @Override
//    @NotNull
//    public ModelIndexSet<DataConsumer.Row> getVisibleRows() {
//        int rowCount = this.isTransposed() ? this.getModel().getColumnCount() : this.getModel().getRowCount();
//        int[] viewRowIndices = new int[rowCount];
//        for (int i2 = 0; i2 < rowCount; ++i2) {
//            viewRowIndices[i2] = i2;
//        }
//        ModelIndexSet<DataConsumer.Row> rowIndices = ViewIndexSet.forRows(this.myResultPanel, viewRowIndices).toModel(this.myResultPanel);
//        return this.validIndexSet(rowIndices, rowIndices1 -> ModelIndexSet.forRows(this.myResultPanel, rowIndices1));
//    }
//
//    @Override
//    @NotNull
//    public ModelIndexSet<DataConsumer.Column> getVisibleColumns() {
//        int visibleColumns = this.getViewColumnCount();
//        int[] viewIndices = new int[visibleColumns];
//        for (int i2 = 0; i2 < visibleColumns; ++i2) {
//            viewIndices[i2] = i2;
//        }
//        ModelIndexSet<DataConsumer.Column> columnIndices = ViewIndexSet.forColumns(this.myResultPanel, viewIndices).toModel(this.myResultPanel);
//        return this.validIndexSet(columnIndices, columnIndices1 -> ModelIndexSet.forColumns(this.myResultPanel, columnIndices1));
//    }
//
//    @Override
//    public int getViewColumnCount() {
//        return this.isTransposed() ? this.getRowCount() : this.getColumnCount();
//    }
//
//    @Override
//    public int getViewRowCount() {
//        return this.isTransposed() ? this.getColumnCount() : this.getRowCount();
//    }
//
//    @NotNull
//    private <T> ModelIndexSet<T> validIndexSet(@NotNull ModelIndexSet<T> indexSet, @NotNull Function<int[], ModelIndexSet<T>> factory) {
//        IntArrayList validIndices = new IntArrayList(indexSet.size());
//        for (ModelIndex idx : indexSet.asIterable()) {
//            if (!idx.isValid(this.myResultPanel)) continue;
//            validIndices.add(idx.asInteger());
//        }
//        ModelIndexSet modelIndexSet = (ModelIndexSet)factory.fun((Object)validIndices.toArray());
//        return modelIndexSet;
//    }
//
//    @Override
//    public boolean stopEditing() {
//        int[] arrn;
//        int[] arrn2;
//        TableCellEditor editor = this.getCellEditor();
//        if (editor == null) {
//            return true;
//        }
//        if (!this.isMultiEditingAllowed()) {
//            int[] arrn3 = new int[1];
//            arrn2 = arrn3;
//            arrn3[0] = this.getEditingColumn();
//        } else {
//            arrn2 = this.getSelectedColumns();
//        }
//        int[] columnDataIdx = Arrays.stream(arrn2).map(this::convertColumnIndexToModel).toArray();
//        if (!this.isMultiEditingAllowed()) {
//            int[] arrn4 = new int[1];
//            arrn = arrn4;
//            arrn4[0] = this.getEditingRow();
//        } else {
//            arrn = this.getSelectedRows();
//        }
//        int[] rowDataIdx = Arrays.stream(arrn).map(this::convertRowIndexToModel).toArray();
//        ModelIndexSet<DataConsumer.Row> myEditingRowIdx = ModelIndexSet.forRows(this.myResultPanel, this.isTransposed() ? columnDataIdx : rowDataIdx);
//        ModelIndexSet<DataConsumer.Column> myEditingColumnIdx = ModelIndexSet.forColumns(this.myResultPanel, this.isTransposed() ? rowDataIdx : columnDataIdx);
//        return this.myResultPanel.isSafeToUpdate(myEditingRowIdx, myEditingColumnIdx, editor.getCellEditorValue()) && editor.stopCellEditing();
//    }
//
//    @Override
//    public void cancelEditing() {
//        TableCellEditor editor = this.getCellEditor();
//        if (editor != null) {
//            editor.cancelCellEditing();
//        }
//    }
//
//    @Override
//    public boolean isCellEditingAllowed() {
//        return true;
//    }
//
//    @Override
//    public void editSelectedCell() {
//        int leadRow = this.getSelectionModel().getLeadSelectionIndex();
//        int leadColumn = this.getColumnModel().getSelectionModel().getLeadSelectionIndex();
//        if (leadRow == -1 || leadColumn == -1) {
//            return;
//        }
//        TableUtil.editCellAt((JTable)((Object)this), (int)leadRow, (int)leadColumn);
//    }
//
//    @Override
//    public boolean isMultiEditingAllowed() {
//        int[] selectedColumns = this.isTransposed() ? this.getSelectedRows() : this.getSelectedColumns();
//        int[] selectedRows = this.isTransposed() ? this.getSelectedColumns() : this.getSelectedRows();
//        ModelIndexSet<DataConsumer.Column> indexSet = ViewIndexSet.forColumns(this.myResultPanel, selectedColumns).toModel(this.myResultPanel);
//        List<DataConsumer.Column> columns = this.myResultPanel.getDataModel(DataAccessType.DATABASE_DATA).getColumns(indexSet);
//        DasTable dasTable = DataGridUtil.getDatabaseTable(this.myResultPanel);
//        JBIterable indices = dasTable == null ? JBIterable.empty() : DasUtil.getIndices((DasTable)dasTable);
//        DataConsumer.Column uniqueColumn = (DataConsumer.Column)ContainerUtil.find(columns, column -> {
//            DasColumn dasColumn = DataGridUtil.getDatabaseColumn((DataGrid)this.myResultPanel, column);
//            if (dasColumn == null || DasUtil.isPrimary((DasColumn)dasColumn) || DasUtil.isAutoGenerated((DasColumn)dasColumn)) {
//                return dasColumn != null;
//            }
//            for (DasIndex index2 : indices) {
//                if (!DasUtil.containsName((String)dasColumn.getName(), (MultiRef)index2.getColumnsRef()) || !index2.isUnique()) continue;
//                return true;
//            }
//            return false;
//        });
//        return this.myCommonValue != null && (uniqueColumn == null || selectedRows.length == 1) && this.canEditTogether(columns);
//    }
//
//    private boolean canEditTogether(@NotNull List<DataConsumer.Column> columns) {
//        DbDataSource system;
//        if ((system = DataGridUtil.getDatabaseSystem(this.myResultPanel)) == null) {
//            return true;
//        }
//        DomainRegistry registry = DomainRegistry.get(DataGridUtil.getDbms(this.myResultPanel), system.getVersion());
//        if (registry == null) {
//            return false;
//        }
//        for (int i2 = 0; i2 < columns.size(); ++i2) {
//            DataConsumer.Column outer = columns.get(i2);
//            Domain outerDomain = registry.getDomain(outer);
//            if (outerDomain == null) {
//                return false;
//            }
//            for (int j = i2 + 1; j < columns.size(); ++j) {
//                DataConsumer.Column inner = columns.get(j);
//                Domain innerDomain = registry.getDomain(inner);
//                if (innerDomain == null) {
//                    return false;
//                }
//                Compatibility compatibility = outerDomain.getCompatibility(innerDomain);
//                if (compatibility.isCompatible()) continue;
//                return false;
//            }
//        }
//        return true;
//    }
//
//    protected TableRowSorter<TableModel> createRowSorter(TableModel model) {
//        final GridTableModel m = this.getModel();
//        return new TableRowSorter<TableModel>((TableModel)m){
//            {
//                super(x0);
//                this.setModelWrapper(new DefaultRowSorter.ModelWrapper<TableModel, Integer>(){
//
//                    @Override
//                    public TableModel getModel() {
//                        return m;
//                    }
//
//                    @Override
//                    public int getColumnCount() {
//                        return m.getColumnCount();
//                    }
//
//                    @Override
//                    public int getRowCount() {
//                        return m.getRowCount();
//                    }
//
//                    @Override
//                    public Object getValueAt(int row, int column) {
//                        return TableResultView.this.getRow(row);
//                    }
//
//                    @Override
//                    public Integer getIdentifier(int row) {
//                        return row;
//                    }
//                });
//            }
//
//            @Override
//            public void toggleSortOrder(int columnDataIdx) {
//                if (TableResultView.this.isTransposed()) {
//                    return;
//                }
//                TableResultView.this.myResultPanel.getAutoscrollLocker().runWithLock(() -> TableResultView.this.myResultPanel.changeSortOrder(ModelIndex.forColumn(TableResultView.this.myResultPanel, columnDataIdx), null, false));
//            }
//
//            @Override
//            protected boolean useToString(int column) {
//                return false;
//            }
//
//            @Override
//            public Comparator<?> getComparator(int modelColumnIdx) {
//                Comparator<Object> comparator2 = null;
//                if (!TableResultView.this.isTransposed()) {
//                    DataConsumer.Column column = TableResultView.this.getColumn(modelColumnIdx);
//                    comparator2 = TableResultView.this.myResultPanel.getColumnAttributes().getComparator(column);
//                }
//                return comparator2 != null ? comparator2 : super.getComparator(modelColumnIdx);
//            }
//
//            @Override
//            public boolean isSortable(int columnDataIdx) {
//                DataConsumer.Column column = !TableResultView.this.isTransposed() ? TableResultView.this.getColumn(columnDataIdx) : null;
//                return column != null && TableResultView.this.myResultPanel.getColumnAttributes().getComparator(column) != null;
//            }
//        };
//    }
//
//    public TableCellEditor getCellEditor(int row, int column) {
//        ModelIndex<DataConsumer.Row> rowIdx = ViewIndex.forRow(this.myResultPanel, this.isTransposed() ? column : row).toModel(this.myResultPanel);
//        ModelIndex<DataConsumer.Column> columnIdx = ViewIndex.forColumn(this.myResultPanel, this.isTransposed() ? row : column).toModel(this.myResultPanel);
//        GridCellEditorFactory editorFactory = this.myEditorProvider.getEditorFactory(this.myResultPanel, rowIdx, columnIdx);
//        DataConsumer.Column dataColumn = this.myResultPanel.getDataModel(DataAccessType.DATABASE_DATA).getColumn(columnIdx);
//        return dataColumn != null && !DbImplUtil.isRowId(dataColumn) && editorFactory != null ? new GridTableCellEditor(this.myResultPanel, rowIdx, columnIdx, editorFactory) : null;
//    }
//
//    public TableCellRenderer getCellRenderer(int row, int column) {
//        TableCellRenderer renderer = this.myRenderers.getRenderer(row, column);
//        renderer = renderer == null ? super.getCellRenderer(row, column) : renderer;
//        return renderer == null ? null : this.myCellImageCache.wrapCellRenderer(renderer);
//    }
//
//    public void setValueAt(Object value2, int viewRowIdx, int viewColumnIdx) {
//        int[] arrn;
//        int[] arrn2;
//        boolean allowed = this.isMultiEditingAllowed();
//        if (allowed) {
//            arrn2 = this.getSelectedRows();
//        } else {
//            int[] arrn3 = new int[1];
//            arrn2 = arrn3;
//            arrn3[0] = viewRowIdx;
//        }
//        int[] rows = arrn2;
//        if (allowed) {
//            arrn = this.getSelectedColumns();
//        } else {
//            int[] arrn4 = new int[1];
//            arrn = arrn4;
//            arrn4[0] = viewColumnIdx;
//        }
//        int[] columns = arrn;
//        ViewIndexSet<DataConsumer.Row> rowsSet = ViewIndexSet.forRows(this.myResultPanel, this.isTransposed() ? columns : rows);
//        ViewIndexSet<DataConsumer.Column> columnsSet = ViewIndexSet.forColumns(this.myResultPanel, this.isTransposed() ? rows : columns);
//        this.myResultPanel.setValueAt(rowsSet, columnsSet, value2);
//    }
//
//    private Font doGetFont() {
//        return this.getScaledFont(this.getColorsScheme().getFont(EditorFontType.PLAIN));
//    }
//
//    private Font getScaledFont(Font font) {
//        return UISettings.getInstance().getPresentationMode() ? font : (font == null ? null : font.deriveFont((float)this.fontSize(this.myFontSizeIncrement, this.myFontSizeScale)));
//    }
//
//    private int doGetRowHeight() {
//        return this.getTextLineHeight() + this.getRowMargin();
//    }
//
//    public static Rectangle getLabelTextRect(JLabel label) {
//        Dimension pref = label.getPreferredSize();
//        Rectangle bounds = label.getBounds();
//        Insets insets = label.getInsets();
//        int w = Math.min(pref.width, bounds.width);
//        bounds.setSize(w - insets.left - insets.right, bounds.height - insets.top - insets.bottom);
//        bounds.translate(insets.left, insets.top);
//        return bounds;
//    }
//
//    @Nullable
//    private DataConsumer.Row getRow(int modelRowIdx) {
//        return this.myResultPanel.getDataModel(DataAccessType.DATABASE_DATA).getRow(ModelIndex.forRow(this.myResultPanel, modelRowIdx));
//    }
//
//    @Nullable
//    private DataConsumer.Column getColumn(int modelColumnIdx) {
//        return this.myResultPanel.getDataModel(DataAccessType.DATABASE_DATA).getColumn(ModelIndex.forColumn(this.myResultPanel, modelColumnIdx));
//    }
//
//    @NotNull
//    public static Icon getSortOrderIcon(int sortOrder) {
//        Icon icon = sortOrder < 0 ? AllIcons.General.ArrowUp : (sortOrder > 0 ? AllIcons.General.ArrowDown : AllIcons.General.ArrowSplitCenterV);
//        return icon;
//    }
//
//    @NotNull
//    public ActionCallback layoutColumns() {
//        this.myResultPanel.trueLayout();
//        ActionCallback callback = new ActionCallback();
//        DbUIUtil.invokeLater(() -> {
//            if (SwingUtilities.getWindowAncestor((Component)((Object)this)) != null && this.myColumnLayout.resetLayout()) {
//                callback.setDone();
//            }
//        });
//        ActionCallback actionCallback = callback;
//        return actionCallback;
//    }
//
//    public static String getSortOrderText(int sortOrder) {
//        return sortOrder != 0 ? String.valueOf(Math.abs(sortOrder)) : "";
//    }
//
//    private static class MySearchRowFilter
//    extends RowFilter<TableModel, Integer> {
//        final TableResultPanel myGrid;
//
//        MySearchRowFilter(@NotNull TableResultPanel grid) {
//            this.myGrid = grid;
//        }
//
//        @Override
//        public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
//            GridSearchSession searchSession = (GridSearchSession)ObjectUtils.tryCast((Object)this.myGrid.getSearchSession(), GridSearchSession.class);
//            if (searchSession == null || !searchSession.isFilteringEnabled() || StringUtil.isEmpty((String)searchSession.getFindModel().getStringToFind())) {
//                return true;
//            }
//            ModelIndex<DataConsumer.Row> row = ModelIndex.forRow(this.myGrid, (int)entry.getIdentifier());
//            return !this.myGrid.getVisibleColumns().asIterable().filter(column -> searchSession.isMatchedCell(row, column)).isEmpty();
//        }
//
//    }
//
//    private static class MyTransposedViewColumnFilter
//    extends RowFilter<TableModel, Integer> {
//        final DataGrid myGrid;
//
//        MyTransposedViewColumnFilter(@NotNull DataGrid grid) {
//            this.myGrid = grid;
//        }
//
//        @Override
//        public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
//            ModelIndex<DataConsumer.Column> column = ModelIndex.forColumn(this.myGrid, (int)entry.getIdentifier());
//            return column.isValid(this.myGrid) && this.myGrid.isColumnEnabled(column);
//        }
//
//    }
//
//    public class MyTableColumnCache
//    implements Iterable<MyTableColumn> {
//        private final TIntObjectHashMap<Entry> myColumnDataIndicesToEntries = new TIntObjectHashMap();
//
//        public boolean hasCachedColumn(int columnDataIdx) {
//            boolean isCached = this.hasValidEntry(columnDataIdx);
//            if (!isCached) {
//                this.myColumnDataIndicesToEntries.remove(columnDataIdx);
//                return false;
//            }
//            return true;
//        }
//
//        public MyTableColumn getOrCreateColumn(int columnDataIdx) {
//            Entry e = (Entry)this.myColumnDataIndicesToEntries.get(columnDataIdx);
//            if (!this.hasCachedColumn(columnDataIdx) || e == null) {
//                e = this.createEntry(columnDataIdx);
//                this.myColumnDataIndicesToEntries.put(columnDataIdx, (Object)e);
//            }
//            return e.myTableColumn;
//        }
//
//        @NotNull
//        private Entry createEntry(int columnDataIdx) {
//            MyTableColumn tableColumn = TableResultView.this.isTransposed() ? this.createTableColumnForRow(ModelIndex.forRow(TableResultView.this.myResultPanel, columnDataIdx)) : this.createTableColumnForColumn(ModelIndex.forColumn(TableResultView.this.myResultPanel, columnDataIdx));
//            Object columnData = this.getColumnData(columnDataIdx);
//            return new Entry(Objects.requireNonNull(columnData), tableColumn);
//        }
//
//        public void retainColumns(final Collection<? extends DataConsumer.Column> columnsToRetain) {
//            this.myColumnDataIndicesToEntries.retainEntries((TIntObjectProcedure)new TIntObjectProcedure<Entry>(){
//
//                public boolean execute(int columnDataIdx, @NotNull Entry e) {
//                    return columnsToRetain.contains(e.myColumnData);
//                }
//
//            });
//        }
//
//        @Override
//        public Iterator<MyTableColumn> iterator() {
//            return new Iterator<MyTableColumn>(){
//                private final TIntObjectIterator<Entry> myWrappee;
//                private Entry myCurrentEntry;
//                {
//                    this.myWrappee = MyTableColumnCache.this.myColumnDataIndicesToEntries.iterator();
//                }
//
//                @Override
//                public boolean hasNext() {
//                    this.advance();
//                    return this.myCurrentEntry != null;
//                }
//
//                @Override
//                public MyTableColumn next() {
//                    MyTableColumn column = this.myCurrentEntry.myTableColumn;
//                    this.myCurrentEntry = null;
//                    return column;
//                }
//
//                @Override
//                public void remove() {
//                    throw new UnsupportedOperationException();
//                }
//
//                private void advance() {
//                    this.myCurrentEntry = null;
//                    while (this.myWrappee.hasNext()) {
//                        this.myWrappee.advance();
//                        if (MyTableColumnCache.this.hasValidEntry(this.myWrappee.key())) {
//                            this.myCurrentEntry = (Entry)this.myWrappee.value();
//                            break;
//                        }
//                        this.myWrappee.remove();
//                    }
//                }
//            };
//        }
//
//        private boolean hasValidEntry(int columnDataIdx) {
//            Entry e = (Entry)this.myColumnDataIndicesToEntries.get(columnDataIdx);
//            if (e == null) {
//                return false;
//            }
//            Object cachedColumnData = e.myColumnData;
//            Object currentColumnData = this.getColumnData(columnDataIdx);
//            if (Comparing.equal((Object)cachedColumnData, (Object)currentColumnData)) {
//                return true;
//            }
//            if (cachedColumnData instanceof DataConsumer.Row && currentColumnData instanceof DataConsumer.Row) {
//                DataConsumer.Row cachedRow = (DataConsumer.Row)cachedColumnData;
//                DataConsumer.Row currentRow = (DataConsumer.Row)currentColumnData;
//                if (cachedRow.rowNum == currentRow.rowNum && cachedRow.values.length == currentRow.values.length) {
//                    int mismatchedValuesCount = 0;
//                    for (int i2 = 0; i2 < cachedRow.values.length; ++i2) {
//                        if (Comparing.equal((Object)cachedRow.values[i2], (Object)currentRow.values[i2])) continue;
//                        ++mismatchedValuesCount;
//                    }
//                    if (mismatchedValuesCount < 2) {
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        private Object getColumnData(int columnDataIdx) {
//            return TableResultView.this.isTransposed() ? TableResultView.this.getRow(columnDataIdx) : TableResultView.this.getColumn(columnDataIdx);
//        }
//
//        @NotNull
//        private MyTableColumn createTableColumnForColumn(ModelIndex<DataConsumer.Column> columnIdx) {
//            DataConsumer.Column column = Objects.requireNonNull(TableResultView.this.myResultPanel.getDataModel(DataAccessType.DATABASE_DATA).getColumn(columnIdx));
//            return new MyTableColumn(column, columnIdx.asInteger());
//        }
//
//        @NotNull
//        private MyTableColumn createTableColumnForRow(ModelIndex<DataConsumer.Row> rowIdx) {
//            return new MyTableColumn(rowIdx.asInteger());
//        }
//
//        private final class Entry {
//            @NotNull
//            public final Object myColumnData;
//            @NotNull
//            public final MyTableColumn myTableColumn;
//
//            Entry(@NotNull Object columnData, MyTableColumn tableColumn) {
//                this.myColumnData = columnData;
//                this.myTableColumn = tableColumn;
//            }
//
//        }
//    }
//
//    protected class MyTableColumn
//    extends TableColumn
//    implements GridColumn {
//        private final DataConsumer.Column myColumn;
//        private final UserDataHolder myDataHolderDelegate;
//        private int myWidthFromLayout;
//
//        public MyTableColumn(int modelIndex) {
//            this(null, modelIndex);
//        }
//
//        private MyTableColumn(DataConsumer.Column column, int modelIndex) {
//            super(modelIndex);
//            this.myDataHolderDelegate = new UserDataHolderBase();
//            this.myColumn = column;
//        }
//
//        public Icon getIcon(boolean forDisplay) {
//            if (this.myColumn == null) {
//                return null;
//            }
//            DasColumn column = forDisplay ? DataGridUtil.getDatabaseColumn((DataGrid)TableResultView.this.myResultPanel, this.myColumn) : null;
//            return column != null ? DbPresentation.getIcon((DasObject)column) : DbPresentation.getIcon(DataGridUtil.getDbms(TableResultView.this.myResultPanel), ObjectKind.COLUMN);
//        }
//
//        @Override
//        @NotNull
//        public String getHeaderValue() {
//            String string = this.myColumn == null ? DataGridUtil.getRowName(TableResultView.this.myResultPanel, this.getModelIndex()) : TableResultView.this.myResultPanel.getColumnAttributes().getName(this.myColumn);
//            return string;
//        }
//
//        @Override
//        public int getPreferredHeaderWidth() {
//            Component headerComponent;
//            TableCellRenderer cellRenderer = TableResultView.this.getTableHeader().getDefaultRenderer();
//            MyCellRenderer renderer = (MyCellRenderer)ObjectUtils.tryCast((Object)cellRenderer, MyCellRenderer.class);
//            if (renderer != null) {
//                headerComponent = renderer.getHeaderCellRendererComponent(this.getModelIndex(), false);
//            } else {
//                int viewIndex = TableResultView.this.convertColumnIndexToView(this.getModelIndex());
//                headerComponent = cellRenderer.getTableCellRendererComponent((JTable)((Object)TableResultView.this), this.getHeaderValue(), false, false, -1, viewIndex);
//            }
//            return headerComponent.getPreferredSize().width;
//        }
//
//        @Nullable
//        public String getTooltipText() {
//            return TableResultView.this.myResultPanel.getColumnTooltipHtml(this.myColumn);
//        }
//
//        @Nullable
//        public <T> T getUserData(@NotNull Key<T> key) {
//            return (T)this.myDataHolderDelegate.getUserData(key);
//        }
//
//        public <T> void putUserData(@NotNull Key<T> key, @Nullable T value2) {
//            this.myDataHolderDelegate.putUserData(key, value2);
//        }
//
//        @Override
//        public int getColumnWidth() {
//            return this.getPreferredWidth();
//        }
//
//        @Override
//        public void setColumnWidth(int width) {
//            this.myWidthFromLayout = width;
//            this.setPreferredWidth(width);
//        }
//
//        public boolean isWidthSetByLayout() {
//            return this.myWidthFromLayout == this.getPreferredWidth();
//        }
//
//    }
//
//    protected static class MyHeaderCellComponent
//    extends CellRendererPanel {
//        private final TableResultView myTable;
//        private final JLabel myNameLabel;
//        private final JLabel mySortLabel;
//        private MyTableColumn myCurrentColumn;
//
//        public MyHeaderCellComponent(TableResultView table) {
//            this.myTable = table;
//            this.myNameLabel = new MyJBLabel();
//            this.mySortLabel = new MyJBLabel();
//            this.myNameLabel.setHorizontalAlignment(2);
//            this.myNameLabel.setBorder(IdeBorderFactory.createEmptyBorder((Insets)CellRenderingUtils.NAME_LABEL_INSETS));
//            this.mySortLabel.setVerticalAlignment(0);
//            this.mySortLabel.setBorder(IdeBorderFactory.createEmptyBorder((Insets)CellRenderingUtils.SORT_LABEL_INSETS));
//            this.setLayout(new BorderLayout(JBUIScale.scale((int)2), 0));
//            this.add(this.myNameLabel, "Center");
//            this.add(this.mySortLabel, "East");
//            this.setBorder((Border)new CustomLineBorder(0, 0, 1, 1){
//
//                protected Color getColor() {
//                    Color gridColor = myTable.getGridColor();
//                    return gridColor != null ? gridColor : super.getColor();
//                }
//            });
//        }
//
//        protected void paintComponent(Graphics g) {
//            UISettings.setupAntialiasing((Graphics)g);
//            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
//            this.setSelected(true);
//            super.paintComponent(g);
//        }
//
//        protected Rectangle getNameRect() {
//            return TableResultView.getLabelTextRect(this.myNameLabel);
//        }
//
//        protected int getModelIdx() {
//            return this.myCurrentColumn.getModelIndex();
//        }
//
//        public MyHeaderCellComponent prepare(int columnDataIdx, boolean forDisplay) {
//            this.myCurrentColumn = this.myTable.getColumnCache().getOrCreateColumn(columnDataIdx);
//            this.myNameLabel.setIcon(this.myCurrentColumn.getIcon(forDisplay));
//            this.myNameLabel.setText(this.myCurrentColumn.getHeaderValue());
//            Icon sortLabelIcon = null;
//            String sortLabelText = "";
//            if (!this.myTable.isTransposed()) {
//                DataConsumer.Column column = this.myTable.getColumn(columnDataIdx);
//                if (column != null) {
//                    if (this.myTable.myResultPanel.getColumnAttributes().getComparator(column) != null) {
//                        int sortOrder = this.myTable.myResultPanel.getSortOrder(column);
//                        sortLabelIcon = TableResultView.getSortOrderIcon(sortOrder);
//                        sortLabelText = TableResultView.getSortOrderText(sortOrder);
//                    }
//                    this.myNameLabel.setHorizontalAlignment(ObjectFormatter.isNumericColumn(column) ? 4 : 2);
//                }
//            } else {
//                this.myNameLabel.setHorizontalAlignment(2);
//            }
//            this.mySortLabel.setIcon(sortLabelIcon);
//            this.mySortLabel.setText(sortLabelText);
//            return this;
//        }
//
//        public String getToolTipText(MouseEvent event) {
//            return this.myCurrentColumn != null ? this.myCurrentColumn.getTooltipText() : super.getToolTipText(event);
//        }
//
//        public Color getBackground() {
//            return this.myCurrentColumn == null ? super.getBackground() : (this.myTable.isTransposed() ? this.myTable.myResultPanel.getColorModel().getRowHeaderBackground(ModelIndex.forRow(this.myTable.myResultPanel, this.myCurrentColumn.getModelIndex())) : this.myTable.myResultPanel.getColorModel().getColumnHeaderBackground(ModelIndex.forColumn(this.myTable.myResultPanel, this.myCurrentColumn.getModelIndex())));
//        }
//
//        private class MyJBLabel
//        extends JBLabel {
//            private MyJBLabel() {
//            }
//
//            public Color getBackground() {
//                return MyHeaderCellComponent.this.myTable.getBackground();
//            }
//
//            public Color getForeground() {
//                return MyHeaderCellComponent.this.myTable.getForeground();
//            }
//
//            public Font getFont() {
//                return UIUtil.getFontWithFallback((Font)MyHeaderCellComponent.this.myTable.getTableHeader().getFont());
//            }
//        }
//    }
//
//    private static class GridCellRendererWrapper
//    implements TableCellRenderer {
//        final GridCellRenderer delegate;
//        final TableResultView myResultView;
//
//        private GridCellRendererWrapper(GridCellRenderer renderer, TableResultView resultView) {
//            this.delegate = renderer;
//            this.myResultView = resultView;
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value2, boolean isSelected, boolean hasFocus, int row, int column) {
//            DataGrid grid = this.delegate.myGrid;
//            ViewIndex<DataConsumer.Row> rowIdx = ViewIndex.forRow(grid, this.myResultView.isTransposed() ? column : row);
//            ViewIndex<DataConsumer.Column> columnIdx = ViewIndex.forColumn(grid, this.myResultView.isTransposed() ? row : column);
//            return this.delegate.getComponent(rowIdx, columnIdx, value2);
//        }
//    }
//
//    private static class Renderers {
//        final GridCellRenderers myGridRenderers;
//        final TableResultView myResultView;
//        final Map<GridCellRenderer, TableCellRenderer> myTableCellRenderers = ContainerUtil.newIdentityTroveMap();
//
//        Renderers(TableResultPanel resultPanel, TableResultView resultView) {
//            this.myGridRenderers = new GridCellRenderers(resultPanel);
//            this.myResultView = resultView;
//            Disposer.register((Disposable)resultPanel, (Disposable)this.myGridRenderers);
//        }
//
//        public TableCellRenderer getRenderer(int row, int column) {
//            ModelIndex<DataConsumer.Column> columnIdx;
//            DataGrid grid = this.myGridRenderers.grid;
//            ModelIndex<DataConsumer.Row> rowIdx = ViewIndex.forRow(grid, this.myResultView.isTransposed() ? column : row).toModel(grid);
//            GridCellRenderer gridCellRenderer = this.myGridRenderers.getRenderer(rowIdx, columnIdx = ViewIndex.forColumn(grid, this.myResultView.isTransposed() ? row : column).toModel(grid));
//            if (gridCellRenderer == null) {
//                return null;
//            }
//            TableCellRenderer renderer = this.myTableCellRenderers.get(gridCellRenderer);
//            if (renderer == null) {
//                renderer = new GridCellRendererWrapper(gridCellRenderer, this.myResultView);
//                this.myTableCellRenderers.put(gridCellRenderer, renderer);
//            }
//            return renderer;
//        }
//    }
//
//    private class PaintingSession {
//        private Font myFont;
//        private Color myGridColor;
//        private Color myForeground;
//        private Color myBackground;
//        private Color mySelectionForeground;
//        private Color mySelectionBackground;
//        private int myRowHeight = -1;
//
//        private PaintingSession() {
//        }
//
//        public Font getFont() {
//            return this.myFont != null ? this.myFont : (this.myFont = TableResultView.this.doGetFont());
//        }
//
//        public int getRowHeight() {
//            return this.myRowHeight != -1 ? this.myRowHeight : (this.myRowHeight = TableResultView.this.doGetRowHeight());
//        }
//
//        public Color getGridColor() {
//            return this.myGridColor != null ? this.myGridColor : (this.myGridColor = GridColorSchemeUtil.doGetGridColor(TableResultView.this.getColorsScheme()));
//        }
//
//        public Color getForeground() {
//            return this.myForeground != null ? this.myForeground : (this.myForeground = GridColorSchemeUtil.doGetForeground(TableResultView.this.getColorsScheme()));
//        }
//
//        public Color getBackground() {
//            return this.myBackground != null ? this.myBackground : (this.myBackground = GridColorSchemeUtil.doGetBackground(TableResultView.this.getColorsScheme()));
//        }
//
//        public Color getSelectionForeground() {
//            return this.mySelectionForeground != null ? this.mySelectionForeground : (this.mySelectionForeground = GridColorSchemeUtil.doGetSelectionForeground(TableResultView.this.getColorsScheme()));
//        }
//
//        public Color getSelectionBackground() {
//            return this.mySelectionBackground != null ? this.mySelectionBackground : (this.mySelectionBackground = GridColorSchemeUtil.doGetSelectionBackground(TableResultView.this.getColorsScheme()));
//        }
//    }
//
//    protected class MyTableHeader
//    extends JBTable.JBTableHeader {
//        private int myLastPositiveHeight;
//
//        protected MyTableHeader() {
//            super();
//            this.myLastPositiveHeight = 0;
//            this.setOpaque(false);
//            this.setFocusable(false);
//            this.addMouseListener(new MouseAdapter(){
//
//                @Override
//                public void mouseClicked(@NotNull MouseEvent e) {
//                    this.processEvent(e);
//                }
//
//                @Override
//                public void mousePressed(@NotNull MouseEvent e) {
//                    this.processEvent(e);
//                }
//
//                @Override
//                public void mouseReleased(@NotNull MouseEvent e) {
//                    this.processEvent(e);
//                }
//
//                private void processEvent(MouseEvent e) {
//                    if (TableResultView.this.isTransposed()) {
//                        int modelRow = TableResultView.this.myRawIndexConverter.row2Model().fun(MyTableHeader.this.columnAtPoint(e.getPoint()));
//                        if (modelRow >= 0) {
//                            TableResultView.this.onRowHeaderClicked(ModelIndex.forRow(TableResultView.this.myResultPanel, modelRow), e);
//                        }
//                    } else {
//                        int modelColumn = TableResultView.this.myRawIndexConverter.column2Model().fun(MyTableHeader.this.columnAtPoint(e.getPoint()));
//                        if (modelColumn >= 0) {
//                            TableResultView.this.onColumnHeaderClicked(ModelIndex.forColumn(TableResultView.this.myResultPanel, modelColumn), e);
//                        }
//                    }
//                }
//
//            });
//        }
//
//        public boolean getReorderingAllowed() {
//            return !TableResultView.this.isTransposed();
//        }
//
//        public Dimension getPreferredSize() {
//            Dimension d2 = super.getPreferredSize();
//            if (d2.height <= 0) {
//                d2.height = this.myLastPositiveHeight;
//            }
//            return d2;
//        }
//
//        public void setSize(int width, int height) {
//            super.setSize(width, height);
//            if (height > 0) {
//                this.myLastPositiveHeight = height;
//            }
//        }
//
//        public void paint(@NotNull Graphics g) {
//            Rectangle clip = g.getClipBounds();
//            clip.width = Math.max(0, Math.min(clip.width, this.getTable().getWidth() - clip.x));
//            g.setClip(clip);
//            super.paint(g);
//        }
//
//    }
//
//    static class MyTableColumnModel
//    extends DefaultTableColumnModel {
//        MyTableColumnModel() {
//        }
//
//        @Override
//        public void addColumn(TableColumn aColumn) {
//            if (!(aColumn instanceof MyTableColumn)) {
//                throw new IllegalArgumentException("Unexpected column type");
//            }
//            super.addColumn(aColumn);
//        }
//
//        @Override
//        public MyTableColumn getColumn(int columnIndex) {
//            return (MyTableColumn)super.getColumn(columnIndex);
//        }
//
//        public void removeAllColumns() {
//            int columnCount = this.tableColumns.size();
//            if (columnCount == 0) {
//                return;
//            }
//            if (this.selectionModel != null) {
//                this.selectionModel.removeIndexInterval(0, columnCount - 1);
//            }
//            for (TableColumn column : this.tableColumns) {
//                column.removePropertyChangeListener(this);
//            }
//            this.totalColumnWidth = -1;
//            for (int i2 = columnCount - 1; i2 >= 0; --i2) {
//                this.tableColumns.remove(i2);
//                this.fireColumnRemoved(new TableColumnModelEvent(this, i2, 0));
//            }
//        }
//    }
//
//    public static class MyCellRenderer
//    extends MyHeaderCellComponent
//    implements TableCellRenderer {
//        private final TableCellRenderer myOriginalRenderer;
//
//        public MyCellRenderer(@NotNull TableResultView table) {
//            super(table);
//            this.myOriginalRenderer = table.getTableHeader().getDefaultRenderer();
//        }
//
//        @Override
//        @NotNull
//        public Component getTableCellRendererComponent(@NotNull JTable table, Object value2, boolean isSelected, boolean hasFocus, int row, int column) {
//            Component component = row == -1 ? this.getHeaderCellRendererComponent(table.convertColumnIndexToModel(column), true) : this.myOriginalRenderer.getTableCellRendererComponent(table, value2, isSelected, hasFocus, row, column);
//            return component;
//        }
//
//        protected Component getHeaderCellRendererComponent(int columnDataIdx, boolean forDisplay) {
//            return this.prepare(columnDataIdx, forDisplay);
//        }
//    }
//}
//
