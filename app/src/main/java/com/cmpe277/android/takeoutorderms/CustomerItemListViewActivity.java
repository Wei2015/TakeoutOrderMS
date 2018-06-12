package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.cmpe277.android.takeoutorderms.model.Constant;
import com.cmpe277.android.takeoutorderms.model.CustomerItemListAdapter;
import com.cmpe277.android.takeoutorderms.model.Item;
import com.cmpe277.android.takeoutorderms.model.SortByName;
import com.cmpe277.android.takeoutorderms.model.SortByPopularity;
import com.cmpe277.android.takeoutorderms.model.SortByPrice;
import com.cmpe277.android.takeoutorderms.service.CategoryItemsService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerItemListViewActivity extends AppCompatActivity{

    //firebase database
    private DatabaseReference mDatabase;

    //view objects
    private ListView listView;
    private TextView listTitle;
    private Toolbar toolbar;

    private CustomerItemListAdapter adapter;

    //Buttons
    private Button sort;
    private RadioButton sortByName;
    private RadioButton sortByPrice;
    private RadioButton sortByPopularity;

    //pass user id and email
    private String userId;
    private String email;
    private ArrayList<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_view);
        listView = (ListView)findViewById(R.id.item_list_view);
        listTitle = (TextView) findViewById(R.id.list_title);

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        //RadioButtons
        sortByName = (RadioButton)findViewById(R.id.sort_by_name);
        sortByPrice = (RadioButton)findViewById(R.id.sort_by_price);
        sortByPopularity = (RadioButton) findViewById(R.id.sort_by_pop);


        //Get the intent message from previous activity
        Intent intent = getIntent();
        final String categoryName = intent.getStringExtra(Constant.CATEGORY_NAME);
        userId = intent.getStringExtra(Constant.USER_ID);
        email = intent.getStringExtra(Constant.USER_EMAIL);
        listTitle.setText(categoryName + " List");
        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainCustomerActivity.class);
                intent.putExtra(Constant.USER_ID, userId);
                intent.putExtra(Constant.USER_EMAIL, email);
                startActivity(intent);
            }
        });


        //set up firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equalsIgnoreCase(categoryName)){
                    fetchData(dataSnapshot);
                    adapter.sort(new SortByName());
                    sortByName.setChecked(true);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //adapter
        adapter = new CustomerItemListAdapter(this, itemList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent itemSelect = new Intent(getApplicationContext(), CustomerItemViewActivity.class);
                Item selectedItem = (Item)adapterView.getItemAtPosition(i);

                itemSelect.putExtra(Constant.USER_ID, userId);
                itemSelect.putExtra(Constant.USER_EMAIL, email);
                itemSelect.putExtra(Constant.ITEM_ID, selectedItem.getId());
                itemSelect.putExtra(Constant.ITEM_CATEGORY, selectedItem.getCategory());
                itemSelect.putExtra(Constant.ITEM_NAME, selectedItem.getName());
                itemSelect.putExtra(Constant.ITEM_PRICE, String.valueOf(selectedItem.getPrice()));
                itemSelect.putExtra(Constant.ITEM_IMAGE, selectedItem.getPicturePath());
                itemSelect.putExtra(Constant.ITEM_CALORIES, String.valueOf(selectedItem.getCalories()));
                itemSelect.putExtra(Constant.ITEM_PRETIME, String.valueOf(selectedItem.getPreparationTime()));
                startActivity(itemSelect);
            }
        });

        //Sort Button;
        sort = (Button)findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortByName.isChecked()) {
                    adapter.sort(new SortByName());
                } else if (sortByPrice.isChecked()) {
                    adapter.sort(new SortByPrice());
                } else if (sortByPopularity.isChecked()){
                    adapter.sort(new SortByPopularity());
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void fetchData(DataSnapshot dataSnapshot) {
        itemList.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            itemList.add(ds.getValue(Item.class));
        }

    }


}
