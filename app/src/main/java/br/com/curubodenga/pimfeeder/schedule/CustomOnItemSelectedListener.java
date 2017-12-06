package br.com.curubodenga.pimfeeder.schedule;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import br.com.curubodenga.pimfeeder.period.Period;
import br.com.curubodenga.pimfeeder.period.PeriodActivity;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(pos);
        Period period = Period.getPeriod(cursor);

        if(period == null){
            ScheduleAdjustActivity host = (ScheduleAdjustActivity) parent.getContext();
            host.openPeriodAdjustActivity();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
