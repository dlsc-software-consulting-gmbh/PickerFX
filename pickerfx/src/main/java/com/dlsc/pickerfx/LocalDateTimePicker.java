package com.dlsc.pickerfx;

import com.dlsc.pickerfx.skins.LocalDateTimePickerSkin;
import javafx.scene.control.Skin;

import java.time.LocalDateTime;

/**
 * A specific picker implementation used to let the user select a date and a time. The picker's skin composes
 * a {@see LocalDatePicker} and a {@see LocalTimePicker} to achieve the desired look and feel.
 */
public class LocalDateTimePicker extends Picker<LocalDateTime> {

    private final LocalDatePicker datePicker;

    private final LocalTimePicker timePicker;

    /**
     * Constructs a new date time picker instance.
     */
    public LocalDateTimePicker() {
        getStyleClass().add("local-date-time-picker");
        getStylesheets().add(getClass().getResource("local-date-time-picker.css").toExternalForm());
        setValue(LocalDateTime.now());

        datePicker = new LocalDatePicker();
        timePicker = new LocalTimePicker();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LocalDateTimePickerSkin(this);
    }

    /**
     * Returns the date picker used for displaying the date.
     *
     * @return the date picker
     */
    public final LocalDatePicker getDatePicker() {
        return datePicker;
    }

    /**
     * Returns the time picker used for displaying the time.
     *
     * @return the time picker
     */
    public final LocalTimePicker getTimePicker() {
        return timePicker;
    }
}
