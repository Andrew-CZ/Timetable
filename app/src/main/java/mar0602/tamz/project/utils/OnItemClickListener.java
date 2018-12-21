package mar0602.tamz.project.utils;

import android.view.View;

/**
 * @author Ondrej
 * @since 2018-12-19
 */
public interface OnItemClickListener<T> {
    void onShortClick(View view, int index, T item);
    void onLongClick(View view, int index, T item);
}
