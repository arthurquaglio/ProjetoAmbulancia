package com.example.projetoambulancia;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.button_new_call).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, NewCallActivity.class)));
        findViewById(R.id.button_occupancy).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, OccupancyActivity.class)));
        findViewById(R.id.button_history).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, HistoryActivity.class)));
        findViewById(R.id.button_graph_list).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, GraphListActivity.class)));
        findViewById(R.id.button_graph_map).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, GraphMapActivity.class)));
        findViewById(R.id.button_tests).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, TestsActivity.class)));
    }
}