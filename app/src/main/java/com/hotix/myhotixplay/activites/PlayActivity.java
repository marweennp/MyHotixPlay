package com.hotix.myhotixplay.activites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.hotix.myhotixplay.R;
import com.hotix.myhotixplay.helpers.AppSettings;
import com.hotix.myhotixplay.helpers.InputValidation;
import com.hotix.myhotixplay.models.Degree;
import com.hotix.myhotixplay.models.HotelSettings;
import com.hotix.myhotixplay.models.ItemPlay;
import com.hotix.myhotixplay.models.Prize;
import com.hotix.myhotixplay.models.RoomDetails;
import com.hotix.myhotixplay.models.Success;
import com.hotix.myhotixplay.retrofit.RetrofitClient;
import com.hotix.myhotixplay.retrofit.RetrofitInterface;
import com.hotix.myhotixplay.views.kbv.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.parseColor;
import static com.hotix.myhotixplay.helpers.ConnectionChecher.checkNetwork;
import static com.hotix.myhotixplay.helpers.ConstantConfig.GLOBAL_DEGREE;
import static com.hotix.myhotixplay.helpers.ConstantConfig.BASE_URL;
import static com.hotix.myhotixplay.helpers.ConstantConfig.FINAL_APP_ID;
import static com.hotix.myhotixplay.helpers.ConstantConfig.GLOBAL_PRIZES_LIST;
import static com.hotix.myhotixplay.helpers.ConstantConfig.GLOBAL_ROOM_DETAILS;
import static com.hotix.myhotixplay.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixplay.helpers.Utils.showSnackbar;
import static com.hotix.myhotixplay.helpers.Utils.stringEmptyOrNull;

public class PlayActivity extends AppCompatActivity implements Animation.AnimationListener {

    private int _STEP = 1;
    private static final String TAG = "PlayActivity";

    private long mSpinDuration;
    private float mSpinRevolutions;
    private String prizeText = "";
    private int prizeIndex = 0;

    // Views
    private LinearLayoutCompat llRoomForm;
    private TextInputLayout ilPlayRoom;
    private TextInputEditText etPlayRoom;
    private AppCompatButton btnPlaySearch;
    private ContentLoadingProgressBar pbPlaySearch;

    private LinearLayoutCompat llDataForm;
    private TextInputLayout ilPlayFirstName;
    private TextInputEditText etPlayFirstName;
    private TextInputLayout ilPlayLasttName;
    private TextInputEditText etPlayLasttName;
    private TextInputLayout ilPlayDocNum;
    private TextInputEditText etPlayDocNum;
    private TextInputLayout ilPlayEmail;
    private TextInputEditText etPlayEmail;
    private AppCompatButton btnPlayNextData;

    private LinearLayoutCompat llLoisirForm;
    private LinearLayoutCompat llLoisirContainer;
    private AppCompatButton btnPlayNextLoisir;

    private LinearLayoutCompat llConsomationForm;
    private LinearLayoutCompat llConsomationContainer;
    private AppCompatButton btnPlayValidateConsomation;
    private ContentLoadingProgressBar pbPlayValidateConsomation;

    private RelativeLayout rlPlayGame;
    private RelativeLayout rlPlayWeel;
    private AppCompatImageButton btnPlaySpin;

    private AppCompatTextView tvPlayHotelName;
    private AppCompatTextView tvPlayVersion;
    private AppCompatImageButton ibtnPlaySetting;

    // Dialog
    private AlertDialog prizeDialog;
    private TextInputLayout ilHotelCode;
    private TextInputEditText etHotelCode;
    private AppCompatButton btnPrizeOk;
    private ContentLoadingProgressBar pbDialogProgress;

    //AppSettings
    private AppSettings mSettings;

    private KenBurnsView mKenBurns;
    private InputValidation mInputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        bindViews();

