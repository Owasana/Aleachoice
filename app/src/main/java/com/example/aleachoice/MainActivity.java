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

import model.Collection;
import model.Item;
import view.ItemAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Collection m_collection;
    private Button go_button;
    private FloatingActionButton add_button;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView add_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_collection = new Collection();

        Item item1 = new Item("Pizza");
        Item item2 = new Item("Burger");
        Item item3 = new Item("Tacos");

        m_collection.addItem(item1);
        m_collection.addItem(item2);
        m_collection.addItem(item3);

        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ItemAdapter(m_collection);
        recyclerView.setAdapter(mAdapter);

        go_button = findViewById(R.id.go_button);
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection.PickResult result = m_collection.pick();
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("result", result);
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
                        m_collection.addItem(item);
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


}
