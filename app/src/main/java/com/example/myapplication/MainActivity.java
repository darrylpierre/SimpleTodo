package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static int EDIT_REQUEST_CODE =20;

    // keys used for passing data between activities
    public final static String ITEM_TEXT="itemText";
    public final static String ITEM_POSITION="itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        items= new ArrayList<>();
        itemsAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvItems= (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

       // items.add("Premier element");
       // items.add("Deuxieme eleement");

        setupListViewListener();


    }
    public void onAddItem (View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewitem);
        String itemText= etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writedItems();
        Toast.makeText(getApplicationContext(),"article ajouté à la liste",Toast.LENGTH_SHORT).show();
    }
    private void setupListViewListener (){
        Log.i("MainActivity", "configuration de l'auditeur en mode liste");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("MainActivity","Article retiré de la liste: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writedItems();

                return true;
            }
        });
        //regular click
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //create the new activity
                Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
                intent.putExtra(ITEM_TEXT,items.get(position));
                intent.putExtra(ITEM_POSITION,position);
                startActivityForResult(intent,EDIT_REQUEST_CODE);
                //pass the data being edited
                //display the activity

            }
        });

    }

    // handle results fron the edit Activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the ecdit activity completed ok
        if (resultCode == RESULT_OK && requestCode ==  EDIT_REQUEST_CODE){
            //extra update item text from result intent Extra
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extra original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model with the new item text at the edited position
            items.set(position,updatedItem);
             //notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();
            //persit the model changed
            writedItems();
            //notify the user the operation cpmpleted ok
            Toast.makeText(this,"mise à jour de l'article avec succès", Toast.LENGTH_SHORT).show();



        }
    }

    private File getDataFile() {
        return new File(getFilesDir(),"todo.txt");
    }
    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Erreur de lecture du fichier",e);
            items=new ArrayList<>();
        }
    }
    private void writedItems() {
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Erreur d'ecriture du fichier",e);
        }
    }
}
