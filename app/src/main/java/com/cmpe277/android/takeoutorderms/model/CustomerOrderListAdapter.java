package com.cmpe277.android.takeoutorderms.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.R;
import com.cmpe277.android.takeoutorderms.service.CategoryItemsService;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class CustomerOrderListAdapter extends ArrayAdapter<Order> {

    Context mContext;
    ArrayList<Order> orderList;

    public CustomerOrderListAdapter(Context context, ArrayList<Order> orderList) {
        super(context, R.layout.admin_orderlist_view, orderList);
        this.mContext = context;
        this.orderList = orderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Order order = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_order_view,parent,false);
        }

        TextView orderID = (TextView) convertView.findViewById(R.id.order_id);
        TextView pickUpTime = (TextView) convertView.findViewById(R.id.pickDate);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.order_status);


        String id = order.getOrderID();
        id = id.substring(id.length()-3, id.length()-1);

        orderID.setText("Order ID:  " + id);
        pickUpTime.setText("PickUp at: " + order.getPickupDate());
        orderStatus.setText("Status: " + order.getStatus());

        ImageButton removeBtn = (ImageButton) convertView.findViewById(R.id.delete);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderId = order.getOrderID();
                String orderStatus = order.getStatus();
                if (orderStatus.equalsIgnoreCase(Constant.QUEUED)){
                    //set up firebase database
                    DatabaseReference mDatabase = getInstance().getReference();
                    mDatabase.child("orders").child(orderId).removeValue();
                    mDatabase.child("users").child(order.getUserID()).child("orderList").child(orderId).removeValue();
                    orderList.remove(order);
                    Toast.makeText(view.getContext(), "This order is removed.", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(view.getContext(), "This order can't be removed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }
}
