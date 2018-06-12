package com.cmpe277.android.takeoutorderms.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.AdminItemListActivity;
import com.cmpe277.android.takeoutorderms.R;

import java.util.ArrayList;

public class AdminCategoryAdapter extends BaseAdapter {

    Context c;
    ArrayList<Category> categories;


    public AdminCategoryAdapter (Context c, ArrayList<Category> categories) {
        this.c = c;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.category_card_view, viewGroup, false);
        }

        final Category category = (Category)this.getItem(i);

        TextView categoryView = (TextView) view.findViewById(R.id.category_text);
        ImageView categoryImg = (ImageView) view.findViewById(R.id.category_img);

        categoryView.setText(category.getName());
        categoryImg.setImageResource(category.getImage());

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, AdminItemListActivity.class);
                intent.putExtra(Constant.CATEGORY_NAME, category.getName());
                c.startActivity(intent);
                //new Intent
                Toast.makeText(c, category.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
