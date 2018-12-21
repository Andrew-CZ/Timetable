package mar0602.tamz.project.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mar0602.tamz.project.R;
import mar0602.tamz.project.dto.SimpleEntity;
import mar0602.tamz.project.utils.OnItemClickListener;

/**
 * @author Ondrej
 * @since 2018-12-19
 */
public abstract class MyAdapter<T extends SimpleEntity> extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private OnItemClickListener<T> listener;
    private List<T> data;

    public MyAdapter(List<T> data) {
        this.data = data;
        setHasStableIds(true);
    }

    public abstract String getItemText(T item);

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.my_adapter_item, parent, false);
        return new MyAdapter.MyViewHolder(itemView);
    }

    @Override
    public final void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        data.remove(index);
        notifyItemRemoved(index);
    }

    public void addItem(T item) {
        int index = getNewIndex(getItemText(item));
        data.add(index, item);
        notifyItemInserted(index);
    }

    public void updateItem(int index, T item) {
        if (getItemText(item).equals(getItemText(data.get(index)))) {
            data.set(index, item);
            notifyItemChanged(index);
        } else {
            int newIndex = getNewIndex(getItemText(item));
            if (newIndex == index) {
                data.set(index, item);
                notifyItemChanged(index);
            } else {
                data.set(index, item);
                notifyItemChanged(index);

                if (newIndex > index) newIndex--;

                int step = newIndex > index ? 1 : -1;
                for (int i = index; i != newIndex; i += step)
                    data.set(i, data.get(i + step));

                data.set(newIndex, item);
                notifyItemMoved(index, newIndex);
            }
        }
    }

    private int getNewIndex(String name) {
        int low = 0, high = data.size();

        while (low < high) {
            int mid = (low + high) / 2;
            int cmp = getItemText(data.get(mid)).compareToIgnoreCase(name);

            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else return mid;
        }

        int cmp = low >= data.size() ? 1 : getItemText(data.get(low)).compareToIgnoreCase(name);
        return cmp > 0 ? low : (low +1);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int index = getLayoutPosition();
                    if (listener != null)
                        listener.onLongClick(v, index, data.get(index));
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getLayoutPosition();
                    if (listener != null)
                        listener.onShortClick(v, index, data.get(index));
                }
            });
        }

        public void setData(T item) {
            textView.setText(getItemText(item));
        }
    }
}
