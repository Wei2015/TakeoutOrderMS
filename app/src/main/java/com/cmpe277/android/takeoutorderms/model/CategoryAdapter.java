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

import com.cmpe277.android.takeoutorderms.CustomerItemListViewActivity;
import com.cmpe277.android.takeoutorderms.R;

import java.util.ArrayList;

/**
 * Created by weiyao on 4/14/18.
 */

public class CategoryAdapter extends BaseAdapter {

    Context c;
    String userID;
    String email;
    ArrayList<Category> categories;


    public CategoryAdapter (Context c, String userID, String email, ArrayList<Category> categories) {
        this.c = c;
        this.userID = userID;
        this.email = email;
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
                Intent intent = new Intent(c, CustomerItemListViewActivity.class);
                intent.putExtra(Constant.CATEGORY_NAME, category.getName());
                intent.putExtra(Constant.USER_ID, userID);
                intent.putExtra(Constant.USER_EMAIL, email);
                c.startActivity(intent);
            }
        });


        return view;
    }
}
