package link.couple.jin.couplelink.data;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원가입시 데이터베이스에 이메일과 이름 저장
 */

public class UserClass {

    public String email;
    public String username;
    public String couple = "";
    public boolean isCouple = false;
    public String uid;

    public UserClass(){}

    public UserClass(@NonNull String email, @NonNull String username, @NonNull String couple, @NonNull boolean isCouple){
        this.email = email;
        this.username = username;
        this.couple = couple;
        this.isCouple = isCouple;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("email", email);
        result.put("username", username);
        result.put("couple", couple);
        result.put("isCouple", isCouple);
        return result;
    }

}
