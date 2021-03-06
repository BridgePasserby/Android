package com.zingking.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void setListener() {
        btnWeight.setOnClickListener(this);
    }

    private void initView() {
        btnWeight = (Button) findViewById(R.id.btn_weight);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_weight:
                startActivity(new Intent(this, WeightActivity.class));
                break;
            default:
                break;
        }
    }
}
