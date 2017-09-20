package link.couple.jin.couplelink.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import link.couple.jin.couplelink.utile.Log;

/**
 * Created by jeongjin-yong on 2017. 9. 20..
 */

public class CpFirebaseIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed token: " + refreshedToken);
    }
}

