package link.couple.jin.couplelink

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

import butterknife.BindView
import butterknife.ButterKnife
import link.couple.jin.couplelink.data.UserClass
import link.couple.jin.couplelink.dialog.CoupleInvite
import link.couple.jin.couplelink.utile.Log

import link.couple.jin.couplelink.utile.Constant.USER_EMAIL_ALL
import link.couple.jin.couplelink.utile.Constant.USER_UID

/**
 * 커플 신청 및 커플 초대
 */

class CoupleconnectActivity : BaseActivity(), View.OnClickListener {

    @BindView(R.id.apply_email)
    internal var applyEmail: EditText? = null
    @BindView(R.id.apply_couple_apply)
    internal var applyCoupleApply: Button? = null
    @BindView(R.id.apply_couple_invite)
    internal var applyCoupleInvite: Button? = null
    @BindView(R.id.apply_couplenm)
    internal var applyCouplenm: EditText? = null

    internal var user_data: JSONObject? = null
    internal var email = ""
    internal var couple_name = ""

    internal var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(this@CoupleconnectActivity, "다시버튼클릭하시오", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
            when (deniedPermissions[0]) {
                Manifest.permission.READ_PHONE_STATE -> Toast.makeText(this@CoupleconnectActivity, "승낙안하면 진행못함", Toast.LENGTH_SHORT).show()
                Manifest.permission.SEND_SMS -> CoupleInvite(this@CoupleconnectActivity).show()
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_activity)
        ButterKnife.bind(this)
        applyCoupleInvite!!.setOnClickListener(this)
        applyCoupleApply!!.setOnClickListener(this)
        applyEmail!!.setText("image5956@naver.com")
        applyCouplenm!!.setText("진용 은이")
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == applyCoupleApply!!.id) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                TedPermission(this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage(R.string.permission_phone)
                        .setDeniedMessage(R.string.permission_phone_setting)
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .check()
                return
            }
            email = applyEmail!!.text.toString()
            couple_name = applyCouplenm!!.text.toString()
            if (TextUtils.isEmpty(email)) {
                applyEmail!!.error = util.getStringResources(R.string.edit_email_notinput)
                applyEmail!!.requestFocus()
                return
            }
            if (TextUtils.isEmpty(couple_name) || couple_name.length > 10) {
                applyCouplenm!!.error = util.getStringResources(R.string.edit_couplenm_notinput)
                applyCouplenm!!.requestFocus()
                return
            }
            checkCouple()
        }
        if (i == applyCoupleInvite!!.id) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                TedPermission(this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage(R.string.permission_sms)
                        .setDeniedMessage(R.string.permission_sms_setting)
                        .setPermissions(Manifest.permission.SEND_SMS)
                        .check()
            } else {
                CoupleInvite(this).show()
            }
        }
    }

    private fun checkCouple() {
        getUserQuery(BaseActivity.userLogin.uid, USER_UID)!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userClass = dataSnapshot.getValue<UserClass>(UserClass::class.java)
                if (!userClass.isCoupleConnect)
                    coupleConnect()
                else
                    Toast.makeText(this@CoupleconnectActivity, R.string.error_connect_overlap, Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    private fun coupleConnect() {
        showProgressDialog()

        getUserQuery(email, USER_EMAIL_ALL)!!.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            val tmp_data = dataSnapshot.children.iterator().next()
                            //데이터를 어떻게 가공할지 생각해보면됨
                            val userClass = tmp_data.getValue<UserClass>(UserClass::class.java)
                            Log.e(userClass.email)
                            if (userClass.email == BaseActivity.userLogin.email) {
                                Toast.makeText(this@CoupleconnectActivity, R.string.error_connect_this, Toast.LENGTH_SHORT).show()
                                hideProgressDialog()
                                return
                            }
                            if (userClass.isCouple) {
                                Toast.makeText(this@CoupleconnectActivity, R.string.error_connect_couple, Toast.LENGTH_SHORT).show()
                            } else {
                                if (userClass.couple.isEmpty()) {
                                    val couple_nm = couple_name + util.nowTime + util.randomAlphabet
                                    userClass.couple = couple_nm
                                    var childUpdates: MutableMap<String, Any> = HashMap()
                                    childUpdates["/user/" + tmp_data.key] = userClass.toMap()
                                    databaseReference.updateChildren(childUpdates)
                                    childUpdates = HashMap()
                                    BaseActivity.userLogin.isCoupleConnect = true
                                    BaseActivity.userLogin.couple = couple_nm
                                    childUpdates["/user/" + BaseActivity.userLogin.uid!!] = BaseActivity.userLogin.toMap()
                                    databaseReference.updateChildren(childUpdates)
                                    Toast.makeText(this@CoupleconnectActivity, R.string.toast_couple_connect, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@CoupleconnectActivity, R.string.error_connect_apply, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(e.message)
                        }

                        hideProgressDialog()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@CoupleconnectActivity, R.string.error_connect_email, Toast.LENGTH_SHORT).show()
                        Log.e(databaseError.toString())
                        hideProgressDialog()
                    }
                }
        )
    }


}

