package mar0602.tamz.project.gui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dto.LessonEntry;
import mar0602.tamz.project.dto.LessonTime;
import mar0602.tamz.project.dto.Weekday;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public class TableViewAdapter extends AbstractTableAdapter<LessonTime, Weekday, LessonEntry> {
    private int colorHeader;
    private int colorHeaderFont;
    private int colorHeaderBorder;
    private int colorCellEmpty;
    private int colorCellLesson;
    private int colorCellLessonFont;
    private int colorCellLecture;
    private int colorCellLectureFont;
    /*public static final int COLOR_HEADER = Color.WHITE;
    public static final int COLOR_BORDER_HEADER = Color.BLACK;
    public static final int COLOR_CELL_LESSON = Color.rgb(221, 221, 255);
    public static final int COLOR_CELL_LECTURE = Color.rgb(255, 221, 221);
    public static final int COLOR_CELL_EMPTY = Color.WHITE;*/

    public TableViewAdapter(Context context) {
        super(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        colorHeader = prefs.getInt("color_header", Color.WHITE);
        colorHeaderFont = prefs.getInt("color_header_font", Color.BLACK);
        colorHeaderBorder = prefs.getInt("color_header_separator", Color.BLACK);
        colorCellEmpty = prefs.getInt("color_cell_empty", Color.WHITE);
        colorCellLesson = prefs.getInt("color_cell_lesson", Color.rgb(221, 221, 255));
        colorCellLessonFont = prefs.getInt("color_cell_lesson_font", Color.BLACK);
        colorCellLecture = prefs.getInt("color_cell_lecture", Color.rgb(255, 221, 221));
        colorCellLectureFont = prefs.getInt("color_cell_lecture_font", Color.BLACK);
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_cell, parent, false);
        return new MyCellViewHolder(view);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object item, int columnPosition, int rowPosition) {
        ((MyCellViewHolder) holder).setData((LessonEntry) item);
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_header_column, parent, false);
        MyColumnHeaderViewHolder holder = new MyColumnHeaderViewHolder(view);
        holder.setColors(colorHeader, colorHeaderFont, colorHeaderBorder);
        return holder;
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object item, int columnPosition) {
        LessonTime time = (LessonTime) item;
        ((MyColumnHeaderViewHolder) holder).setData(time);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_header_row, parent, false);
        MyRowHeaderViewHolder holder = new MyRowHeaderViewHolder(view);
        holder.setColors(colorHeader, colorHeaderFont, colorHeaderBorder);
        return holder;
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object item, int rowPosition) {
        Weekday day = (Weekday) item;
        ((MyRowHeaderViewHolder) holder).setData(day);
    }

    @Override
    public View onCreateCornerView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_corner, null);
        view.setBackgroundColor(colorHeader);
        return view;
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    class MyCellViewHolder extends AbstractViewHolder {
        private final TextView lbSubject;
        private final TextView lbTeacher;
        private final TextView lbRoom;

        MyCellViewHolder(View itemView) {
            super(itemView);

            lbSubject = itemView.findViewById(R.id.lb_subject);
            lbTeacher = itemView.findViewById(R.id.lb_teacher);
            lbRoom = itemView.findViewById(R.id.lb_room);
        }

        void setData(LessonEntry data) {
            int bgColor, fontColor;
            if (data == null) {
                bgColor = colorCellEmpty;
                fontColor = colorCellLessonFont;
                lbSubject.setText("");
                lbTeacher.setText("");
                lbRoom.setText("");
            } else {
                lbSubject.setText(data.getSubject().getAbbreviation());
                lbTeacher.setText(data.getTeacher());
                lbRoom.setText(data.getRoom());

                switch (data.getType()) {
                    case LECTURE:
                        bgColor = colorCellLecture;
                        fontColor = colorCellLectureFont;
                        break;
                    case LESSON:
                        bgColor = colorCellLesson;
                        fontColor = colorCellLessonFont;
                        break;
                    default:
                        bgColor = colorCellEmpty;
                        fontColor = colorCellLessonFont;
                        break;
                }
            }

            itemView.setBackgroundColor(bgColor);
            lbSubject.setTextColor(fontColor);
            lbTeacher.setTextColor(fontColor);
            lbRoom.setTextColor(fontColor);

            itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lbSubject.requestLayout();
            lbTeacher.requestLayout();
            lbRoom.requestLayout();
        }

        @Override
        public void setBackgroundColor(int p_nColor) {

        }
    }

    class MyColumnHeaderViewHolder extends AbstractViewHolder {
        private final TextView lbStart;
        private final TextView lbEnd;

        MyColumnHeaderViewHolder(View itemView) {
            super(itemView);

            lbStart = itemView.findViewById(R.id.lb_start);
            lbEnd = itemView.findViewById(R.id.lb_end);
        }

        void setData(LessonTime data) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
            lbStart.setText(sdf.format(data.getStart()));
            lbEnd.setText(sdf.format(data.getEnd()));

            /*column_header_container.getLayoutParams().width = LinearLayout
                    .LayoutParams.WRAP_CONTENT;*/
            lbStart.requestLayout();
            lbEnd.requestLayout();
        }

        @Override
        public void setBackgroundColor(int p_nColor) {

        }

        public void setColors(int colorHeader, int colorHeaderFont, int colorHeaderBorder) {
            itemView.setBackgroundColor(colorHeader);
            lbStart.setTextColor(colorHeaderFont);
            lbEnd.setTextColor(colorHeaderFont);
            itemView.findViewById(R.id.separator).setBackgroundColor(colorHeaderBorder);
        }
    }

    class MyRowHeaderViewHolder extends AbstractViewHolder {
        private TextView lbName;

        MyRowHeaderViewHolder(View itemView) {
            super(itemView);
            lbName = itemView.findViewById(R.id.lb_day);
        }

        void setData(Weekday data) {
            String text;

            switch (data) {
                case MONDAY:
                    text = "Monday";
                    break;
                case TUESDAY:
                    text = "Tuesday";
                    break;
                case WEDNESDAY:
                    text = "Wednesday";
                    break;
                case THURSDAY:
                    text = "Thursday";
                    break;
                case FRIDAY:
                    text = "Friday";
                    break;
                default:
                    text = "Uknown";
                    break;
            }

            lbName.setText(text);

            itemView.requestLayout();
        }

        @Override
        public void setBackgroundColor(int p_nColor) {

        }

        public void setColors(int colorHeader, int colorHeaderFont, int colorHeaderBorder) {
            itemView.setBackgroundColor(colorHeader);
            lbName.setTextColor(colorHeaderFont);
            itemView.findViewById(R.id.separator).setBackgroundColor(colorHeaderBorder);
        }
    }
}
