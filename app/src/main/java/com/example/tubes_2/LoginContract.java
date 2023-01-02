package com.example.tubes_2;

import com.android.volley.VolleyError;

import org.json.JSONException;

public interface LoginContract {
    interface model{
        void callAPI(String email,String password,String role,LoginContract.model.OnFailedListener notSure);
        void processingSuccesResponse(String response);
        void processingFailedResponse(VolleyError error) throws JSONException;
        interface OnFailedListener{
            void openInput();
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
    }
}
