package com.example.tubes_2;

import androidx.fragment.app.FragmentActivity;

public class PresenterLogin implements LoginContract.presenter,LoginContract.model.OnFinishedListener{
    LoginContract.view view;
    LoginContract.model model;
    FragmentActivity fragmentActivity;
    public PresenterLogin(LoginContract.view view, FragmentActivity fragmentActivity){
        this.fragmentActivity = fragmentActivity;
        this.view = view;
        this.model = new ModelLogin(fragmentActivity);
    }
    @Override
    public void buttonClick() {
        view.disabledInput();
        model.callAPI(view.getEmail(), view.getPassword(), view.getRole(),this);
    }

    @Override
    public void onSuccess(String hasil) {
        view.showToast(hasil);
    }
    @Override
    public void onFailed(String hasil) {
        view.enabledInput();
        view.showToast(hasil);
    }

}
