package link.couple.jin.couplelink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by jin on 2016-11-24.
 */

public class LoginActivity extends MainClass implements View.OnClickListener{

    private EditText login_email_edit;
    private EditText login_pw_edit;
    private CheckBox login_auto_check;
    private CheckBox login_save_check;
    private Button login_login_btn;
    private Button login_find_btn;
    private Button login_signup_btn;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        login_email_edit = (EditText)findViewById(R.id.login_email_edit);
        login_pw_edit = (EditText)findViewById(R.id.login_pw_edit);
        login_auto_check = (CheckBox)findViewById(R.id.login_auto_check);
        login_save_check = (CheckBox)findViewById(R.id.login_save_check);
        login_login_btn = (Button)findViewById(R.id.login_login_btn);
        login_find_btn = (Button)findViewById(R.id.login_find_btn);
        login_signup_btn = (Button)findViewById(R.id.login_signup_btn);

        login_login_btn.setOnClickListener(this);
        login_find_btn.setOnClickListener(this);
        login_signup_btn.setOnClickListener(this);


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
                        hideProgressDialog();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                /**
                 * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
                 */
                util.log("e",e.getMessage());
            }
        });
        // [END sign_in_with_email]
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            login_email_edit.setError(util.getStringResources(R.string.edit_email_notinput));
            login_email_edit.requestFocus();
            valid = false;
            return valid;
        } else {
            login_email_edit.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            login_pw_edit.setError(util.getStringResources(R.string.edit_pw_notinput));
            login_pw_edit.requestFocus();
            valid = false;
        } else {
            login_pw_edit.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_login_btn) {
            signIn(login_email_edit.getText().toString(), login_pw_edit.getText().toString());
        }
        if (i == R.id.login_signup_btn) {
            Intent intent = new Intent(this,SignUpActivity.class);
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
