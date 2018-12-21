package mar0602.tamz.project.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class Subject extends SimpleEntity {
    private String name;
    private String abbreviation;

    public Subject() {
        this(0, null, null);
    }

    public Subject(int id) {
        this(id, null, null);
    }

    public Subject(int id, String name, String abbreviation) {
        super(id);
        this.name = name;
        this.abbreviation = abbreviation;
    }

    protected Subject(Parcel in) {
        super(in);
        name = in.readString();
        abbreviation = in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(abbreviation);
    }
}
