package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.model.CategoryAdapter;
import com.cmpe277.android.takeoutorderms.model.CategoryCollection;
import com.cmpe277.android.takeoutorderms.model.Constant;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainCustomerActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private CategoryAdapter adapter;
    private GridView gridView;

    private String userID;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);


        //set up navi bar
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        //set up toolbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        //obtain firebase uid for current user
        Intent intent = getIntent();

        userID = intent.getStringExtra(Constant.USER_ID);
        email = intent.getStringExtra(Constant.USER_EMAIL);


        //set up adapter for grid view
        gridView = (GridView) findViewById(R.id.gv);
        adapter = new CategoryAdapter(this, userID, email, CategoryCollection.getData());
        gridView.setAdapter(adapter);

    }


    //set up navigation drawer
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        switch (menuItem.getItemId()){
                            case R.id.account:
                                //show account UI
                                sentIntent(AccountActivity.class);
                                break;

                            case R.id.order:
                                //show order UI
                               sentIntent(OrderActivity.class);
                                break;
                        }
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    //sent Intent to other activity
    private void sentIntent(Class tClass) {
        Intent accountIntent = new Intent(MainCustomerActivity.this, tClass);
        accountIntent.putExtra(Constant.USER_ID, userID);
        accountIntent.putExtra(Constant.USER_EMAIL, email);
        startActivity(accountIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                //show cart UI
                Intent intent = new Intent(MainCustomerActivity.this, CartActivity.class);
                intent.putExtra(Constant.USER_ID, userID);
                intent.putExtra(Constant.USER_EMAIL, email);
                startActivity(intent);

                break;

            case R.id.sign_out:

                GoogleSignInClient client = App.getInstance().getmGoogleSignInClient();
                client.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "You are signed out.", Toast.LENGTH_SHORT).show();
                }
            });
                client.revokeAccess();
                FirebaseAuth.getInstance().signOut();
                //return to login activity
                startActivity(new Intent(MainCustomerActivity.this, LoginActivity.class));
                finish();
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

}
