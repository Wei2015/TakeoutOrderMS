package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.cmpe277.android.takeoutorderms.model.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AccountActivity extends AppCompatActivity {

    private String userID;
    private String email;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //obtain firebase uid for current user
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra(Constant.USER_ID);
            email = intent.getStringExtra(Constant.USER_EMAIL);
        }


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



        DatabaseReference userDbRef = FirebaseDatabase.getInstance().getReference().child(Constant.USERS).child(userID);
        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equalsIgnoreCase("username")) {
                            ((EditText) findViewById(R.id.user_name)).setText(child.getValue().toString());
                        }
                        if (child.getKey().equalsIgnoreCase("email")) {
                            ((EditText) findViewById(R.id.user_email)).setText(child.getValue().toString());
                        }
                    }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
