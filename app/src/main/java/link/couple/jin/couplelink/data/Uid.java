package link.couple.jin.couplelink.data;

import android.support.annotation.NonNull;

/**
 * 회원가입시 데이터베이스에 이메일과 이름 저장
 */

public class Uid {

    public String uid;
    public UserClass userClass;

    public Uid(@NonNull String uid, @NonNull UserClass userClass){
        this.uid = uid;
        this.userClass = userClass;
    }

}
