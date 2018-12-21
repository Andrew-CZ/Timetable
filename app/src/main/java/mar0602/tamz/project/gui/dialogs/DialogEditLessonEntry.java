package mar0602.tamz.project.gui.dialogs;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dao.DbContext;
import mar0602.tamz.project.dao.TableLessonEntry;
import mar0602.tamz.project.dao.TableSubject;
import mar0602.tamz.project.dto.LessonEntry;
import mar0602.tamz.project.dto.LessonEntryDataContainer;
import mar0602.tamz.project.dto.LessonTime;
import mar0602.tamz.project.dto.LessonType;
import mar0602.tamz.project.dto.Subject;
import mar0602.tamz.project.dto.Weekday;
import mar0602.tamz.project.gui.adapters.SubjectSpinnerAdapter;
import mar0602.tamz.project.gui.fragments.FragmentTimeTable;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public class DialogEditLessonEntry extends DialogFragment {
    private boolean isNew;
    private int column, row;
    private int lessonTimeId;
    private Weekday day;

    private TextView error;
    private Spinner spinnerSubject;
    private EditText inputTeacher, inputRoom;
    private RadioButton radioLesson, radioLecture;
    private SubjectSpinnerAdapter spinnerAdapter;

    public static DialogEditLessonEntry newInstance(boolean isNew, int column, int row, LessonEntry entry) {
        DialogEditLessonEntry dialog = new DialogEditLessonEntry();
        dialog.bindArguments(isNew, column, row, entry);
        return dialog;
    }

    final void bindArguments(boolean isNew, int column, int row, LessonEntry item) {
        Bundle args = new Bundle();
        args.putByte("isNew", (byte) (isNew ? 1 : 0));
        args.putInt("column", column);
        args.putInt("row", row);

        LessonEntryDataContainer container = new LessonEntryDataContainer(item.getSubject().getId(),
                item.getTime().getId(), item.getDay(), item.getType(), item.getTeacher(), item.getRoom());

        args.putParcelable("item", container);
        setArguments(args);
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        isNew = args.getByte("isNew") != 0;
        column = args.getInt("column");
        row = args.getInt("row");
        LessonEntryDataContainer entity = args.getParcelable("item");
        lessonTimeId = entity.getLessonTimeId();
        day = entity.getDay();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_lesson_entry, container, false);

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
        spinnerSubject = v.findViewById(R.id.spinner_subject);
        radioLesson = v.findViewById(R.id.radio_lesson);
        radioLecture = v.findViewById(R.id.radio_lecture);
        inputTeacher = v.findViewById(R.id.input_teacher);
        inputRoom = v.findViewById(R.id.input_room);

        initSpinner();
        LessonEntryDataContainer entity = getArguments().getParcelable("item");
        inputTeacher.setText(entity.getTeacher());
        inputRoom.setText(entity.getRoom());

        if (entity.getType() == LessonType.LECTURE)
            radioLecture.setChecked(true);
        else radioLesson.setChecked(true);

        return v;
    }

    private void initSpinner() {
        try (DbContext context = new DbContext(getActivity());
                SQLiteDatabase db = context.getWritableDatabase()){
            TableSubject table = new TableSubject(db);
            spinnerAdapter = new SubjectSpinnerAdapter(getActivity(), table.getAll());
            spinnerSubject.setAdapter(spinnerAdapter);
        }
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
        int subjIndex = spinnerSubject.getSelectedItemPosition();
        Subject subject = spinnerAdapter.getItem(subjIndex);
        LessonType type = radioLesson.isChecked() ? LessonType.LESSON : LessonType.LECTURE;
        String teacher = inputTeacher.getText().toString();
        String room = inputRoom.getText().toString();

        LessonEntry entry = new LessonEntry(new LessonTime(lessonTimeId),
                subject, day, type, teacher, room);

        TableLessonEntry table = new TableLessonEntry(null);
        List<String> state =  table.validate(entry);

        if (state.isEmpty()) {
            FragmentTimeTable frag = (FragmentTimeTable) getTargetFragment();
            if (frag != null) {
                if (isNew) frag.addItem(column, row, entry);
                else frag.updateItem(column, row, entry);
            }
            dismiss();
        } else {
            error.setText(state.get(0));
            error.setVisibility(View.VISIBLE);
        }
    }
}
