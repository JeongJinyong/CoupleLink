package link.couple.jin.couplelink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import butterknife.BindView;
import butterknife.ButterKnife;
import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.data.UserClass;
import link.couple.jin.couplelink.home.HomeActivity;
import link.couple.jin.couplelink.utile.Log;

import static link.couple.jin.couplelink.utile.Constant.USER_UID;

/**
 * 로그인엑티비티
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.login_email_edit) EditText loginEmailEdit;
    @BindView(R.id.login_pw_edit) EditText loginPwEdit;
    @BindView(R.id.login_auto_check) CheckBox loginAutoCheck;
    @BindView(R.id.login_save_check) CheckBox loginSaveCheck;
    @BindView(R.id.login_login_btn) Button loginLoginBtn;
    @BindView(R.id.login_find_btn) Button loginFindBtn;
    @BindView(R.id.login_signup_btn) Button loginSignupBtn;
    private SessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        loginLoginBtn.setOnClickListener(this);
        loginFindBtn.setOnClickListener(this);
        loginSignupBtn.setOnClickListener(this);
        loginEmailEdit.setText("image5956@naver.com");
        loginPwEdit.setText("123456");

        // [START initialize_auth]
        firebaseAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        if(getIntent().hasExtra("url")){
            Log.e(getIntent().getStringExtra("url"));
        }

//        if(firebaseAuth.getCurrentUser() != null && util.isAutoLogin()){
//            settingUid(firebaseAuth.getCurrentUser().getUid());
//        }

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void signIn(final String email, final String password) {
        if (!validateForm(email, password)) {
            return;
        }

        showProgressDialog();
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        settingUid(task.getResult().getUser().getUid());
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                /**
                 * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
                 */
                hideProgressDialog();
                Log.e( e.getMessage());
            }
        });
        // [END sign_in_with_email]
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    void settingUid(String uid){
        showProgressDialog();
        getUserQuery(uid, USER_UID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserClass userClass = dataSnapshot.getValue(UserClass.class);
                        userClass.uid = dataSnapshot.getKey();
                        if(!userClass.fcm.equals(refreshedToken)) {
                            userClass.fcm = refreshedToken;
                        }
                        dataSnapshot.getRef().updateChildren(userClass.toMap());
                        setUserClass(userClass);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        hideProgressDialog();
                        Log.e( databaseError.toString()+"//"+databaseError.getCode()+"//"+databaseError.getDetails()+"//"+databaseError.getMessage());
                    }
                });
    }

    public void setUserClass(UserClass userClass){
        try {
            userLogin = userClass;
            if(userClass.isCouple){
                if(loginAutoCheck.isChecked()){
                    util.setAutoLogin(true);
                }else{
                    util.setAutoLogin(false);
                }
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                if(getIntent().hasExtra("url"))
                    intent.putExtra("url", getIntent().getStringExtra("url"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                LoginActivity.this.finish();
            }else if(userClass.isCoupleConnect) {
                Toast.makeText(LoginActivity.this, R.string.toast_couple_wait, Toast.LENGTH_SHORT).show();
            }else{
                if(userClass.couple.isEmpty()){
                    Intent intent = new Intent(LoginActivity.this, CoupleconnectActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(LoginActivity.this, CoupleapplyActivity.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            Log.e( e.getMessage());
            e.printStackTrace();
        }
        hideProgressDialog();
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            loginEmailEdit.setError(util.getStringResources(R.string.edit_email_notinput));
            loginEmailEdit.requestFocus();
            valid = false;
            return valid;
        } else {
            loginEmailEdit.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            loginPwEdit.setError(util.getStringResources(R.string.edit_pw_notinput));
            loginPwEdit.requestFocus();
            valid = false;
        } else {
            loginPwEdit.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_login_btn) {
            signIn(loginEmailEdit.getText().toString(), loginPwEdit.getText().toString());
        }
        if (i == R.id.login_signup_btn) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                exception.printStackTrace();
                Log.e(exception.toString());
            }
        }
    }
    protected void redirectSignupActivity() {
        Log.e("로그인");
        requestMe();
//        UserManagement.requestLogout(new LogoutResponseCallback() {
//            @Override
//            public void onCompleteLogout() {
//                Log.e("로그아웃");
//            }
//        });
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.e(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                UserClass userClass = new UserClass(userProfile.getEmail(),userProfile.getNickname(),"",false,false);
                if(!userClass.fcm.equals(refreshedToken)) {
                    userClass.fcm = refreshedToken;
                }
                getUserQuery(userProfile.getId()+"", USER_UID).getRef().setValue(userClass);
                userClass.uid = userProfile.getId()+"";
                setUserClass(userClass);
            }

            @Override
            public void onNotSignedUp() {
            }
        });
    }
}
