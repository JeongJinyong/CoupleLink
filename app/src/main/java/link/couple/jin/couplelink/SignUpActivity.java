package link.couple.jin.couplelink;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** 회원가입 페이지
 * Created by jin on 2016-11-29.
 */

public class SignUpActivity extends MainClass{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText signup_email;
    private EditText signup_pw;
    private EditText signup_pwok;
    private EditText signup_name;
    private Button signup_btn;

    private boolean isPwdconfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        firebaseAuth = FirebaseAuth.getInstance();

        signup_email = (EditText)findViewById(R.id.signup_email);
        signup_pw = (EditText)findViewById(R.id.signup_pw);
        signup_pwok = (EditText)findViewById(R.id.signup_pwok);
        signup_name = (EditText)findViewById(R.id.signup_name);
        signup_btn = (Button) findViewById(R.id.signup_btn);

        signup_email.setText("image_5956@naver.com");
        signup_pw.setText("5659kh");
        signup_pwok.setText("5659kh");
        signup_name.setText("정진용");
        isPwdconfirm = true;

        /**
         * 비밀번호 일치 검사
         * 참고 : http://cocomo.tistory.com/387
         */
        signup_pwok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signup_pw.getText().toString();
                String password_ok = signup_pwok.getText().toString();

                if( password.equals(password_ok) ) {
                    signup_pw.setError(null);
                    signup_pwok.setError(null);
                    isPwdconfirm = true;
                } else {
                    signup_pw.setError(util.getStringResources(R.string.error_pw_notmatch));
                    signup_pwok.setError(util.getStringResources(R.string.error_pw_notmatch));
                    isPwdconfirm = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        //[END TextChangedListener]

        /**
         * 회원가입 버튼 액션
         * 참고 : http://cocomo.tistory.com/387
         */
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = signup_email.getText().toString();
                String password = signup_pw.getText().toString();
                String username = signup_name.getText().toString();

                // 이메일 입력 확인
                if( TextUtils.isEmpty(email) ) {
                    signup_email.setError(util.getStringResources(R.string.edit_email_notinput));
                    signup_email.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if( TextUtils.isEmpty(password) ) {
                    signup_pw.setError(util.getStringResources(R.string.edit_pw_notinput));
                    signup_pw.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( TextUtils.isEmpty(signup_pwok.getText().toString()) ) {
                    signup_pwok.setError(util.getStringResources(R.string.edit_pw_notinput));
                    signup_pwok.requestFocus();
                    return;
                }

                // 이름 및 닉네임 입력 확인
                if( TextUtils.isEmpty(username) ) {
                    signup_name.setError(util.getStringResources(R.string.edit_email_notinput));
                    signup_name.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !isPwdconfirm ) {
                    signup_pw.setError(util.getStringResources(R.string.edit_pw_notinput));
                    signup_pwok.setError(util.getStringResources(R.string.edit_pw_notinput));
                    return;
                }

                signUpUser(email, password, username);

            }
        });
        //[END OnClickListener]
    }

    /**
     * 회원가입 메서드
     * 참고 :
     *      http://blog.naver.com/mingkeymagic/220716442524
     *      https://firebase.google.com/docs/database/android/read-and-write
     * @param email
     * @param password
     * @param username
     */
    public void signUpUser(final String email, final String password, final String username){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    UserClass userModel = new UserClass(username,email);
                    databaseReference.child("user").child(user.getUid()).setValue(userModel);
                    Toast.makeText(SignUpActivity.this,R.string.toast_signup_complete,Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                util.log("e",e.getMessage());
                /**
                 * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
                 */
                if(e.getMessage().contains("email"))
                    Toast.makeText(SignUpActivity.this,R.string.error_email_already,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
