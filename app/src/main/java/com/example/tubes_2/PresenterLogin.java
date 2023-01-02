package com.example.tubes_2;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;

public class PresenterLogin implements LoginContract.presenter,LoginContract.model.OnFailedListener{
    LoginContract.view view;
    LoginContract.model model;
    FragmentActivity fragmentActivity;
    public PresenterLogin(LoginContract.view view, LoginContract.model model, FragmentActivity fragmentActivity){
        this.fragmentActivity = fragmentActivity;
        this.view = view;
        this.model = model;
    }
    @Override
    public void buttonClick() {
        view.disabledInput();
        model.callAPI(view.getEmail(), view.getPassword(), view.getRole(),this);
    }

    @Override
    public void openInput() {
        view.enabledInput();
    }

}
