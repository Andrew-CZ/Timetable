package mar0602.tamz.project.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mar0602.tamz.project.dto.LessonEntry;
import mar0602.tamz.project.dto.LessonTime;
import mar0602.tamz.project.dto.LessonType;
import mar0602.tamz.project.dto.Subject;
import mar0602.tamz.project.dto.Weekday;
import mar0602.tamz.project.utils.Utils;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class TableLessonEntry extends ATable<Integer, LessonEntry> {
    public static final String TABLE_NAME = "LessonEntries";
    public static final String COL_SUBJ_ID = "subjectId";
    public static final String COL_TIME_ID = "lessonTimeId";
    public static final String COL_DAY = "day";
    public static final String COL_TYPE = "type";
    public static final String COL_TEACHER = "teacher";
    public static final String COL_ROOM = "room";

    public TableLessonEntry(SQLiteDatabase context) {
        super(context);
    }

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_NAME + " ("
                + COL_SUBJ_ID + " INTEGER NOT NULL, "
                + COL_TIME_ID + " INTEGER NOT NULL, "
                + COL_DAY + " INTEGER NOT NULL, "
                + COL_TYPE + " INTEGER NOT NULL, "
                + COL_TEACHER + " TEXT NOT NULL, "
                + COL_ROOM + " TEXT NOT NULL, "
                + "PRIMARY KEY (" + COL_TIME_ID + ", " + COL_DAY + "), "
                + "FOREIGN KEY (" + COL_SUBJ_ID + ") REFERENCES " + TableSubject.TABLE_NAME + "(" + TableSubject.COL_ID + "), "
                + "FOREIGN KEY (" + COL_TIME_ID + ") REFERENCES " + TableLessonTime.TABLE_NAME + "(" + TableLessonTime.COL_ID + "))";
    }

    public static String getDropQuery() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    @Override
    public void validate(List<String> state, LessonEntry obj) {
        if (obj == null) {
            state.add("Object null");
        } else {
            if (obj.getSubject() == null || obj.getSubject().getId() <= 0)
                state.add("Invalid subject");

            if (obj.getTime() == null || obj.getTime().getId() <= 0)
                state.add("Invalid time");

            if (obj.getDay() == null) state.add("Invalid day");
            if (obj.getType() == null) state.add("Invalid type");

            if (Utils.isNullOrEmpty(obj.getTeacher()))
                state.add("Invalid teacher");

            if (Utils.isNullOrEmpty(obj.getRoom()))
                state.add("Invalid room");
        }
    }

    @Override
    public long insert(LessonEntry obj) {
        validateOrThrow(obj);

        ContentValues values = new ContentValues();
        values.put(COL_SUBJ_ID, obj.getSubject().getId());
        values.put(COL_TIME_ID, obj.getTime().getId());
        values.put(COL_DAY, obj.getDay().getValue());
        values.put(COL_TYPE, obj.getType().getValue());
        values.put(COL_TEACHER, obj.getTeacher());
        values.put(COL_ROOM, obj.getRoom());

        return context.insert(TABLE_NAME, null, values);
    }

    @Override
    public int update(LessonEntry obj) {
        validateOrThrow(obj);

        ContentValues values = new ContentValues();
        values.put(COL_SUBJ_ID, obj.getSubject().getId());
        values.put(COL_TYPE, obj.getType().getValue());
        values.put(COL_TEACHER, obj.getTeacher());
        values.put(COL_ROOM, obj.getRoom());

        return context.update(TABLE_NAME, values, COL_TIME_ID + " = ? AND " + COL_DAY + " = ?",
                new String[] { String.valueOf(obj.getTime().getId()), String.valueOf(obj.getDay().getValue()) });
    }

    public List<LessonEntry> getAll() {
        return getAll(null);
    }

    public List<LessonEntry> getAllByDay() {
        return getAll(COL_DAY + " ASC");
    }

    private List<LessonEntry> getAll(String order) {
        String[] cols = { COL_SUBJ_ID, COL_TIME_ID, COL_DAY, COL_TYPE, COL_TEACHER, COL_ROOM };
        Cursor c = context.query(TABLE_NAME, cols, null, null, null, null, order);
        List<LessonEntry> result = new ArrayList<>();
        try {
            while(c.moveToNext()) {
                int subjId = c.getInt(0);
                int timeId = c.getInt(1);
                int day = c.getInt(2);
                int type = c.getInt(3);
                String teacher = c.getString(4);
                String room = c.getString(5);

                result.add(new LessonEntry(new LessonTime(timeId), new Subject(subjId),
                        Weekday.get(day), LessonType.get(type), teacher, room));
            }
        } finally {
            c.close();
        }

        /*TableSubject table = new TableSubject(context);
        for (int i = 0, len = result.size(); i < len; i++) {
            LessonEntry entry = result.get(i);
            Subject subj = table.find(entry.getSubject().getId());
            entry.setSubject(subj);
        }*/

        return result;
    }

    public int deleteByTime(int lessonTimeId) {
        return delete(COL_TIME_ID + " = ?", String.valueOf(lessonTimeId));
    }

    public int deleteBySubject(int subjectId) {
        return delete(COL_SUBJ_ID + " = ?", String.valueOf(subjectId));
    }

    public int delete(LessonEntry entry) {
        return delete(entry.getTime(), entry.getDay());
    }

    public int delete(LessonTime time, Weekday day) {
        return delete(COL_TIME_ID + " = ? AND " + COL_DAY + " = ?",
                String.valueOf(time.getId()), String.valueOf(day.getValue()));

    }
    private int delete(String whereClause, String... params) {
        return context.delete(TABLE_NAME, whereClause, params);
    }
}
