package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.myapplication.MainActivity.ITEM_POSITION;
import static com.example.myapplication.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    //track edit text
    EditText edititemtext;
     //position of edited item in list
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //resolve edit text from layout
        edititemtext = (EditText) findViewById(R.id.edititemText);

        //set edit text value from intent extra
        edititemtext.setText(getIntent().getStringExtra(ITEM_TEXT));

        //update edit text from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION,0);
        //update the title bar for the activity
        getSupportActionBar().setTitle("Modifier l'élément");

    }

    //Handler for the save button
    public void onSaveItem(View v){
        //prepare the new intent for the result
        Intent intent = new Intent();
        //pass updated item text as extra
        intent.putExtra(ITEM_TEXT,edititemtext.getText().toString());
        //pass original position
        intent.putExtra(ITEM_POSITION,position);
        //set the intent as the result of the activity
        setResult(RESULT_OK,intent);
        //close the activity and redirect
        finish();
    }
}
