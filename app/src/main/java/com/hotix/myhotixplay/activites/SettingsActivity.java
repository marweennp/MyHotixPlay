package com.hotix.myhotixplay.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hotix.myhotixplay.R;
import com.hotix.myhotixplay.helpers.AppSettings;
import com.hotix.myhotixplay.models.HotelSettings;
import com.hotix.myhotixplay.retrofit.RetrofitClient;
import com.hotix.myhotixplay.retrofit.RetrofitInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixplay.helpers.ConstantConfig.BASE_URL;
import static com.hotix.myhotixplay.helpers.ConstantConfig.FINAL_APP_ID;
import static com.hotix.myhotixplay.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixplay.helpers.Utils.showSnackbar;
import static com.hotix.myhotixplay.helpers.Utils.stringEmptyOrNull;

public class SettingsActivity extends AppCompatActivity {

    // Views
    private Toolbar toolbar;

    private TextInputLayout ilPublicIp;
    private TextInputLayout ilLocalIp;
    private TextInputLayout ilApiPublicUrl;
    private TextInputLayout ilApiLocalUrl;
    private TextInputLayout ilHotelCode;

    private TextInputEditText etPublicIp;
    private TextInputEditText etLocalIp;
    private TextInputEditText etApiPublicUrl;
    private TextInputEditText etApiLocalUrl;
    private TextInputEditText etHotelCode;

    private RelativeLayout rlApiPublicUrl;
    private RelativeLayout rlApiLocalUrl;

    private AppCompatImageView imgApiPublicUrl;
    private AppCompatImageView imgApiLocalUrl;

    private ContentLoadingProgressBar pbApiPublicUrl;
    private ContentLoadingProgressBar pbApiLocalUrl;
    private ContentLoadingProgressBar pbSettings;

    private SwitchCompat swSsl;
    private SwitchCompat swAutoUpdate;

    private AppCompatCheckBox chbPublicIp;
    private AppCompatCheckBox chbLocalIp;

    private AppCompatRadioButton rbDefaultPublic;
    private AppCompatRadioButton rbDefaultLocal;

    //AppSettings
    private AppSettings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindViews();

