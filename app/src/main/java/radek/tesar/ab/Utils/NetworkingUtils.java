package radek.tesar.ab.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import radek.tesar.ab.App;

/**
 * Created by tesar on 28.04.2017.
 */

public class NetworkingUtils {

    public static boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo ethernet = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

        boolean wifib = false;
        if(wifi != null && wifi.isConnected()){
            wifib = true;
        }
        boolean mobileb = false;
        if(mobile != null && mobile.isConnected()){
            mobileb = true;
        }
        boolean ethernetb = false;
        if(ethernet != null && ethernet.isConnected()){
            ethernetb = true;
        }
        boolean connection = false;
        if(netInfo != null && netInfo.isConnected()){
            connection = true  ;
        }


        return ethernetb || mobileb || wifib || connection;

    }
}
