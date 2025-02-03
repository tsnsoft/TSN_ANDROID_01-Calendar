package kz.talipovsn.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final int maxSemesterWeek = 15; // Максимальное количество недель в семестре
    final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU")); // Формат даты
    final Calendar calendar = Calendar.getInstance(); // Календарь

    SharedPreferences sp; // Настройки
    CalendarView cal; // Календарь
    EditText editText; // Поле вывода данных

    boolean offsetNumerator; // Смещение числителя
    int displacementWeekSemester; // Смещение недели семестра

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Установка тулбара
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Установка темы
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        sp = PreferenceManager.getDefaultSharedPreferences(this); // Получение настроек

        editText = findViewById(R.id.editText); // Поле вывода данных
        cal = findViewById(R.id.calendarView);  // Календарь
        cal.setFirstDayOfWeek(2); // Понедельник первым днем недели
        calendar.setFirstDayOfWeek(Calendar.MONDAY); // Понедельник первым днем недели

        readSettings(); // Чтение настроек

        // Обработка выбора даты
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth); // Установка даты
                editText.setText(getDataCalc()); // Вывод данных
            }
        });

        Button exitButton = findViewById(R.id.exit_button); // Кнопка выхода
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // Закрытие приложения
            }
        });
    }

    // Чтение настроек
    private void readSettings() {
        offsetNumerator = sp.getBoolean("offsetNumerator", false); // Смещение числителя
        displacementWeekSemester = Integer.valueOf(sp.getString("displacementWeekSemester", "1")) - 1; // Смещение недели семестра
        int fontSize = Integer.valueOf(sp.getString("fontSize", "16")); // Размер шрифта
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize); // Установка размера шрифта
    }

    // Обновление настроек
    @Override
    protected void onResume() {
        super.onResume();
        readSettings();
        Date date = new Date();
        cal.setDate(date.getTime());
        calendar.setTime(date);
        editText.setText(getDataCalc());
    }

    // Проверка на четность
    public static boolean isEven(int x) {
        return (x & 1) == 0;
    }

    // Получение строки с данными
    public String getDataCalc() {
        String date = sdf.format(calendar.getTime());
        int semesterWeek = calendar.get(Calendar.WEEK_OF_YEAR) - displacementWeekSemester;
        boolean numerator = isEven(semesterWeek);
        if (offsetNumerator) {
            numerator = !numerator;
        }
        if ((semesterWeek <= 0) || (semesterWeek > maxSemesterWeek)) {
            return getString(R.string.Выбрано) + date + getString(R.string.Эта_дата_не_входит_в_семестр);
        } else {
            return getString(R.string.Выбрано) + date + getString(R.string.Неделя_семестра) + semesterWeek + getString(R.string.Сейчас)
                    + (numerator ? getString(R.string.Числитель) : getString(R.string.Знаменатель));
        }
    }

    @Override
    // Создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    // Обработка нажатий на пункты меню
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Открытие настроек
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        // О программе
        if (id == R.id.about) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.email, Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item); // Вызов метода суперкласса
    }
}