package com.cmpe277.android.takeoutorderms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemListActivity extends AppCompatActivity implements View.OnClickListener{

    //firebase objects: storage and database
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    //view objects
    private ListView itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        itemList = (ListView)findViewById(R.id.item_list_view);


    }

    @Override
    public void onClick(View view) {

    }
}
