package mar0602.tamz.project.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public class LessonEntryDataContainer implements Parcelable {
    private int subjectId;
    private int lessonTimeId;

    private Weekday day;
    private LessonType type;

    private String teacher;
    private String room;

    public LessonEntryDataContainer(int subjectId, int lessonTimeId, Weekday day, LessonType type, String teacher, String room) {
        this.subjectId = subjectId;
        this.lessonTimeId = lessonTimeId;
        this.day = day;
        this.type = type;
        this.teacher = teacher;
        this.room = room;
    }

    protected LessonEntryDataContainer(Parcel in) {
        subjectId = in.readInt();
        lessonTimeId = in.readInt();
        day = Weekday.get(in.readInt());
        type = LessonType.get(in.readInt());
        teacher = in.readString();
        room = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(subjectId);
        dest.writeInt(lessonTimeId);
        dest.writeInt(day.getValue());
        dest.writeInt(type.getValue());
        dest.writeString(teacher);
        dest.writeString(room);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LessonEntryDataContainer> CREATOR = new Creator<LessonEntryDataContainer>() {
        @Override
        public LessonEntryDataContainer createFromParcel(Parcel in) {
            return new LessonEntryDataContainer(in);
        }

        @Override
        public LessonEntryDataContainer[] newArray(int size) {
            return new LessonEntryDataContainer[size];
        }
    };

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getLessonTimeId() {
        return lessonTimeId;
    }

    public void setLessonTimeId(int lessonTimeId) {
        this.lessonTimeId = lessonTimeId;
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
