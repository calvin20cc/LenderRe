package com.magneto.moneylender.moneylender.activities.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magneto.moneylender.R;
import com.magneto.moneylender.adapters.common.SelectLanguageAdapter;
import com.magneto.moneylender.beans.common.LanguageBean;
import com.magneto.moneylender.interfaces.EnumClicks;
import com.magneto.moneylender.interfaces.OnButtonClickListener;
import com.magneto.moneylender.retrofit.APICallBack;
import com.magneto.moneylender.retrofit.APIService;
import com.magneto.moneylender.utils.Constants;
import com.magneto.moneylender.utils.ProgressDialog;
import com.magneto.moneylender.utils.Utility;

import java.util.ArrayList;

public class SelectLanguageActivity extends AppCompatActivity implements OnButtonClickListener {

    private LinearLayout ll_main;
    private ArrayList<LanguageBean.Languages> languagesArrayList = null;
    private RecyclerView language_recyclerview;
    private Context mContext = null;
    private Button btn_next;
    private SelectLanguageAdapter adapter = null;
    private ImageView imageView;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.getInstance().localisation(this, Utility.getInstance().getPreference(this, ""));
        setContentView(R.layout.activity_select_language);

        overridePendingTransition(R.anim.animation_slide_bottom_center, R.anim.animation_stable);
        mContext = this;
        initUI();
        setClickListener();
        getLanguagesAPI();
    }

    private void initUI() {
        ll_main = findViewById(R.id.ll_main);
        btn_next = findViewById(R.id.btn_next);

        imageView = findViewById(R.id.imageView);
        language_recyclerview = findViewById(R.id.language_recyclerview);
        languagesArrayList = new ArrayList<>();
        language_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SelectLanguageAdapter(mContext, languagesArrayList, this);
        language_recyclerview.setAdapter(adapter);
    }

    private void setClickListener() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < languagesArrayList.size(); i++) {
                    if (languagesArrayList.get(i).isSelected()) {
                        if (languagesArrayList.get(i).getLang_code().equalsIgnoreCase("zh-TW")) {
                            Utility.getInstance().setPreference(mContext, Constants.getInstance().pref_language, "zh");

                        } else {
                            Utility.getInstance().setPreference(mContext, Constants.getInstance().pref_language, "");
                        }
                        Utility.getInstance().setPreference(mContext, Constants.getInstance().pref_language_id, languagesArrayList.get(i).getLang_id());
                    }
                }
//                Intent intent = new Intent(mContext, HowItWorksActivity.class);
//                intent.putExtra("from", "splash");
//                startActivity(intent);
//                finish();

                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getLanguagesAPI() {

        if (Utility.getInstance().isNetworkAvailable(mContext)) {
            final ProgressDialog pDialog = new ProgressDialog(mContext);
            pDialog.show();

            APIService.getInstance().getBaseUrl()
                    .get_languages()
                    .enqueue(new APICallBack<LanguageBean>() {
                        @Override
                        protected void success(LanguageBean model) {
                            pDialog.dismiss();
                            btn_next.setVisibility(View.VISIBLE);

                            if (model.getStatus().equalsIgnoreCase("1")) {
                                if (model.getLanguages() != null) {
                                    languagesArrayList.addAll(model.getLanguages());
                                    languagesArrayList.get(0).setIsSelected("1");
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Utility.getInstance().showSnackBar(ll_main, model.getMsg());
                                }
                            } else {
                                Utility.getInstance().showSnackBar(ll_main, model.getMsg());
                            }
                        }

                        @Override
                        protected void failure(String errorMsg) {
                            pDialog.dismiss();
                            btn_next.setVisibility(View.GONE);
                            Utility.getInstance().showSnackBar(ll_main, errorMsg);
                        }

                        @Override
                        protected void onFailure(String failureReason) {
                            pDialog.dismiss();
                            btn_next.setVisibility(View.GONE);
                            final Snackbar snackbar = Snackbar.make(ll_main, Utility.getInstance().fromHtml(getString(R.string.oops_something_went_wrong)), Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                    getLanguagesAPI();
                                }
                            });
                            snackbar.setActionTextColor(ContextCompat.getColor(mContext, R.color.white));
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                            Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "font_medium_railway.ttf");
                            TextView textView = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextSize(layout.getResources().getDimension(R.dimen._5sdp));
                            textView.setTypeface(typeface);
                            TextView actionTextView = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_action);
                            actionTextView.setTypeface(typeface, Typeface.BOLD);
//        actionTextView.setTextSize(layout.getResources().getDimension(R.dimen._10sdp));
                            snackbar.show();
                        }
                    });

        } else {

            btn_next.setVisibility(View.GONE);

            final Snackbar snackbar = Snackbar.make(ll_main, Utility.getInstance().fromHtml(getString(R.string.please_check_your_internet_connection)), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    getLanguagesAPI();
                }
            });
            snackbar.setActionTextColor(ContextCompat.getColor(mContext, R.color.white));
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "font_medium_railway.ttf");
            TextView textView = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextSize(layout.getResources().getDimension(R.dimen._5sdp));
            textView.setTypeface(typeface);
            TextView actionTextView = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_action);
            actionTextView.setTypeface(typeface, Typeface.BOLD);
//        actionTextView.setTextSize(layout.getResources().getDimension(R.dimen._10sdp));
            snackbar.show();
        }


    }

    @Override
    public void onButtonClick(EnumClicks where, View view, int position) {
        switch (where) {
            case CELL_CLICK:
                for (int i = 0; i < languagesArrayList.size(); i++) {
                    languagesArrayList.get(i).setIsSelected("0");
                }
                languagesArrayList.get(position).setIsSelected("1");
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
