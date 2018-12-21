package mar0602.tamz.project.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;

import mar0602.tamz.project.dto.LessonEntry;
import mar0602.tamz.project.dto.LessonTime;
import mar0602.tamz.project.dto.LessonType;
import mar0602.tamz.project.dto.Subject;
import mar0602.tamz.project.dto.Weekday;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class DbContext extends SQLiteOpenHelper implements AutoCloseable {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ProjectTAMZ2.db";

    public DbContext(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableLessonTime.getCreateQuery());
        db.execSQL(TableSubject.getCreateQuery());
        db.execSQL(TableLessonEntry.getCreateQuery());

        seed(db);
    }

    private void seed(SQLiteDatabase db) {
        {
            TableSubject tb = new TableSubject(db);
            tb.insert(new Subject(1, "Architektura technologie .NET", "AT.NET"));
            tb.insert(new Subject(2, "Java technologie", "JAT"));
            tb.insert(new Subject(3, "Počítačové sítě", "POS"));
            tb.insert(new Subject(4, "Tvorba aplikací pro mobilní zařízení II", "TAMZ II"));
            tb.insert(new Subject(5, "Vývoj informačních systémů", "VIS"));
            tb.insert(new Subject(6, "Vývoj internetových aplikací", "VIA"));
        }

        {
            int[][] times = {
                    { 7, 15, 8, 45 }, { 9, 0, 10, 30 }, { 10, 45, 12, 15 }, { 12, 30, 14, 0 },
                    { 14, 15, 15, 45}, { 16, 0, 17, 30 }, { 17, 45, 19, 15 } };

            TableLessonTime table = new TableLessonTime(db);
            for (int[] item : times) {
                table.insert(new LessonTime(0, new Time(item[0], item[1], 0), new Time(item[2], item[3], 0)));
            }
        }

        {
            TableLessonEntry table = new TableLessonEntry(db);
            table.insert(new LessonEntry(new LessonTime(2), new Subject(6), Weekday.MONDAY, LessonType.LESSON, "M. Radecký", "PORRV203"));
            table.insert(new LessonEntry(new LessonTime(4), new Subject(6), Weekday.MONDAY, LessonType.LECTURE, "M. Radecký", "POREC2"));
            table.insert(new LessonEntry(new LessonTime(5), new Subject(2), Weekday.MONDAY, LessonType.LESSON, "D. Ježek", "POREB208"));
            table.insert(new LessonEntry(new LessonTime(1), new Subject(3), Weekday.TUESDAY, LessonType.LESSON, "R. Kunčický", "POREB425")); //kjh
            table.insert(new LessonEntry(new LessonTime(2), new Subject(1), Weekday.TUESDAY, LessonType.LESSON, "J. Janoušek", "PORRV203"));
            table.insert(new LessonEntry(new LessonTime(5), new Subject(5), Weekday.TUESDAY, LessonType.LECTURE, "M. Kudělka", "POREC1"));
            table.insert(new LessonEntry(new LessonTime(6), new Subject(5), Weekday.TUESDAY, LessonType.LESSON, "V. Kotík", "POREB104")); //kjhkjh
            table.insert(new LessonEntry(new LessonTime(1), new Subject(4), Weekday.WEDNESDAY, LessonType.LESSON, "M. Krumnikl", "POREB113"));
            table.insert(new LessonEntry(new LessonTime(2), new Subject(2), Weekday.WEDNESDAY, LessonType.LECTURE, "D. Ježek", "POREB330"));
            table.insert(new LessonEntry(new LessonTime(3), new Subject(1), Weekday.WEDNESDAY, LessonType.LECTURE, "J. Janoušek", "POREC3"));
            table.insert(new LessonEntry(new LessonTime(4), new Subject(4), Weekday.WEDNESDAY, LessonType.LECTURE, "M. Krumnikl", "POREC1"));
            table.insert(new LessonEntry(new LessonTime(5), new Subject(3), Weekday.WEDNESDAY, LessonType.LECTURE, "P. Moravec", "POREC1"));
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TableLessonEntry.getDropQuery());
        db.execSQL(TableSubject.getDropQuery());
        db.execSQL(TableLessonTime.getDropQuery());
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
