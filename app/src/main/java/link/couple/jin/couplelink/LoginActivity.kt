package link.couple.jin.couplelink

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.ISessionCallback
import com.kakao.auth.KakaoAdapter
import com.kakao.auth.KakaoSDK
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException

import butterknife.BindView
import butterknife.ButterKnife
import link.couple.jin.couplelink.data.CoupleClass
import link.couple.jin.couplelink.data.UserClass
import link.couple.jin.couplelink.home.HomeActivity
import link.couple.jin.couplelink.utile.Log

import link.couple.jin.couplelink.utile.Constant.USER_UID

/**
 * 로그인엑티비티
 */

class LoginActivity : BaseActivity(), View.OnClickListener {

    @BindView(R.id.login_email_edit)
    internal var loginEmailEdit: EditText? = null
    @BindView(R.id.login_pw_edit)
    internal var loginPwEdit: EditText? = null
    @BindView(R.id.login_auto_check)
    internal var loginAutoCheck: CheckBox? = null
    @BindView(R.id.login_save_check)
    internal var loginSaveCheck: CheckBox? = null
    @BindView(R.id.login_login_btn)
    internal var loginLoginBtn: Button? = null
    @BindView(R.id.login_find_btn)
    internal var loginFindBtn: Button? = null
    @BindView(R.id.login_signup_btn)
    internal var loginSignupBtn: Button? = null
    private var callback: SessionCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        ButterKnife.bind(this)

        loginLoginBtn!!.setOnClickListener(this)
        loginFindBtn!!.setOnClickListener(this)
        loginSignupBtn!!.setOnClickListener(this)
        loginEmailEdit!!.setText("image5956@naver.com")
        loginPwEdit!!.setText("123456")

        // [START initialize_auth]
        firebaseAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]
        if (intent.hasExtra("url")) {
            Log.e(intent.getStringExtra("url"))
        }

        //        if(firebaseAuth.getCurrentUser() != null && util.isAutoLogin()){
        //            settingUid(firebaseAuth.getCurrentUser().getUid());
        //        }

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()
    }

    private fun signIn(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }

        showProgressDialog()
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    settingUid(task.result.user.uid)
                }.addOnFailureListener(this) { e ->
                    /**
                     * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
                     */
                    /**
                     * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
                     */
                    hideProgressDialog()
                    Log.e(e.message)
                }
        // [END sign_in_with_email]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    internal fun settingUid(uid: String) {
        showProgressDialog()
        getUserQuery(uid, USER_UID)!!.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val userClass = dataSnapshot.getValue<UserClass>(UserClass::class.java)
                        userClass.uid = dataSnapshot.key
                        if (userClass.fcm != refreshedToken) {
                            userClass.fcm = refreshedToken
                        }
                        dataSnapshot.ref.updateChildren(userClass.toMap())
                        setUserClass(userClass)

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        hideProgressDialog()
                        Log.e(databaseError.toString() + "//" + databaseError.code + "//" + databaseError.details + "//" + databaseError.message)
                    }
                })
    }

    fun setUserClass(userClass: UserClass) {
        try {
            BaseActivity.userLogin = userClass
            if (userClass.isCouple) {
                if (loginAutoCheck!!.isChecked) {
                    util.isAutoLogin = true
                } else {
                    util.isAutoLogin = false
                }
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                if (getIntent().hasExtra("url"))
                    intent.putExtra("url", getIntent().getStringExtra("url"))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                this@LoginActivity.finish()
            } else if (userClass.isCoupleConnect) {
                Toast.makeText(this@LoginActivity, R.string.toast_couple_wait, Toast.LENGTH_SHORT).show()
            } else {
                if (userClass.couple.isEmpty()) {
                    val intent = Intent(this@LoginActivity, CoupleconnectActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@LoginActivity, CoupleapplyActivity::class.java)
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            Log.e(e.message)
            e.printStackTrace()
        }

        hideProgressDialog()
    }

    private fun validateForm(email: String, password: String): Boolean {
        var valid = true

        if (TextUtils.isEmpty(email)) {
            loginEmailEdit!!.error = util.getStringResources(R.string.edit_email_notinput)
            loginEmailEdit!!.requestFocus()
            valid = false
            return valid
        } else {
            loginEmailEdit!!.error = null
        }

        if (TextUtils.isEmpty(password)) {
            loginPwEdit!!.error = util.getStringResources(R.string.edit_pw_notinput)
            loginPwEdit!!.requestFocus()
            valid = false
        } else {
            loginPwEdit!!.error = null
        }

        return valid
    }


    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.login_login_btn) {
            signIn(loginEmailEdit!!.text.toString(), loginPwEdit!!.text.toString())
        }
        if (i == R.id.login_signup_btn) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            redirectSignupActivity()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                exception.printStackTrace()
                Log.e(exception.toString())
            }
        }
    }

    protected fun redirectSignupActivity() {
        Log.e("로그인")
        requestMe()
        //        UserManagement.requestLogout(new LogoutResponseCallback() {
        //            @Override
        //            public void onCompleteLogout() {
        //                Log.e("로그아웃");
        //            }
        //        });
    }

    private fun requestMe() {
        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                val message = "failed to get user info. msg=" + errorResult!!
                Log.e(message)
            }

            override fun onSessionClosed(errorResult: ErrorResult) {}

            override fun onSuccess(userProfile: UserProfile) {
                val userClass = UserClass(userProfile.email, userProfile.nickname, "", false, false)
                if (userClass.fcm != refreshedToken) {
                    userClass.fcm = refreshedToken
                }
                getUserQuery(userProfile.id.toString() + "", USER_UID)!!.ref.setValue(userClass)
                userClass.uid = userProfile.id.toString() + ""
                setUserClass(userClass)
            }

            override fun onNotSignedUp() {}
        })
    }
}
