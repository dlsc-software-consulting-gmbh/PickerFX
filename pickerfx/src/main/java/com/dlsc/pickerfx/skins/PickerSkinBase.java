package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.Picker;
import com.dlsc.pickerfx.Segment;

import java.util.Collections;
import java.util.List;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PickerSkinBase<T extends Picker<?>> extends SkinBase<T> {

    private Pane container;

    private final Region indicator = new Region();
    private final Region topShadow = new Region();
    private final Region bottomShadow = new Region();

    public PickerSkinBase(T picker) {
        super(picker);

        indicator.getStyleClass().add("indicator");
        indicator.setManaged(false);
        indicator.setMouseTransparent(true);
        indicator.visibleProperty().bind(picker.showIndicatorProperty());

        topShadow.setManaged(false);
        topShadow.getStyleClass().addAll("shadow", "top");
        topShadow.setMouseTransparent(true);

        bottomShadow.setManaged(false);
        bottomShadow.getStyleClass().addAll("shadow", "bottom");
        bottomShadow.setMouseTransparent(true);

        picker.orientationProperty().addListener(it -> buildView());
        buildView();
    }

    private void buildView() {
        List<Node> children = Collections.emptyList();

        if (container != null) {
            children = container.getChildren();
        }

        if (getSkinnable().getOrientation().equals(Orientation.VERTICAL)) {
            HBox hBox = new HBox();
            hBox.getStyleClass().add("container");
            hBox.setFillHeight(true);
            hBox.setAlignment(Pos.CENTER);
            container = hBox;
        } else {
            VBox vBox = new VBox();
            vBox.getStyleClass().add("container");
            vBox.setFillWidth(true);
            vBox.setAlignment(Pos.CENTER);
            container = vBox;
        }

        container.getChildren().setAll(children);

        getChildren().setAll(container, topShadow, bottomShadow, indicator);
    }

    protected Pane getContainer() {
        return container;
    }

    protected void add(Segment<?,?> segment) {
        container.getChildren().add(segment);
    }

    protected void add(Node node) {
        container.getChildren().add(node);
    }

    protected void clear() {
        container.getChildren().clear();
    }

    public static class SegmentSeparator extends Region {

        public SegmentSeparator() {
            getStyleClass().add("segment-separator");
            setMaxHeight(Double.MAX_VALUE);
            HBox.setHgrow(this, Priority.ALWAYS);
        }
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);

        if (getSkinnable().getOrientation().equals(Orientation.VERTICAL)) {
            double middle = contentY + contentHeight / 2;
            double indicatorSize = getSkinnable().getIndicatorSize();
            indicator.resizeRelocate(contentX, middle - indicatorSize / 2, contentWidth, indicatorSize);

            double ps = topShadow.prefHeight(-1);
            topShadow.resizeRelocate(contentX, contentY, contentWidth, ps);

            ps = bottomShadow.prefHeight(-1);
            bottomShadow.resizeRelocate(contentX, contentY + contentHeight - ps, contentWidth, ps);
        } else {
            double middle = contentX + contentWidth / 2;
            double indicatorSize = getSkinnable().getIndicatorSize();
            indicator.resizeRelocate(middle - indicatorSize / 2, contentY, indicatorSize, contentHeight);

            double ps = topShadow.prefWidth(-1);
            topShadow.resizeRelocate(contentX, contentY, ps, contentHeight);

            ps = bottomShadow.prefWidth(-1);
            bottomShadow.resizeRelocate(contentX + contentWidth - ps, contentY, ps, contentHeight);
        }
    }
}
