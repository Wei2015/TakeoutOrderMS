package com.cmpe277.android.takeoutorderms;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.cmpe277.android.takeoutorderms.model.CartItem;
import com.cmpe277.android.takeoutorderms.model.CartItemListAdapter;
import com.cmpe277.android.takeoutorderms.model.Constant;
import com.cmpe277.android.takeoutorderms.model.Order;
import com.cmpe277.android.takeoutorderms.service.GMailSender;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    //view objects
    private ListView listView;
    private Button btnDatePicker, btnTimePicker, btnOrder, cancelBtn;
    private TextView txtDate, txtTime;
    private TextView totalCost;
    private Toolbar toolbar;

    //firebase database
    private DatabaseReference mDatabase, userDatabase;

    //pass user id and email
    private String userId;
    private String email;


    //item list adapter
    private CartItemListAdapter adapter;

    private String pick_date, pick_time;
    private String totalPrice;
    private Order order;
    private ArrayList<CartItem> orderItems = new ArrayList<>();

    GMailSender sender;
    String pattern = "MM-dd-yyyy HH:mm:ss";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    TreeMap<String, Object> dataSet = new TreeMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnOrder = (Button) findViewById(R.id.place_order);
        btnOrder.setOnClickListener(this);

        cancelBtn = (Button) findViewById(R.id.cancel_order);
        cancelBtn.setOnClickListener(this);

        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        txtDate = (TextView) findViewById(R.id.date);
        txtTime = (TextView) findViewById(R.id.time);
        totalCost = (TextView) findViewById(R.id.cost);

        sender = new GMailSender("yaoweibio@gmail.com", Constant.PASSWORD );

        //Get the intent message from previous activity
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra(Constant.USER_ID);
            email = intent.getStringExtra(Constant.USER_EMAIL);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainCustomerActivity.class);
                intent.putExtra(Constant.USER_ID, userId);
                intent.putExtra(Constant.USER_EMAIL, email);
                startActivity(intent);
            }
        });


        listView = (ListView) findViewById(R.id.cart_item_view);

        //adapter
        adapter = new CartItemListAdapter(this, orderItems, userId);
        listView.setAdapter(adapter);

        //set up firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDatabase = mDatabase.child("users").child(userId);
        userDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equalsIgnoreCase("cart")){
                    fetchData(dataSnapshot);
                    adapter.notifyDataSetChanged();
                    totalPrice ="$" + String.format("%.2f", getCost());
                    ((TextView)findViewById(R.id.cost)).setText("Total Cost: " + totalPrice);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equalsIgnoreCase("cart")){
                    fetchData(dataSnapshot);
                    adapter.notifyDataSetChanged();
                    totalPrice ="$" + String.format("%.2f", getCost());
                    ((TextView)findViewById(R.id.cost)).setText("Total Cost: " + totalPrice);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().equalsIgnoreCase("cart")){
                    fetchData(dataSnapshot);
                    adapter.notifyDataSetChanged();
                    totalPrice ="$" + String.format("%.2f", getCost());
                    ((TextView)findViewById(R.id.cost)).setText("Total Cost: " + totalPrice);
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fetchData(DataSnapshot dataSnapshot) {
        orderItems.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            orderItems.add(ds.getValue(CartItem.class));
        }
    }

    private Double getCost() {
        Double totalCost = 0.0;
        for (CartItem c :orderItems) {
            totalCost += (Double.valueOf(c.getItemPrice())*c.getItemQuantity());
        }
        return totalCost;
    }


    @Override
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        //Get current date
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        //Get current time
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        switch (view.getId()) {

            case R.id.cancel_order:
                Intent intent = new Intent(view.getContext(), MainCustomerActivity.class);
                intent.putExtra(Constant.USER_ID, userId);
                intent.putExtra(Constant.USER_EMAIL, email);
                startActivity(intent);
                break;

            case R.id.btn_date: //pick date button
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                String adjustDay, adjustMonth;
                                if (dayOfMonth < 10) adjustDay = "0" + dayOfMonth;
                                else adjustDay = String.valueOf(dayOfMonth);

                                if (monthOfYear + 1 < 10) adjustMonth = "0" + (monthOfYear + 1);
                                else adjustMonth = String.valueOf((monthOfYear + 1));

                                pick_date = adjustMonth + "-" + adjustDay + "-" + year;
                                txtDate.setText(pick_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

            case R.id.btn_time: //pick time button

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String adjustMin;

                                if (minute < 10) adjustMin = "0" + minute;
                                else adjustMin = String.valueOf(minute);

                                pick_time = hourOfDay + ":" + adjustMin + ":00";

                                if (hourOfDay > 12)
                                    txtTime.setText("0" + (hourOfDay - 12) + ":" + adjustMin + " PM");
                                else if (hourOfDay == 12)
                                    txtTime.setText(hourOfDay + ":" + adjustMin + " PM");
                                else
                                    txtTime.setText(hourOfDay + ":" + adjustMin + " AM");

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;

            case R.id.place_order: //order button

                //check if the pickup date and time is within 7 days from today, between 6 - 21
                String pattern = "MM-dd-yyyy HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                //get place time and pick up time
                String placeDateAndTime = (mMonth+1)+"-"+mDay+"-"+mYear+" "+mHour+":"+mMinute+":00";
                String pickDateAndTime = pick_date + " " + pick_time;
                try {
                    if (pick_date == null) pick_date = (mMonth+1)+ "-" + mDay +"-" +mYear;
                    if (pick_time == null) pick_time = mHour + ":" + mMinute + ":00";
                    Date pickD = simpleDateFormat.parse(pick_date +" "+ pick_time);
                    Date current =  simpleDateFormat.parse((mMonth+1)+ "-" + mDay +"-" +mYear+" "+ mHour + ":" + mMinute + ":00");
                    long difference = pickD.getTime() - current.getTime();
                    if (difference > Constant.SEVEN_DAY || difference < 0) {
                        Toast.makeText(this, "Pick up date is invalid!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!checkPickUpTime()){
                    Toast.makeText(this, "Pick up time should between 6AM - 9PM", Toast.LENGTH_SHORT).show();
                    break;
                }

                //obtain a new orderID by push() method, the same as add a new item
                String orderID = mDatabase.child("orders").push().getKey();

                //create a temporary order
                order = new Order(userId, email, orderID,  totalPrice, Constant.QUEUED,
                            pickDateAndTime, placeDateAndTime, orderItems);

                String[] dateAndTime =pickDateAndTime.split(" ");
                String pickUpDate = dateAndTime[0];
                new CartActivity.OrderAsyncClass().execute(pickUpDate);
        }

    }

    //check pick up time is in the working time range 6AM - 9PM
    private boolean checkPickUpTime() {
        String[] pickTime = pick_time.split(":");
        Integer hour = Integer.valueOf(pickTime[0]);

        if (hour < 6 || hour >= 21) return false;
        else return true;

    }

    //Async Task class for sending order confirmation email.
    class MyAsyncClass extends AsyncTask<String, Void, Void> {

        ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CartActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... para) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail("Order Confirmation", Constant.EMAIL_CONFIRMATION + para[0] + ". \n\n Thank you!",
                        "yaoweibio@gmail.com",email);
            }
            catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Toast.makeText(getApplicationContext(), "Email send", Toast.LENGTH_SHORT).show();
        }
    }

    //Async Task class for insert new order into Firebase
    class OrderAsyncClass extends AsyncTask<String, Void, TreeMap<String,Object>> {
        ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CartActivity.this);
            pDialog.setMessage("Checking order status...");
            pDialog.show();

        }

        @Override
        protected TreeMap<String, Object> doInBackground(String... para) {

            ValueEventListener postListener = new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        dataSet.put(data.getKey(), data.getValue());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabase.child("records").child(para[0]).addValueEventListener(postListener);
            return dataSet;

        }

        @Override
        protected void onPostExecute(TreeMap<String, Object> timeSet) {

            super.onPostExecute(timeSet);
            pDialog.cancel();
            System.out.println(timeSet.size());

            //check if pick up date is valid with prepareTime
            if (isValid(order.getPlaceDate(), order.getPickupDate())) {

                addOrder(timeSet);

                if (order.isValid()) { //if return true, fulfillmentTime and readyTime are also updated
                    //pushing new order to the database using orderID
                    mDatabase.child("orders").child(order.getOrderID()).setValue(order);
                    //Add new order into users profile
                    userDatabase.child("orderList").child(order.getOrderID()).setValue(order);
                    //Add order time segment into records profile
                    mDatabase.child("records").child(pick_date).child(order.getFulfillmentStartTime()).setValue(order.getReadyTime());
                    //update popularity count in order item
                    updatePopularity(order);

                    //send email to user
                    sender = new GMailSender("yaoweibio@gmail.com", Constant.PASSWORD);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                            Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        new CartActivity.MyAsyncClass().execute(order.getReadyTime());
                    } catch (Exception e) {
                        Log.e("Send Confirmation Mail", e.getMessage(), e);
                    }
                    mDatabase.child("users").child(userId).child("cart").setValue(null);
                    totalCost.setText("");
                    orderItems.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Order Added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.KITCHEN_BUSY, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), Constant.KITCHEN_BUSY, Toast.LENGTH_LONG).show();
            }
            //after order placed, remove cart content from database, clear UI input
            txtDate.setText("");
            txtTime.setText("");
        }
    }


    private void  updatePopularity(Order order) {
        for (CartItem c : order.getOrderItems()) {
            String category = c.getItemCategory();
            String itemId = c.getCartItemId();
            DatabaseReference popularity = mDatabase.child(category).child(itemId).child("popularity");
            popularity.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {

                                                     String current = dataSnapshot.getValue().toString();
                                                     Integer update = Integer.valueOf(current)+1;
                                                     dataSnapshot.getRef().setValue(update);
                                                 }

                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                 }
                                             });
        }
    }

    private long totalPrepareTimeInMiliSec() {
        long totalPrepareTime = 0;
        for (CartItem item : order.getOrderItems()) {
            totalPrepareTime += (item.getItemQuantity()*item.getPreparationTime());
        }
        return totalPrepareTime *60 * 1000;
    }

    private Boolean isValid(String placeDateAndTime, String pickDateAndTime) {
        long totalPreTimeInMiliSec = totalPrepareTimeInMiliSec();

        String[] dateAndTime =pickDateAndTime.split(" ");
        String pickUpDate = dateAndTime[0];
        String pickUpTime = dateAndTime[1];


        Date pickUp = new Date();
        Date placeTime = new Date();
        Date endTime = new Date();
        try {
            pickUp = simpleDateFormat.parse(pickDateAndTime);
            placeTime = simpleDateFormat.parse(placeDateAndTime);
            endTime = simpleDateFormat.parse(pickUpDate + " 21:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pickUp.getTime() - placeTime.getTime() < totalPreTimeInMiliSec) {
            return false;
        } else if (endTime.getTime() - placeTime.getTime() < totalPreTimeInMiliSec) {
            return false;
        }

        return true;
    }

    private void addOrder (TreeMap<String, Object> dataSet) {
        long totalPreTimeInMiliSec = totalPrepareTimeInMiliSec();
        final long ONE_HOUR = 1 * 60 * 60 * 1000;
        Date pickUp = new Date();
        String pick_date = order.getPickupDate();
        String[] dateAndTime = pick_date.split(" ");
        String pickUpDate = dateAndTime[0];
        try {
            pickUp = simpleDateFormat.parse(order.getPickupDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date fulfillmentStartTimeRight = new Date (pickUp.getTime() - totalPreTimeInMiliSec);
        Date fulfillmentStartTimeLeft = new Date (fulfillmentStartTimeRight.getTime() - ONE_HOUR);

        String fulfillLeftComplete = simpleDateFormat.format(fulfillmentStartTimeLeft);
        String fulfillRightComplete = simpleDateFormat.format(fulfillmentStartTimeRight);
        String fulfillLeft = (fulfillLeftComplete.split(" "))[1]; //start prepare earliest time
        String fulfillRight = (fulfillRightComplete.split(" "))[1]; // start prepare latest time



        Set startTimes = dataSet.entrySet();
        Iterator i = startTimes.iterator();

        while (i.hasNext()) {
            Map.Entry<String, Object> current = (Map.Entry<String, Object>) i.next();
            String currentReadyTime = (String) current.getValue();

            Date currentReady = new Date();
            try {
                currentReady = simpleDateFormat.parse(pickUpDate + " " + currentReadyTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentReadyTime.compareTo(fulfillRight) >= 0) {
                break;
            } else  {
                if (i.hasNext()) {
                    Map.Entry<String, Object> next = (Map.Entry<String, Object>) i.next();
                    Date nextStart = new Date();
                    try {
                        nextStart = simpleDateFormat.parse(pickUpDate + " " + next.getKey());
                    }catch( Exception e){
                        e.printStackTrace();
                    }
                    if ((nextStart.getTime() - currentReady.getTime()) > totalPrepareTimeInMiliSec()) {
                        order.setFulfillmentStartTime(currentReadyTime);
                        String readyTime = simpleDateFormat.format(currentReady.getTime()+totalPrepareTimeInMiliSec());
                        String[] dateWithTime = readyTime.split(" ");
                        order.setReadyTime(dateWithTime[1]);
                        order.setValid(true);
                    } else {
                        break;
                    }

                } else {
                    order.setFulfillmentStartTime(currentReadyTime);
                    String readyTime = simpleDateFormat.format(currentReady.getTime()+totalPrepareTimeInMiliSec());
                    String[] dateWithTime = readyTime.split(" ");
                    order.setReadyTime(dateWithTime[1]);
                    order.setValid(true);
                }

            }


        }
        order.setFulfillmentStartTime(fulfillLeft);
        String startFulFil = pickUpDate + " " + fulfillLeft;
        Date ready = new Date();
        try {
            ready = simpleDateFormat.parse(startFulFil);
        } catch (Exception e ){
            e.printStackTrace();
        }
        String readyTime = simpleDateFormat.format(ready.getTime()+totalPrepareTimeInMiliSec());
        String[] dateWithTime = readyTime.split(" ");
        order.setReadyTime(dateWithTime[1]);
        order.setValid(true);


    }

}
