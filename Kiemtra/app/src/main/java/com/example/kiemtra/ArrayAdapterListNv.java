package com.example.kiemtra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArrayAdapterListNv extends ArrayAdapter<NhanVien> {
    private Context mContext;
    private List<NhanVien> listNv;

    public ArrayAdapterListNv (@NonNull Context context, List<NhanVien> list) {
        super(context, 0, list);
        mContext = context;
        listNv = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_list_item_nv, parent, false);
        }

        NhanVien nhanVien = listNv.get(position);

        TextView nameTextView = convertView.findViewById(R.id.txtTenNvItem);

        nameTextView.setText(nhanVien.getHoten());

        return convertView;
    }
}
