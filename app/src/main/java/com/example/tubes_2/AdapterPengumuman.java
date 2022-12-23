package com.example.tubes_2;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_2.databinding.ItemListPengumumanBinding;

public class AdapterPengumuman extends BaseAdapter {
    ItemListPengumumanBinding itemListPengumumanBinding;
    DaftarPengumuman[] daftarPengumuman;
    Activity activity;
    public AdapterPengumuman(DaftarPengumuman[]daftarPengumuman,Activity activity){
        this.daftarPengumuman = daftarPengumuman;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return daftarPengumuman.length;
    }

    @Override
    public Object getItem(int position) {
        return daftarPengumuman[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       itemListPengumumanBinding = ItemListPengumumanBinding.inflate(activity.getLayoutInflater());
       View view = itemListPengumumanBinding.getRoot();
       itemListPengumumanBinding.TampilJudul.setText(this.daftarPengumuman[position].title);
       itemListPengumumanBinding.TampilTags.setText(this.daftarPengumuman[position].tags.tag);
       return view;
    }
}
