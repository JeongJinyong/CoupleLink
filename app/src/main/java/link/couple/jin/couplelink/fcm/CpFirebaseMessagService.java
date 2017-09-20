package link.couple.jin.couplelink.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import link.couple.jin.couplelink.utile.Log;

/**
 * Created by jeongjin-yong on 2017. 9. 20..
 */

public class CpFirebaseMessagService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String msg = remoteMessage.getNotification().getBody();
        Log.e(msg);
    }
}
