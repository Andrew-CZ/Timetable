package mar0602.tamz.project.dto;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public enum LessonType {
    LESSON(1), LECTURE(2);

    private int value;
    LessonType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LessonType get(int value) {
        switch(value) {
            case 1: return LESSON;
            case 2: return LECTURE;
            default: throw new IllegalArgumentException();
        }
    }
}
