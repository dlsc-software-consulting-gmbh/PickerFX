package com.dlsc.pickerfx;

import com.dlsc.pickerfx.util.ControlsUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The base class of all picker controls.
 *
 * @param <T> the type of the items displayed by the picker.
 */
public abstract class Picker<T> extends Control {

    private static final PseudoClass PSEUDO_CLASS_VERTICAL = PseudoClass.getPseudoClass("vertical");

    private static final PseudoClass PSEUDO_CLASS_HORIZONTAL = PseudoClass.getPseudoClass("horizontal");

    protected Picker() {
        getStyleClass().add("picker");
        getStylesheets().add(Picker.class.getResource("picker.css").toExternalForm());
        setMaxWidth(Region.USE_PREF_SIZE);
        setMaxHeight(Region.USE_PREF_SIZE);
        listenForInvalidChanges();
        // TODO: replace
        ControlsUtil.bindBooleanToPseudoclass(this, invalid, PseudoClass.getPseudoClass("invalid"));
        ControlsUtil.bindBooleanToPseudoclass(this, readOnly, PseudoClass.getPseudoClass("disabled"));
        pseudoClassStateChanged(PSEUDO_CLASS_VERTICAL, true);
    }

    /**
     * The value currently specified by the picker.
     */
    private final ObjectProperty<T> value = new SimpleObjectProperty<>(this, "value");

    public final ObjectProperty<T> valueProperty() {
        return value;
    }

    public final T getValue() {
        return valueProperty().get();
    }

    public final void setValue(T value) {
        valueProperty().set(value);
    }

    /**
     * A flag used to signal whether the current settings made by the user define a valid or invalid value.
     * E.g. in the {@see LocalDatePicker} the date set by the user could be February 30th, hence "invalid" as this
     * date never exists.
     */
    private final ReadOnlyBooleanWrapper invalid = new ReadOnlyBooleanWrapper(this, "invalid");

    public final ReadOnlyBooleanProperty invalidProperty() {
        return invalid.getReadOnlyProperty();
    }

    public final boolean isInvalid() {
        return invalid.get();
    }

    private void setInvalid(boolean invalid) {
        this.invalid.set(invalid);
    }

    public final BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    /**
     * A flag used to mark the picker as a "read-only" picker. The user will not be able to change the
     * value.
     */
    private final BooleanProperty readOnly = new SimpleBooleanProperty(this, "readOnly");

    public final boolean isReadOnly() {
        return readOnlyProperty().get();
    }

    public final void setReadOnly(boolean readOnly) {
        readOnlyProperty().set(readOnly);
    }

    /**
     * Controls whether the picker will display the "indicator" area, which is the "glass" that marks the
     * currently selected value.
     */
    private final BooleanProperty showIndicator = new SimpleBooleanProperty(this, "showIndicator", true);

    public boolean isShowIndicator() {
        return showIndicator.get();
    }

    public BooleanProperty showIndicatorProperty() {
        return showIndicator;
    }

    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator.set(showIndicator);
    }

    /**
     * Specifies the height / width of the indicator glass node.
     */
    private DoubleProperty indicatorSize;

    public final DoubleProperty indicatorSizeProperty() {
        if (indicatorSize == null) {
            indicatorSize = new StyleableDoubleProperty(20) {

                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return Picker.this;
                }

                @Override
                public String getName() {
                    return "indicatorSize";
                }

                @Override
                public CssMetaData<Picker, Number> getCssMetaData() {
                    return StyleableProperties.INDICATOR_SIZE;
                }
            };
        }
        return indicatorSize;
    }

    public final void setIndicatorSize(double value) {
        indicatorSizeProperty().set(value);
    }

    public final double getIndicatorSize() {
        return indicatorSize == null ? 70 : indicatorSize.get();
    }

    /**
     * Controls the orientation of the picker (horizontal, vertical).
     */
    private ObjectProperty<Orientation> orientation;

    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    public final Orientation getOrientation() {
        return orientation == null ? Orientation.VERTICAL : orientation.get();
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (orientation == null) {

            orientation = new StyleableObjectProperty<Orientation>(Orientation.VERTICAL) {
                @Override
                public void invalidated() {
                    final boolean active = (get() == Orientation.VERTICAL);
                    pseudoClassStateChanged(PSEUDO_CLASS_VERTICAL, active);
                    pseudoClassStateChanged(PSEUDO_CLASS_HORIZONTAL, !active);
                }

                @Override
                public CssMetaData<Picker<?>, Orientation> getCssMetaData() {
                    return Picker.StyleableProperties.ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return Picker.this;
                }

                @Override
                public String getName() {
                    return "orientation";
                }
            };
        }
        return orientation;
    }

    /**
     * Controls the width / height of the picker cells.
     *
     * @return the cell size
     */
    public final DoubleProperty cellSizeProperty() {
        if (cellSize == null) {
            cellSize = new StyleableDoubleProperty() {
                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return Picker.this;
                }

                @Override
                public String getName() {
                    return "cellSize";
                }

                @Override
                public CssMetaData<Picker, Number> getCssMetaData() {
                    return Picker.StyleableProperties.CELL_SIZE;
                }
            };
        }
        return cellSize;
    }

    private DoubleProperty cellSize;

    public final void setCellSize(double value) {
        cellSizeProperty().set(value);
    }

    public final double getCellSize() {
        return cellSize == null ? 50 : cellSize.get();
    }

    private static class StyleableProperties {

        private static final CssMetaData<Picker, Number> CELL_SIZE = new CssMetaData<Picker, Number>(
                "-fx-cell-size", SizeConverter.getInstance(), 50d) {
            @Override
            public boolean isSettable(Picker node) {
                return node.cellSize == null || !node.cellSize.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Picker node) {
                return (StyleableProperty<Number>) node.cellSizeProperty();
            }
        };

        private static final CssMetaData<Picker, Number> INDICATOR_SIZE = new CssMetaData<Picker, Number>(
                "-fx-indicator-size", SizeConverter.getInstance(), 30d) {

            @Override
            public boolean isSettable(Picker node) {
                return node.indicatorSize == null || !node.indicatorSize.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Picker node) {
                return (StyleableProperty<Number>) node.indicatorSizeProperty();
            }
        };


        private static final CssMetaData<Picker<?>, Orientation> ORIENTATION =
                new CssMetaData<Picker<?>, Orientation>("-fx-orientation",
                        new EnumConverter<>(Orientation.class),
                        Orientation.VERTICAL) {

                    @Override
                    public Orientation getInitialValue(Picker<?> node) {
                        return node.getOrientation();
                    }

                    @Override
                    public boolean isSettable(Picker<?> n) {
                        return n.orientation == null || !n.orientation.isBound();
                    }

                    @Override
                    public StyleableProperty<Orientation> getStyleableProperty(Picker<?> n) {
                        return (StyleableProperty<Orientation>) n.orientationProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());

            styleables.add(CELL_SIZE);
            styleables.add(INDICATOR_SIZE);
            styleables.add(ORIENTATION);

            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return Picker.StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    private void listenForInvalidChanges() {
        getProperties().addListener((MapChangeListener<Object, Object>) change -> {
            if (change.getKey().equals("invalid")) {
                Boolean value = (Boolean) change.getValueAdded();
                setInvalid(Boolean.TRUE.equals(value));
            }
        });
    }



}
