package com.example.aleachoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

import model.Collection;
import model.Item;

public class ResultActivity extends AppCompatActivity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.result);

        /*Intent intent = getIntent();
        Collection.PickResult pickResult = (Collection.PickResult)intent.getSerializableExtra("result");

        Item result_item = pickResult.item;
        result.setText(result_item.name());
        result.setTextColor(result_item.color());*/
    }
}