        mSettings = new AppSettings(getApplicationContext());
        mInputValidation = new InputValidation(getApplicationContext());

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.getFirstStart()) {
            Log.e(TAG, "AppSettings FirstStart : " + mSettings.getFirstStart());
            startDownloadSettingsDialog();
        }
        setBaseUrl(this);
        loadeImage();
    }

    @Override
    public void onBackPressed() {
        if (_STEP == 1) {
            startExitDialog();
        }

        if (_STEP == 2) {
            _STEP = 1;
            llRoomForm.setVisibility(View.VISIBLE);
            llDataForm.setVisibility(View.GONE);
        }

        if (_STEP == 3) {
            _STEP = 2;
            llDataForm.setVisibility(View.VISIBLE);
            llLoisirForm.setVisibility(View.GONE);
        }

        if (_STEP == 4) {
            _STEP = 3;
            llLoisirForm.setVisibility(View.VISIBLE);
            llConsomationForm.setVisibility(View.GONE);
        }

        if (_STEP == 5) {
            _STEP = 1;
            llRoomForm.setVisibility(View.VISIBLE);
            rlPlayGame.setVisibility(View.GONE);
        }

    }

    /**********************************************************************************************/

    private void bindViews() {

        mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);

        llRoomForm = (LinearLayoutCompat) findViewById(R.id.ll_room_form);
        ilPlayRoom = (TextInputLayout) findViewById(R.id.il_play_room);
        etPlayRoom = (TextInputEditText) findViewById(R.id.et_play_room);
        btnPlaySearch = (AppCompatButton) findViewById(R.id.btn_play_search);
        pbPlaySearch = (ContentLoadingProgressBar) findViewById(R.id.pb_play_search);

        llDataForm = (LinearLayoutCompat) findViewById(R.id.ll_data_form);
        ilPlayFirstName = (TextInputLayout) findViewById(R.id.il_play_first_name);
        etPlayFirstName = (TextInputEditText) findViewById(R.id.et_play_first_name);
        ilPlayLasttName = (TextInputLayout) findViewById(R.id.il_play_last_name);
        etPlayLasttName = (TextInputEditText) findViewById(R.id.et_play_last_name);
        ilPlayDocNum = (TextInputLayout) findViewById(R.id.il_play_doc_num);
        etPlayDocNum = (TextInputEditText) findViewById(R.id.et_play_doc_num);
        ilPlayEmail = (TextInputLayout) findViewById(R.id.il_play_email);
        etPlayEmail = (TextInputEditText) findViewById(R.id.et_play_email);
        btnPlayNextData = (AppCompatButton) findViewById(R.id.btn_play_next_data);

        llLoisirForm = (LinearLayoutCompat) findViewById(R.id.ll_loisir_form);
        llLoisirContainer = (LinearLayoutCompat) findViewById(R.id.ll_loisir_container);
        btnPlayNextLoisir = (AppCompatButton) findViewById(R.id.btn_play_next_loisir);

        llConsomationForm = (LinearLayoutCompat) findViewById(R.id.ll_consomation_form);
        llConsomationContainer = (LinearLayoutCompat) findViewById(R.id.ll_consomation_container);
        btnPlayValidateConsomation = (AppCompatButton) findViewById(R.id.btn_play_validate_consomation);
        pbPlayValidateConsomation = (ContentLoadingProgressBar) findViewById(R.id.pb_play_validate_consomation);

        rlPlayGame = (RelativeLayout) findViewById(R.id.rl_play_game);
        rlPlayWeel = (RelativeLayout) findViewById(R.id.rl_play_weel);
        btnPlaySpin = (AppCompatImageButton) findViewById(R.id.btn_play_spin);

        tvPlayHotelName = (AppCompatTextView) findViewById(R.id.tv_play_hotel_name);
        tvPlayVersion = (AppCompatTextView) findViewById(R.id.tv_play_version);
        ibtnPlaySetting = (AppCompatImageButton) findViewById(R.id.ibtn_play_setting);
    }

    private void init() {
        mKenBurns.setImageResource(R.drawable.bg);

        loadeImage();

        tvPlayHotelName.setText(mSettings.getHotelName());

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvPlayVersion.setText(getString(R.string.text_version) + " " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        btnPlaySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetwork(getApplicationContext())) {
                    if (inputTextValidationRoom()) {
                        try {
                            loadRoomDetails();
                        } catch (Exception e) {
                            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                        }
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_no_internet));
                }
            }
        });

        btnPlayNextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTextValidationData()) {
                    GLOBAL_ROOM_DETAILS.setFirstName(etPlayFirstName.getText().toString().trim());
                    GLOBAL_ROOM_DETAILS.setLastName(etPlayLasttName.getText().toString().trim());
                    GLOBAL_ROOM_DETAILS.setIdDocNumber(etPlayDocNum.getText().toString().trim());
                    GLOBAL_ROOM_DETAILS.setEmail(etPlayEmail.getText().toString().trim());

                    _STEP = 3;
                    llDataForm.setVisibility(View.GONE);
                    llLoisirForm.setVisibility(View.VISIBLE);
                }
            }
        });

        btnPlayNextLoisir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _STEP = 4;
                llLoisirForm.setVisibility(View.GONE);
                llConsomationForm.setVisibility(View.VISIBLE);
            }
        });

        btnPlayValidateConsomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetwork(getApplicationContext())) {
                    try {
                        updateClient();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_no_internet));
                }
            }
        });

        btnPlaySpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSpinner();
            }
        });

        ibtnPlaySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean inputTextValidationRoom() {

        if (!mInputValidation.isInputEditTextFilled(etPlayRoom, ilPlayRoom, getString(R.string.error_message_field_required))) {
            return false;
        }
        return true;

    }

    private boolean inputTextValidationData() {

        if (!mInputValidation.isInputEditTextFilled(etPlayFirstName, ilPlayFirstName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etPlayLasttName, ilPlayLasttName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etPlayDocNum, ilPlayDocNum, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etPlayEmail, ilPlayEmail, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextEmail(etPlayEmail, ilPlayEmail, getString(R.string.error_message_email_invalid))) {
            return false;
        }
        return true;

    }

    private void loadeImage() {

        Picasso.get().load(BASE_URL + "Android/pics_play/hotel.jpg").into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                mKenBurns.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

        });
    }

    private void startDownloadSettingsDialog() {

        Log.e(TAG, "Download Settings Dialog Started");

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_hotel_settings, null);
        AppCompatButton btnDownload = (AppCompatButton) mView.findViewById(R.id.btn_dialog_hotel_settings_download);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_hotel_settings_cancel);
        ilHotelCode = (TextInputLayout) mView.findViewById(R.id.il_dialog_hotel_settings_code);
        etHotelCode = (TextInputEditText) mView.findViewById(R.id.et_dialog_hotel_settings_code);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInputValidation.isInputEditTextFilled(etHotelCode, ilHotelCode, getString(R.string.error_message_field_required))) {
                    try {
                        Log.e(TAG, "Download Clicked");
                        lodeHotelInfos(etHotelCode.getText().toString());
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_something_wrong));
                        recreate();
                    }
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

    }

    private void startContactSupportDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_contact_support, null);
        AppCompatButton btnOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_contact_support_ok);
        AppCompatButton btnRtery = (AppCompatButton) mView.findViewById(R.id.btn_dialog_contact_support_retry);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        btnRtery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownloadSettingsDialog();
                dialog.dismiss();
            }
        });

    }

    private void startPrizeDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_prize, null);
        AppCompatImageView imgPrizeIcon = (AppCompatImageView) mView.findViewById(R.id.img_dialog_prize_icon);
        AppCompatTextView tvPrize = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_prize_text);
        btnPrizeOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_prize_ok);
        pbDialogProgress = (ContentLoadingProgressBar) mView.findViewById(R.id.pb_dialog_prize);

        tvPrize.setText(GLOBAL_PRIZES_LIST.get(prizeIndex).getPrizeMsg());

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        prizeDialog = mBuilder.create();
        prizeDialog.show();

        btnPrizeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmPrize();
            }
        });

    }

    private void startExitDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_exit, null);
        AppCompatButton btnYes = (AppCompatButton) mView.findViewById(R.id.btn_dialog_exit_yes);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_exit_cancel);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    /**********************************************************************************************/

    public void addLoisirs() {
        llLoisirContainer.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int _INDEX = 0;
        for (ItemPlay _ITEM : GLOBAL_ROOM_DETAILS.getLoisirs()) {

            final View cbxView = inflater.inflate(R.layout.item_check_box_row, null);

            AppCompatTextView tv = (AppCompatTextView) cbxView.findViewById(R.id.tv_check_box_title);
            AppCompatCheckBox cbx = (AppCompatCheckBox) cbxView.findViewById(R.id.cbx_check_box);

            tv.setText(_ITEM.getName());
            cbx.setChecked(false);
            cbx.setId(_INDEX);

            cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    GLOBAL_ROOM_DETAILS.getLoisirs().get(buttonView.getId()).setChecked(isChecked);
                }
            });

            llLoisirContainer.addView(cbxView);
            _INDEX++;
        }
    }

    public void addConsomations() {
        llConsomationContainer.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int _INDEX = 0;
        for (ItemPlay _ITEM : GLOBAL_ROOM_DETAILS.getConsomations()) {

            final View cbxView = inflater.inflate(R.layout.item_check_box_row, null);

            AppCompatTextView tv = (AppCompatTextView) cbxView.findViewById(R.id.tv_check_box_title);
            AppCompatCheckBox cbx = (AppCompatCheckBox) cbxView.findViewById(R.id.cbx_check_box);

            tv.setText(_ITEM.getName());
            cbx.setChecked(false);
            cbx.setId(_INDEX);

            cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    GLOBAL_ROOM_DETAILS.getConsomations().get(buttonView.getId()).setChecked(isChecked);
                }
            });

            llConsomationContainer.addView(cbxView);
            _INDEX++;
        }
    }

    /***(GAME)*************************************************************************************/

    public void addArcs() {
        rlPlayWeel.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int deg = (GLOBAL_PRIZES_LIST.size() > 8) ? 15 : 22;
        for (Prize _PRIZE : GLOBAL_PRIZES_LIST) {
            final View arcView = inflater.inflate((GLOBAL_PRIZES_LIST.size() > 8) ? R.layout.item_arc_30 : R.layout.item_arc_45, null);
            arcView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            AppCompatImageView imgArc = (AppCompatImageView) arcView.findViewById(R.id.img_arc_color);
            AppCompatImageView imgIcon = (AppCompatImageView) arcView.findViewById(R.id.img_arc_icon);
            AppCompatTextView tvPrize = (AppCompatTextView) arcView.findViewById(R.id.tv_arc_value);

            imgArc.setColorFilter(parseColor(_PRIZE.getPrizeColor()));

            Glide.with(this)
                    .load(_PRIZE.getPrizeIcon())
                    .placeholder(R.drawable.svg_place_holder_512)
                    .error(R.drawable.svg_place_holder_512)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imgIcon);

            //tvPrize.setText(_PRIZE.getPrizeValue().replace(" ", "\n"));
            tvPrize.setText(_PRIZE.getPrizeValue());

            arcView.setRotation(deg);
            deg += (GLOBAL_PRIZES_LIST.size() > 8) ? 30 : 45;
            rlPlayWeel.addView(arcView);
        }
    }

    public void StartSpinner() {

        mSpinRevolutions = 3600;
        mSpinDuration = 5000;

        int numOfPrizes = GLOBAL_PRIZES_LIST.size();// quantity of prizes
        int degreesPerPrize = 360 / numOfPrizes;// size of sector per prize in degrees
        int shift = 0; //shit where the arrow points
        prizeIndex = ((shift + GLOBAL_DEGREE.getDegree()) / degreesPerPrize) % numOfPrizes;
        prizeText = "Prize is: " + GLOBAL_PRIZES_LIST.get(prizeIndex).getPrizeValue();

        RotateAnimation rotateAnim = new RotateAnimation(0f, mSpinRevolutions - GLOBAL_DEGREE.getDegree(), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setRepeatCount(0);
        rotateAnim.setDuration(mSpinDuration);
        rotateAnim.setAnimationListener(this);
        rotateAnim.setFillAfter(true);

        rlPlayWeel.startAnimation(rotateAnim);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startPrizeDialog();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**********************************************************************************************/

    public void lodeHotelInfos(String code) {

        Log.e(TAG, "Hotel Infos Loading Started : " + code);

        RetrofitInterface service = RetrofitClient.getHotixSupportApi().create(RetrofitInterface.class);
        Call<HotelSettings> userCall = service.getInfosQuery(code, FINAL_APP_ID);

        final ProgressDialog progressDialog = new ProgressDialog(PlayActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_downloading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<HotelSettings>() {
            @Override
            public void onResponse(Call<HotelSettings> call, Response<HotelSettings> response) {
                Log.e(TAG, "Response : " + response.raw().code());
                progressDialog.dismiss();

                if (response.raw().code() == 200) {

                    HotelSettings hotelSettings = response.body();
                    //Check if hotel id > 0
                    if (!(hotelSettings.getId() > 0)) {
                        //Hotel do not exist
                        startContactSupportDialog();
                    } else {

                        //Get Public IP
                        if (!stringEmptyOrNull(hotelSettings.getIPPublic())) {
                            mSettings.setPublicIp(hotelSettings.getIPPublic());
                            mSettings.setPublicBaseUrl("http://" + hotelSettings.getIPPublic() + "/");
                            mSettings.setPublicIpEnabled(true);
                        } else {
                            mSettings.setPublicIp("xxx.xxx.xxx.xxx");
                            mSettings.setPublicIpEnabled(false);
                        }

                        //Get Local IP
                        if (!stringEmptyOrNull(hotelSettings.getIPLocal())) {
                            mSettings.setLocalIp(hotelSettings.getIPLocal());
                            mSettings.setLocalBaseUrl("http://" + hotelSettings.getIPLocal() + "/");
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

                        mSettings.setFirstStart(false);
                        mSettings.setConfigured(true);
                        mSettings.setSettingsUpdated(true);

                        Log.e(TAG, "MySettings FirstStart : " + mSettings.getFirstStart());

                        showSnackbar(findViewById(android.R.id.content), getString(R.string.message_settings_updated));
                        setBaseUrl(getApplicationContext());
                    }

                } else {
                    startDownloadSettingsDialog();
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<HotelSettings> call, Throwable t) {
                Log.e(TAG, "Failure : " + t.toString());
                progressDialog.dismiss();
                startDownloadSettingsDialog();
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_down));
            }
        });

    }

    private void loadRoomDetails() {

        final String sRoomNum = etPlayRoom.getText().toString().trim();
        btnPlaySearch.setEnabled(false);
        pbPlaySearch.setVisibility(View.VISIBLE);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<RoomDetails> userCall = service.getRoomDetailsQuery(sRoomNum);

        userCall.enqueue(new Callback<RoomDetails>() {
            @Override
            public void onResponse(Call<RoomDetails> call, Response<RoomDetails> response) {

                btnPlaySearch.setEnabled(true);
                pbPlaySearch.setVisibility(View.GONE);

                if (response.raw().code() == 200) {

                    RoomDetails mRoomDetails = response.body();
                    GLOBAL_ROOM_DETAILS = mRoomDetails;

                    if (mRoomDetails.getRoom() != null) {
                        _STEP = 2;
                        llRoomForm.setVisibility(View.GONE);
                        llDataForm.setVisibility(View.VISIBLE);
                        etPlayFirstName.setText(GLOBAL_ROOM_DETAILS.getFirstName());
                        etPlayLasttName.setText(GLOBAL_ROOM_DETAILS.getLastName());
                        etPlayDocNum.setText(GLOBAL_ROOM_DETAILS.getIdDocNumber());
                        etPlayEmail.setText(GLOBAL_ROOM_DETAILS.getEmail());
                        addLoisirs();
                        addConsomations();
                    } else {
                        showSnackbar(findViewById(android.R.id.content), "Room Do Not Exist!!!");
                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.raw().code() + " : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RoomDetails> call, Throwable t) {
                btnPlaySearch.setEnabled(true);
                pbPlaySearch.setVisibility(View.GONE);
                showSnackbar(findViewById(android.R.id.content), "error_message_server_unreachable");
            }
        });
    }

    private void loadAllPrizes() {

        btnPlayValidateConsomation.setEnabled(false);
        pbPlayValidateConsomation.setVisibility(View.VISIBLE);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ArrayList<Prize>> userCall = service.getPrizesListQuery();

        userCall.enqueue(new Callback<ArrayList<Prize>>() {
            @Override
            public void onResponse(Call<ArrayList<Prize>> call, Response<ArrayList<Prize>> response) {

                btnPlayValidateConsomation.setEnabled(true);
                pbPlayValidateConsomation.setVisibility(View.GONE);

                if (response.raw().code() == 200) {

                    ArrayList<Prize> mPrizes = response.body();
                    GLOBAL_PRIZES_LIST.clear();
                    GLOBAL_PRIZES_LIST.addAll(mPrizes);

                    loadRandomDegree();

                    if (mPrizes.size() > 0) {
                        //showSnackbar(findViewById(android.R.id.content), ""+GLOBAL_PRIZES_LIST.size());
                    } else {

                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Prize>> call, Throwable t) {
                btnPlayValidateConsomation.setEnabled(true);
                pbPlayValidateConsomation.setVisibility(View.GONE);
                showSnackbar(findViewById(android.R.id.content), "error_message_server_unreachable");
            }
        });
    }

    private void loadRandomDegree() {

        btnPlayValidateConsomation.setEnabled(false);
        pbPlayValidateConsomation.setVisibility(View.VISIBLE);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<Degree> userCall = service.getRandomDegreeQuery();

        userCall.enqueue(new Callback<Degree>() {
            @Override
            public void onResponse(Call<Degree> call, Response<Degree> response) {

                btnPlayValidateConsomation.setEnabled(true);
                pbPlayValidateConsomation.setVisibility(View.GONE);

                if (response.raw().code() == 200) {

                    Degree mDegree = response.body();
                    GLOBAL_DEGREE = mDegree;

                    _STEP = 5;
                    llConsomationForm.setVisibility(View.GONE);
                    rlPlayGame.setVisibility(View.VISIBLE);
                    addArcs();

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<Degree> call, Throwable t) {
                btnPlayValidateConsomation.setEnabled(true);
                pbPlayValidateConsomation.setVisibility(View.GONE);
                showSnackbar(findViewById(android.R.id.content), "error_message_server_unreachable");
            }
        });
    }

    public void updateClient() {

        btnPlayValidateConsomation.setEnabled(false);
        pbPlayValidateConsomation.setVisibility(View.VISIBLE);

        final String content_type = "application/json";

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<Success> userCall = service.updateClientQuery(content_type, GLOBAL_ROOM_DETAILS);

        userCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                btnPlayValidateConsomation.setEnabled(true);
                pbPlayValidateConsomation.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    Success success = response.body();
                    if (success.getSuccess()) {
                        loadAllPrizes();
                    } else {
                        showSnackbar(findViewById(android.R.id.content), success.getMessage());
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                btnPlayValidateConsomation.setEnabled(true);
                pbPlayValidateConsomation.setVisibility(View.GONE);
                showSnackbar(findViewById(android.R.id.content), "error_message_server_unreachable");
            }
        });

    }

    public void ConfirmPrize() {

        btnPrizeOk.setEnabled(false);
        pbDialogProgress.setVisibility(View.VISIBLE);

        final String content_type = "application/json";

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<Success> userCall = service.confirmPrizeQuery(content_type, GLOBAL_ROOM_DETAILS);

        userCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                btnPrizeOk.setEnabled(true);
                pbDialogProgress.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    Success success = response.body();
                    if (success.getSuccess()) {

                        _STEP = 1;
                        etPlayRoom.setText("");
                        llRoomForm.setVisibility(View.VISIBLE);
                        rlPlayGame.setVisibility(View.GONE);
                        prizeDialog.dismiss();

                    } else {
                        showSnackbar(findViewById(android.R.id.content), success.getMessage());
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                btnPrizeOk.setEnabled(true);
                pbDialogProgress.setVisibility(View.GONE);
                showSnackbar(findViewById(android.R.id.content), "error_message_server_unreachable");
            }
        });

    }


}
