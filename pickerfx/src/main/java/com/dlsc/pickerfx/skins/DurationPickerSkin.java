package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.DurationPicker;
import com.dlsc.pickerfx.Segment;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DurationPickerSkin extends PickerSkinBase<DurationPicker> {

    private final Map<ChronoUnit, Segment<Duration, Long>> segmentMap = new HashMap<>();

    public DurationPickerSkin(DurationPicker picker) {
        super(picker);

        picker.valueProperty().addListener(it -> updateSegmentValues());

        InvalidationListener buildListener = it -> buildView();
        picker.getFields().addListener(buildListener);
        picker.minimumDurationProperty().addListener(buildListener);
        picker.maximumDurationProperty().addListener(buildListener);

        buildView();
        updateSegmentValues();
    }

    private Label createColon() {
        Label colon = new Label();
        colon.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        colon.setText(":");
        colon.getStyleClass().add("colon");
        colon.setAlignment(Pos.CENTER);
        return colon;
    }

    private void buildView() {
        Pane container = getContainer();
        container.getChildren().clear();

        ObservableList<ChronoUnit> fields = getSkinnable().getFields();
        for (int i = 0; i < fields.size(); i++) {

            ChronoUnit chronoUnit = fields.get(i);
            Segment<Duration, Long> segment = createSegment(chronoUnit);

            segment.valueProperty().addListener(it -> updateValue());
            segmentMap.put(chronoUnit, segment);
            container.getChildren().add(segment);

            HBox.setHgrow(segment, Priority.ALWAYS); // for horizontal orientation
            VBox.setVgrow(segment, Priority.ALWAYS); // for vertical orientation

            if (i < fields.size() - 1) {
                container.getChildren().add(new SegmentSeparator());
                container.getChildren().add(createColon());
                container.getChildren().add(new SegmentSeparator());
            }
        }
    }

    private Segment<Duration, Long> createSegment(ChronoUnit unit) {
        Segment<Duration, Long> field = new Segment<>(getSkinnable());
        final Duration maximumDuration = getSkinnable().getMaximumDuration();
        switch (unit) {
            default:
            case DAYS:
                addItems(field, maximumDuration.toDaysPart());
                break;
            case HOURS:
                if (maximumDuration.toDays() > 0) {
                    addItems(field, 23);
                } else {
                    addItems(field, maximumDuration.toHoursPart());
                }
                break;
            case MINUTES:
                if (maximumDuration.toHours() > 0) {
                    addItems(field, 59);
                } else {
                    addItems(field, maximumDuration.toMinutesPart());
                }
                break;
            case SECONDS:
                if (maximumDuration.toMinutes() > 0) {
                    addItems(field, 59);
                } else {
                    addItems(field, maximumDuration.toSecondsPart());
                }
                break;
            case MILLIS:
                if (maximumDuration.toSeconds() > 0) {
                    addItems(field, 999);
                } else {
                    addItems(field, maximumDuration.toMillisPart());
                }
                break;
        }

        field.setValue(0L);

        return field;
    }

    private void addItems(Segment<Duration, Long> field, long maxValue) {
        for (long i = 0; i <= maxValue; i++) {
            field.getItems().add(i);
        }
    }


    private void updateSegmentValues() {
        if (updatingValue) {
            return;
        }

        Duration duration = getSkinnable().getValue();
        if (duration != null) {
            for (ChronoUnit unit : getSkinnable().getFields()) {
                Segment<Duration, Long> segment = segmentMap.get(unit);
                switch (unit) {
                    case DAYS:
                        segment.setValue(duration.toDaysPart());
                        break;
                    case HOURS:
                        segment.setValue((long) duration.toHoursPart());
                        break;
                    case MINUTES:
                        segment.setValue((long) duration.toMinutesPart());
                        break;
                    case SECONDS:
                        segment.setValue((long) duration.toSecondsPart());
                        break;
                    case MILLIS:
                        segment.setValue((long) duration.toMillisPart());
                        break;
                }
            }
        }
    }

    private boolean updatingValue;

    private void updateValue() {
        updatingValue = true;
        try {

            Duration duration = Duration.ZERO;
            for (ChronoUnit unit : getSkinnable().getFields()) {
                Segment<Duration, Long> segment = segmentMap.get(unit);
                duration = duration.plus(segment.getValue(), unit);
            }

            getSkinnable().setValue(duration);
        } finally {
            updatingValue = false;
        }
    }
}
