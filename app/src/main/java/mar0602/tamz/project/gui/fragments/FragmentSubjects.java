package mar0602.tamz.project.gui.fragments;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import mar0602.tamz.project.dao.ATable;
import mar0602.tamz.project.dao.TableSubject;
import mar0602.tamz.project.dto.Subject;
import mar0602.tamz.project.gui.adapters.MyAdapter;
import mar0602.tamz.project.gui.adapters.SubjectsAdapter;
import mar0602.tamz.project.gui.dialogs.DialogEditSubject;

/**
 * @author Ondrej
 * @since 2018-12-19
 */
public class FragmentSubjects extends DbManagementFragment<Subject> {
    @Override
    ATable<Integer, Subject> getTable(SQLiteDatabase database) {
        return new TableSubject(database);
    }

    @Override
    MyAdapter<Subject> createAdapter(List<Subject> data) {
        return new SubjectsAdapter(data);
    }

    @Override
    void showEditDialog(int index, Subject item) {
        DialogEditSubject dialog = DialogEditSubject.newInstance(index, item);
        dialog.setTargetFragment(this, 1337);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    Subject getItemForAdd() {
        return new Subject(-1);
    }

    @Override
    boolean isItemForAdd(Subject item) {
        return item.getId() <= 0;
    }
}
