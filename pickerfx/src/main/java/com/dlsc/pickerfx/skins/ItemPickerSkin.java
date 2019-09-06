package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.ItemPicker;
import com.dlsc.pickerfx.Segment;
import javafx.beans.binding.Bindings;

public class ItemPickerSkin<T> extends PickerSkinBase<ItemPicker<T>> {

    public ItemPickerSkin(ItemPicker<T> picker) {
        super(picker);

        Segment<T, T> segment = new Segment<>(picker);
        segment.cellFactoryProperty().bind(picker.cellFactoryProperty());
        Bindings.bindContent(segment.getItems(), picker.itemsProperty());
        add(segment);

        picker.valueProperty().bind(segment.valueProperty());
    }
}
