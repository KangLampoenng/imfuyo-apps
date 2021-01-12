package com.example.imfuyo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomeOwnerActivity extends AppCompatActivity {

    ImageView ImgLiveStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner);

        ImgLiveStock=(ImageView)findViewById(R.id.liveStock);
        ImgLiveStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
    }

    public void open(){
        Intent intent = new Intent(this, LiveStockOwnerActivity.class);
        startActivity(intent);
    }

}