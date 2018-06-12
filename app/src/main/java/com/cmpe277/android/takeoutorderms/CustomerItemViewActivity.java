package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.model.CartItem;
import com.cmpe277.android.takeoutorderms.model.Constant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class CustomerItemViewActivity extends AppCompatActivity {

    private TextView itemCategory;
    private TextView itemName;
    private TextView itemPrice;
    private TextView itemCalories;
    private ImageView itemImage;
    private EditText orderQuantity;
    private Button BtnAddToCart;
    private String itemId;
    private String item_name;
    private String item_price;
    private String item_prepareTime;
    private String item_image_path;
    private String item_category;
    private String userId;
    private String email;

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemCategory = findViewById(R.id.item_show_categroy);
        itemName = findViewById(R.id.item_show_name);
        itemPrice = findViewById(R.id.item_show_price);
        itemCalories = findViewById(R.id.item_show_calories);
        itemImage = findViewById(R.id.item_show_image);

        orderQuantity = findViewById(R.id.order_quantity);
        BtnAddToCart = findViewById(R.id.addToCart);


        Intent intent = getIntent();

        if (intent != null) {
            item_name = intent.getStringExtra(Constant.ITEM_NAME);
            item_price = intent.getStringExtra(Constant.ITEM_PRICE);
            itemId = intent.getStringExtra(Constant.ITEM_ID);
            item_prepareTime = intent.getStringExtra(Constant.ITEM_PRETIME);
            userId = intent.getStringExtra(Constant.USER_ID);
            email = intent.getStringExtra(Constant.USER_EMAIL);
            item_category = intent.getStringExtra(Constant.ITEM_CATEGORY);

            itemCategory.setText("Category:    " + item_category);
            itemName.setText("Name:    " + item_name);
            itemPrice.setText("Unit Price:    $" + item_price);
            itemCalories.setText("Calories:    " +intent.getStringExtra(Constant.ITEM_CALORIES));
            item_image_path = intent.getStringExtra(Constant.ITEM_IMAGE);

            //retrieve item image
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(item_image_path);
            imageRef.getBytes(Constant.SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    itemImage.setImageBitmap(imageBitmap);
                }
            });
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CustomerItemListViewActivity.class);
                intent.putExtra(Constant.USER_ID, userId);
                intent.putExtra(Constant.USER_EMAIL, email);
                intent.putExtra(Constant.CATEGORY_NAME, item_category);
                startActivity(intent);
            }
        });
    }

    public void addToCart(View view) {
        Integer quantityInput = Integer.valueOf(orderQuantity.getText().toString().trim());
        if(quantityInput == 0 || quantityInput > 99 ) {
            Toast.makeText(this, "Quantity input should be 1 ~ 99", Toast.LENGTH_SHORT).show();
            return;
        }

        //upload data into firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        CartItem itemAdded = new CartItem(itemId, item_category, item_name, item_price, item_image_path, quantityInput, Integer.valueOf(item_prepareTime));
        itemAdded.setCartItemId(itemId);
        HashMap<String, Object> itemDetail= new HashMap<>();
        itemDetail.put(itemId, itemAdded);
        mDatabase.child("users").child(userId).child("cart").updateChildren(itemDetail);

        //reset the quantity number to 1
        orderQuantity.setText("1");

        Toast.makeText(this, "Add "+ quantityInput + " " + item_name + " to your cart!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cart) {

            //show cart UI
            Intent intent = new Intent(CustomerItemViewActivity.this, CartActivity.class);
            intent.putExtra(Constant.USER_ID, userId);
            intent.putExtra(Constant.USER_EMAIL, email);
            startActivity(intent);


        }
        return true;
    }


}
