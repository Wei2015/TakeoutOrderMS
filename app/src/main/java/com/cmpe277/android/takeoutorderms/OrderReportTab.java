package com.cmpe277.android.takeoutorderms;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.cmpe277.android.takeoutorderms.model.AdminOrderListAdapter;
import com.cmpe277.android.takeoutorderms.model.Constant;
import com.cmpe277.android.takeoutorderms.model.Order;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OrderReportTab extends Fragment {

    private ListView listView;
    private Button resetBtn;

    private AdminOrderListAdapter adapter;
    private DatabaseReference mDatabase;

    private ArrayList<Order> orderList = new ArrayList<>();

    public OrderReportTab() { }


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_orderlist_view, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.order_list_view);
        //adapter
        adapter = new AdminOrderListAdapter(view.getContext(), orderList);
        listView.setAdapter(adapter);


        //set up firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Order current = ds.getValue(Order.class);

                    //update order status based on current time with fulfillment
                    String pattern = "MM-dd-yyyy HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String[] pickUp = current.getPickupDate().split(" ");
                    String pickUpDate = pickUp[0];
                    String fullfillTime = current.getFulfillmentStartTime();
                    String readyTime = current.getReadyTime();
                    Date currentTime = new Date();
                    Date fulfillT = new Date();
                    Date readyT = new Date();
                    try{
                        readyT = simpleDateFormat.parse(pickUpDate + " " + readyTime);
                        fulfillT = simpleDateFormat.parse(pickUpDate + " " + fullfillTime);
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    if (fulfillT.getTime()<currentTime.getTime()) {
                        current.setStatus(Constant.BEING_PRE);
                    }
                    if (readyT.getTime() < currentTime.getTime()){
                        current.setStatus(Constant.FULFILLED);
                    }
                    ds.getRef().setValue(current);
                    orderList.add(current);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        resetBtn = (Button) view.findViewById(R.id.reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Order> temp = new ArrayList<>();
                for (Order order : orderList) {
                    if (order.getStatus().equalsIgnoreCase(Constant.QUEUED)) {
                        mDatabase.child("orders").child(order.getOrderID()).removeValue();
                        temp.add(order);
                        mDatabase.child("users").child(order.getUserID()).child("orderList").child(order.getOrderID()).removeValue();
                    }
                }
                adapter.notifyDataSetChanged();
                orderList.removeAll(temp);
            }
        });


    }
}
