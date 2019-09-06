package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.LocalTimePicker;
import com.dlsc.pickerfx.LocalTimePicker.Meridiem;
import com.dlsc.pickerfx.Segment;
import com.dlsc.pickerfx.TimeFormat;
import javafx.beans.InvalidationListener;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.time.LocalTime;

public class LocalTimePickerSkin extends PickerSkinBase<LocalTimePicker> {

    private final Segment<LocalTime, Integer> hourSegment;
    private final Segment<LocalTime, Integer> minuteSegment;
    private final Segment<LocalTime, Meridiem> meridiemSegment;

    private final Label colon;

    public LocalTimePickerSkin(LocalTimePicker picker) {
        super(picker);

        InvalidationListener updateValueListener = it -> updateValue();

        hourSegment = new Segment<>(picker);
        hourSegment.getStyleClass().add("hour");
        hourSegment.valueProperty().addListener(updateValueListener);
        hourSegment.cellFactoryProperty().bind(picker.hourCellFactoryProperty());

        minuteSegment = new Segment<>(picker);
        minuteSegment.getStyleClass().add("minute");
        minuteSegment.valueProperty().addListener(updateValueListener);
        minuteSegment.cellFactoryProperty().bind(picker.minuteCellFactoryProperty());

        for (int i = 0; i < 60; i++) {
            minuteSegment.getItems().add(i);
        }

        meridiemSegment = new Segment<>(picker);
        meridiemSegment.getItems().setAll(Meridiem.values());
        meridiemSegment.getStyleClass().add("meridiem");
        meridiemSegment.valueProperty().addListener(updateValueListener);
        meridiemSegment.setWrapItems(false);
        meridiemSegment.setValue(Meridiem.AM);
        meridiemSegment.cellFactoryProperty().bind(picker.meridiemCellFactoryProperty());

        colon = new Label();
        colon.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        colon.setText(":");
        colon.getStyleClass().add("colon");
        colon.setAlignment(Pos.CENTER);

        HBox.setHgrow(hourSegment, Priority.ALWAYS);
        HBox.setHgrow(colon, Priority.NEVER);
        HBox.setHgrow(minuteSegment, Priority.ALWAYS);
        HBox.setHgrow(meridiemSegment, Priority.ALWAYS);

        picker.timeFormatProperty().addListener(it -> {
            buildView();
            updateSegmentValues();
        });

        buildView();

        picker.valueProperty().addListener(it -> updateSegmentValues());
        updateSegmentValues();

        updateColon();
        picker.orientationProperty().addListener(it -> updateColon());
    }

    private void updateColon() {
        if (getSkinnable().getOrientation().equals(Orientation.VERTICAL)) {
            colon.setText(":");
        } else {
            colon.setText("");
        }
    }

    private void buildView() {clear();
        add(hourSegment);
        add(new SegmentSeparator());
        add(colon);
        add(new SegmentSeparator());
        add(minuteSegment);

        if (getSkinnable().getTimeFormat().equals(TimeFormat.TWELVE_HOURS)) {
            hourSegment.getItems().setAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
            add(new SegmentSeparator());
            add(meridiemSegment);
        } else {
            hourSegment.getItems().setAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
        }
    }

    private void updateSegmentValues() {
        if (updatingValue) {
            return;
        }

        LocalTime time = getSkinnable().getValue();

        if (time != null) {

            if (getSkinnable().getTimeFormat().equals(TimeFormat.TWENTY_FOUR_HOURS)) {
                hourSegment.setValue(time.getHour());
            } else {
                hourSegment.setValue(time.getHour() % 12);
                if (time.getHour() >= 12) {
                    meridiemSegment.setValue(Meridiem.PM);
                }
            }

            minuteSegment.setValue(time.getMinute());
        }
    }

    private boolean updatingValue;

    private void updateValue() {
        Integer hour = hourSegment.getValue();
        Integer minute = minuteSegment.getValue();

        if (hour != null && minute != null) {
            updatingValue = true;
            try {
                LocalTime time = LocalTime.of(hour, minute);

                if (getSkinnable().getTimeFormat().equals(TimeFormat.TWELVE_HOURS)) {
                    Meridiem meridiem = meridiemSegment.getValue();
                    if (meridiem != null && meridiem.equals(Meridiem.PM)) {
                        if (time.getHour() < 12) {
                            time = time.plusHours(12);
                        }
                    }
                }

                getSkinnable().setValue(time);
            } finally {
                updatingValue = false;
            }
        }
    }
}
