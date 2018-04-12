package com.cmpe277.android.takeoutorderms.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;

import com.cmpe277.android.takeoutorderms.R;

import java.util.ArrayList;

/**
 * Created by weiyao on 4/11/18.
 */

public class CustomItemListAdapter extends ArrayAdapter<Item> implements View.OnClickListener {

    private ArrayList<Item> itemList;
    Context mContext;


    public CustomItemListAdapter(Context context, ArrayList<Item> itemList) {
        super(context, R.layout.row_item, itemList);
        this.itemList = itemList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent,false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView price = (TextView) convertView.findViewById(R.id.item_price);

        name.setText(item.getName());
        price.setText(String.valueOf(item.getPrice()));

        return convertView;

    }

    @Override
    public void onClick(View view) {

    }
}
