package com.google.firebase.ml.md.java.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.ml.md.R;

public class ShowData_ViewHolder extends RecyclerView.ViewHolder {
    public TextView package_id, name, loc;

    public ShowData_ViewHolder(@NonNull View itemView) {
        super(itemView);

        package_id = itemView.findViewById(R.id.barang_id);
        name = itemView.findViewById(R.id.nama);
        loc = itemView.findViewById(R.id.lokasi);
    }
}
