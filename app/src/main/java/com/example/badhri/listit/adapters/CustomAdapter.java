package com.example.badhri.listit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.badhri.listit.models.TodoItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.example.badhri.listit.R.id;
import static com.example.badhri.listit.R.layout;

/**
 * Created by badhri on 9/22/16.
 */
public class CustomAdapter extends ArrayAdapter<TodoItem> {
    public CustomAdapter (Context context, ArrayList<TodoItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todoItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout.list_item, parent, false);
        }
        CheckedTextView item = (CheckedTextView) convertView.findViewById(id.Item);
        if (todoItem.status.contentEquals("done"))
            item.setChecked(true);
        else
            item.setChecked(false);

        // Item.setBackgroundColor(Color.parseColor("#E8EAF6"));
        item.setTextColor(Color.parseColor("#004D40"));
        TextView date = (TextView) convertView.findViewById(id.date);
        //date.setTextColor(Color.parseColor("#283593"));
        TextView priority = (TextView) convertView.findViewById(id.priority);
        //priority.setTextColor(Color.parseColor("#283593"));
        // Populate the data into the template view using the data object
        int tempposition = position + 1;
        item.setText(tempposition + ". " + todoItem.item);

        Calendar target = Calendar.getInstance();
        String[] parts = todoItem.date.split("-");
        target.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
        long msDiff = target.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

        date.setText(daysDiff + " days left");
        priority.setText(todoItem.priority);
        if (todoItem.priority.contentEquals("Low"))
            priority.setTextColor(Color.parseColor("#1B5E20"));
        if (todoItem.priority.contentEquals("Medium"))
            priority.setTextColor(Color.parseColor("#FFAB00"));
        if (todoItem.priority.contentEquals("High"))
            priority.setTextColor(Color.parseColor("#B71C1C"));
        return convertView;
    }

}
