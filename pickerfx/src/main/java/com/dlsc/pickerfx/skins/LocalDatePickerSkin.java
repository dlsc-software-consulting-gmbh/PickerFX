package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.DateFormat;
import com.dlsc.pickerfx.LocalDatePicker;
import com.dlsc.pickerfx.Segment;
import javafx.beans.InvalidationListener;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.time.LocalDate;
import java.time.Month;

public class LocalDatePickerSkin extends PickerSkinBase<LocalDatePicker> {

    private final Segment<LocalDate, Integer> daySegment;
    private final Segment<LocalDate, Month> monthSegment;
    private final Segment<LocalDate, Integer> yearSegment;

    // TODO: allow application to specify a supported date range

    public LocalDatePickerSkin(LocalDatePicker picker) {
        super(picker);

        InvalidationListener updateValueListener = it -> updateValue();

        daySegment = new Segment<>(picker, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
        daySegment.getStyleClass().add("day");
        daySegment.valueProperty().addListener(updateValueListener);
        daySegment.cellFactoryProperty().bind(picker.dayCellFactoryProperty());

        monthSegment = new Segment<>(picker);
        monthSegment.getItems().setAll(Month.values());
        monthSegment.getStyleClass().add("month");
        monthSegment.valueProperty().addListener(updateValueListener);
        monthSegment.cellFactoryProperty().bind(picker.monthCellFactoryProperty());

        yearSegment = new Segment<>(picker);
        yearSegment.getStyleClass().add("year");
        yearSegment.valueProperty().addListener(updateValueListener);
        yearSegment.cellFactoryProperty().bind(picker.yearCellFactoryProperty());

        int i = LocalDate.now().getYear() - 10;
        while (i <= LocalDate.now().getYear() + 10) {
            yearSegment.getItems().add(i++);
        }

        HBox.setHgrow(daySegment, Priority.ALWAYS);
        HBox.setHgrow(monthSegment, Priority.ALWAYS);
        HBox.setHgrow(yearSegment, Priority.ALWAYS);

        picker.valueProperty().addListener(obs -> updateSegmentValues());
        updateSegmentValues();

        picker.dateFormatProperty().addListener(obs -> updateSkin());
        updateSkin();
    }

    private void updateSkin() {
        clear();

        if (getSkinnable().getDateFormat() == DateFormat.STANDARD) {

            add(daySegment);
            add(new SegmentSeparator());
            add(monthSegment);
            add(new SegmentSeparator());
            add(yearSegment);

        } else {

            add(monthSegment);
            add(new SegmentSeparator());
            add(daySegment);
            add(new SegmentSeparator());
            add(yearSegment);

        }
    }

    private boolean updatingValue;

    private void updateValue() {
        Integer day = daySegment.getValue();
        Month month = monthSegment.getValue();
        Integer year = yearSegment.getValue();

        if (day != null && month != null && year != null) {
            try {
                updatingValue = true;
                getSkinnable().setValue(LocalDate.of(year, month, day));
                getSkinnable().getProperties().put("invalid", false);
            } catch (Exception e) {
                getSkinnable().setValue(null);
                getSkinnable().getProperties().put("invalid", true);
            } finally {
                updatingValue = false;
            }
        }
    }

    private void updateSegmentValues() {
        if (!updatingValue) {
            LocalDate value = getSkinnable().getValue();
            if (value != null) {
                daySegment.setValue(value.getDayOfMonth());
                monthSegment.setValue(value.getMonth());
                yearSegment.setValue(value.getYear());
            }
        }
    }
}
