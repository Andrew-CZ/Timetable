package mar0602.tamz.project.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import mar0602.tamz.project.dto.Subject;
import mar0602.tamz.project.utils.Utils;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class TableSubject extends ATable<Integer, Subject> {
    public static final String TABLE_NAME = "Subjects";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_ABBR = "abbreviation";

    public TableSubject(SQLiteDatabase context) {
        super(context);
    }

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + COL_NAME + " TEXT NOT NULL,"
                + COL_ABBR + " TEXT NOT NULL)";
    }

    public static String getDropQuery() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    @Override
    public void validate(List<String> state, Subject obj) {
        if (obj == null) {
            state.add("Object null");
        } else {
            if (Utils.isNullOrEmpty(obj.getName()))
                state.add("Subject name not specified");

            if (Utils.isNullOrEmpty(obj.getAbbreviation()))
                state.add("Subject abbreviation not specified");
        }
    }

    @Override
    public long insert(Subject obj) {
        validateOrThrow(obj);

        ContentValues values = new ContentValues();
        values.put(COL_NAME, obj.getName());
        values.put(COL_ABBR, obj.getAbbreviation());

        long id = context.insert(TABLE_NAME, null, values);
        obj.setId((int) id);
        return id;
    }

    @Override
    public int update(Subject obj) {
        validateOrThrow(obj);

        ContentValues values = new ContentValues();
        values.put(COL_NAME, obj.getName());
        values.put(COL_ABBR, obj.getAbbreviation());

        return context.update(TABLE_NAME, values, COL_ID + " = ?",
                new String[] { String.valueOf(obj.getId()) });
    }

    public int delete(Subject subj) {
        return delete(subj.getId());
    }

    public int delete(Integer id) {
        new TableLessonEntry(context).deleteBySubject(id);
        return context.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public List<Subject> getAll() {
        String[] cols = { COL_ID, COL_NAME, COL_ABBR };
        String order = COL_NAME + " COLLATE NOCASE ASC";

        try (Cursor c = context.query(TABLE_NAME, cols, null,
                null, null, null, order)) {
            List<Subject> result = new ArrayList<>();
            while (c.moveToNext()) result.add(parseSubject(c));

            return result;
        }
    }

    public SparseArray<Subject> getAllAsMap() {
        String[] cols = { COL_ID, COL_NAME, COL_ABBR };
        String order = COL_NAME + " COLLATE NOCASE ASC";

        try (Cursor c = context.query(TABLE_NAME, cols, null,
                null, null, null, order)) {

            SparseArray<Subject> result = new SparseArray<>();

            while (c.moveToNext()) {
                Subject subj = parseSubject(c);
                result.put(subj.getId(), subj);
            }

            return result;
        }
    }

    public Subject find(int id) {
        String[] cols = { COL_ID, COL_NAME, COL_ABBR };

        try (Cursor c = context.query(TABLE_NAME, cols, null,
                null, null, null, null)) {
            if (c.moveToNext()) return parseSubject(c);
        }

        return null;
    }

    private Subject parseSubject(Cursor c) {
        int id = c.getInt(0);
        String name = c.getString(1);
        String abbr = c.getString(2);

        return new Subject(id, name, abbr);
    }
}
