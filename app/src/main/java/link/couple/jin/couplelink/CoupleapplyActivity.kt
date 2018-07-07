package link.couple.jin.couplelink

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import link.couple.jin.couplelink.data.UserClass
import link.couple.jin.couplelink.home.HomeActivity
import link.couple.jin.couplelink.utile.Log

import link.couple.jin.couplelink.utile.Constant.USER_COUPLE

/**
 * Created by image on 2017-08-15.
 */

class CoupleapplyActivity : BaseActivity() {


    @BindView(R.id.maintop_title)
    internal var maintopTitle: TextView? = null
    @BindView(R.id.apply_nickname)
    internal var applyNickname: TextView? = null
    @BindView(R.id.apply_email)
    internal var applyEmail: TextView? = null
    @BindView(R.id.btn_refuse)
    internal var btnRefuse: Button? = null
    @BindView(R.id.btn_consent)
    internal var btnConsent: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.apply_activity)
        ButterKnife.bind(this)
        getCoupleInfo()
    }

    private fun getCoupleInfo() {
        showProgressDialog()
        getUserQuery(BaseActivity.userLogin.couple, USER_COUPLE)!!.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val post = postSnapshot.getValue<UserClass>(UserClass::class.java)
                            if (BaseActivity.userLogin.email != post.email) {
                                applyNickname!!.setText(post.username)
                                applyEmail!!.setText(post.email)
                            }
                        }
                        hideProgressDialog()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@CoupleapplyActivity, R.string.error_connect_email, Toast.LENGTH_SHORT).show()
                        Log.e(databaseError.toString())
                        hideProgressDialog()
                    }
                }
        )
    }

    @OnClick(R.id.btn_refuse, R.id.btn_consent)
    fun onViewClicked(view: View) {
        getUserQuery(BaseActivity.userLogin.couple, USER_COUPLE)!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<UserClass>(UserClass::class.java)
                    when (view.id) {
                        R.id.btn_refuse -> {
                            post.couple = ""
                            post.isCoupleConnect = false
                        }
                        R.id.btn_consent -> {
                            post.isCouple = true
                            post.isCoupleConnect = false
                        }
                    }
                    postSnapshot.ref.updateChildren(post.toMap())
                }
                val intent = Intent(this@CoupleapplyActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
