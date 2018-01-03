package link.couple.jin.couplelink.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import link.couple.jin.couplelink.utile.Log;

/**
 * 핸드폰 시작시 브로드캐스트 리시버로 서비스 실행
 */
public class ClipboardMonitorStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ComponentName service = context.startService(
                    new Intent(context, ClipboardMonitor.class));
            if (service == null) {
                Log.e("Can't start service "
                        + ClipboardMonitor.class.getName());
            }
        } else {
            Log.e("Recieved unexpected intent " + intent.toString());
        }
    }
}
