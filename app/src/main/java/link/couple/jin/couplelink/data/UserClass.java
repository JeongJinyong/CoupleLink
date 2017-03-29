package link.couple.jin.couplelink.data;

import android.support.annotation.NonNull;

/**
 * 회원가입시 데이터베이스에 이메일과 이름 저장
 * Created by jin on 2016-11-30.
 */

public class UserClass {

    public String username;
    public String email;
    public String couple = "";
    public boolean isCouple = false;

    public UserClass(@NonNull String username, @NonNull String email, @NonNull String couple, @NonNull boolean isCouple){
        this.username = username;
        this.email = email;
        this.couple = couple;
        this.isCouple = isCouple;
    }

}
