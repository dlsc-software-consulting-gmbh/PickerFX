package com.dlsc.pickerfx;

import com.dlsc.pickerfx.skins.IntegerPickerSkin;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * A specific picker implementation used for entering integer values. The application
 * has to first choose how many digits the picker should show.
 */
public class IntegerPicker extends Picker<Integer> {

    /**
     * Constructs a new picker.
     *
     * @param numberOfDigits the number of digits to show
     */
    public IntegerPicker(int numberOfDigits) {
        getStyleClass().add("integer-picker");
        setNumberOfDigits(numberOfDigits);
    }

    /**
     * Constructs a new picker. The number of digists is 3.
     */
    public IntegerPicker() {
        this(3);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new IntegerPickerSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("integer-picker.css").toExternalForm();
    }

    /**
     * Stores the number of digits that the picker will display.
     */
    private final IntegerProperty numberOfDigits = new SimpleIntegerProperty(this, "numberOfDigits", 3);

    public final int getNumberOfDigits() {
        return numberOfDigits.get();
    }

    public final IntegerProperty numberOfDigitsProperty() {
        return numberOfDigits;
    }

    public final void setNumberOfDigits(int numberOfDigits) {
        this.numberOfDigits.set(numberOfDigits);
    }

    /**
     * Stores a cell factory used for the segments that are created by the skin of the picker.
     */
    private final ObjectProperty<Callback<Segment<Integer, Integer>, SegmentCell<Integer>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory", segment -> new SegmentCell<>());

    public final Callback<Segment<Integer, Integer>, SegmentCell<Integer>> getCellFactory() {
        return cellFactory.get();
    }

    public final ObjectProperty<Callback<Segment<Integer, Integer>, SegmentCell<Integer>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final void setCellFactory(Callback<Segment<Integer, Integer>, SegmentCell<Integer>> cellFactory) {
        this.cellFactory.set(cellFactory);
    }
}
