package com.hotix.myhotixplay.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.hotix.myhotixplay.R;
import com.hotix.myhotixplay.helpers.AppSettings;
import com.hotix.myhotixplay.models.HotelSettings;
import com.hotix.myhotixplay.models.ItemPlay;
import com.hotix.myhotixplay.models.PlayData;
import com.hotix.myhotixplay.retrofit.RetrofitClient;
import com.hotix.myhotixplay.retrofit.RetrofitInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixplay.helpers.ConnectionChecher.isNetworkAvailable;
import static com.hotix.myhotixplay.helpers.ConstantConfig.FINAL_APP_ID;
import static com.hotix.myhotixplay.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixplay.helpers.Utils.stringEmptyOrNull;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 200;

    // Settings Class
    private AppSettings mSettings;

    // Views
    private RelativeLayout rlMainContainer;
    private AppCompatImageView ivSplashLogo;
    private LinearLayoutCompat llSplashProgress;
    private AppCompatTextView tvSplashProgress;
    private ContentLoadingProgressBar pbSplashProgress;

    // Infos View
    private RelativeLayout rlInfosView;
    private AppCompatImageView ivInfosIcon;
    private AppCompatTextView tvInfosMsg;
    private AppCompatButton btnInfosRefresh;

    private Drawable mIconOne, mIconTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        bindViews();

        //settings
        mSettings = new AppSettings(getApplicationContext());

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvSplashProgress.setText(R.string.checking_internet_providers);
        if (isNetworkAvailable(this)) {

            if (mSettings.getConfigured()) {
                setBaseUrl(this);
                if (mSettings.getAutoUpdate()) {
                    try {
                        UpdateHotelInfos(mSettings.getHotelCode());
                    } catch (Exception e) {
                        Log.e("SPLASH LOG", e.toString());
                    }
                } else {
                    startDelay();
                }
            } else {
                startDelay();
            }

        } else {
            rlMainContainer.setVisibility(View.GONE);
            rlInfosView.setVisibility(View.VISIBLE);
            tvInfosMsg.setText(R.string.check_internet_connection);
            ivInfosIcon.setImageDrawable(mIconOne);
        }
    }

    /**********************************************************************************************/

    private void startDelay() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreenActivity.this, PlayActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }

    /**********************************************************************************************/

    private void bindViews() {
        rlMainContainer = (RelativeLayout) findViewById(R.id.rl_splash_main_container);
        ivSplashLogo = (AppCompatImageView) findViewById(R.id.iv_splash_logo);
        llSplashProgress = (LinearLayoutCompat) findViewById(R.id.ll_splash_progress);
        tvSplashProgress = (AppCompatTextView) findViewById(R.id.tv_spalsh_progress);
        pbSplashProgress = (ContentLoadingProgressBar) findViewById(R.id.pb_spalsh_progress);

        rlInfosView = (RelativeLayout) findViewById(R.id.rl_infos_view);
        ivInfosIcon = (AppCompatImageView) findViewById(R.id.iv_infos_icon);
        tvInfosMsg = (AppCompatTextView) findViewById(R.id.tv_infos_msg);
        btnInfosRefresh = (AppCompatButton) findViewById(R.id.btn_infos_refresh);
    }

    private void init() {

        //load logo
        Picasso.get().load(R.drawable.hotix_logo).fit().placeholder(R.drawable.hotix_logo).into(ivSplashLogo);

        //Check android vertion and load image
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            mIconOne = getResources().getDrawable(R.drawable.svg_internet_grey_512, this.getTheme());
            mIconTwo = getResources().getDrawable(R.drawable.svg_server_grey_512, this.getTheme());
        } else {
            mIconOne = VectorDrawableCompat.create(this.getResources(), R.drawable.svg_internet_grey_512, this.getTheme());
            mIconTwo = VectorDrawableCompat.create(this.getResources(), R.drawable.svg_server_grey_512, this.getTheme());
        }

        btnInfosRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    /**********************************************************************************************/

    private void UpdateHotelInfos(String hotelCode) {

        llSplashProgress.setVisibility(View.VISIBLE);
        tvSplashProgress.setText(R.string.loading_hotel_settings);

        RetrofitInterface service = RetrofitClient.getHotixSupportApi().create(RetrofitInterface.class);
        Call<HotelSettings> userCall = service.getInfosQuery(hotelCode, FINAL_APP_ID);

        userCall.enqueue(new Callback<HotelSettings>() {
            @Override
            public void onResponse(Call<HotelSettings> call, Response<HotelSettings> response) {
                llSplashProgress.setVisibility(View.GONE);
                tvSplashProgress.setText("");

                if (response.raw().code() == 200) {
                    HotelSettings hotelSettings = response.body();

                    //Get Public IP
                    if (!stringEmptyOrNull(hotelSettings.getIPPublic())) {
                        mSettings.setPublicIp(hotelSettings.getIPPublic());
                        mSettings.setPublicBaseUrl("http://" + hotelSettings.getIPPublic() + "/");
                        mSettings.setPublicIpEnabled(true);
                    } else {
                        mSettings.setPublicIp("xxx.xxx.xxx.xxx");
                        mSettings.setPublicBaseUrl("http://xxx.xxx.xxx.xxx/");
                        mSettings.setPublicIpEnabled(false);
                    }

                    //Get Local IP
                    if (!stringEmptyOrNull(hotelSettings.getIPLocal())) {
                        mSettings.setLocalIp(hotelSettings.getIPLocal());
                        mSettings.setLocalBaseUrl("http://" + hotelSettings.getIPLocal() + "/");
                        mSettings.setLocalIpEnabled(true);
                    } else {
                        mSettings.setLocalIp("xxx.xxx.xxx.xxx");
                        mSettings.setLocalBaseUrl("http://xxx.xxx.xxx.xxx/");
                        mSettings.setLocalIpEnabled(false);
                    }

                    //Get Hotel ID
                    if (!stringEmptyOrNull(hotelSettings.getCode())) {
                        mSettings.setHotelCode(hotelSettings.getCode());
                    } else {
                        mSettings.setHotelCode("0000");
                    }

                    //Get Hotel Name
                    if (!stringEmptyOrNull(hotelSettings.getName())) {
                        mSettings.setHotelName(hotelSettings.getName());
                    } else {
                        mSettings.setHotelName("HOTEL");
                    }

                    mSettings.setSettingsUpdated(true);

                    setBaseUrl(getApplicationContext());

                } else {
                    mSettings.setSettingsUpdated(false);
                }

                startDelay();
            }

            @Override
            public void onFailure(Call<HotelSettings> call, Throwable t) {
                llSplashProgress.setVisibility(View.GONE);
                tvSplashProgress.setText("");
                mSettings.setSettingsUpdated(false);
                startDelay();
            }
        });
    }

}
