package br.com.curubodenga.pimfeeder.period;

import android.widget.NumberPicker.Formatter;

/**
 * Created by curup on 20/10/2017.
 */
public class SecondsFormatter implements Formatter {
    @Override
    public String format(int value) {
        return value + " seg";
    }
}
