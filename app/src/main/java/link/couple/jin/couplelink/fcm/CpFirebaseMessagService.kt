package link.couple.jin.couplelink.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import link.couple.jin.couplelink.utile.Log

/**
 * Created by jeongjin-yong on 2017. 9. 20..
 */

class CpFirebaseMessagService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        val msg = remoteMessage!!.notification.body
        Log.e(msg)
    }
}
