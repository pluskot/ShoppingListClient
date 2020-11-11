package com.example.patryk.shoppinglist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.ShoppingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingListListAdapter extends ArrayAdapter<ShoppingList> {

    private HashMap<ShoppingList, Integer> mIdMap = new HashMap<>();
    private List<ShoppingList> dataSet = new ArrayList<>();
    Context mContext;

    public ShoppingListListAdapter(List<ShoppingList> data, int textViewResourceId, Context context) {
        super(context, textViewResourceId, data);
        for (int i = 0; i < data.size(); ++i) {
            mIdMap.put(data.get(i), i);
        }
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public ShoppingList getItem(int position){
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        ShoppingList item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ShoppingList dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(dataModel.getName());
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
    }
}

