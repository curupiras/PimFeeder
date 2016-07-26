package br.com.curubodenga.pimfeeder.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import br.com.curubodenga.pimfeeder.R;

public class ScheduleActivity extends AppCompatActivity {

    private final String[] horarios = {"7:00","8:00","7:00","8:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ListView schedulesList = (ListView) findViewById(R.id.schedulesList);
        schedulesList.setAdapter(new ScheduleArrayAdapter(this, horarios));

    }


    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        ListView schedulesList = (ListView) findViewById(R.id.schedulesList);
        ListAdapter listAdapter = schedulesList.getAdapter();
        String selectedValue = (String) listAdapter.getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
    }




}
