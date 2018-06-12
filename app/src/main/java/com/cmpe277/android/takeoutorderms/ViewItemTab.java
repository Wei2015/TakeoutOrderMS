package com.cmpe277.android.takeoutorderms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.cmpe277.android.takeoutorderms.model.AdminCategoryAdapter;
import com.cmpe277.android.takeoutorderms.model.CategoryCollection;


public class ViewItemTab extends Fragment {

    private AdminCategoryAdapter adapter;
    private GridView gridView;


    public ViewItemTab() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_category_view, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set up adapter for grid view
        gridView = (GridView) view.findViewById(R.id.admin_gv);
        adapter = new AdminCategoryAdapter(view.getContext(), CategoryCollection.getData());
        gridView.setAdapter(adapter);

    }




}
