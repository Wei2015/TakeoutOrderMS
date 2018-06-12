package com.cmpe277.android.takeoutorderms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.model.Constant;
import com.cmpe277.android.takeoutorderms.model.Item;
import com.cmpe277.android.takeoutorderms.service.CategoryItemsService;
import com.cmpe277.android.takeoutorderms.service.ValidationHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;



public class AddItemTab extends Fragment implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 111;

    //TextInputLayout variables
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPrice;
    private TextInputLayout textInputLayoutCalories;
    private TextInputLayout textInputLayoutTime;
    private TextInputLayout textInputLayoutRadioBtn;

    //EditText variables
    private EditText textName;
    private EditText textPrice;
    private EditText textCalories;
    private EditText textPrepareTime;

    //Radio Buttons
    private RadioButton[] radioButtons = new RadioButton[4];

    //ImageView
    private ImageView photoTaken;

    //Button
    private Button reset;
    private Button submit;
    private Button uploadPhoto;

    private ValidationHelper validation;

    //firebase database
    private DatabaseReference mDatabase;
    private CategoryItemsService categoryItemsService;

    //firebase storage
    private StorageReference mStorage;

    //string store image encode
    //private String imageEncoded;
    private byte[] imageData;



    public AddItemTab() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_tab, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners(view);

    }

    private void initViews(View v) {
        textInputLayoutName = (TextInputLayout) v.findViewById(R.id.text_input_layout_name);
        textInputLayoutPrice = (TextInputLayout) v.findViewById(R.id.text_input_layout_price);
        textInputLayoutCalories = (TextInputLayout) v.findViewById(R.id.text_input_layout_calories);
        textInputLayoutTime = (TextInputLayout) v.findViewById(R.id.text_input_layout_preparetime);
        textInputLayoutRadioBtn = (TextInputLayout) v.findViewById(R.id.text_input_layout_radioButton);

        textName = (EditText) v.findViewById(R.id.item_name);
        textPrice = (EditText) v.findViewById(R.id.item_price);
        textCalories = (EditText) v.findViewById(R.id.calories);
        textPrepareTime = (EditText) v.findViewById(R.id.preparetime);

        for (int i = 0; i < radioButtons.length; i++){
            switch (i) {
                case 0:
                    radioButtons[i] = (RadioButton) v.findViewById(R.id.radio_appetizer);
                    break;
                case 1:
                    radioButtons[i] = (RadioButton) v.findViewById(R.id.radio_dessert);
                    break;
                case 2:
                    radioButtons[i] = (RadioButton) v.findViewById(R.id.radio_drink);
                    break;
                case 3:
                    radioButtons[i] = (RadioButton) v.findViewById(R.id.radio_main);
            }
        }


        reset = (Button) v.findViewById(R.id.reset_btn);
        submit = (Button) v.findViewById(R.id.submit_btn);
        submit.setEnabled(false);
        uploadPhoto = (Button) v.findViewById(R.id.uploadPhoto_btn);

        photoTaken = (ImageView) v.findViewById(R.id.photo_taken);

        validation = new ValidationHelper(v.getContext());

    }

    private void initListeners(View v) {
        reset.setOnClickListener(this);
        submit.setOnClickListener(this);
        uploadPhoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_btn:
                reset();
                break;

            case R.id.submit_btn:
                checkValidation();
                createNewItem();
                reset();
                break;

            case R.id.uploadPhoto_btn:
                submit.setEnabled(true);
                onLaunchCamera();

        }

    }

    private void reset() {
        textInputLayoutRadioBtn.setErrorEnabled(false);
        textInputLayoutName.setErrorEnabled(false);
        textInputLayoutCalories.setErrorEnabled(false);
        textInputLayoutPrice.setErrorEnabled(false);
        textInputLayoutTime.setErrorEnabled(false);

        textName.setText("");
        textPrice.setText("");
        textCalories.setText("");
        textPrepareTime.setText("");

        photoTaken.setImageBitmap(null);

        for(RadioButton btn : radioButtons) {
            btn.setChecked(false);
        }
        submit.setEnabled(false);
    }


    private void checkValidation() {

        boolean check = false;
        for (RadioButton btn : radioButtons) {
            if (btn.isChecked()) check = true;
        }
        if (!check) {
            Toast.makeText(this.getContext(), Constant.RADIO, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validation.isEditTextFilled(textName, textInputLayoutName, getString(R.string.error_message_item_name)))
            return;
        if (!validation.isEditTextFilled(textPrice, textInputLayoutPrice, getString(R.string.error_message_price)))
            return;
        if (!validation.isEditTextFilled(textCalories, textInputLayoutCalories, getString(R.string.error_message_calories)))
            return;
        if (!validation.isTimeValid(textPrepareTime, textInputLayoutTime, getString(R.string.error_message_time)))
            return;
        if (!validation.isRadioButtonSelected(radioButtons, textInputLayoutRadioBtn, getString(R.string.error_message_radioButton)))
            return;


        Toast.makeText(this.getContext(), getString(R.string.success_message), Toast.LENGTH_SHORT).show();

    }

    private void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoTaken.setImageBitmap(imageBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            //imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            imageData = baos.toByteArray();
        }
    }


    private void createNewItem() {

        if ( imageData != null ) {

            String categoryName = "";
            for (RadioButton btn : radioButtons) {
                if (btn.isChecked()) {
                    categoryName = btn.getText().toString();
                    break;
                }
            }
            //set up firebase database
            mDatabase = FirebaseDatabase.getInstance().getReference();
            categoryItemsService = new CategoryItemsService(mDatabase, categoryName);

            final String category = categoryName;

            //obtain item information from input
            final String itemName = textName.getText().toString();
            final Double itemPrice = Double.valueOf(textPrice.getText().toString());
            final Integer itemPrepareTime = Integer.valueOf(textPrepareTime.getText().toString());
            final Integer itemCalories = Integer.valueOf(textCalories.getText().toString());

            mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference node = mStorage.child(itemName);
            UploadTask uploadTask = node.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String itemImageUri = downloadUrl.toString();

                    //create a new item and add to firebase (works after login with auth)
                    Item newItem = new Item(itemName, itemImageUri, itemPrice, itemPrepareTime, category, itemCalories);
                    categoryItemsService.addItem(newItem);
                }
            });
        }
    }

}
