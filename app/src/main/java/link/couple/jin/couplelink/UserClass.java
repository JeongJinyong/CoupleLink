package link.couple.jin.couplelink;

/**
 * 회원가입시 데이터베이스에 이메일과 이름 저장
 * Created by jin on 2016-11-30.
 */

public class UserClass {

    public String username;
    public String email;


    public UserClass(){

    }

    public UserClass(String username, String email){
        this.username = username;
        this.email = email;
    }

}
