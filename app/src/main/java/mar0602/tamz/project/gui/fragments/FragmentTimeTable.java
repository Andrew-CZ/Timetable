package mar0602.tamz.project.gui.fragments;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.evrencoskun.tableview.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dao.DbContext;
import mar0602.tamz.project.dao.TableLessonEntry;
import mar0602.tamz.project.dao.TableLessonTime;
import mar0602.tamz.project.dao.TableSubject;
import mar0602.tamz.project.dto.LessonEntry;
import mar0602.tamz.project.dto.LessonTime;
import mar0602.tamz.project.dto.LessonType;
import mar0602.tamz.project.dto.Subject;
import mar0602.tamz.project.dto.Weekday;
import mar0602.tamz.project.gui.adapters.TableViewAdapter;
import mar0602.tamz.project.gui.dialogs.DialogEditLessonEntry;
import mar0602.tamz.project.utils.TableViewListenerAdapter;
import mar0602.tamz.project.utils.Utils;

public class FragmentTimeTable extends Fragment {
    private DbContext dbContext;
    private SQLiteDatabase database;
    private TableViewAdapter adapter;

    private int selectedColumn = -1, selectedRow = -1;
    private LessonEntry selectedItem = null;

    public FragmentTimeTable() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);

        TableView table = v.findViewById(R.id.tableView);
        adapter = new TableViewAdapter(getActivity());
        table.setAdapter(adapter);

        table.setTableViewListener(new TableViewListenerAdapter() {
            @Override
            public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
                openCellContextMenu(cellView, column, row);
            }
        });

        initAdapter(getDatabase(), adapter);

        return v;
    }

    private void openCellContextMenu(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        selectedColumn = column;
        selectedRow = row;
        selectedItem = adapter.getCellItem(column, row);

        PopupMenu popup = new PopupMenu(getActivity(), cellView.itemView);
        popup.getMenuInflater().inflate(selectedItem == null ? R.menu.menu_cell_empty : R.menu.menu_cell_lesson, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add:
                        addNewItem();
                    case R.id.menu_edit:
                        editSelectedItem();
                        break;
                    case R.id.menu_delete:
                        deleteSelectedItem();
                        break;
                }
                return false;
            }
        });

        popup.show();
    }

    private void addNewItem() {
        //LessonTime time, Subject subject, Weekday day, LessonType type, String teacher, String room
        LessonTime time = adapter.getColumnHeaderItem(selectedColumn);
        showEditDialog(true, new LessonEntry(time, new Subject(-1), Weekday.get(selectedRow +1),
                LessonType.LESSON, "", ""));
    }

    private void editSelectedItem() {
        if (selectedItem != null) showEditDialog(false, selectedItem);
    }

    private void deleteSelectedItem() {
        if (selectedItem != null) {
            Utils.showConfirmDeleteDialog(getActivity(), null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TableLessonEntry table = new TableLessonEntry(getDatabase());
                    table.delete(selectedItem);
                    adapter.changeCellItem(selectedColumn, selectedRow, null);
                }
            });
        }
    }

    private void showEditDialog(boolean isNew, LessonEntry item) {
        DialogEditLessonEntry dialog = DialogEditLessonEntry.newInstance(isNew, selectedColumn, selectedRow, item);
        dialog.setTargetFragment(this, 1337);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void initAdapter(SQLiteDatabase database, TableViewAdapter adapter) {
        List<Weekday> days = Arrays.asList(Weekday.MONDAY, Weekday.TUESDAY,
                Weekday.WEDNESDAY, Weekday.THURSDAY, Weekday.FRIDAY);

        List<LessonTime> times = new TableLessonTime(database).getAll();
        SparseIntArray timeMap = new SparseIntArray(times.size());
        for (int i = 0, len = times.size(); i < len ;i++)
            timeMap.put(times.get(i).getId(), i);

        List<List<LessonEntry>> data = new ArrayList<>(times.size());
        for (int i = 0, width = times.size(), height = days.size(); i < height; i++) {
            List<LessonEntry> row = new ArrayList<>(width);
            for (int j = 0; j < width; j++) row.add(null);

            data.add(row);
        }

        List<LessonEntry> entries = new TableLessonEntry(database).getAllByDay();
        SparseArray<Subject> subjects = new TableSubject(database).getAllAsMap();

        int lastY = -1;
        List<LessonEntry> lastRow = null;
        for (LessonEntry entry : entries) {
            entry.setSubject(subjects.get(entry.getSubject().getId()));
            int x = timeMap.get(entry.getTime().getId());
            int y = entry.getDay().getValue() -1;

            if (y != lastY) lastRow = data.get(y);
            lastRow.set(x, entry);
        }

        /*List<List<LessonEntry>> data = new ArrayList<>();
        int width = 5, height = 5;
        for (int i = 0; i < width; i++) {
            List<LessonEntry> row = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                row.add(new LessonEntry(null, new Subject(0, "Random", "rand"), null, LessonType.LESSON, i + "", j + ""));
            }

            data.add(row);
        }

        int[][] timeData = {
                { 5, 0, 6, 0 }, { 7, 0, 8, 0 }, { 9, 0, 10, 0 }, { 11, 0, 12, 0 }, { 13, 0, 14, 0 }
        };

        List<LessonTime> times = new ArrayList<>();
        for (int i = 0; i < timeData.length; i++) {
            int[] item = timeData[i];
            times.add(new LessonTime(i+1, new Time(item[0], item[1], 0),
                    new Time(item[2], item[3], 0)));
        }*/

        adapter.setAllItems(times, days, data);
    }

    public SQLiteDatabase getDatabase() {
        if (dbContext == null) {
            dbContext = new DbContext(getActivity());
            database = dbContext.getReadableDatabase();
        }

        return database;
    }

    @Override
    public void onDestroy() {
        if (database != null) database.close();
        if (dbContext != null) dbContext.close();
        super.onDestroy();
    }

    public void addItem(int column, int row, LessonEntry entry) {
        TableLessonEntry table = new TableLessonEntry(getDatabase());
        table.insert(entry);
        adapter.changeCellItem(column, row, entry);
    }

    public void updateItem(int column, int row, LessonEntry entry) {
        TableLessonEntry table = new TableLessonEntry(getDatabase());
        table.update(entry);
        adapter.changeCellItem(column, row, entry);
    }
}
