package mar0602.tamz.project.gui.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mar0602.tamz.project.dto.LessonTime;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class LessonTimeAdapter extends MyAdapter<LessonTime> {
    public LessonTimeAdapter(List<LessonTime> data) {
        super(data);
    }

    @Override
    public String getItemText(LessonTime item) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(item.getStart()) + " - " + sdf.format(item.getEnd());
    }
}
