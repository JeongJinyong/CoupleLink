package link.couple.jin.couplelink.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat

import java.util.regex.Matcher
import java.util.regex.Pattern

import link.couple.jin.couplelink.LoginActivity
import link.couple.jin.couplelink.R
import link.couple.jin.couplelink.home.HomeActivity
import link.couple.jin.couplelink.utile.Log

/**
 * 서비스에서 저장된 링크 받기
 */
class ClipboardMonitor : Service() {
    private var mNM: NotificationManager? = null
    private val mTask = MonitorTask()
    private var mCM: ClipboardManager? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mCM = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mTask.start()
    }

    private fun showNotification(str: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("url", str)
        Log.e(intent.getStringExtra("url"))
        val contentIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.search_icon)
                .setContentTitle("링크추출")
                .setContentText(str)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        mNM!!.notify(R.string.clip_monitor_service, builder.build())
    }

    override fun onDestroy() {
        mNM!!.cancel(R.string.clip_monitor_service)
        mTask.cancel()
    }

    /**
     * Monitor task: monitor new text clips in global system clipboard and
     * new image clips in browser download directory
     */
    private inner class MonitorTask : Thread("ClipboardMonitor") {

        @Volatile
        private var mKeepRunning = false
        private var mOldClip: String? = null

        /** Cancel task  */
        fun cancel() {
            mKeepRunning = false
            interrupt()
        }

        override fun run() {
            mKeepRunning = true
            while (true) {
                doTask()
                if (!mKeepRunning) {
                    break
                }
            }
        }

        private fun doTask() {
            if (mCM!!.hasPrimaryClip()) {
                val newClip = mCM!!.primaryClip.getItemAt(0).text.toString()
                if (newClip != mOldClip) {
                    val regex = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"
                    val p = Pattern.compile(regex)
                    val m = p.matcher(newClip)
                    mOldClip = newClip
                    if (m.find()) {
                        showNotification(m.group(0))
                        Log.i("new text clip inserted: " + m.group(0))
                    }
                }
            }
        }
    }
}
