package com.example.patryk.shoppinglist.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.Entry;
import com.example.patryk.shoppinglist.models.ShoppingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntryListAdapter extends ArrayAdapter<Entry> {

    private HashMap<Entry, Integer> mIdMap = new HashMap<>();
    private List<Entry> dataSet = new ArrayList<>();
    public List<Entry> selectedItems = new ArrayList<>();
    Context mContext;

    public EntryListAdapter(List<Entry> data, int textViewResourceId, Context context) {
        super(context, textViewResourceId, data);
        for (int i = 0; i < data.size(); ++i) {
            mIdMap.put(data.get(i), i);
        }
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public Entry getItem(int position){
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        Entry item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Entry dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.entry_list_view_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            viewHolder.txtUnit = (TextView) convertView.findViewById(R.id.txtUnit);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        if (selectedItems.contains(getItem(position))) {
            viewHolder.txtUnit.setPaintFlags(viewHolder.txtUnit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.txtQuantity.setPaintFlags(viewHolder.txtQuantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.txtUnit.setPaintFlags(0);
            viewHolder.txtQuantity.setPaintFlags(0);
            viewHolder.txtName.setPaintFlags(0);
        }
        viewHolder.txtName.setText(dataModel.getProduct());
        viewHolder.txtQuantity.setText(Integer.toString(dataModel.getQuantity()));
        viewHolder.txtUnit.setText(dataModel.getUnit().toString());
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtUnit;
        TextView txtQuantity;
    }

    public List<Entry> getSelectedItems() {
        return selectedItems;
    }
}

