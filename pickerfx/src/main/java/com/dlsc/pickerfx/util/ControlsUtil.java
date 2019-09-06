package com.dlsc.pickerfx.util;

import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;


/**
 * Utility methods for JavaFX controls.
 */
public final class ControlsUtil {

    private ControlsUtil() {
    }

    /**
     * Binds a boolean property to a style class in the given node.  Doing this the style class is switched on/off
     * depending on the boolean property.
     *
     * @param node The node which the style class will be applied to.
     * @param booleanProperty The flag to switch on/off.
     * @param styleClass The style class to be applied.
     */
    public static void bindBooleanToStyleClass(Node node, ObservableValue<Boolean> booleanProperty, String styleClass) {
        booleanProperty.addListener((obs, oldV, newV) -> {
            if (Boolean.TRUE.equals(booleanProperty.getValue())) {
                if (!node.getStyleClass().contains(styleClass)) {
                    node.getStyleClass().add(styleClass);
                }
            } else {
                node.getStyleClass().remove(styleClass);
            }
        });
    }

    /**
     * Binds a boolean property to a pseudo class.  Doing this the pseudo class is switched on/off in the given node.
     *
     * @param node The node which the pseudo class will be applied to.
     * @param booleanProperty The flag to switch on/off.
     * @param pseudoClass The style class to be applied.
     */
    public static void bindBooleanToPseudoclass(Node node, ObservableValue<Boolean> booleanProperty, PseudoClass pseudoClass) {
        booleanProperty.addListener((obs, oldV, newV) -> node.pseudoClassStateChanged(pseudoClass, Boolean.TRUE.equals(booleanProperty.getValue())));
    }
}
