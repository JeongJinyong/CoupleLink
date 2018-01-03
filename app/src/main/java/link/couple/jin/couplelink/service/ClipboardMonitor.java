package link.couple.jin.couplelink.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.home.HomeActivity;
import link.couple.jin.couplelink.utile.Log;

/**
 * 서비스에서 저장된 링크 받기
 */
public class ClipboardMonitor extends Service {
    private NotificationManager mNM;
    private MonitorTask mTask = new MonitorTask();
    private ClipboardManager mCM;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mTask.start();
    }

    private void showNotification(String str) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("url",str);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent
              , 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.myclips_icon)
                .setContentTitle("링크추출")
                .setContentText(str)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mNM.notify(R.string.clip_monitor_service, builder.build());
    }
    
    @Override
    public void onDestroy() {
        mNM.cancel(R.string.clip_monitor_service);
        mTask.cancel();
    }

    /**
     * Monitor task: monitor new text clips in global system clipboard and
     * new image clips in browser download directory
     */
    private class MonitorTask extends Thread {

        private volatile boolean mKeepRunning = false;
        private String mOldClip = null;
        
        public MonitorTask() {
            super("ClipboardMonitor");
        }

        /** Cancel task */
        public void cancel() {
            mKeepRunning = false;
            interrupt();
        }
        
        @Override
        public void run() {
            mKeepRunning = true;
            while (true) {
                doTask();
                if (!mKeepRunning) {
                    break;
                }
            }
        }
        
        private void doTask() {
            if (mCM.hasPrimaryClip()) {
                String newClip = mCM.getPrimaryClip().getItemAt(0).getText().toString();
                if (!newClip.equals(mOldClip)) {
                    String regex ="[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
                    Pattern p = Pattern.compile(regex);
                    Matcher m=p.matcher(newClip);
                    mOldClip = newClip;
                    if(m.find()){
                        showNotification(m.group(0));
                        Log.i("new text clip inserted: " +m.group(0));
                    }
                }
            }
        }
    }
}
