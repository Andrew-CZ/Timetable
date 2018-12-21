package mar0602.tamz.project.gui;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mar0602.tamz.project.R;
import mar0602.tamz.project.gui.fragments.FragmentLessonTimes;
import mar0602.tamz.project.gui.fragments.FragmentSubjects;
import mar0602.tamz.project.gui.fragments.FragmentTimeTable;
import mar0602.tamz.project.gui.fragments.MySettingsFragment;
import mar0602.tamz.project.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "fragment_content";
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content),
                new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin =
                                insets.getSystemWindowInsetBottom();

                        LinearLayout layout = findViewById(R.id.content);
                        ((ViewGroup.MarginLayoutParams) layout.getLayoutParams()).topMargin =
                                insets.getSystemWindowInsetTop();
                        /*findViewById(R.id.content).setPadding(insets.getSystemWindowInsetLeft(),
                                insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), 0);*/

                        return insets.consumeSystemWindowInsets();
                    }
                });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (!menuItem.isChecked()) {
                            Fragment activity = null;
                            switch (menuItem.getItemId()) {
                                case R.id.nav_timetable:
                                    activity = new FragmentTimeTable();
                                    break;
                                case R.id.nav_subjects:
                                    activity = new FragmentSubjects();
                                    break;
                                case R.id.nav_lesson_times:
                                    activity = new FragmentLessonTimes();
                                    break;
                                case R.id.nav_preferences:
                                    activity = new MySettingsFragment();
                                    break;
                                case R.id.nav_reset_preferences:
                                    confirmResetPreferences();
                                    break;
                            }

                            if (activity != null) {
                                menuItem.setChecked(true);
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.content_fragment, activity, TAG);
                                transaction.commit();
                            }

                            mDrawerLayout.closeDrawers();
                        }

                        return true;
                    }
                });
        if (savedInstanceState == null) {
            FragmentTimeTable activity = new FragmentTimeTable();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, activity, TAG);
            transaction.commit();
        }
    }

    private void confirmResetPreferences() {
        Resources res = getResources();
        String title = res.getString(R.string.title_reset_preferences);
        String message = res.getString(R.string.confirm_reset_preferences);

        Utils.showConfirmDialog(MainActivity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().clear().apply();

                FragmentManager manager = getSupportFragmentManager();
                Fragment frg = manager.findFragmentByTag(TAG);
                if (frg != null) manager.beginTransaction().detach(frg).attach(frg).commit();
            }
        });
    }
}
