package mar0602.tamz.project.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class LessonTime extends SimpleEntity {
    private Time start, end;

    public LessonTime() {
        this(0, null, null);
    }

    public LessonTime(int id) {
        this(id, null, null);
    }

    public LessonTime(int id, Time start, Time end) {
        super(id);
        this.start = start;
        this.end = end;
    }

    protected LessonTime(Parcel in) {
        super(in);
        start = readTime(in);
        end = readTime(in);
    }

    public static final Creator<LessonTime> CREATOR = new Creator<LessonTime>() {
        @Override
        public LessonTime createFromParcel(Parcel in) {
            return new LessonTime(in);
        }

        @Override
        public LessonTime[] newArray(int size) {
            return new LessonTime[size];
        }
    };

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        writeTime(dest, start);
        writeTime(dest, end);
    }

    private void writeTime(Parcel dest, Time time) {
        if (time == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(time.getTime());
        }
    }

    private Time readTime(Parcel in) {
        return in.readByte() == 0 ? null : new Time(in.readLong());
    }
}
