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
public class MyArrayAdapterAbs extends ArrayAdapter<String> {
    private final Context context;
    private final  List<String> values, values1, values2, values3;
    private final HashMap<String, Integer> mIdMap, mIdMap1;
    public MyArrayAdapterAbs(Context context, List<String> value, List<String> value1, List<String> value2, List<String> value3) {
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
        textView.setText("Du "+values.get(position)+" au "+values1.get(position)+" - "+values2.get(position) + " h");
        textView1.setText(values3.get(position));
        // Change the icon for Windows and iPhone
        String s = values3.get(position);
        if (s.startsWith("Injustifiée")) {
            imageView.setImageResource(R.drawable.nok);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }
        return rowView;
    }
}