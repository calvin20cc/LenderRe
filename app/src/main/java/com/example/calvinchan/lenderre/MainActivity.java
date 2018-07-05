package com.example.calvinchan.lenderre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

//import com.google.firebase.iid.FirebaseInstanceId;
//import com.magneto.moneylender.utils.Constants;
//import com.magneto.moneylender.utils.Utility;

public class MainActivity extends AppCompatActivity {
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        //FirebaseInstanceId.getInstance().getToken();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                /*
                if (Utility.getInstance().getPreference(mContext, Constants.getInstance().pref_is_login).equalsIgnoreCase("true")) {
                    intent = new Intent(mContext, MainDrawerActivity.class);
                } else {
                    intent = new Intent(mContext, SelectLanguageActivity.class);
                }
                */
                intent = new Intent(mContext, PostActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);

    }
}
