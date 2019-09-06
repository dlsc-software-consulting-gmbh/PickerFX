package com.dlsc.pickerfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * A cell used for displaying an item inside a {@see Segement}.
 *
 * @param <T> the type of the value / the item shown inside the cell
 */
public class SegmentCell<T> extends Label {

    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    public SegmentCell(Pos alignment) {
        getStyleClass().add("segment-cell");
        setAlignment(alignment);
    }

    public SegmentCell() {
        this(Pos.CENTER);
    }

    /**
     * The position is the "offset" of the cell from the middle. The selected cell has the
     * index 0, the cells above it are -1, -2, -3, etc.... The cells below the selected cell
     * have the position value 1, 2, 3, .... The position is basically the distance from the
     * selected value / the middle.
     */
    private final IntegerProperty position = new SimpleIntegerProperty(this, "position");

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    /**
     * The index of the model item shown by the cell.
     *
     * @see Segment#getItems()
     */
    private final IntegerProperty index = new SimpleIntegerProperty(this, "index");

    public int getIndex() {
        return index.get();
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public void update(T item) {
        setText(item.toString());
    }

    private BooleanProperty selected;

    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }

    public final boolean isSelected() {
        return selected == null ? false : selected.get();
    }

    public final BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new BooleanPropertyBase() {
                @Override
                protected void invalidated() {
                    final boolean selected = get();
                    pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, selected);
                }

                @Override
                public Object getBean() {
                    return SegmentCell.this;
                }

                @Override
                public String getName() {
                    return "selected";
                }
            };
        }
        return selected;
    }
}
