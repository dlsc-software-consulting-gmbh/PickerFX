package com.dlsc.pickerfx;

import com.dlsc.pickerfx.skins.LocalTimePickerSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.util.Callback;

import java.time.LocalTime;

/**
 * A specific picker implementation used to let the user select a time.
 */
public class LocalTimePicker extends Picker<LocalTime> {

    /**
     * An enum used to distinguish between AM and PM times.
     */
    public enum Meridiem {
        AM,
        PM
    }

    public LocalTimePicker() {
        getStyleClass().add("local-time-picker");
        getStylesheets().add(getClass().getResource("local-time-picker.css").toExternalForm());
        setValue(LocalTime.now());

        setMinuteCellFactory(segment -> new SegmentCell<Integer>() {
            @Override
            public void update(Integer item) {
                super.update(item);

                if (item < 10) {
                    setText("0" + getText());
                }
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LocalTimePickerSkin(this);
    }

    /**
     * Specifies whether we want to display time in 24 hour time format or 12 hour time format with AM and PM indicator.
     */
    private final ObjectProperty<TimeFormat> timeFormat = new SimpleObjectProperty<>(this, "timeFormat", TimeFormat.TWENTY_FOUR_HOURS);

    public final TimeFormat getTimeFormat() {
        return timeFormat.get();
    }

    public final ObjectProperty<TimeFormat> timeFormatProperty() {
        return timeFormat;
    }

    public final void setTimeFormat(TimeFormat timeFormat) {
        this.timeFormat.set(timeFormat);
    }

    /**
     * The cell factory used by the segment that displays the hours.
     */
    private final ObjectProperty<Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>> hourCellFactory = new SimpleObjectProperty<>(this, "hourCellFactory", segment -> new SegmentCell<>());

    public final Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>> getHourCellFactory() {
        return hourCellFactory.get();
    }

    public final ObjectProperty<Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>> hourCellFactoryProperty() {
        return hourCellFactory;
    }

    public final void setHourCellFactory(Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>> factory) {
        this.hourCellFactory.set(factory);
    }

    // minute cell factory

    /**
     * The cell factory used by the segment that displays the minutes.
     */
    private final ObjectProperty<Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>> minuteCellFactory = new SimpleObjectProperty<>(this, "minuteCellFactory", segment -> new SegmentCell<>());

    public final Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>> getMinuteCellFactory() {
        return minuteCellFactory.get();
    }

    public final ObjectProperty<Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>> minuteCellFactoryProperty() {
        return minuteCellFactory;
    }

    public final void setMinuteCellFactory(Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>> factory) {
        this.minuteCellFactory.set(factory);
    }

    // meridiem cell factory

    /**
     * The cell factory used by the segment that displays the meridiem ("AM", "PM").
     */
    private final ObjectProperty<Callback<Segment<LocalTime, Meridiem>, SegmentCell<Meridiem>>> meridiemCellFactory = new SimpleObjectProperty<>(this, "meridiemCellFactory", segment -> new SegmentCell<>());

    public final Callback<Segment<LocalTime, Meridiem>, SegmentCell<Meridiem>> getMeridiemCellFactory() {
        return meridiemCellFactory.get();
    }

    public final ObjectProperty<Callback<Segment<LocalTime, Meridiem>, SegmentCell<Meridiem>>> meridiemCellFactoryProperty() {
        return meridiemCellFactory;
    }

    public final void setMeridiemCellFactory(Callback<Segment<LocalTime, Meridiem>, SegmentCell<Meridiem>> factory) {
        this.meridiemCellFactory.set(factory);
    }
}
