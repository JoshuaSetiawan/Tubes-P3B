package com.example.tubes_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_2.databinding.HomeBinding;

public class HomeFragment extends Fragment {
    HomeBinding homeBinding;
    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeBinding = HomeBinding.inflate(inflater,container,false);
        View view = homeBinding.getRoot();
        homeBinding.pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("page","pengumuman");
                getParentFragmentManager().setFragmentResult("changePage",result);
            }
        });
        return view;
    }

    public static HomeFragment newInstance(){
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }
}
