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

import mar0602.tamz.project.R;
import mar0602.tamz.project.dao.TableSubject;
import mar0602.tamz.project.dto.Subject;
import mar0602.tamz.project.gui.fragments.DbManagementFragment;
import mar0602.tamz.project.gui.fragments.FragmentSubjects;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public class DialogEditSubject extends DbManagementDialog<Subject> {
    private TextView error;
    private EditText inputName, inputAbbr;

    public static DialogEditSubject newInstance(int index, Subject subject) {
        DialogEditSubject dialog = new DialogEditSubject();
        dialog.bindArguments(index, subject);
        return dialog;
    }

    @Override
    public View createView(Subject subject, @NonNull LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_edit_subject, container, false);

        v.findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndClose();
            }
        });
        v.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        error = v.findViewById(R.id.tv_error);
        inputName = v.findViewById(R.id.input_name);
        inputAbbr = v.findViewById(R.id.input_abbreviation);

        inputName.setText(subject.getName());
        inputAbbr.setText(subject.getAbbreviation());

        return v;
    }

    @Override
    Subject getEntity(int id) {
        String name = inputName.getText().toString();
        String abbr = inputAbbr.getText().toString();
        Subject subject = new Subject(id, name, abbr);

        TableSubject table = new TableSubject(null);
        List<String> state =  table.validate(subject);

        if (state.isEmpty()) return subject;

        error.setText(state.get(0));
        error.setVisibility(View.VISIBLE);
        return null;
    }
}
