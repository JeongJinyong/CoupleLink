package link.couple.jin.couplelink.data;

import android.support.annotation.NonNull;

/**
 * 회원가입시 데이터베이스에 이메일과 이름 저장
 */

public class UserClass {

    public String username;
    public String couple = "";
    public boolean isCouple = false;

    public UserClass(@NonNull String username, @NonNull String couple, @NonNull boolean isCouple){
        this.username = username;
        this.couple = couple;
        this.isCouple = isCouple;
    }

}
