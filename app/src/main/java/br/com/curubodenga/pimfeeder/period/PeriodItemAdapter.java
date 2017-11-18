package br.com.curubodenga.pimfeeder.period;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.curubodenga.pimfeeder.R;
import br.com.curubodenga.pimfeeder.schedule.ScheduleDbAdapter;
import br.com.curubodenga.pimfeeder.utils.DateUtils;

public class PeriodItemAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Context appContext;
    private boolean enabled;
    private int layout;
    private Cursor cursor;
    private final LayoutInflater inflater;
    private String[] from;
    private int[] to;

    public PeriodItemAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int
            flags) {
        super(context, layout, c, from, to, flags);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cursor = c;
        this.from = from;
        this.to = to;
        enabled = true;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView = (ImageView) view.findViewById(to[0]);
        int icon = cursor.getInt(cursor.getColumnIndex(from[0]));
        imageView.setImageResource(icon);

        TextView aliasTextView = (TextView) view.findViewById(to[1]);
        String alias = cursor.getString(cursor.getColumnIndex(from[1]));
        aliasTextView.setText(alias);

        TextView secondsTextView = (TextView) view.findViewById(to[2]);
        String seconds = cursor.getString(cursor.getColumnIndex(from[2]));
        secondsTextView.setText(seconds + " seg");

        TextView periodItemId = (TextView) view.findViewById(to[3]);
        String itemId = cursor.getString(cursor.getColumnIndex(from[3]));
        periodItemId.setText(itemId);
    }
}