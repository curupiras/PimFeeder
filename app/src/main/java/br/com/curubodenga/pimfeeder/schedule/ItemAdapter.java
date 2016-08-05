package br.com.curubodenga.pimfeeder.schedule;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.utils.DateUtils;

public class ItemAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cursor;
    private final LayoutInflater inflater;
    private String[] from;
    private int[] to;

    public ItemAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cursor = c;
        this.from = from;
        this.to = to;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView timeTextView = (TextView) view.findViewById(to[0]);
        String time = DateUtils.getHHmm(cursor.getString(cursor.getColumnIndex(from[0])));
        timeTextView.setText(time);

        TextView completeDayTextView = (TextView) view.findViewById(R.id.completeDayTextView);
        time = DateUtils.getCompleteDay(cursor.getString(cursor.getColumnIndex(from[0])));
        completeDayTextView.setText(time);

        TextView scheduleItemId = (TextView) view.findViewById(R.id.scheduleItemId);
        scheduleItemId.setText(cursor.getString(cursor.getColumnIndex(ScheduleDbAdapter.KEY_ROWID)));


        boolean isAllUnsetted = true;
        List<TextView> textViews = new ArrayList<TextView>();
        for (int i = 1; i < from.length-1; i++) {
            TextView dayTextView = (TextView) view.findViewById(to[i]);
            textViews.add(dayTextView);
            int daySettedInt = cursor.getInt(cursor.getColumnIndex(from[i]));
            boolean daySetted = toBoolean(daySettedInt);
            isAllUnsetted = isAllUnsetted && !daySetted;
            if (daySetted) {
                dayTextView.setTextColor(Color.RED);
            } else {
                dayTextView.setTextColor(Color.BLACK);
            }
        }

        if (isAllUnsetted) {
            hideWeekDaysLayout(view);
            showCompleteDayTextView(view);
        } else {
            showWeekDaysLayout(view);
            hideCompleteDayTextView(view);
        }
    }

    private void hideWeekDaysLayout(View view) {
        LinearLayout weekDayLayout = (LinearLayout) view.findViewById(R.id.weekDayLayout);
        weekDayLayout.setVisibility(View.GONE);
    }

    private void showWeekDaysLayout(View view) {
        LinearLayout weekDayLayout = (LinearLayout) view.findViewById(R.id.weekDayLayout);
        weekDayLayout.setVisibility(View.VISIBLE);
    }

    private void hideCompleteDayTextView(View view) {
        LinearLayout completeDayLayout = (LinearLayout) view.findViewById(R.id.completeDayLayout);
        completeDayLayout.setVisibility(View.GONE);
    }

    private void showCompleteDayTextView(View view) {
        LinearLayout completeDayLayout = (LinearLayout) view.findViewById(R.id.completeDayLayout);
        completeDayLayout.setVisibility(View.VISIBLE);
    }

    private Boolean toBoolean(int i) {
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

}