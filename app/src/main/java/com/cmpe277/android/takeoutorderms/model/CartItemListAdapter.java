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

public class CartItemListAdapter extends ArrayAdapter<CartItem> {

    private ArrayList<CartItem> itemList;
    Context mContext;
    private String userID;



    public CartItemListAdapter (Context context, ArrayList<CartItem> itemList, String userID) {
        super(context, R.layout.cart_row_item, itemList);
        this.itemList = itemList;
        this.mContext = context;
        this.userID = userID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final CartItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_row_item, parent,false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView price = (TextView) convertView.findViewById(R.id.item_price);
        TextView quantity = (TextView) convertView.findViewById(R.id.item_quantity);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemId = item.getCartItemId();

                //set up firebase database
                DatabaseReference mDatabase = getInstance().getReference();
                mDatabase.child("users").child(userID).child("cart").child(itemId).removeValue();
                itemList.remove(item);
                Toast.makeText(view.getContext(), item.getItemName() + " is removed.", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

        final ImageView icon = (ImageView) convertView.findViewById(R.id.item_icon);

        name.setText(item.getItemName());
        price.setText("$"+item.getItemPrice());
        quantity.setText("Qt: " + item.getItemQuantity());


        //retrieve item image
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(item.getItemImage());
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
