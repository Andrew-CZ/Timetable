package mar0602.tamz.project.dto;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public enum Weekday {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5);

    private int value;
    Weekday(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Weekday get(int value) {
        switch(value) {
            case 1: return MONDAY;
            case 2: return TUESDAY;
            case 3: return WEDNESDAY;
            case 4: return THURSDAY;
            case 5: return FRIDAY;
            default: throw new IllegalArgumentException();
        }
    }
}
