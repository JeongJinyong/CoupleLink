package link.couple.jin.couplelink.home

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import java.io.IOException
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import link.couple.jin.couplelink.BaseActivity
import link.couple.jin.couplelink.LoginActivity
import link.couple.jin.couplelink.R
import link.couple.jin.couplelink.data.CoupleClass
import link.couple.jin.couplelink.dialog.WriteDialog
import link.couple.jin.couplelink.search.SearchActivity
import link.couple.jin.couplelink.service.ClipboardMonitor
import link.couple.jin.couplelink.utile.DownloadFilesTask
import link.couple.jin.couplelink.utile.Log

import link.couple.jin.couplelink.utile.Constant.COUPLE_UID

/**
 * Created by jeongjin-yong on 2017. 9. 25..
 */

class HomeActivity : BaseActivity() {

    @BindView(R.id.navigation_view)
    internal var navigationView: NavigationView? = null
    @BindView(R.id.home_recycler)
    internal var homeRecycler: RecyclerView? = null

    internal var classArrayList = ArrayList<CoupleClass>()
    internal var homeAdapter: HomeAdapter
    @BindView(R.id.main_drawer_layout)
    internal var mainDrawerLayout: DrawerLayout? = null
    internal var list_cnt: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        ButterKnife.bind(this)
        homeAdapter = HomeAdapter(this@HomeActivity, classArrayList)
        homeRecycler!!.layoutManager = LinearLayoutManager(this)
        homeRecycler!!.adapter = homeAdapter
        settingList()
        settingList(homeAdapter, classArrayList)
        if (intent.hasExtra("url")) {
            val writeDialog = WriteDialog(this, intent.getStringExtra("url"))
            writeDialog.show()
        }

        startService(Intent(this, ClipboardMonitor::class.java))

        val headerView = navigationView!!.getHeaderView(0)
        val nameText = headerView.findViewById(R.id.name_text)
        nameText.setText(BaseActivity.userLogin.username)
        navigationView!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener() {
            fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.groupId) {
                //                    case R.id.navigat_category:
                //                        break;
                    R.id.navigat_setting -> {
                    }
                    R.id.navigat_logout -> {
                        firebaseAuth.signOut()
                        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                return false
            }
        })
    }

    /**
     * 리스트 셋팅
     */
    private fun settingList() {
        getUserQuery(BaseActivity.userLogin.couple, COUPLE_UID)!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_cnt = dataSnapshot.childrenCount
                classArrayList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val coupleClass = postSnapshot.getValue<CoupleClass>(CoupleClass::class.java)
                    classArrayList.add(coupleClass)
                }
                val downloadFilesTask = DownloadFilesTask(this@HomeActivity)
                downloadFilesTask.execute(classArrayList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                databaseError.toException().printStackTrace()
            }
        })
    }


    fun setCoupleLink(coupleClass: CoupleClass): Boolean {
        getUserQuery(BaseActivity.userLogin.couple, COUPLE_UID)!!.ref.child(list_cnt.toString() + "").setValue(coupleClass)
        classArrayList.add(0, coupleClass)
        homeAdapter.notifyDataSetChanged()
        return true
    }

    @OnClick(R.id.fl_category, R.id.fl_search, R.id.btn_write)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.fl_category -> if (mainDrawerLayout!!.isDrawerOpen(Gravity.LEFT))
                mainDrawerLayout!!.closeDrawer(Gravity.LEFT)
            else
                mainDrawerLayout!!.openDrawer(Gravity.LEFT)
            R.id.fl_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putParcelableArrayListExtra("list", classArrayList)
                startActivity(intent)
            }
            R.id.btn_write -> {
                val writeDialog = WriteDialog(this)
                writeDialog.show()
            }
        }
    }
}
