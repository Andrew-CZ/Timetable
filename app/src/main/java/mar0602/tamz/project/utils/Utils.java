package mar0602.tamz.project.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

import mar0602.tamz.project.R;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class Utils {
    public static final boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static final void showConfirmDeleteDialog(Context context, String itemName, DialogInterface.OnClickListener listener) {
        Resources res = context.getResources();
        String title = res.getString(R.string.title_delete_item);

        String message = isNullOrEmpty(itemName)
                ? res.getString(R.string.confirm_delete_generic)
                : res.getString(R.string.confirm_delete, itemName);

        showConfirmDialog(context, title, message, listener);
    }

    public static final void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setCancelable(true)
                .setPositiveButton(R.string.dialog_confirm_positive, listener)
                .setNegativeButton(R.string.dialog_confirm_negative, null);
        builder.show();
    }
}
