package com.example.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hudomju.swipe.OnItemClickListener;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        todo = new ArrayList<>();
        final ListView main = findViewById(R.id.list);

        final ItemListAdapter adapter = new ItemListAdapter(todo, MainActivity.this);
        main.setAdapter(adapter);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        Map<String, String> prefList = (Map<String, String>) pref.getAll();
        if (prefList.size()!=0) {
            for (int i = 0; i < prefList.size(); i++) {
                String item = prefList.get(String.format("Item%d", i));
                todo.add(new Item(item, false));
                adapter.notifyDataSetChanged();
            }
        }

        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Add task");
                alertDialog.setMessage("Enter text here:");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);



                alertDialog.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                String newTask = input.getText().toString();
                                todo.add(new Item(newTask, false));
                                adapter.notifyDataSetChanged();

                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                alertDialog.setView(input);
                alertDialog.show();
            }
        });

        main.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Item item = todo.get(position);
                AlertDialog.Builder doublecheck = new AlertDialog.Builder(MainActivity.this);
                doublecheck.setTitle("Delete");
                doublecheck.setMessage("Are you sure you want to delete this item?");
                final int positionToRemove = position;
                doublecheck.setNegativeButton("Cancel", null);
                doublecheck.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        todo.remove(item);
                        adapter.notifyDataSetChanged();
                    }});
                doublecheck.show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();

        ListView list = findViewById(R.id.list);

        for (int i=0; i<todo.size(); i++){
            //Item item = (Item) list.getAdapter().getItem(i);
            Item item = todo.get(i);
            if (!item.isChecked()) {
                String val = item.getText();
                editor.putString(String.format("Item%d", i), val);
            }
        }
        editor.apply();

    }

}
