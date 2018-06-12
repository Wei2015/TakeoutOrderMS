package com.cmpe277.android.takeoutorderms.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpe277.android.takeoutorderms.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by weiyao on 4/11/18.
 */

public class CustomerItemListAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> itemList;
    Context mContext;


    public CustomerItemListAdapter(Context context, ArrayList<Item> itemList) {
        super(context, R.layout.row_item, itemList);
        this.itemList = itemList;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent,false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView price = (TextView) convertView.findViewById(R.id.item_price);
        final ImageView icon = (ImageView) convertView.findViewById(R.id.item_icon);

        name.setText(item.getName());
        price.setText("$"+String.valueOf(item.getPrice()));

        //retrieve item image
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(item.getPicturePath());
        imageRef.getBytes(Constant.SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                icon.setImageBitmap(imageBitmap);
            }
        });

        return convertView;

    }

//    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
//        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
//    }


}
