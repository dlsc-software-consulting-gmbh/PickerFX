package com.dlsc.pickerfx;

import com.dlsc.pickerfx.skins.ItemPickerSkin;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.util.Callback;

import java.util.Objects;

/**
 * A specific picker implementation used for displaying any type of item. Similar in
 * behaviour to a ComboBox instance.
 *
 * @param <T> the item type
 */
public class ItemPicker<T> extends Picker<T> {

    /**
     * Constructs a new item picker with the given items.
     *
     * @param items the items
     */
    @SafeVarargs
    public ItemPicker(T... items) {
        this();
        getStyleClass().add("item-picker");

        if (items != null) {
            getItems().setAll(items);
        }
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(ItemPicker.class.getResource("picker.css")).toExternalForm();
    }

    /**
     * Constructs a new item picker. Items can be added to the list returned
     * by the {@see #getTabs} method.
     */
    public ItemPicker() {
        super();
    }

    private final ListProperty<T> items = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * The list of items that will be displayed by the picker.
     */
    public final ListProperty<T> itemsProperty() {
        return items;
    }

    public final ObservableList<T> getItems() {
        return items;
    }

    public final void setItems(ObservableList<T> items) {
        this.items.set(items);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemPickerSkin<>(this);
    }

    private final ObjectProperty<Callback<Segment<T, T>, SegmentCell<T>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory", segment -> new SegmentCell<>());

    public final Callback<Segment<T, T>, SegmentCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    /**
     * The cell factory for this picker.
     *
     * @return the cell factory
     */
    public final ObjectProperty<Callback<Segment<T, T>, SegmentCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final void setCellFactory(Callback<Segment<T, T>, SegmentCell<T>> factory) {
        this.cellFactory.set(factory);
    }
}
