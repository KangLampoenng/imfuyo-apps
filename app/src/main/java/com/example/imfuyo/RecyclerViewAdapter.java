package com.example.imfuyo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<LiveStockData> listTernak;
    private Context context;
    dataListener listener;

    public RecyclerViewAdapter(ArrayList<LiveStockData> listTernak, Context context) {
        this.listTernak = listTernak;
        this.context = context;
        listener = (LiveStockOwnerActivity)context;
    }

    public interface dataListener{
        void onDeleteData(LiveStockData data, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView namaTernak, namaPeternak, jenisTernak;
        private ImageView ivSapi;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            namaTernak = itemView.findViewById(R.id.idNamaTernak);
            namaPeternak = itemView.findViewById(R.id.idPeternak);
            ivSapi=itemView.findViewById(R.id.idFoto);
            ListItem = itemView.findViewById(R.id.list_item);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_live_stock_adapter, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String namaTernak = listTernak.get(position).getNamaTernak();
        final String namaPeternak = listTernak.get(position).getNamaPeternak();
        final String jenisTernak = listTernak.get(position).getJenisTernak();

        holder.namaTernak.setText(namaTernak);
        holder.namaPeternak.setText(namaPeternak);
        Glide.with(context).load(listTernak.get(position).getUrl()).into(holder.ivSapi);

        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //bundle.putString("getPrimaryKey", listTernak.get(position).getKey());
                Intent intent = new Intent(v.getContext(), LiveStockDetailOwnerActivity.class);
                bundle.putString("nama_ternak", listTernak.get(position).getNamaTernak());
                bundle.putString("nama_peternak", listTernak.get(position).getNamaPeternak());
                bundle.putString("jenis_ternak", listTernak.get(position).getJenisTernak());
                bundle.putString("foto_ternak",listTernak.get(position).getUrl());
                bundle.putString("kondisi_ternak", listTernak.get(position).getKondisi());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                listener.onDeleteData(listTernak.get(position), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTernak.size();
    }
}
