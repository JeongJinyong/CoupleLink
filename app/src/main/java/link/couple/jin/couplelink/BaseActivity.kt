package link.couple.jin.couplelink

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipboardManager
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Base64

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.iid.FirebaseInstanceId

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.ArrayList

import link.couple.jin.couplelink.data.CoupleClass
import link.couple.jin.couplelink.data.UserClass
import link.couple.jin.couplelink.utile.Log
import link.couple.jin.couplelink.utile.Util

import com.kakao.util.helper.Utility.getPackageInfo
import link.couple.jin.couplelink.utile.Constant.COUPLE_UID
import link.couple.jin.couplelink.utile.Constant.TITLE
import link.couple.jin.couplelink.utile.Constant.USER_COUPLE
import link.couple.jin.couplelink.utile.Constant.USER_EMAIL_ALL
import link.couple.jin.couplelink.utile.Constant.USER_UID

/**
 * 엑티비티에서 공용으로 쓰는건 최대한 메인으로 빼도록 하자.
 */

open class BaseActivity : Activity(), MainInterface {

    var mProgressDialog: ProgressDialog? = null
    var util: Util

    var firebaseAuth: FirebaseAuth
    var databaseReference: DatabaseReference

    var classArrayList = ArrayList<CoupleClass>()
    var adapter: RecyclerView.Adapter<*>

    internal var refreshedToken: String? = null

    fun settingList(adapter: RecyclerView.Adapter<*>, classArrayList: ArrayList<CoupleClass>) {
        this.adapter = adapter
        this.classArrayList = classArrayList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        util = Util(this)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        refreshedToken = FirebaseInstanceId.getInstance().token
        val packageInfo = getPackageInfo(this, PackageManager.GET_SIGNATURES)
        for (signature in packageInfo.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e(Base64.encodeToString(md.digest(), Base64.NO_WRAP))
            } catch (e: NoSuchAlgorithmException) {
                Log.w("Unable to get MessageDigest. signature=$signature")
            }

        }

        val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoard.addPrimaryClipChangedListener { Log.e("asdasd") }

    }

    fun getUserQuery(str: String, type: Int): Query? {
        when (type) {
            USER_UID -> return databaseReference.child("user").child(str)
            USER_EMAIL_ALL -> return databaseReference.child("user").orderByChild("/email").equalTo(str)
            USER_COUPLE -> return databaseReference.child("user").orderByChild("/couple").equalTo(str)
            COUPLE_UID -> return databaseReference.child("couple").child(str)
            TITLE -> return databaseReference.child("couple").child(userLogin!!.couple).orderByChild("/title")
        }
        return null
    }


    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setMessage("로딩중입니다.")
            mProgressDialog!!.isIndeterminate = true
        }

        mProgressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    /**
     * 데이터 수집 후 어댑터 체인
     * @param coupleClasses
     */
    fun notifyDataSetChanged(coupleClasses: ArrayList<CoupleClass>) {
        classArrayList = coupleClasses
        adapter.notifyDataSetChanged()
    }

    override fun test() {

    }

    companion object {
        var userLogin: UserClass? = null
    }
}
