package mar0602.tamz.project.gui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import mar0602.tamz.project.R;

/**
 * @author Ondrej
 * @since 2018-12-20
 */
public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View lv = view.findViewById(android.R.id.list);
        if (lv instanceof ListView)
            Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
    }
}
