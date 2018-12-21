package mar0602.tamz.project.gui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import mar0602.tamz.project.dto.SimpleEntity;
import mar0602.tamz.project.gui.fragments.DbManagementFragment;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public abstract class DbManagementDialog<T extends SimpleEntity> extends DialogFragment {
    private int index;
    private int itemId;

    private TextView error;
    private EditText inputName, inputAbbr;

    abstract View createView(T entity, @NonNull LayoutInflater inflater, @Nullable ViewGroup container);
    abstract T getEntity(int id);

    final void bindArguments(int index, T item) {
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putParcelable("item", item);
        setArguments(args);
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        index = args.getInt("index");
        SimpleEntity entity = args.getParcelable("item");
        itemId = entity.getId();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        T entity = getArguments().getParcelable("item");
        return createView(entity, inflater, container);
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        return dialog;
    }

    final void saveAndClose() {
        T entity = getEntity(itemId);

        if (entity != null) {
            DbManagementFragment<T> frag = (DbManagementFragment<T>) getTargetFragment();
            if (frag != null) frag.saveItem(index, entity);
            dismiss();
        }
    }
}
