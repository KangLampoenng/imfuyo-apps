package com.example.imfuyo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class LiveStockDetailOwnerActivity extends AppCompatActivity {

    ImageView imageViewFotoSapi;
    TextView textNamaTernak, textNamaPeternak, textJenisTernak, textKondisiTernak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stock_detail_owner);

        imageViewFotoSapi = findViewById(R.id.idFotoSapi);
        textNamaTernak = findViewById(R.id.idNamaTernak);
        textNamaPeternak = findViewById(R.id.idNamaPeternak);
        textJenisTernak = findViewById(R.id.textView12);
        textKondisiTernak = findViewById(R.id.textView9);

        getData();
    }

    private void getData() {
        Bundle extra = this.getIntent().getExtras();
        if(extra != null) {
            String fotoTernak = extra.getString("foto_ternak");
            String namaTernak = extra.getString("nama_ternak");
            String namaPeternak = extra.getString("nama_peternak");
            String jenisTernak = extra.getString("jenis_ternak");
            String kondisiTernak = extra.getString("kondisi_ternak");
            setDataActivity(fotoTernak, namaTernak, namaPeternak, jenisTernak, kondisiTernak);
        }
    }


    private void setDataActivity(String fotoTernak, String namaTernak, String namaPeternak, String jenisTernak, String kondisiTernak){

        Glide.with(this).asBitmap().load(fotoTernak).into(imageViewFotoSapi);

        textNamaTernak.setText(namaTernak);
        textNamaPeternak.setText(namaPeternak);
        textJenisTernak.setText(jenisTernak);
        textKondisiTernak.setText(kondisiTernak);
    }
}