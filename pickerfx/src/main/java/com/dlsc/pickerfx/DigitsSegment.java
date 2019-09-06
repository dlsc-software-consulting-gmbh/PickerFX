package com.dlsc.pickerfx;

/**
 * A concrete implementation of a segment which will always display the digits 0 .. 9.
 * This control is used by the picker skins.
 */
public class DigitsSegment extends Segment<Integer, Integer> {

    /**
     * Constructs a new segment with the given picker parent.
     *
     * @param picker the parent picker control
     */
    public DigitsSegment(Picker picker) {
        super(picker, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }
}
