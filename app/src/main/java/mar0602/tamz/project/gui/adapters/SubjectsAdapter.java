package mar0602.tamz.project.gui.adapters;

import java.util.List;

import mar0602.tamz.project.dto.Subject;

/**
 * @author Ondrej
 * @since 2018-12-19
 */
public class SubjectsAdapter extends MyAdapter<Subject> {
    public SubjectsAdapter(List<Subject> data) {
        super(data);
    }

    @Override
    public String getItemText(Subject item) {
        return item.getName();
    }
}
