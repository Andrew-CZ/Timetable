package mar0602.tamz.project.gui.fragments;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import java.util.List;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dao.ATable;
import mar0602.tamz.project.dao.DbContext;
import mar0602.tamz.project.dto.SimpleEntity;
import mar0602.tamz.project.gui.adapters.MyAdapter;
import mar0602.tamz.project.utils.OnItemClickListener;
import mar0602.tamz.project.utils.Utils;

/**
 * @author Ondrej
 * @since 2018-12-19
 */
public abstract class DbManagementFragment<T extends SimpleEntity> extends Fragment {
    private DbContext context;
    private SQLiteDatabase database;

    private MyAdapter<T> adapter;

    private int selectedIndex;
    private T selectedItem;

    abstract ATable<Integer, T> getTable(SQLiteDatabase database);
    abstract MyAdapter<T> createAdapter(List<T> data);
    abstract void showEditDialog(int index, T item);
    abstract T getItemForAdd();
    abstract boolean isItemForAdd(T item);

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_database_management, container, false);

        List<T> data = getTable(getDatabase()).getAll();
        adapter = createAdapter(data);

        RecyclerView view = v.findViewById(R.id.recyclerView);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        view.setAdapter(adapter);
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new OnItemClickListener<T>() {
            @Override
            public void onShortClick(View view, int index, T item) {
                showEditDialog(index, item);
            }

            @Override
            public void onLongClick(View view, int index, T item) {
                showContextMenu(view, index, item);
            }
        });

        v.findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(-1, getItemForAdd());
            }
        });

        return v;
    }

    private SQLiteDatabase getDatabase() {
        if (database == null) {
            if (context == null)
                context = new DbContext(getActivity());
            database = context.getWritableDatabase();
        }

        return database;
    }

    private void showContextMenu(View view, int index, T subject) {
        selectedIndex = index;
        selectedItem = subject;

        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.menu_manage, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        showEditDialog(selectedIndex, selectedItem);
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

    private void deleteSelectedItem() {
        Utils.showConfirmDeleteDialog(getActivity(), adapter.getItemText(selectedItem), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ATable<Integer, T> table = getTable(getDatabase());
                table.delete(selectedItem);
                adapter.removeItem(selectedIndex);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (database != null) database.close();
        if (context != null) context.close();
        super.onDestroy();
    }

    public void saveItem(int index, T item) {
        ATable<Integer, T> table = getTable(getDatabase());

        if (isItemForAdd(item)) {
            table.insert(item);
            adapter.addItem(item);
        } else {
            table.update(item);
            adapter.updateItem(index, item);
        }
    }
}
