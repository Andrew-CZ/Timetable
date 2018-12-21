package mar0602.tamz.project.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import mar0602.tamz.project.dto.LessonTime;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class TableLessonTime extends ATable<Integer,LessonTime> {
    public static final String TABLE_NAME = "LessonTimes";
    public static final String COL_ID = "id";
    public static final String COL_START = "start";
    public static final String COL_END = "end";

    public TableLessonTime(SQLiteDatabase context) {
        super(context);
    }

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + COL_START + " INTEGER NOT NULL,"
                + COL_END + " INTEGER NOT NULL)";
    }

    public static String getDropQuery() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    @Override
    public void validate(List<String> state, LessonTime obj) {
        if (obj == null) {
            state.add("Object null");
        } else {
            boolean startNull = obj.getStart() == null;
            if (startNull)
                state.add("Lesson start not specified");

            if (obj.getEnd() == null) {
                state.add("Lesson end not specified");
            } else if (!startNull) {
                long dif = obj.getEnd().getTime() - obj.getStart().getTime();
                if (dif <= 0) state.add("Invalid lesson time span");
            }
        }
    }

    @Override
    public long insert(LessonTime obj) {
        validateOrThrow(obj);

        ContentValues values = new ContentValues();
        values.put(COL_START, toLong(obj.getStart()));
        values.put(COL_END, toLong(obj.getEnd()));

        long id = context.insert(TABLE_NAME, null, values);
        obj.setId((int) id);
        return id;
    }

    @Override
    public int update(LessonTime obj) {
        validateOrThrow(obj);

        ContentValues values = new ContentValues();
        values.put(COL_START, toLong(obj.getStart()));
        values.put(COL_END, toLong(obj.getEnd()));

        return context.update(TABLE_NAME, values, COL_ID + " = ?",
                new String[] { String.valueOf(obj.getId()) });
    }

    public int delete(LessonTime time) {
        return delete(time.getId());
    }

    public int delete(Integer id) {
        new TableLessonEntry(context).deleteByTime(id);
        return context.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public List<LessonTime> getAll() {
        String[] cols = { COL_ID, COL_START, COL_END };
        String order = COL_START + " ASC";
        Cursor c = context.query(TABLE_NAME, cols, null, null, null, null, order);
        try {
            List<LessonTime> result = new ArrayList<>();
            while(c.moveToNext()) {
                int id = c.getInt(0);
                long start = c.getLong(1);
                long end = c.getLong(2);

                result.add(new LessonTime(id, getTime(start), getTime(end)));
            }

            return result;
        } finally {
            c.close();
        }
    }

    private long toLong(Time time) {
        return time.getTime();
    }

    private Time getTime(long time) {
        return new Time(time);
    }
}
