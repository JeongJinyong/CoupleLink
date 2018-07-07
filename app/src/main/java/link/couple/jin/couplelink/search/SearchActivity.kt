package link.couple.jin.couplelink.search

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import link.couple.jin.couplelink.BaseActivity
import link.couple.jin.couplelink.R
import link.couple.jin.couplelink.data.CoupleClass
import link.couple.jin.couplelink.home.HomeAdapter
import link.couple.jin.couplelink.utile.DownloadFilesTask

import link.couple.jin.couplelink.utile.Constant.TITLE

/**
 * Created by jeongjin-yong on 2018. 2. 5..
 */

class SearchActivity : BaseActivity() {

    @BindView(R.id.edit_search)
    internal var editSearch: EditText? = null
    @BindView(R.id.recycler_search)
    internal var recyclerSearch: RecyclerView? = null
    internal var coupleClasses = ArrayList<CoupleClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        ButterKnife.bind(this)
        settingList(HomeAdapter(this, coupleClasses), coupleClasses)
        recyclerSearch!!.adapter = adapter
        editSearch!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                coupleClasses.clear()
                getUserQuery(v.text.toString(), TITLE)!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            if (postSnapshot.child("title").value.toString().contains(v.text.toString())) {
                                val coupleClass = postSnapshot.getValue<CoupleClass>(CoupleClass::class.java)
                                coupleClasses.add(coupleClass)
                            }
                            val downloadFilesTask = DownloadFilesTask(this@SearchActivity)
                            downloadFilesTask.execute(coupleClasses)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        databaseError.toException().printStackTrace()
                    }
                })
            }
            false
        }

    }

    @OnClick(R.id.btn_delete)
    fun onViewClicked() {
        editSearch!!.setText("")
    }
}
