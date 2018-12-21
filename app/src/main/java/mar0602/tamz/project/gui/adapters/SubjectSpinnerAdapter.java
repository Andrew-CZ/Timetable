package mar0602.tamz.project.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dto.Subject;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public class SubjectSpinnerAdapter extends ArrayAdapter<Subject> {
    public SubjectSpinnerAdapter(Context context, List<Subject> items) {
        super(context, android.R.layout.simple_list_item_1, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.adapter_item_subject, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.lb_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Subject item = getItem(position);
        if (item!= null) viewHolder.itemView.setText(item.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        private TextView itemView;
    }
}
