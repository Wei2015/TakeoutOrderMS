package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.model.AdminItemListAdapter;
import com.cmpe277.android.takeoutorderms.model.Constant;
import com.cmpe277.android.takeoutorderms.model.Item;
import com.cmpe277.android.takeoutorderms.model.SortByPopularity;
import com.cmpe277.android.takeoutorderms.service.CategoryItemsService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminItemListActivity extends AppCompatActivity {

    //firebase database
    private DatabaseReference mDatabase;

    //view objects
    private ListView listView;
    private TextView listTitle;
    private Toolbar toolbar;

    private ArrayList<Item> itemList= new ArrayList<>();

    private AdminItemListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_item_list);

        listView = (ListView)findViewById(R.id.item_list_view);
        listTitle = (TextView)findViewById(R.id.list_title);

        //Get the intent message from previous activity
        Intent intent = getIntent();
        final String categoryName = intent.getStringExtra(Constant.CATEGORY_NAME);
        listTitle.setText(categoryName + " List");
        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainAdminActivity.class);
                intent.putExtra(Constant.TABNUM, 1);
                startActivity(intent);
            }
        });


        //adapter
        adapter = new AdminItemListAdapter(this, itemList);
        listView.setAdapter(adapter);

        //set up firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equalsIgnoreCase(categoryName)){
                    fetchData(dataSnapshot);
                    adapter.sort(new SortByPopularity());
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


    }

    private void fetchData(DataSnapshot dataSnapshot) {
        itemList.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            itemList.add(ds.getValue(Item.class));
        }

    }
}
