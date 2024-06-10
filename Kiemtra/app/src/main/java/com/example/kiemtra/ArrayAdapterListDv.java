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

public class ArrayAdapterListDv extends ArrayAdapter<DonVi> {
    private Context mContext;
    private List<DonVi> unitList;

    public ArrayAdapterListDv(@NonNull Context context, List<DonVi> list) {
        super(context, 0, list);
        mContext = context;
        unitList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_list_item_dv, parent, false);
        }

        DonVi donVi = unitList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.txtTenDvItem);

        nameTextView.setText(donVi.getTen());

        return convertView;
    }
}
