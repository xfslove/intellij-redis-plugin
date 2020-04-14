/*
 * Decompiled with CFR 0.149.
 *
 * Could not load the following classes:
 *  com.intellij.ui.table.JBTable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.xfslove.intellij.plugin.redis.experimental;

import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

public class JBTableWithResizableCells extends JBTable {

  private static final int RESIZE_AREA_RADIUS = 3;
  private CellRectangle myResizingCellOrigin;
  private boolean myExpandableItemsHandlerState;

  public JBTableWithResizableCells(TableModel model, TableColumnModel columnModel) {
    super(model, columnModel);
  }

  @Override
  protected void processMouseEvent(MouseEvent e) {
    this.preProcessMouseEvent(e);
    if (!e.isConsumed()) {
      super.processMouseEvent(e);
    }
  }

  @Override
  protected void processMouseMotionEvent(MouseEvent e) {
    this.preProcessMouseEvent(e);
    if (!e.isConsumed()) {
      super.processMouseMotionEvent(e);
    }
  }

  private void preProcessMouseEvent(MouseEvent e) {
    if (e.isPopupTrigger()) {
      return;
    }
    Point mouseLocation = e.getPoint();
    CellRectangle cellToResize = this.getCellToResize(mouseLocation);
    int eventId = e.getID();
    // mouse event
    if (eventId == MouseEvent.MOUSE_DRAGGED) {
      if (this.myResizingCellOrigin != null) {
        this.updateResizingCell(mouseLocation);
        e.consume();
      }
    } else if (eventId == MouseEvent.MOUSE_MOVED || eventId == MouseEvent.MOUSE_ENTERED || eventId == MouseEvent.MOUSE_EXITED) {
      this.updateCursor(cellToResize);
    } else if (eventId == MouseEvent.MOUSE_PRESSED) {
      if (cellToResize != null) {
        this.myExpandableItemsHandlerState = this.getExpandableItemsHandler().isEnabled();
        this.setExpandableItemsEnabled(false);
        this.myResizingCellOrigin = cellToResize;
        this.updateResizingCell(mouseLocation);
        e.consume();
      }
    } else if (eventId == MouseEvent.MOUSE_RELEASED && this.myResizingCellOrigin != null) {
      this.setExpandableItemsEnabled(this.myExpandableItemsHandlerState);
      this.myResizingCellOrigin = null;
      e.consume();
    }
  }

  private void updateCursor(@Nullable CellRectangle cellToResize) {
    int newCursorType = (cellToResize != null || this.myResizingCellOrigin != null)
        ? Cursor.DEFAULT_CURSOR : Cursor.CROSSHAIR_CURSOR;
    if (this.getCursor().getType() != newCursorType) {
      this.setCursor(Cursor.getPredefinedCursor(newCursorType));
    }
  }

  private void updateResizingCell(@NotNull Point mouseLocation) {
    int diffX = mouseLocation.x - this.myResizingCellOrigin.x - this.myResizingCellOrigin.width - 1;
    int diffY = mouseLocation.y - this.myResizingCellOrigin.y - this.myResizingCellOrigin.height - 1;
    int newWidth = this.myResizingCellOrigin.width + diffX;
    int newHeight = Math.max(this.getRowHeight(), this.myResizingCellOrigin.height + diffY);
    this.setRowHeight(this.myResizingCellOrigin.row, newHeight);
    this.getColumnModel().getColumn(this.myResizingCellOrigin.column).setPreferredWidth(newWidth);
  }

  @Nullable
  private CellRectangle getCellToResize(@NotNull Point mouseLocation) {
    int row = this.rowAtPoint(mouseLocation);
    int column = this.columnAtPoint(mouseLocation);
    Rectangle hoveredCell = (row != -1 && column != -1)
        ? this.getCellRect(row, column, true) : null;
    if (hoveredCell == null) {
      return null;
    }
    if (column != 0 && mouseLocation.distance(hoveredCell.x, hoveredCell.y + hoveredCell.height - 1) < RESIZE_AREA_RADIUS) {
      --column;
    } else if (row != 0 && mouseLocation.distance(hoveredCell.x + hoveredCell.width - 1, hoveredCell.y) < RESIZE_AREA_RADIUS) {
      --row;
    } else if (column != 0 && row != 0 && mouseLocation.distance(hoveredCell.x, hoveredCell.y) < RESIZE_AREA_RADIUS) {
      --row;
      --column;
    }
    Rectangle toResize = this.getCellRect(row, column, true);
    boolean inResizeArea = mouseLocation.distance(toResize.x + toResize.width - 1, toResize.y + toResize.height - 1) < 3.0;
    return inResizeArea ? new CellRectangle(row, column, toResize) : null;
  }

  private static class CellRectangle extends Rectangle {
    public final int row;
    public final int column;

    CellRectangle(int row, int column, Rectangle r) {
      super(r);
      this.row = row;
      this.column = column;
    }
  }
}

