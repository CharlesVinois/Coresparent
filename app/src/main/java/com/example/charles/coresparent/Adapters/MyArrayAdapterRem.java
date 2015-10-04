package com.example.charles.coresparent.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charles.coresparent.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Charles on 29/03/2015.
 */
public class MyArrayAdapterRem  extends ArrayAdapter<String> {
    private final Context context;
    private final  List<String> values, values1, values2, values3;
    private final HashMap<String, Integer> mIdMap, mIdMap1;
    public MyArrayAdapterRem(Context context, List<String> value, List<String> value1, List<String> value2, List<String> value3) {
        super(context, R.layout.row, value);
        this.context = context;
        this.values = value;
        this.values1 = value1;
        values2 = value2;
        values3 = value3;
        this.mIdMap = new HashMap<String, Integer>();
        this.mIdMap1 = new HashMap<String, Integer>();
        for (int i = 0; i < values.size(); ++i) {
            mIdMap.put(values.get(i), i);
            mIdMap.put(values1.get(i), i);
            mIdMap.put(values2.get(i), i);
        }
    }
    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView1 = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values1.get(position));
        textView1.setText(values2.get(position));
        /*String s = values1.get(position);
        if (s.startsWith("Niveau")) {
            imageView.setImageResource(R.drawable.dicipline);
        }
        else if (s.startsWith("Dicipline")) {
            imageView.setImageResource(R.drawable.dicipline);
        }
        else if (s.startsWith("Retards")) {
            imageView.setImageResource(R.drawable.retard);
        }
        else if (s.startsWith("Convocation")) {
            imageView.setImageResource(R.drawable.dicipline);
        }
        else if (s.startsWith("Info")) {
            imageView.setImageResource(R.drawable.informations);
        }
        else if (s.startsWith("Absences")) {
            imageView.setImageResource(R.drawable.absences);
        }
        else {*/
            imageView.setImageResource(R.drawable.ok);
        //}
        return rowView;
    }
}