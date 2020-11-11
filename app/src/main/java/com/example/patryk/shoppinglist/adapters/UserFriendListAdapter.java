package com.example.patryk.shoppinglist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.User;

import java.util.HashMap;
import java.util.List;

public class UserFriendListAdapter extends ArrayAdapter<User> {

    private final HashMap<User, Integer> mIdMap = new HashMap<>();
    private final List<User> dataSet;
    Context mContext;

    public UserFriendListAdapter(List<User> data, int textViewResourceId, Context context) {
        super(context, textViewResourceId, data);
        for (int i = 0; i < data.size(); ++i) {
            mIdMap.put(data.get(i), i);
        }
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public User getItem(int position){
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        User item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        User dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_item, parent, false);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtUserName.setText(dataModel.getUsername());
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        TextView txtUserName;
    }
}

