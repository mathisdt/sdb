package com.hoardersoft.util;

/**
 * Class with utility methods for dealing with numbers.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public class HSMathUtil {
    /**
     * Check the value of an integer is within a particular range.
     * @param value the value
     * @param min the minimum value
     * @param max the maximum value
     * @return the value - checked to be within the specified range
     */
    public static int checkRange(int value, int min, int max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /**
     * Check the value of a long is within a particular range.
     * @param value the value
     * @param min the minimum value
     * @param max the maximum value
     * @return the value - checked to be within the specified range
     */
    public static long checkRange(long value, long min, long max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /**
     * Check the value of a float is within a particular range.
     * @param value the value
     * @param min the minimum value
     * @param max the maximum value
     * @return the value - checked to be within the specified range
     */
    public static float checkRange(float value, float min, float max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /**
     * Check the value of a double is within a particular range.
     * @param value the value
     * @param min the minimum value
     * @param max the maximum value
     * @return the value - checked to be within the specified range
     */
    public static double checkRange(double value, double min, double max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }
}
