package com.dlsc.pickerfx;

import com.dlsc.pickerfx.skins.SegmentSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *    A segment represents one single selection block that contains elements which the user can pick from.  Basically a
 *    segment is composed by a predefined list of elements ({@link #getItems() items}), and one
 *    selected {@link #valueProperty() value}.
 * </p>
 *
 * <p>
 *     Usually a segment is one piece of a bigger control called {@link Picker}, so in order to use segments a picker
 *     must be created.  In that sense, every time a Segment instance is required, it is also necessary to have a Picker
 *     instance. The segment's value is independent of the picker value, so they do not have any direct binding.
 *     However the segment's value can be used to compose the picker's value.
 * </p>
 *
 * @param <T> The picker's value type.
 * @param <S> The segment's value type.
 *
 * @see Picker
 */
public class Segment<T, S> extends Control {

    private final Picker<T> picker;

    /**
     * Creates a new instance of a segment with no items and {@code null} value.
     * @param picker The picker parent of this segment control.
     */
    public Segment(Picker<T> picker) {
        this(picker, null);
    }

    /**
     * Creates a new instance of a segment with the given list of items and {@code null} value.
     *
     * @param picker The picker parent of this segment control.
     * @param items The default items in the segment.
     */
    public Segment(Picker<T> picker, S... items) {
        this.picker = Objects.requireNonNull(picker);

        getStyleClass().add("segment");

        setMinHeight(Region.USE_PREF_SIZE);
        setMinWidth(Region.USE_PREF_SIZE);

        setCellFactory(p -> new SegmentCell<>());

        if (items != null) {
            getItems().setAll(items);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SegmentSkin<>(this);
    }

    /**
     * @return The picker parent of this segment instance.
     */
    public final Picker<T> getPicker() {
        return picker;
    }

    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * @return The list of items available for the segment.
     */
    public final ObservableList<S> getItems() { return items; }
    private final ObservableList<S> items = FXCollections.observableArrayList();


    /**
     * @return The object property that holds the selected value.
     */
    public final ObjectProperty<S> valueProperty() { return value; }
    private final ObjectProperty<S> value = new SimpleObjectProperty<>(this, "value");
    public final S getValue() { return valueProperty().get(); }
    public final void setValue(S value) { this.value.set(value); }

    /**
     *
     * @return
     */
    public final BooleanProperty wrapItemsProperty() {
        return wrapItems;
    }

    private final BooleanProperty wrapItems = new SimpleBooleanProperty(this, "wrapItems", true);

    public final boolean isWrapItems() {
        return wrapItems.get();
    }

    public final void setWrapItems(boolean wrapItems) {
        this.wrapItems.set(wrapItems);
    }

    private final ObjectProperty<Callback<Segment<T, S>, SegmentCell<S>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory");

    public final ObjectProperty<Callback<Segment<T, S>, SegmentCell<S>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final Callback<Segment<T, S>, SegmentCell<S>> getCellFactory() {
        return cellFactoryProperty().get();
    }

    public final void setCellFactory(Callback<Segment<T, S>, SegmentCell<S>> cellFactory) {
        cellFactoryProperty().set(cellFactory);
    }

    public final BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    private final BooleanProperty readOnly = new SimpleBooleanProperty(this, "readOnly");

    public final boolean isReadOnly() {
        return readOnlyProperty().get();
    }

    public final void setReadOnly(boolean readOnly) {
        readOnlyProperty().set(readOnly);
    }
}
