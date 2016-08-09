package br.com.curubodenga.pimfeeder.schedule;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.settings.SettingsActivity;

public class ScheduleActivity extends AppCompatActivity {

    private ScheduleDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        dbHelper = new ScheduleDbAdapter(this);
        dbHelper.open();

        displayListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
                ScheduleDbAdapter.KEY_ROWID
        };

        int[] scheduleListLayoutIDs = new int[]{
                R.id.timeTextView,
                R.id.mondayTextView,
                R.id.tuesdayTextView,
                R.id.wednesdayTextView,
                R.id.thursdayTextView,
                R.id.fridayTextView,
                R.id.saturdayTextView,
                R.id.sundayTextView,
                R.id.scheduleItemId
        };

        dataAdapter = new ItemAdapter(this, R.layout.scheduler_list_layout, cursor,
                scheduleColumns, scheduleListLayoutIDs, 0);

        ListView listView = (ListView) findViewById(R.id.schedulesScheduleActivityListView);
        listView.setAdapter(dataAdapter);

        if (listView.getAdapter().getCount() < 1) {
            openScheduleAdjustActivity();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String key = cursor.getString(cursor.getColumnIndex(ScheduleDbAdapter.KEY_ROWID));
                openScheduleAdjustActivity(key);
            }
        });
    }

    public void openScheduleAdjustActivity(String key) {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        intent.putExtra(ScheduleDbAdapter.KEY_ROWID, key);
        startActivity(intent);
    }

    public void openScheduleAdjustActivity(View view) {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        startActivity(intent);
    }
    public void openScheduleAdjustActivity() {
        Intent intent = new Intent(this, ScheduleAdjustActivity.class);
        startActivity(intent);
    }


    public void deleteItem(View view) {
        ScheduleDbAdapter scheduleDbAdapter = new ScheduleDbAdapter(this);
        scheduleDbAdapter.open();

        View linearLayout = (View) view.getParent().getParent().getParent();
        TextView scheduleItemIdTextView = (TextView) linearLayout.findViewById(R.id.scheduleItemId);
        String itemId = scheduleItemIdTextView.getText().toString();

        scheduleDbAdapter.deleteItem(itemId);
        displayListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_schedule:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
