package com.example.aleachoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Collection;
import model.Item;
import view.ItemAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Collection collection;
    private Button go_button;
    private FloatingActionButton add_button;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView add_text;

    private final static String CHOICE_STORAGE = "choice.data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On essaie de recupérer les données de la dernière activité (e.g changement d'orientation du téléphone).
        /*if (savedInstanceState != null && savedInstanceState.containsKey("collection")) {
            collection = (Collection)savedInstanceState.getSerializable("collection");
        }*/
        try {
            FileInputStream in = this.openFileInput(CHOICE_STORAGE);
            ObjectInputStream br = new ObjectInputStream(in);
            collection = (Collection)br.readObject();
        } catch (Exception e) {
            // Sinon on crée une collection avec trois éléments.
            collection = new Collection();

            Item item1 = new Item("Pizza");
            Item item2 = new Item("Burger");
            Item item3 = new Item("Tacos");

            collection.addItem(item1);
            collection.addItem(item2);
            collection.addItem(item3);
        }

        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ItemAdapter(collection);
        recyclerView.setAdapter(mAdapter);

        go_button = findViewById(R.id.go_button);
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("collection", collection);
                startActivity(intent);
            }
        });

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.add_dialog, null);
        add_text = dialogView.findViewById(R.id.add_text);

        builder.setView(dialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Item item = new Item(add_text.getText().toString());
                        collection.addItem(item);
                    }
                });

        dialog = builder.create();

        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //savedInstanceState.putSerializable("collection", collection);
        // TODO json
        try {
            FileOutputStream out = this.openFileOutput(CHOICE_STORAGE, MODE_PRIVATE);
            ObjectOutputStream br = new ObjectOutputStream(out);
            br.writeObject(collection);
        }
        catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            super.onDestroy();
            FileOutputStream out = this.openFileOutput(CHOICE_STORAGE, MODE_PRIVATE);
            ObjectOutputStream br = new ObjectOutputStream(out);
            br.writeObject(collection);
        }
        catch (Exception e) {

        }
    }
}
