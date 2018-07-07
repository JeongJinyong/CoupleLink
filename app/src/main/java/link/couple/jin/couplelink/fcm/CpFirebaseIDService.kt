package link.couple.jin.couplelink.fcm

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

import link.couple.jin.couplelink.utile.Log

/**
 * Created by jeongjin-yong on 2017. 9. 20..
 */

class CpFirebaseIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("Refreshed token: " + refreshedToken!!)
    }
}

