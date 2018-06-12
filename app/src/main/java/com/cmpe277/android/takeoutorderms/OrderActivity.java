package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.cmpe277.android.takeoutorderms.model.Constant;
import com.cmpe277.android.takeoutorderms.model.CustomerOrderListAdapter;
import com.cmpe277.android.takeoutorderms.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderActivity extends AppCompatActivity {


    //view objects
    private ListView listView;
    private Toolbar toolbar;

    private String userID;
    private String email;

    private CustomerOrderListAdapter adapter;
    private ArrayList<Order> orderList = new ArrayList<>();

    //firebase database
    private DatabaseReference mDatabase, userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainCustomerActivity.class);
                intent.putExtra(Constant.USER_ID, userID);
                intent.putExtra(Constant.USER_EMAIL, email);
                startActivity(intent);
            }
        });


        //obtain firebase uid for current user
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra(Constant.USER_ID);
            email = intent.getStringExtra(Constant.USER_EMAIL);
        }


        listView = (ListView) findViewById(R.id.order_list_view);

        //setup adapter
        adapter = new CustomerOrderListAdapter(this, orderList);
        listView.setAdapter(adapter);

        //set up firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDatabase = mDatabase.child("users").child(userID).child("orderList");
        userDatabase.addValueEventListener(new ValueEventListener() {
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

    }
}
