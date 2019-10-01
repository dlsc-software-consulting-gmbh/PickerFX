package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.Segment;
import com.dlsc.pickerfx.SegmentCell;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;

public class SegmentSkin<T, S> extends SkinBase<Segment<T, S>> {

    private boolean updatingValue;

    private double STEPS = 100;

    private double mouseStartX;

    private double mouseStartY;

    private final DoubleProperty location = new SimpleDoubleProperty(this, "location", -1) {
        @Override
        public void set(double newValue) {
            if (!getSkinnable().isWrapItems()) {
                newValue = Math.max(0, Math.min(newValue, (getSkinnable().getItems().size() - 1) * STEPS));
            }
            super.set(newValue);
        }
    };

    private boolean dragging;

    private FixerService fixerService;

    // inertia = an already finished gesture
    private boolean inertia;

    public SegmentSkin(Segment<T, S> control) {
        super(control);

        fixerService = new FixerService(this);

        control.addEventFilter(ScrollEvent.SCROLL, evt -> {
            if (control.isReadOnly()) {
                return;
            }

            double newLocation;

            if (control.getPicker().getOrientation().equals(Orientation.VERTICAL)) {
                newLocation = location.get() - evt.getDeltaY();
            }
            else {
                newLocation = location.get() - evt.getDeltaX();
            }

            scrollTo(newLocation);
            inertia = evt.isInertia();
            fixerService.restart();

            evt.consume();
        });

        control.addEventFilter(ScrollEvent.SCROLL_FINISHED, evt -> {
        });

        control.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            if (control.isReadOnly()) {
                return;
            }

            mouseStartX = evt.getX();
            mouseStartY = evt.getY();
            dragging = false;
        });

        control.addEventFilter(MouseEvent.MOUSE_DRAGGED, evt -> {
            if (control.isReadOnly()) {
                return;
            }

            if (mouseStartY >= 0 || mouseStartX >= 0) {
                dragging = true;

                switch (control.getPicker().getOrientation()) {
                    case VERTICAL:
                        scrollTo(location.get() - (evt.getY() - mouseStartY) * (STEPS / control.getPicker().getCellSize()));
                        break;
                    case HORIZONTAL:
                        scrollTo(location.get() - (evt.getX() - mouseStartX) * (STEPS / control.getPicker().getCellSize()));
                        break;
                }

                mouseStartX = evt.getX();
                mouseStartY = evt.getY();
            }
        });

        control.addEventFilter(MouseEvent.MOUSE_RELEASED, evt -> {
            if (dragging) {
                snapLocation();
            }
        });

        final InvalidationListener layoutListener = obs -> getSkinnable().requestLayout();

        control.getPicker().orientationProperty().addListener(layoutListener);
        control.cellFactoryProperty().addListener(layoutListener);
        control.getItems().addListener(layoutListener);

        InvalidationListener locationListener = it -> {
            updatingValue = true;
            try {
                control.setValue(control.getItems().get(getValueIndexForLocation()));
                getSkinnable().requestLayout();
            } finally {
                updatingValue = false;
            }
        };

        location.addListener(locationListener);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(control.widthProperty());
        clip.heightProperty().bind(control.heightProperty());
        control.setClip(clip);

        control.valueProperty().addListener(it -> {
            if (!updatingValue) {
                scrollToCurrentValue();
            }
        });

        scrollToCurrentValue();
    }

    private boolean isInertia() {
        return inertia;
    }

    static class FixerService extends Service<Void> {

        private final SegmentSkin<?, ?> segmentSkin;

        FixerService(SegmentSkin<?, ?> skin) {
            this.segmentSkin = skin;
        }

        @Override
        protected Task<Void> createTask() {
            return new FixerTask(segmentSkin);
        }
    }

    static class FixerTask extends Task<Void> {

        private final SegmentSkin<?, ?> segmentSkin;

        FixerTask(SegmentSkin<?, ?> skin) {
            this.segmentSkin = skin;
        }

        @Override
        protected Void call() throws Exception {
            // If the last scroll event was an "inertia" event, then we do not have to wait so long
            // before snapping the location.
            Thread.sleep(segmentSkin.isInertia() ? 100 : 1000);
            if (!isCancelled()) {
                segmentSkin.snapLocation();
            }
            return null;
        }
    }

    private void scrollToCurrentValue() {
        if (getSkinnable().getValue() != null) {
            int index = getSkinnable().getItems().indexOf(getSkinnable().getValue());
            scrollTo(index * STEPS);
        }
        else {
            scrollTo(0);
        }
    }

    private void snapLocation() {
        final double a = location.get() - location.get() % STEPS;
        double b;

        if (location.get() > 0) {
            b = a + STEPS;
        } else {
            b = a - STEPS;
        }

        if (Math.abs(a - location.get()) < Math.abs(b - location.get())) {
            scrollToAnimated(a);
        } else {
            scrollToAnimated(b);
        }
    }

    private Timeline timeline;

    private void scrollTo(double loc) {
        location.set(loc);
    }

    private void scrollToAnimated(double loc) {
        if (timeline != null) {
            timeline.stop();
        }
        KeyValue keyValue = new KeyValue(location, loc, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
        timeline = new Timeline(keyFrame);
        timeline.play();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        getChildren().removeIf(node -> node instanceof SegmentCell);

        final Segment<T, S> segment = getSkinnable();
        final Callback<Segment<T, S>, SegmentCell<S>> cellFactory = segment.getCellFactory();
        final ObservableList<S> items = segment.getItems();
        final double cellSize = segment.getPicker().getCellSize();

        if (getSkinnable().getPicker().getOrientation().equals(Orientation.VERTICAL)) {
            layoutChildrenVertically(contentX, contentY, contentWidth, contentHeight, segment, cellFactory, items, cellSize);
        }
        else {
            layoutChildrenHorizontally(contentX, contentY, contentWidth, contentHeight, segment, cellFactory, items, cellSize);
        }
    }

    private void layoutChildrenVertically(double contentX, double contentY, double contentWidth, double contentHeight, Segment<T, S> tumbler, Callback<Segment<T, S>, SegmentCell<S>> cellFactory, ObservableList<S> items, double cellSize) {
        final double x = snapPosition(contentX);

        double middle = contentY + contentHeight / 2;

        int index = getIndexForLocation();
        double offset = getOffset();

        int i = index;
        int position = 0;

        double loc = middle;

        final boolean wrapItems = getSkinnable().isWrapItems();

        do {
            layoutCell(contentX, contentY, contentWidth, contentHeight, x, loc, offset, tumbler, cellFactory, items, i++, position++);
            loc += cellSize;

            if (i >= items.size() && wrapItems) {
                i = 0;
            }

        } while (loc < contentHeight + cellSize && (wrapItems || (i >= 0 && i < getSkinnable().getItems().size())));

        loc = middle - cellSize;
        position = 1;

        i = index - 1;

        if (i < 0 && wrapItems) {
            i = getSkinnable().getItems().size() - 1;
        }

        if (i >= 0) {
            do {
                layoutCell(contentX, contentY, contentWidth, contentHeight, x, loc, offset, tumbler, cellFactory, items, i--, position--);
                loc -= cellSize;

                if (i < 0 && wrapItems) {
                    i = items.size() - 1;
                }
            } while (loc > contentY - cellSize && (wrapItems || (i >= 0 && i < getSkinnable().getItems().size())));
        }
    }

    private void layoutChildrenHorizontally(double contentX, double contentY, double contentWidth, double contentHeight, Segment<T, S> tumbler, Callback<Segment<T, S>, SegmentCell<S>> cellFactory, ObservableList<S> items, double cellSize) {
        final double y = snapPosition(contentY);

        double middle = contentX + contentWidth / 2;

        int index = getIndexForLocation();
        double offset = getOffset();

        int i = index;
        int position = 0;

        double loc = middle;

        final boolean wrapItems = getSkinnable().isWrapItems();

        do {
            layoutCell(contentX, contentY, contentWidth, contentHeight, loc, y, offset, tumbler, cellFactory, items, i++, position++);
            loc += cellSize;

            if (i >= items.size() && wrapItems) {
                i = 0;
            }

        } while (loc < contentWidth + cellSize && (wrapItems || (i >= 0 && i < getSkinnable().getItems().size())));

        loc = middle - cellSize;
        position = 1;

        i = index - 1;

        if (i < 0 && wrapItems) {
            i = getSkinnable().getItems().size() - 1;
        }

        if (i >= 0) {
            do {
                layoutCell(contentX, contentY, contentWidth, contentHeight, loc, y, offset, tumbler, cellFactory, items, i--, position--);
                loc -= cellSize;

                if (i < 0 && wrapItems) {
                    i = items.size() - 1;
                }
            } while (loc > contentX - cellSize && (wrapItems || (i >= 0 && i < getSkinnable().getItems().size())));
        }
    }

    private void layoutCell(double contentX, double contentY, double contentWidth, double contentHeight, double x, double y,
                            double offset, Segment<T, S> segment, Callback<Segment<T, S>, SegmentCell<S>> cellFactory, ObservableList<S> items,
                            int index, int position) {

        final SegmentCell<S> cell = cellFactory.call(segment);

        // Important to add cell first before calling setters and update on it,
        // otherwise layout code gets invoked indefinitely.
        getChildren().add(cell);

        cell.update(items.get(index));
        cell.setIndex(index);
        cell.setPosition(position);
        cell.setManaged(false);
        cell.setMouseTransparent(true);
        cell.setSelected(getValueIndexForLocation() == index);


        final double cellSize = segment.getPicker().getCellSize();
        if (segment.getPicker().getOrientation().equals(Orientation.VERTICAL)) {
            double cellLocation = snapPosition(y - cellSize / 2 - offset * cellSize);
            double middle = (contentY + contentHeight) / 2;
            double distance = Math.abs(((cellLocation - middle) + cellSize / 2.0) / cellSize); // returns 0, 1, 2, 3, ....
            cell.setOpacity(1.0 - Math.min(.9, distance * .3));
            cell.resizeRelocate(x, cellLocation, contentWidth, cellSize);
        } else {
            double cellLocation = snapPosition(x - cellSize / 2 - offset * cellSize);
            double middle = (contentX + contentWidth) / 2;
            double distance = Math.abs(((cellLocation - middle) + cellSize / 2.0) / cellSize); // returns 0, 1, 2, 3, ....
            cell.setOpacity(1.0 - Math.min(.9, distance * .3));
            cell.resizeRelocate(cellLocation, y, cellSize, contentHeight);
        }
    }

    private int getIndexForLocation() {
        int index = (((int) (location.get() / STEPS)) % getSkinnable().getItems().size());
        if (index < 0) {
            index = getSkinnable().getItems().size() + index;
        }

        return index;
    }

    private int getValueIndexForLocation() {
        int index;

        if (location.get() >= 0) {
            index = (((int) ((location.get() + STEPS / 2) / STEPS)) % getSkinnable().getItems().size());
        } else {
            index = (((int) ((location.get() - STEPS / 2) / STEPS)) % getSkinnable().getItems().size());
        }

        if (index < 0) {
            index = getSkinnable().getItems().size() + index;
        }

        return index;
    }

    private double getOffset() {
        return (location.get() % STEPS) / STEPS;
    }
}
