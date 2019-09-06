package com.dlsc.pickerfx.skins;

import com.dlsc.pickerfx.DigitsSegment;
import com.dlsc.pickerfx.IntegerPicker;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;

import java.util.ArrayList;
import java.util.List;

public class IntegerPickerSkin extends PickerSkinBase<IntegerPicker> {

    private List<DigitsSegment> digitsPickerList = new ArrayList<>();

    public IntegerPickerSkin(IntegerPicker picker) {
        super(picker);

        final InvalidationListener buildListener = it -> buildView();
        picker.numberOfDigitsProperty().addListener(buildListener);
        picker.valueProperty().addListener(it -> updateSegments());

        buildView();

        updateSegments();
    }

    private InvalidationListener updateValueListener = it -> updateValue();

    private WeakInvalidationListener weakUpdateValueListener = new WeakInvalidationListener(updateValueListener);

    private void buildView() {
        clear();
        digitsPickerList.clear();

        final int numberOfDigits = getSkinnable().getNumberOfDigits();

        for (int i = 0; i < numberOfDigits; i++) {
            DigitsSegment digitsPicker = new DigitsSegment(getSkinnable());
            digitsPicker.cellFactoryProperty().bind(getSkinnable().cellFactoryProperty());
            digitsPicker.setValue(0);
            digitsPicker.valueProperty().addListener(weakUpdateValueListener);
            digitsPickerList.add(digitsPicker);
            add(digitsPicker);
            if (i < numberOfDigits - 1) {
                add(new SegmentSeparator());
            }
        }
    }

    private boolean updatingValue;

    private void updateValue() {
        try {
            updatingValue = true;
            long power = digitsPickerList.size() - 1;

            int value = 0;

            for (DigitsSegment segment : digitsPickerList) {
                Integer v = segment.getValue();
                if (v == null) {
                    v = 0;
                }

                value = value + (v * (int) Math.pow(10, power--));
            }

            getSkinnable().setValue(value);
        }
        finally {
            updatingValue = false;
        }
    }

    private void updateSegments() {
        if (!updatingValue) {
            Integer value = getSkinnable().getValue();
            if (value == null) {
                value = 0;
            }

            String valueStr = String.valueOf(value);
            char[] chars = valueStr.toCharArray();

            int i = chars.length - 1;
            int j = digitsPickerList.size() - 1;

            while(i >= 0 && j >= 0) {
                int val = Integer.valueOf(String.valueOf(chars[i]));
                DigitsSegment segment = digitsPickerList.get(j);
                segment.setValue(val);
                i--;
                j--;
            }
        }
    }
}
