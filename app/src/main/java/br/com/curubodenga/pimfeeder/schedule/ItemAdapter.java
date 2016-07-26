package br.com.curubodenga.pimfeeder.schedule;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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

        for (int i = 1; i < from.length; i++) {
            TextView dayTextView = (TextView) view.findViewById(to[i]);
            int daySetted = cursor.getInt(cursor.getColumnIndex(from[i]));
            if (toBoolean(daySetted)) {
                dayTextView.setTextColor(Color.RED);
            } else {
                dayTextView.setTextColor(Color.BLACK);
            }
        }
    }

    private Boolean toBoolean(int i) {
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

}