package link.couple.jin.couplelink.service

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

import link.couple.jin.couplelink.utile.Log

/**
 * 핸드폰 시작시 브로드캐스트 리시버로 서비스 실행
 */
class ClipboardMonitorStarter : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val service = context.startService(
                    Intent(context, ClipboardMonitor::class.java))
            if (service == null) {
                Log.e("Can't start service " + ClipboardMonitor::class.java!!.getName())
            }
        } else {
            Log.e("Recieved unexpected intent " + intent.toString())
        }
    }
}
