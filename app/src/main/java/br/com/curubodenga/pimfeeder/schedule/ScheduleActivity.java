package br.com.curubodenga.pimfeeder.schedule;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import br.com.curubodenga.pimfeeder.R;

public class ScheduleActivity extends AppCompatActivity {

    private ScheduleDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        dbHelper = new ScheduleDbAdapter(this);
        dbHelper.open();

        //TODO: Remover códigos de teste.
        dbHelper.deleteAllSchedules();
        dbHelper.insertSomeSchedules();

        displayListView();
    }

    private void displayListView() {
        Cursor cursor = dbHelper.fetchAllSchedules();

        String[] scheduleColumns = new String[]{
                ScheduleDbAdapter.KEY_DATE,
                ScheduleDbAdapter.KEY_MONDAY,
                ScheduleDbAdapter.KEY_TUESDAY,
                ScheduleDbAdapter.KEY_WEDNESDAY,
                ScheduleDbAdapter.KEY_THURSDAY,
                ScheduleDbAdapter.KEY_FRIDAY,
                ScheduleDbAdapter.KEY_SATURDAY,
                ScheduleDbAdapter.KEY_SUNDAY,
        };

        int[] scheduleListLayoutIDs = new int[]{
                R.id.timeTextView,
                R.id.mondayTextView,
                R.id.tuesdayTextView,
                R.id.wednesdayTextView,
                R.id.thursdayTextView,
                R.id.fridayTextView,
                R.id.saturdayTextView,
                R.id.sundayTextView
        };

        dataAdapter = new ItemAdapter(this, R.layout.scheduler_list_layout, cursor,
                scheduleColumns, scheduleListLayoutIDs, 0);

        ListView listView = (ListView) findViewById(R.id.schedulesScheduleActivityListView);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                openScheduleAdjustActivity(view);
            }
        });
    }

    public void openScheduleAdjustActivity(View view) {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        startActivity(intent);
    }


}
