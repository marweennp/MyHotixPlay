package com.hotix.myhotixplay.helpers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.hotix.myhotixplay.R;
import com.hotix.myhotixplay.retrofit.RetrofitClient;
import com.hotix.myhotixplay.retrofit.RetrofitInterface;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixplay.helpers.ConstantConfig.BASE_URL;

public class Utils {

    // Different dictionaries used for random password
    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
    private static final String DICTIO = ALPHA_CAPS + ALPHA + NUMERIC + SPECIAL_CHARS;

    /**
     * Method will generate random string
     *
     * @param len //the length of the random string
     * @return the random password
     */
    public static String generatePassword(int len) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(DICTIO.length());
            result += DICTIO.charAt(index);
        }
        return result;
    }

    //Calculate the number of days between two dates "yyyy-MM-dd'T'hh:mm:ss"
    public static String calculateDaysBetween(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date start;
        Date end;
        try {
            start = df.parse(startDate);
            end = df.parse(endDate);

            return Long.toString((end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
        }
        return "0";
    }

    //Calculate the number of days between two dates "dd/MM/yyyy"
    public static String newCalculateDaysBetween(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date start;
        Date end;
        try {
            start = df.parse(startDate);
            end = df.parse(endDate);
            return Long.toString((end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
        }
        return "0";
    }

    //Calculate the number of days between two dates "yyyy-MM-dd'T'hh:mm:ss"
    public static String fromTodayToDate(String date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String toDay = df.format(Calendar.getInstance().getTime());
        Date start;
        Date end;
        try {
            start = df.parse(toDay);
            end = df.parse(date);
            return Long.toString((end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
            return e.toString();
        }
    }

    /*Show a SnackBar with msg*/
    public static void showSnackbar(View view, String msg) {
        final Snackbar snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        }).show();

    }

    //SigneUp Text TowColors white & colorPrimary
    public static String signeUpTextTowColors(String text1, String text2, Context context) {

        String color1 = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white)).substring(2, 8);
        String color2 = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary)).substring(2, 8);

        String text = "<font color=" + color1 + ">" + text1 + "</font> <font color=" + color2 + "><b>" + text2 + "</b></color>";

        return text;
    }

    //validate date
    public static Boolean dateBefore(String dateOne, String dateTwo) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date one;
        Date two;
        try {

            one = df.parse(dateOne);
            two = df.parse(dateTwo);
            return two.compareTo(one) > 0;

        } catch (Exception e) {
        }
        return false;

    }

    public static Boolean sameDate(String dateOne, String dateTwo) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date one;
        Date two;
        try {
            one = df.parse(dateOne);
            two = df.parse(dateTwo);
            return two.compareTo(one) == 0;

        } catch (Exception e) {
        }
        return false;

    }


    /**********************************************************************************************/
    //Picasso Cash remover

    public static boolean clearImageDiskCache(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (cache.exists() && cache.isDirectory()) {
            return deleteDir(cache);
        }
        return false;
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    /**********************************************************************************************/

    /*Set BASE_URL*/
    public static void setBaseUrl(Context context) {
        AppSettings mSettings = new AppSettings(context);

        if (mSettings.getLocalIpEnabled() && mSettings.getPublicIpEnabled()) {

            BASE_URL = mSettings.getLocalIpDefault() ? mSettings.getLocalBaseUrl() : mSettings.getPublicBaseUrl();
            try {
                ping(context);
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }

        } else {
            BASE_URL = mSettings.getLocalIpEnabled() ? mSettings.getLocalBaseUrl() : mSettings.getPublicBaseUrl();
        }

    }

    public static void ping(Context context) {
        final AppSettings mSettings = new AppSettings(context);

        RetrofitInterface service = RetrofitClient.getClientPing().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.isConnectedQuery();
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.raw().code() == 200) {
                    Log.e("BASE_URL OK 200", BASE_URL);

                } else {
                    BASE_URL = mSettings.getLocalIpDefault() ? mSettings.getPublicBaseUrl() : mSettings.getLocalBaseUrl();
                    mSettings.setLocalIpDefault(!mSettings.getLocalIpDefault());
                    Log.e("BASE_URL Switshed", BASE_URL);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                BASE_URL = mSettings.getLocalIpDefault() ? mSettings.getPublicBaseUrl() : mSettings.getLocalBaseUrl();
                mSettings.setLocalIpDefault(!mSettings.getLocalIpDefault());
                Log.e("BASE_URL onFailure", BASE_URL);
            }
        });

    }

    /**********************************************************************************************/

    /**
     * String Empty Or Null (String)
     * EX stringEmptyOrNull("hello")
     *
     * @param str //the String to check for null or empty value
     * @return true if the String is !null & !empty false if not
     */
    public static boolean stringEmptyOrNull(String str) {

        if (str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Date formatter (String, String, String)
     * EX dateFormater("2000-01-01'T'00:00:00", "yyyy-MM-dd'T'hh:mm:ss", "dd MMM yyyy")
     *
     * @param date       //the original date to format
     * @param fromFormat //the original date string format EX "yyyy-MM-dd'T'hh:mm:ss"
     * @param toFormat   //the string format to transform to EX "dd MMM yyyy"
     * @return the String Date
     */
    public static String dateFormater(String date, String fromFormat, String toFormat) {
        SimpleDateFormat sdf_from = new SimpleDateFormat(fromFormat);
        SimpleDateFormat sdf_to = new SimpleDateFormat(toFormat);
        Date result;
        String dateResult = "";
        try {
            result = sdf_from.parse(date);
            dateResult = sdf_to.format(result);
        } catch (Exception e) {
        }
        return dateResult;
    }

    /**
     * Date Colored (String, String, String)
     * EX dateFormater("2000-01-01'T'00:00:00", "yyyy-MM-dd'T'hh:mm:ss", "dd MMM yyyy")
     *
     * @param date       //the original date to format
     * @param col_1      //coler 1 default #757575
     * @param col_1      //coler 2 default #424242
     * @param fromFormat //the original date string format EX "yyyy-MM-dd'T'hh:mm:ss"
     * @param full       //if tru return "dd MMM yyyy" else return "dd MMM "
     * @return the String Colered Date
     */
    public static String dateColored(String date, String col_1, String col_2, String fromFormat, boolean full) {

        String color1 = "#9E9E9E";//default color
        String color2 = "#757575";//default color

        SimpleDateFormat sdf_from = new SimpleDateFormat(fromFormat);
        SimpleDateFormat sdf_d = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_m = new SimpleDateFormat("MMM");
        SimpleDateFormat sdf_y = new SimpleDateFormat("yyyy");
        Date result;
        String dateResult = "";
        String st_d = "";
        String st_m = "";
        String st_y = "";

        if (!stringEmptyOrNull(col_1)) {
            color1 = col_1;
        }

        if (!stringEmptyOrNull(col_2)) {
            color2 = col_2;
        }

        try {
            result = sdf_from.parse(date);
            st_d = sdf_d.format(result);
            st_m = sdf_m.format(result);
            st_y = sdf_y.format(result);
        } catch (Exception e) {
        }

        if (full) {
            dateResult = "<font color=" + color1 + ">" + st_d + "</font> <font color=" + color2 + "><b>" + st_m + "</b></font>" + "<font color=" + color1 + "> " + st_y;
        } else {
            dateResult = "<font color=" + color1 + ">" + st_d + "</font> <font color=" + color2 + "><b>" + st_m + "</b></font>";
        }


        return dateResult;
    }

    /**
     * Enum for date formats
     */
    public enum FormatsDate {
        F1("dd/MM/yyyy"),
        F2("yyyy-MM-dd'T'hh:mm:ss");

        private String format;

        FormatsDate(String format) {
            this.format = format;
        }

        public String getFormat() {
            return this.format;
        }
    }
}
