package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.LocalDatePicker;
import com.dlsc.pickerfx.LocalDateTimePicker;
import com.dlsc.pickerfx.LocalTimePicker;
import javafx.beans.InvalidationListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimePickerSkin extends PickerSkinBase<LocalDateTimePicker> {

    public LocalDateTimePickerSkin(LocalDateTimePicker control) {
        super(control);

        InvalidationListener updateValueListener = it -> updateValue();

        LocalDatePicker datePicker = control.getDatePicker();
        datePicker.setShowIndicator(false);
        datePicker.valueProperty().addListener(updateValueListener);

        LocalTimePicker timePicker = control.getTimePicker();
        timePicker.setShowIndicator(false);
        timePicker.valueProperty().addListener(updateValueListener);

        control.valueProperty().addListener(obs -> updateSegmentValues());

        getContainer().getChildren().add(datePicker);
        getContainer().getChildren().add(new SegmentSeparator());
        getContainer().getChildren().add(timePicker);

        updateValue();
    }

    private boolean updatingValue;

    private void updateValue() {
        LocalDate date = getSkinnable().getDatePicker().getValue();
        LocalTime time = getSkinnable().getTimePicker().getValue();
        try {
            updatingValue = true;
            if (date != null && time != null) {
                getSkinnable().setValue(LocalDateTime.of(date, time));
            }
            else {
                getSkinnable().setValue(null);
            }
        }
        finally {
            updatingValue = false;
        }
    }

    private void updateSegmentValues() {
        if (!updatingValue) {
            LocalDateTime dateTime = getSkinnable().getValue();
            if (dateTime != null) {
                getSkinnable().getDatePicker().setValue(dateTime.toLocalDate());
                getSkinnable().getTimePicker().setValue(dateTime.toLocalTime());
            }
        }
    }
}
