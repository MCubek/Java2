package hr.fer.oprpp2.hw5.utils;

/**
 * Helper class with keys used for retreiving Objects from JSP context.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 09/04/2021
 */
public class Keys {
    private Keys() {
    }

    /**
     * Key used for getting server start time from context.
     */
    public static final String KEY_START_TIME = "start_time";

    /**
     * Key used for getting {@link hr.fer.oprpp2.hw5.utils.GlobalData} object from context.
     */
    public static final String KEY_GLOBAL_DATA = "global_data";
}
