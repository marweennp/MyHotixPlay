package com.hotix.myhotixplay.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.hotix.myhotixplay.R;

public class ConnectionChecher {

    public static Boolean online = false;

    /*Connection Checker*/
    public static boolean checkNetwork(View view, final Context context) {
        Snackbar snackBar = Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
        Snackbar snackBarServ = Snackbar.make(view, R.string.unreachable_server, Snackbar.LENGTH_INDEFINITE);
        if (!isNetworkAvailable(context)) {
            snackBar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetwork(view, context); //recursive call.
                }
            }).show();
            return false;
        } else if (!isOnline()) {
            snackBarServ.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetwork(view, context); //recursive call.
                }
            }).show();
            return false;
        }
        snackBar.dismiss();
        return true;
    }

    // Connection Checker
    public static boolean checkNetwork(final Context context) {
        if (!isNetworkAvailable(context)) {
            return false;
        } else if (!isOnline()) {
            return false;
        }
        return true;
    }

    /*Android Network Availability*/
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    /* Check if Serveur Online (ping) */
    private static boolean isOnline() {
        try {
            return (Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
