package com.dlsc.pickerfx;

import com.dlsc.pickerfx.skins.LocalDatePickerSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Skin;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

/**
 * A specific picker implementation used to let the user select a date.
 */
public class LocalDatePicker extends Picker<LocalDate> {

    public LocalDatePicker() {
        getStyleClass().add("local-date-picker");
        getStylesheets().add(getClass().getResource("local-date-picker.css").toExternalForm());
        setValue(LocalDate.now());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LocalDatePickerSkin(this);
    }

    public final ObjectProperty<DateFormat> dateFormatProperty() {
        return dateFormat;
    }

    private final ObjectProperty<DateFormat> dateFormat = new SimpleObjectProperty<DateFormat>(this, "dateFormat",
            DateFormat.STANDARD) {
        @Override
        public void set(DateFormat newValue) {
            super.set(Objects.requireNonNull(newValue));
        }
    };

    public final DateFormat getDateFormat() {
        return dateFormatProperty().get();
    }

    public final void setDateFormat(DateFormat dateFormat) {
        dateFormatProperty().set(dateFormat);
    }

    /**
     * The cell factory used by the {@see Segment} that will display the day of month.
     */
    private final ObjectProperty<Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>>> dayCellFactory = new SimpleObjectProperty<>(this, "dayCellFactory", segment -> new SegmentCell<>());

    public final Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>> getDayCellFactory() {
        return dayCellFactory.get();
    }

    public final ObjectProperty<Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>>> dayCellFactoryProperty() {
        return dayCellFactory;
    }

    public final void setDayCellFactory(Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>> dayCellFactory) {
        this.dayCellFactory.set(dayCellFactory);
    }

    // month cell factory

    /**
     * The cell factory used by the {@see Segment} that will display the month (January, February, ...).
     */
    private final ObjectProperty<Callback<Segment<LocalDate, Month>, SegmentCell<Month>>> monthCellFactory = new SimpleObjectProperty<>(this, "monthCellFactory", segment -> new SegmentCell<>(Pos.CENTER_LEFT));

    public final Callback<Segment<LocalDate, Month>, SegmentCell<Month>> getMonthCellFactory() {
        return monthCellFactory.get();
    }

    public final ObjectProperty<Callback<Segment<LocalDate, Month>, SegmentCell<Month>>> monthCellFactoryProperty() {
        return monthCellFactory;
    }

    public final void setMonthCellFactory(Callback<Segment<LocalDate, Month>, SegmentCell<Month>> monthCellFactory) {
        this.monthCellFactory.set(monthCellFactory);
    }

    // month cell factory

    /**
     * The cell factory used by the {@see Segment} that will display the year (2019, 2020, ...).
     */
    private final ObjectProperty<Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>>> yearCellFactory = new SimpleObjectProperty<>(this, "yearCellFactory", segment-> new SegmentCell<>());

    public Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>> getYearCellFactory() {
        return yearCellFactory.get();
    }

    public final ObjectProperty<Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>>> yearCellFactoryProperty() {
        return yearCellFactory;
    }

    public final void setYearCellFactory(Callback<Segment<LocalDate, Integer>, SegmentCell<Integer>> yearCellFactory) {
        this.yearCellFactory.set(yearCellFactory);
    }


}
