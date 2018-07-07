package link.couple.jin.couplelink

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

import butterknife.BindView
import butterknife.ButterKnife
import link.couple.jin.couplelink.data.UserClass
import link.couple.jin.couplelink.utile.Log

import link.couple.jin.couplelink.utile.Constant.USER_UID

/**
 * 회원가입 페이지
 */

class SignUpActivity : BaseActivity() {


    @BindView(R.id.signup_email)
    internal var signupEmail: EditText? = null
    @BindView(R.id.signup_pw)
    internal var signupPw: EditText? = null
    @BindView(R.id.signup_pwok)
    internal var signupPwok: EditText? = null
    @BindView(R.id.signup_name)
    internal var signupName: EditText? = null
    @BindView(R.id.signup_btn)
    internal var signupBtn: Button? = null
    private var isPwdconfirm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        ButterKnife.bind(this)

        signupEmail!!.setText("image_5956@naver.com")
        signupPw!!.setText("123456")
        signupPwok!!.setText("123456")
        signupName!!.setText("정진용")
        isPwdconfirm = true

        /**
         * 비밀번호 일치 검사
         * 참고 : http://cocomo.tistory.com/387
         */
        signupBtn!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password = signupPw!!.text.toString()
                val password_ok = signupPwok!!.text.toString()

                if (password == password_ok) {
                    signupPw!!.error = null
                    signupPwok!!.error = null
                    isPwdconfirm = true
                } else {
                    signupPw!!.error = util.getStringResources(R.string.error_pw_notmatch)
                    signupPwok!!.error = util.getStringResources(R.string.error_pw_notmatch)
                    isPwdconfirm = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        //[END TextChangedListener]

        /**
         * 회원가입 버튼 액션
         * 참고 : http://cocomo.tistory.com/387
         */
        signupBtn!!.setOnClickListener(View.OnClickListener {
            val email = signupEmail!!.text.toString()
            val password = signupPw!!.text.toString()
            val username = signupName!!.text.toString()

            // 이메일 입력 확인
            if (TextUtils.isEmpty(email)) {
                signupEmail!!.error = util.getStringResources(R.string.edit_email_notinput)
                signupEmail!!.requestFocus()
                return@OnClickListener
            }

            // 비밀번호 입력 확인
            if (TextUtils.isEmpty(password)) {
                signupPw!!.error = util.getStringResources(R.string.edit_pw_notinput)
                signupPw!!.requestFocus()
                return@OnClickListener
            }

            // 비밀번호 확인 입력 확인
            if (TextUtils.isEmpty(signupPwok!!.text.toString())) {
                signupPwok!!.error = util.getStringResources(R.string.edit_pw_notinput)
                signupPwok!!.requestFocus()
                return@OnClickListener
            }

            // 이름 및 닉네임 입력 확인
            if (TextUtils.isEmpty(username)) {
                signupName!!.error = util.getStringResources(R.string.edit_email_name)
                signupName!!.requestFocus()
                return@OnClickListener
            }

            // 비밀번호 일치 확인
            if (!isPwdconfirm) {
                signupPw!!.error = util.getStringResources(R.string.edit_pw_notinput)
                signupPwok!!.error = util.getStringResources(R.string.edit_pw_notinput)
                return@OnClickListener
            }

            if (signupPw!!.length() > 6) {
                Toast.makeText(this@SignUpActivity, R.string.error_pw_short, Toast.LENGTH_SHORT).show()
            }
            signUpUser(email, password, username)
        })
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
    fun signUpUser(email: String, password: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                val userModel = UserClass(email, username, "", false, false)
                getUserQuery(user.uid, USER_UID)!!.ref.setValue(userModel)
                Toast.makeText(this@SignUpActivity, R.string.toast_signup_complete, Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener(this) { e ->
            Log.e(e.message)
            /**
             * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
             */
            /**
             * 여기 에러코드를 알아보자 정녕 에러메세지로만 오류를 뿌려줘야할지 고민좀 해보자
             */
            if (e.message.contains("email"))
                Toast.makeText(this@SignUpActivity, R.string.error_email_already, Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this@SignUpActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
