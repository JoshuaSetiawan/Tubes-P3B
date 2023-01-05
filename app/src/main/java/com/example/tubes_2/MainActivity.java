package com.example.tubes_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tubes_2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    LoginFragment loginFragment;
    HomeFragment homeFragment;
    PengumumanFragment pengumumanFragment;
    FRSFragment FRSfragment;
    FragmentManager fragmentManager;
    SemesterFragment semesterFragment;
    TambahMatkulFragment tambahMatkulFragment;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        this.loginFragment = LoginFragment.newInstance();
        this.homeFragment = HomeFragment.newInstance();
        this.pengumumanFragment = PengumumanFragment.newInstance();
        this.FRSfragment = FRSfragment.newInstance();
        semesterFragment = new SemesterFragment();
        this.tambahMatkulFragment = new TambahMatkulFragment();
        this.fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.add(activityMainBinding.container.getId(),this.loginFragment).commit();
        this.fragmentManager.setFragmentResultListener("changePage", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        token = result.getString("token");
                        if(result.getString("page").equals("detailSemester")){
                            Bundle bundle = new Bundle();
                            bundle.putInt("tahundansem",result.getInt("tahundansem"));
                            bundle.putString("heading",result.getString("heading"));
                            semesterFragment.setArguments(bundle);
                        }
                        else if(result.getString("page").equals("tambahMatkul")){
                            Bundle bundle = new Bundle();
                            bundle.putInt("tahundansem",result.getInt("tahundansem"));
                            bundle.putString("heading",result.getString("heading"));
                            tambahMatkulFragment.setArguments(bundle);
                        }

                        changePage(result.getString("page"));
                    }
                });
    }
    public void changePage(String page){
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        if(page.equals("home")){
            ft.remove(this.loginFragment);
            ft.add(activityMainBinding.container.getId(),this.homeFragment);
        }
        else if(page.equals("pengumuman")){
            ft.remove(this.homeFragment);
            ft.add(activityMainBinding.container.getId(),this.pengumumanFragment).addToBackStack(null);
        }
        else if(page.equals("frs")){
            ft.remove(this.homeFragment);
            ft.add(activityMainBinding.container.getId(),this.FRSfragment).addToBackStack(null);
        }
        else if(page.equals("detailSemester")){
            ft.remove(this.FRSfragment);
            ft.add(activityMainBinding.container.getId(),this.semesterFragment).addToBackStack(null);
        }
        else if(page.equals("tambahMatkul")){
            ft.remove(this.FRSfragment);
            ft.add(activityMainBinding.container.getId(),this.tambahMatkulFragment).addToBackStack(null);
        }
        ft.commit();
    }
}