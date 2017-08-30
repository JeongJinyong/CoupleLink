package link.couple.jin.couplelink;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import link.couple.jin.couplelink.data.UserClass;

/**
 * 로그인엑티비티
 */

public class LoginActivity extends MainClass implements View.OnClickListener {

    @BindView(R.id.login_email_edit) EditText loginEmailEdit;
    @BindView(R.id.login_pw_edit) EditText loginPwEdit;
    @BindView(R.id.login_auto_check) CheckBox loginAutoCheck;
    @BindView(R.id.login_save_check) CheckBox loginSaveCheck;
    @BindView(R.id.login_login_btn) Button loginLoginBtn;
    @BindView(R.id.login_find_btn) Button loginFindBtn;
    @BindView(R.id.login_signup_btn) Button loginSignupBtn;


    private FirebaseAuth.AuthStateListener authStateListener;


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

        // [START auth_state_listener]
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    util.log("e", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    util.log("e", "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]
    }

    private void signIn(String email, String password) {
        if (!validateForm(email, password)) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        util.log("e", "signInWithEmail:onComplete:" + task.isSuccessful());
                        databaseReference.child("user").child(task.getResult().getUser().getUid()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            UserClass userClass = dataSnapshot.getValue(UserClass.class);
                                            userLogin = userClass;
                                            userLogin.uid = dataSnapshot.getKey();
                                            if(userClass.isCouple){
                                                //커플은 커플페이지로)
                                            }else{
                                                if(userClass.couple.isEmpty()){
                                                    Intent intent = new Intent(LoginActivity.this, CoupleconnectActivity.class);
                                                    startActivity(intent);
                                                }else{
                                                    Intent intent = new Intent(LoginActivity.this, CoupleapplyActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                            hideProgressDialog();
                                        } catch (Exception e) {
                                            util.log("e", e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        util.log("e", databaseError.toString()+"//"+databaseError.getCode()+"//"+databaseError.getDetails()+"//"+databaseError.getMessage());
                                    }
                                });
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                /**
                 * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
                 */
                util.log("e", e.getMessage());
            }
        });
        // [END sign_in_with_email]
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


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
    // [END on_stop_remove_listener]

}
