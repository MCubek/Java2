package hr.fer.oprpp2.hw5.utils;

import java.util.Random;

/**
 * Class containing global data for all servlets to use.
 * Data here is declared only once per aplication.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 09/04/2021
 */
public class GlobalData {
    private final Random random = new Random();

    public GlobalData() {
    }

    /**
     * Thread safe method for generating random numbers from interval.
     *
     * @param lowerBound lower inclusive bound
     * @param upperBound upper inclusive bound
     * @return random integer from range
     */
    public synchronized int createRandomInt(int lowerBound, int upperBound) {
        if (lowerBound > upperBound)
            throw new IllegalArgumentException(String.format("lowerBound (was %d) can not be bigger than upper bound (was %d)!", lowerBound, upperBound));
        int intervalSize = upperBound - lowerBound + 1;
        if (intervalSize == 1) return lowerBound;
        return lowerBound + random.nextInt(intervalSize);
    }
}
