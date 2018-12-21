package mar0602.tamz.project.dto;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class LessonEntry {
    private Subject subject;
    private LessonTime time;

    private Weekday day;
    private LessonType type;

    private String teacher;
    private String room;

    public LessonEntry() {
        this(null, null, null, null, null, null);
    }

    public LessonEntry(LessonTime time, Subject subject, Weekday day, LessonType type, String teacher, String room) {
        this.time = time;
        this.subject = subject;
        this.day = day;
        this.type = type;
        this.teacher = teacher;
        this.room = room;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LessonTime getTime() {
        return time;
    }

    public void setTime(LessonTime time) {
        this.time = time;
    }

    public Weekday getDay() {
        return day;
    }

    public void setDay(Weekday day) {
        this.day = day;
    }

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
