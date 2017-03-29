package link.couple.jin.couplelink;

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
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import link.couple.jin.couplelink.data.UserClass;

/**
 * 회원가입 페이지
 * Created by jin on 2016-11-29.
 */

public class SignUpActivity extends MainClass {


    @BindView(R.id.signup_email)
    EditText signupEmail;
    @BindView(R.id.signup_pw)
    EditText signupPw;
    @BindView(R.id.signup_pwok)
    EditText signupPwok;
    @BindView(R.id.signup_name)
    EditText signupName;
    @BindView(R.id.signup_btn)
    Button signupBtn;
    private boolean isPwdconfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ButterKnife.bind(this);

        signupEmail.setText("image_5956@naver.com");
        signupPw.setText("123456");
        signupPwok.setText("123456");
        signupName.setText("정진용");
        isPwdconfirm = true;

        /**
         * 비밀번호 일치 검사
         * 참고 : http://cocomo.tistory.com/387
         */
        signupBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signupPw.getText().toString();
                String password_ok = signupPwok.getText().toString();

                if (password.equals(password_ok)) {
                    signupPw.setError(null);
                    signupPwok.setError(null);
                    isPwdconfirm = true;
                } else {
                    signupPw.setError(util.getStringResources(R.string.error_pw_notmatch));
                    signupPwok.setError(util.getStringResources(R.string.error_pw_notmatch));
                    isPwdconfirm = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //[END TextChangedListener]

        /**
         * 회원가입 버튼 액션
         * 참고 : http://cocomo.tistory.com/387
         */
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = signupEmail.getText().toString();
                String password = signupPw.getText().toString();
                String username = signupName.getText().toString();

                // 이메일 입력 확인
                if (TextUtils.isEmpty(email)) {
                    signupEmail.setError(util.getStringResources(R.string.edit_email_notinput));
                    signupEmail.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if (TextUtils.isEmpty(password)) {
                    signupPw.setError(util.getStringResources(R.string.edit_pw_notinput));
                    signupPw.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if (TextUtils.isEmpty(signupPwok.getText().toString())) {
                    signupPwok.setError(util.getStringResources(R.string.edit_pw_notinput));
                    signupPwok.requestFocus();
                    return;
                }

                // 이름 및 닉네임 입력 확인
                if (TextUtils.isEmpty(username)) {
                    signupName.setError(util.getStringResources(R.string.edit_email_name));
                    signupName.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if (!isPwdconfirm) {
                    signupPw.setError(util.getStringResources(R.string.edit_pw_notinput));
                    signupPwok.setError(util.getStringResources(R.string.edit_pw_notinput));
                    return;
                }

                if(signupPw.length() > 6){
                    Toast.makeText(SignUpActivity.this, R.string.error_pw_short, Toast.LENGTH_SHORT).show();
                }
                signUpUser(email, password, username);

            }
        });
        //[END OnClickListener]
    }

    /**
     * 회원가입 메서드
     * 참고 :
     * http://blog.naver.com/mingkeymagic/220716442524
     * https://firebase.google.com/docs/database/android/read-and-write
     *
     * @param email
     * @param password
     * @param username
     */
    public void signUpUser(final String email, final String password, final String username) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    UserClass userModel = new UserClass(username, email, "", false);
                    databaseReference.child("user").child(user.getUid()).setValue(userModel);
                    Toast.makeText(SignUpActivity.this, R.string.toast_signup_complete, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                util.log("e", e.getMessage());
                /**
                 * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
                 */
                if (e.getMessage().contains("email"))
                    Toast.makeText(SignUpActivity.this, R.string.error_email_already, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
