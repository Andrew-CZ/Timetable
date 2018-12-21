package mar0602.tamz.project.gui.dialogs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dao.TableLessonTime;
import mar0602.tamz.project.dto.LessonTime;
import mar0602.tamz.project.utils.Utils;

/**
 * @author Ondrej
 * @since 2018-12-19
 */
public class DialogEditLessonTime extends DbManagementDialog<LessonTime> {
    private TextView error;
    private EditText inputStart, inputEnd;

    public static DialogEditLessonTime newInstance(int index, LessonTime time) {
        DialogEditLessonTime dialog = new DialogEditLessonTime();
        dialog.bindArguments(index, time);
        return dialog;
    }

    @Override
    View createView(LessonTime time, LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_edit_lesson_time, container, false);

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
        inputStart = v.findViewById(R.id.input_start);
        inputEnd = v.findViewById(R.id.input_end);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        if (time.getStart() != null) inputStart.setText(sdf.format(time.getStart()));
        if (time.getEnd() != null) inputEnd.setText(sdf.format(time.getEnd()));

        return v;
    }

    @Override
    LessonTime getEntity(int id) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
        Time start = getTime(formatter, inputStart);
        Time end = getTime(formatter, inputEnd);

        LessonTime subject = new LessonTime(id, start, end);

        TableLessonTime table = new TableLessonTime(null);
        List<String> state =  table.validate(subject);

        if (state.isEmpty()) return subject;

        error.setText(state.get(0));
        error.setVisibility(View.VISIBLE);
        return null;
    }

    private Time getTime(SimpleDateFormat formatter, EditText input) {
        String str = input.getText().toString();
        if (Utils.isNullOrEmpty(str)) return null;
        try {
            Date date = formatter.parse(str);
            return new Time(date.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
