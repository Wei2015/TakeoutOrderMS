package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cmpe277.android.takeoutorderms.model.Constant;
import com.google.firebase.auth.FirebaseAuth;

public class MainAdminActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Add Item"));
        tabLayout.addTab(tabLayout.newTab().setText("View Item"));
        tabLayout.addTab(tabLayout.newTab().setText("View Order"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) findViewById(R.id.container);
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(pagerAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            int tabNum = intent.getIntExtra(Constant.TABNUM, 0);
            mViewPager.setCurrentItem(tabNum);
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_signout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

            case R.id.admin_sign_out:
                //save file
                FirebaseAuth.getInstance().signOut();
                //return to login activity
                startActivity(new Intent(MainAdminActivity.this, LoginActivity.class));
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