        mSettings = new AppSettings(getApplicationContext());

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
        setBaseUrl(this);
    }

    @Override
    public void onBackPressed() {
        saveSettings();
        setBaseUrl(this);
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSettings();
                return true;

            case R.id.action_synic:
                try {
                    lodeHotelInfos();
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                return true;

            case R.id.action_test:
                try {
                    BASE_URL = mSettings.getLocalBaseUrl();
                    ping(imgApiLocalUrl, pbApiLocalUrl, true);
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                try {
                    BASE_URL = mSettings.getPublicBaseUrl();
                    ping(imgApiPublicUrl, pbApiPublicUrl, false);
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**********************************************************************************************/

    private void bindViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ilPublicIp = (TextInputLayout) findViewById(R.id.il_settings_public_ip);
        ilLocalIp = (TextInputLayout) findViewById(R.id.il_settings_Local_ip);
        ilApiPublicUrl = (TextInputLayout) findViewById(R.id.il_settings_api_public_url);
        ilApiLocalUrl = (TextInputLayout) findViewById(R.id.il_settings_api_local_url);
        ilHotelCode = (TextInputLayout) findViewById(R.id.il_settings_hotel_code);

        etPublicIp = (TextInputEditText) findViewById(R.id.et_settings_public_ip);
        etLocalIp = (TextInputEditText) findViewById(R.id.et_settings_local_ip);
        etApiPublicUrl = (TextInputEditText) findViewById(R.id.et_settings_api_public_url);
        etApiLocalUrl = (TextInputEditText) findViewById(R.id.et_settings_api_local_url);
        etHotelCode = (TextInputEditText) findViewById(R.id.et_settings_hotel_code);

        rlApiPublicUrl = (RelativeLayout) findViewById(R.id.rl_settings_api_public_url);
        rlApiLocalUrl = (RelativeLayout) findViewById(R.id.rl_settings_api_local_url);

        imgApiPublicUrl = (AppCompatImageView) findViewById(R.id.img_settings_public_url_stat);
        imgApiLocalUrl = (AppCompatImageView) findViewById(R.id.img_settings_local_url_stat);

        pbApiPublicUrl = (ContentLoadingProgressBar) findViewById(R.id.pb_settings_public_url_stat);
        pbApiLocalUrl = (ContentLoadingProgressBar) findViewById(R.id.pb_settings_local_url_stat);
        pbSettings = (ContentLoadingProgressBar) findViewById(R.id.pb_settings);

        swSsl = (SwitchCompat) findViewById(R.id.sw_settings_ssl);
        swAutoUpdate = (SwitchCompat) findViewById(R.id.sw_settings_auto_update);

        chbPublicIp = (AppCompatCheckBox) findViewById(R.id.chb_settings_public_ip);
        chbLocalIp = (AppCompatCheckBox) findViewById(R.id.chb_settings_Local_ip);

        rbDefaultPublic = (AppCompatRadioButton) findViewById(R.id.rb_settings_default_public_url);
        rbDefaultLocal = (AppCompatRadioButton) findViewById(R.id.rb_settings_default_local_url);
    }

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgApiPublicUrl.setVisibility(View.GONE);
        imgApiLocalUrl.setVisibility(View.GONE);

        pbApiPublicUrl.setVisibility(View.GONE);
        pbApiLocalUrl.setVisibility(View.GONE);

        // SSL Config
        swSsl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    swSsl.setText(R.string.all_on);
                    etApiPublicUrl.setText("https://" + etPublicIp.getText().toString().trim() + "/");
                    etApiLocalUrl.setText("https://" + etLocalIp.getText().toString().trim() + "/");
                    mSettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
                    mSettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());

                } else {
                    swSsl.setText(R.string.all_off);
                    etApiPublicUrl.setText("http://" + etPublicIp.getText().toString().trim() + "/");
                    etApiLocalUrl.setText("http://" + etLocalIp.getText().toString().trim() + "/");
                    mSettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
                    mSettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());
                }
            }
        });

        // Auto Update Config
        swAutoUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    swAutoUpdate.setText(R.string.all_on);
                    mSettings.setAutoUpdate(true);

                } else {
                    swAutoUpdate.setText(R.string.all_off);
                    mSettings.setAutoUpdate(false);
                }
            }
        });


        //Public IP
        etPublicIp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (!stringEmptyOrNull(etPublicIp.getText().toString().trim())) {
                    if (swSsl.isChecked()) {
                        etApiPublicUrl.setText("https://" + etPublicIp.getText().toString().trim() + "/");
                    } else {
                        etApiPublicUrl.setText("http://" + etPublicIp.getText().toString().trim() + "/");
                    }
                    mSettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
                    mSettings.setPublicIp(etPublicIp.getText().toString().trim());
                } else {
                    etApiPublicUrl.setText("http://" + etPublicIp.getText().toString().trim() + "/");
                    mSettings.setPublicIp("xxx.xxx.xxx.xxx");
                    mSettings.setPublicBaseUrl("http://xxx.xxx.xxx.xxx/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //Local IP
        etLocalIp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (!stringEmptyOrNull(etLocalIp.getText().toString().trim())) {
                    if (swSsl.isChecked()) {
                        etApiLocalUrl.setText("https://" + etLocalIp.getText().toString().trim() + "/");
                    } else {
                        etApiLocalUrl.setText("http://" + etLocalIp.getText().toString().trim() + "/");
                    }
                    mSettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());
                    mSettings.setLocalIp(etLocalIp.getText().toString().trim());
                } else {
                    etApiLocalUrl.setText("http://" + etLocalIp.getText().toString().trim() + "/");
                    mSettings.setLocalIp("xxx.xxx.xxx.xxx");
                    mSettings.setLocalBaseUrl("http://xxx.xxx.xxx.xxx/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //Public IP Eanbled
        chbPublicIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    etPublicIp.setEnabled(true);
                    rlApiPublicUrl.setVisibility(View.VISIBLE);
                    mSettings.setPublicIpEnabled(true);
                } else {
                    etPublicIp.setEnabled(false);
                    rlApiPublicUrl.setVisibility(View.GONE);
                    mSettings.setPublicIpEnabled(false);
                }
            }
        });

        //Local IP Eanbled
        chbLocalIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    etLocalIp.setEnabled(true);
                    rlApiLocalUrl.setVisibility(View.VISIBLE);
                    mSettings.setLocalIpEnabled(true);
                } else {
                    etLocalIp.setEnabled(false);
                    rlApiLocalUrl.setVisibility(View.GONE);
                    mSettings.setLocalIpEnabled(false);
                }
            }
        });


    }

    private void loadSettings() {

        swSsl.setChecked(mSettings.getSsl());
        swAutoUpdate.setChecked(mSettings.getAutoUpdate());

        rbDefaultLocal.setChecked(mSettings.getLocalIpDefault());
        rbDefaultPublic.setChecked(!mSettings.getLocalIpDefault());

        chbPublicIp.setChecked(mSettings.getPublicIpEnabled());
        chbLocalIp.setChecked(mSettings.getLocalIpEnabled());

        etPublicIp.setText(mSettings.getPublicIp().trim());
        etLocalIp.setText(mSettings.getLocalIp().trim());
        etApiPublicUrl.setText(mSettings.getPublicBaseUrl().trim());
        etApiLocalUrl.setText(mSettings.getLocalBaseUrl().trim());
        etHotelCode.setText(mSettings.getHotelCode().trim());
    }

    private void saveSettings() {

        mSettings.setSsl(swSsl.isChecked());
        mSettings.setAutoUpdate(swAutoUpdate.isChecked());
        mSettings.setLocalIpDefault(rbDefaultLocal.isChecked());

        if (!stringEmptyOrNull(etPublicIp.getText().toString().trim())) {
            mSettings.setPublicIp(etPublicIp.getText().toString().trim());
        } else {
            mSettings.setPublicIp("xxx.xxx.xxx.xxx");
        }

        if (!stringEmptyOrNull(etLocalIp.getText().toString().trim())) {
            mSettings.setLocalIp(etLocalIp.getText().toString().trim());
        } else {
            mSettings.setLocalIp("xxx.xxx.xxx.xxx");
        }

        if (!stringEmptyOrNull(etApiPublicUrl.getText().toString().trim())) {
            mSettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
        } else {
            mSettings.setPublicBaseUrl("http://xxx.xxx.xxx.xxx/");
        }

        if (!stringEmptyOrNull(etApiLocalUrl.getText().toString().trim())) {
            mSettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());
        } else {
            mSettings.setLocalBaseUrl("http://xxx.xxx.xxx.xxx/");
        }

        mSettings.setPublicIpEnabled(chbPublicIp.isChecked());
        mSettings.setLocalIpEnabled(chbLocalIp.isChecked());

        if (!stringEmptyOrNull(etHotelCode.getText().toString().trim())) {
            mSettings.setHotelCode(etHotelCode.getText().toString().trim());
        } else {
            mSettings.setLocalIp("0000");
        }

        mSettings.setConfigured(true);

        setBaseUrl(this);

        finish();

    }

    /**********************************************************************************************/

    public void lodeHotelInfos() {

        String code = etHotelCode.getText().toString();
        RetrofitInterface service = RetrofitClient.getHotixSupportApi().create(RetrofitInterface.class);
        Call<HotelSettings> userCall = service.getInfosQuery(code, FINAL_APP_ID);

        pbSettings.setVisibility(View.VISIBLE);

        userCall.enqueue(new Callback<HotelSettings>() {
            @Override
            public void onResponse(Call<HotelSettings> call, Response<HotelSettings> response) {

                pbSettings.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    HotelSettings hotelSettings = response.body();

                    //Check if the hotel is active or not
                    if (!hotelSettings.getIsActive()) {
                        //Hotel not active
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_hotel_not_active));
                    } else {
                        //Hotel active
                        //Check if the hotel can use the app
                        if (!hotelSettings.getAppIsActive()) {
                            //Hotel can't use app
                            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_app_not_active));
                        } else {
                            //Hotel can use app

                            //Get Public IP
                            if (!stringEmptyOrNull(hotelSettings.getIPPublic())) {
                                mSettings.setPublicIp(hotelSettings.getIPPublic());
                                mSettings.setPublicIpEnabled(true);
                            } else {
                                mSettings.setPublicIp("xxx.xxx.xxx.xxx");
                                mSettings.setPublicIpEnabled(false);
                            }

                            //Get Local IP
                            if (!stringEmptyOrNull(hotelSettings.getIPLocal())) {
                                mSettings.setLocalIp(hotelSettings.getIPLocal());
                                mSettings.setLocalIpEnabled(true);
                            } else {
                                mSettings.setLocalIp("xxx.xxx.xxx.xxx");
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
                        }
                    }
                    mSettings.setSettingsUpdated(true);
                    loadSettings();

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }

            }

            @Override
            public void onFailure(Call<HotelSettings> call, Throwable t) {
                pbSettings.setVisibility(View.GONE);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_down));
            }
        });

    }

    public void ping(final AppCompatImageView img, final ProgressBar pb, final boolean local) {

        RetrofitInterface service = RetrofitClient.getClientPing().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.isConnectedQuery();

        img.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.raw().code() == 200) {
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.ic_check_circle_green_500_24dp);
                    pb.setVisibility(View.GONE);
                    if (local) {
                        mSettings.setLocalIpReachable(true);
                    } else {
                        mSettings.setPublicIpReachable(true);
                    }

                } else {
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.ic_cloud_circle_red_500_24dp);
                    pb.setVisibility(View.GONE);
                    if (local) {
                        mSettings.setLocalIpReachable(false);
                    } else {
                        mSettings.setPublicIpEnabled(false);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.ic_cloud_circle_red_500_24dp);
                pb.setVisibility(View.GONE);
                if (local) {
                    mSettings.setLocalIpReachable(false);
                } else {
                    mSettings.setPublicIpEnabled(false);
                }
            }
        });

    }
}
