package com.loltimeline.m1miage.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LolSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static LolSyncAdapter sLolSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("LolSyncService", "onCreate - LolSyncService");
        synchronized (sSyncAdapterLock) {
            if (sLolSyncAdapter == null) {
                sLolSyncAdapter = new LolSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sLolSyncAdapter.getSyncAdapterBinder();
    }
}