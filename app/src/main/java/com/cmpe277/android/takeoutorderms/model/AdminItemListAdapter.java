package com.cmpe277.android.takeoutorderms.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.R;
import com.cmpe277.android.takeoutorderms.service.CategoryItemsService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class AdminItemListAdapter extends ArrayAdapter<Item> {

    Context mContext;
    private ArrayList<Item> itemList;


    public AdminItemListAdapter(Context context, ArrayList<Item> itemList) {
        super(context, R.layout.admin_row_item, itemList);
        this.mContext = context;
        this.itemList = itemList;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
       final Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.admin_row_item, parent,false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView price = (TextView) convertView.findViewById(R.id.item_price);
        TextView popularity = (TextView) convertView.findViewById(R.id.item_popularity);
        final ImageView icon = (ImageView) convertView.findViewById(R.id.item_icon);
        name.setText(item.getName());
        price.setText("$"+String.valueOf(item.getPrice()));
        popularity.setText("Popularity: " + String.valueOf((item.getPopularity())));

        ImageButton removeBtn = (ImageButton) convertView.findViewById(R.id.delete);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = item.getCategory();
                String itemId = item.getId();
                String name = item.getName();
                //set up firebase database
                DatabaseReference mDatabase = getInstance().getReference();
                CategoryItemsService categoryItemsService = new CategoryItemsService(mDatabase, category);
                categoryItemsService.removeItem(itemId);
                Toast.makeText(view.getContext(), name + " is removed.", Toast.LENGTH_SHORT).show();
                itemList.remove(item);
                notifyDataSetChanged();
            }
        });


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
}
