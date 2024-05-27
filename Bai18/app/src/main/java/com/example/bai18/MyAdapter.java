package com.example.bai18;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Item> {
    Activity context = null;
    ArrayList<Item> myArray = null;
    int LayoutId;
    public MyAdapter(Activity context, int LayoutId,ArrayList<Item>arr) {
        super(context, LayoutId,arr);
// TODO Auto-generated constructor stub
        this.context = context;
        this.LayoutId = LayoutId;
        this.myArray = arr;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(LayoutId, null);
        Item myItem = myArray.get(position);
        ImageView btnlike = convertView.findViewById(R.id.btnLike);
        int thich = myItem.getThich();
        if (thich==1) {
            btnlike.setImageResource(R.drawable.heart);
        } else {
            btnlike.setImageResource(R.drawable.heart);
        }
        TextView tieude = convertView.findViewById(R.id.tvSongName);
        tieude.setText(myItem.getTieude());
        TextView maso = convertView.findViewById(R.id.tvSongID);
        maso.setText(myItem.getMaso());
        return convertView;
    }
}
