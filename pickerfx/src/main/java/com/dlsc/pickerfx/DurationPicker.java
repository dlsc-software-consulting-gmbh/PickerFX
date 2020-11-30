package com.dlsc.pickerfx;

import com.dlsc.pickerfx.skins.DurationPickerSkin;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * A specific picker implementation used to let the user select a time.
 */
public class DurationPicker extends Picker<Duration> {

    public DurationPicker() {
        getStyleClass().add("duration-picker");
        getStylesheets().add(getClass().getResource("duration-picker.css").toExternalForm());
        setValue(Duration.ZERO);

        getFields().setAll(ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS);

        for (ChronoUnit unit : ChronoUnit.values()) {
            getCellFactories().put(unit, segment -> new SegmentCell<>());
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DurationPickerSkin(this);
    }

    // minimum duration

    private final ObjectProperty<Duration> minimumDuration = new SimpleObjectProperty<>(this, "earliestTime", Duration.ZERO);

    public final Duration getMinimumDuration() {
        return minimumDuration.get();
    }

    /**
     * Stores the minimum duration that the picker can display. The minimum duration can not
     * be negative.
     *
     * @return the minimum duration
     */
    public final ObjectProperty<Duration> minimumDurationProperty() {
        return minimumDuration;
    }

    public final void setMinimumDuration(Duration minimumDuration) {
        this.minimumDuration.set(minimumDuration);
    }

    // maximum duration

    private final ObjectProperty<Duration> maximumDuration = new SimpleObjectProperty<>(this, "maximumDuration", Duration.ofDays(99));

    public final Duration getMaximumDuration() {
        return maximumDuration.get();
    }

    /**
     * Stores the maximum duration that the picker can display. The default value is "99 days".
     *
     * @return the maximum duration
     */
    public final ObjectProperty<Duration> maximumDurationProperty() {
        return maximumDuration;
    }

    public final void setMaximumDuration(Duration maximumDuration) {
        this.maximumDuration.set(maximumDuration);
    }

    private final ListProperty<ChronoUnit> fields = new SimpleListProperty<>(this, "fields", FXCollections.observableArrayList());

    public final ObservableList<ChronoUnit> getFields() {
        return fields.get();
    }

    public final ListProperty<ChronoUnit> fieldsProperty() {
        return fields;
    }

    public void setFields(ObservableList<ChronoUnit> fields) {
        this.fields.set(fields);
    }

    /**
     * The cell factory used by the segment that displays the hours.
     */
    private final ObjectProperty<Map<ChronoUnit, Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>>> cellFactories =
            new SimpleObjectProperty<>(this, "cellFactories", new HashMap<>());

    public Map<ChronoUnit, Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>> getCellFactories() {
        return cellFactories.get();
    }

    public ObjectProperty<Map<ChronoUnit, Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>>> cellFactoriesProperty() {
        return cellFactories;
    }

    public void setCellFactories(Map<ChronoUnit, Callback<Segment<LocalTime, Integer>, SegmentCell<Integer>>> cellFactories) {
        this.cellFactories.set(cellFactories);
    }
}
