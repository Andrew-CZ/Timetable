package mar0602.tamz.project.gui.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dao.ATable;
import mar0602.tamz.project.dao.DbContext;
import mar0602.tamz.project.dao.TableLessonTime;
import mar0602.tamz.project.dto.LessonTime;
import mar0602.tamz.project.gui.adapters.MyAdapter;
import mar0602.tamz.project.gui.dialogs.DialogEditLessonTime;
import mar0602.tamz.project.gui.adapters.LessonTimeAdapter;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class FragmentLessonTimes extends DbManagementFragment<LessonTime> {
    private DbContext dbContext;
    private SQLiteDatabase database;
    private LessonTimeAdapter adapter;

    public FragmentLessonTimes() {

    }

    @Override
    ATable<Integer, LessonTime> getTable(SQLiteDatabase database) {
        return new TableLessonTime(database);
    }

    @Override
    MyAdapter<LessonTime> createAdapter(List<LessonTime> data) {
        return new LessonTimeAdapter(data);
    }

    @Override
    void showEditDialog(int index, LessonTime item) {
        DialogEditLessonTime dialog = DialogEditLessonTime.newInstance(index, item);
        dialog.setTargetFragment(this, 1337);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    LessonTime getItemForAdd() {
        return new LessonTime(-1);
    }

    @Override
    boolean isItemForAdd(LessonTime item) {
        return item.getId() <= 0;
    }
}
