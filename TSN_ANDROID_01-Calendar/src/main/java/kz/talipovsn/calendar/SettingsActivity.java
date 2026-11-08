package kz.talipovsn.calendar;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Установка темы
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        // Установка фрагмента настроек
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        // Установка тулбара
        Toolbar toolbar = findViewById(R.id.toolbar); // Тулбар
        setSupportActionBar(toolbar); // Установка тулбара

        // Установка кнопки назад
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Кнопка назад
            actionBar.setDisplayShowHomeEnabled(true); // Кнопка домой
        }
    }

    // Обработка нажатия кнопки назад
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    // Фрагмент настроек
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // Установка настроек из файла
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}