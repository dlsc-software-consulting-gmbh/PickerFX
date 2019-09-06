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
    public ItemPicker(T... items) {
        this();
        getStyleClass().add("item-picker");

        if (items != null) {
            getItems().setAll(items);
        }
    }

    /**
     * Constructs a new item picker. Items can be added to the list returned
     * by the {@see #getTabs} method.
     */
    public ItemPicker() {
        super();
    }

    /**
     * The list of items that will be displayed by the picker.
     */
    private final ListProperty<T> items = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<T> itemsProperty() {
        return items;
    }

    public final ObservableList<T> getItems() {
        return items;
    }

    public void setItems(ObservableList items) {
        this.items.set(items);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemPickerSkin<>(this);
    }

    private final ObjectProperty<Callback<Segment<T, T>, SegmentCell<T>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory", segment -> new SegmentCell<>());

    public Callback<Segment<T, T>, SegmentCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    public ObjectProperty<Callback<Segment<T, T>, SegmentCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public void setCellFactory(Callback<Segment<T, T>, SegmentCell<T>> factory) {
        this.cellFactory.set(factory);
    }
}
