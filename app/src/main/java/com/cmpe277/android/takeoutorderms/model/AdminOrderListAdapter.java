package com.cmpe277.android.takeoutorderms.model;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmpe277.android.takeoutorderms.R;

import java.util.ArrayList;


public class AdminOrderListAdapter extends ArrayAdapter<Order> {

    Context mContext;
    ArrayList<Order> orderList;

    public AdminOrderListAdapter(Context context, ArrayList<Order> orderList) {
        super(context, R.layout.admin_orderlist_view, orderList);
        this.mContext = context;
        this.orderList = orderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Order order = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.admin_order_view,parent,false);
        }

        TextView orderID = (TextView) convertView.findViewById(R.id.order_id);
        TextView placeTime = (TextView) convertView.findViewById(R.id.place_time);
        TextView fulfillTime = (TextView) convertView.findViewById(R.id.fulfillment_start_time);
        TextView readyTime = (TextView) convertView.findViewById(R.id.ready_time);
        TextView pickUpTime = (TextView) convertView.findViewById(R.id.pickup_time);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.order_status);
        TextView email = (TextView) convertView.findViewById(R.id.customer_email);


        String id = order.getOrderID();
        id = id.substring(id.length()-3, id.length()-1);

        orderID.setText("Order ID:  " + id);
        placeTime.setText("Order at: " + order.getPlaceDate());
        fulfillTime.setText("Start Prepare at: " + order.getFulfillmentStartTime());
        readyTime.setText("Ready at: " + order.getReadyTime());
        pickUpTime.setText("PickUp at: " + order.getPickupDate());
        orderStatus.setText("Status: " + order.getStatus());
        email.setText("Customer Email:" + order.getEmail());


        return convertView;
    }
}
