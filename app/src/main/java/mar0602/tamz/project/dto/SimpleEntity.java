package mar0602.tamz.project.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public abstract class SimpleEntity implements Parcelable {
    private int id;

    public SimpleEntity(int id) {
        this.id = id;
    }

    protected SimpleEntity(Parcel in) {
        id = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }
}
