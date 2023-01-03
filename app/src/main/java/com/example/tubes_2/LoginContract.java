package com.example.tubes_2;

import com.android.volley.VolleyError;

import org.json.JSONException;

public interface LoginContract {
    interface model{
        void callAPI(String email,String password,String role,LoginContract.model.OnFinishedListener notSure);
        String processingSuccesResponse(String response);
        String processingFailedResponse(VolleyError error) throws JSONException;
        interface OnFinishedListener{
            void onSuccess(String hasil);
            void onFailed(String hasil);
        }
    }
    interface presenter{
        void buttonClick();
    }
    interface view{
        void disabledInput();
        void enabledInput();
        String getEmail();
        String getPassword();
        String getRole();
        void showToast(String hasil);
    }
}
