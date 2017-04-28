package radek.tesar.ab;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tesar on 27.04.2017.
 */

public class App extends MultiDexApplication{

    public static final boolean DEBUG = BuildConfig.DEBUG;
    private static App sInstance;
    private static final ReentrantLock sLock = new ReentrantLock();

    public static Context getContext() {
        if (sInstance == null) {
            try {
                sLock.lock();
                if (sInstance == null)
                    sInstance = new App();
            } finally {
                sLock.unlock();
            }
        }
        return sInstance.getBaseContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static void log(final String TAG, final String message) {
        if (message != null)
            if (DEBUG) Log.d(TAG, message);
    }

    public static void log(final String message) {
        if (message != null)
            if (DEBUG) Log.d("mWaiter", message);
    }
}
