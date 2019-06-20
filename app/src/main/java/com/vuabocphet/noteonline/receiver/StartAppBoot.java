package com.vuabocphet.noteonline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.vuabocphet.noteonline.service.MyService;

public class StartAppBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

        }else {
            Log.e("HELLO","HELLO");
        }

    }
}
