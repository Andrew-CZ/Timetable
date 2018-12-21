package mar0602.tamz.project.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mar0602.tamz.project.utils.EntityValidationFailedException;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public abstract class ATable<K, V> {
    protected SQLiteDatabase context;

    public ATable(SQLiteDatabase context) {
        this.context = context;
    }

    public abstract void validate(List<String> state, V obj);
    public abstract long insert(V obj);
    public abstract int update(V obj);
    public abstract List<V> getAll();
    public abstract int delete(V item);

    public final List<String> validate(V obj) {
        ArrayList<String> state = new ArrayList<>();

        validate(state, obj);

        return state;
    }

    public final void validateOrThrow(V obj) {
        List<String> state = validate(obj);

        if (!state.isEmpty())
            throw new EntityValidationFailedException(state);
    }
}
