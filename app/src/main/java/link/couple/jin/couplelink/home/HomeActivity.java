package link.couple.jin.couplelink.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.BaseActivity;
import link.couple.jin.couplelink.LoginActivity;
import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.dialog.WriteDialog;
import link.couple.jin.couplelink.search.SearchActivity;
import link.couple.jin.couplelink.service.ClipboardMonitor;
import link.couple.jin.couplelink.utile.DownloadFilesTask;
import link.couple.jin.couplelink.utile.Log;

import static link.couple.jin.couplelink.utile.Constant.COUPLE_UID;

/**
 * Created by jeongjin-yong on 2017. 9. 25..
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.home_recycler)
    RecyclerView homeRecycler;

    ArrayList<CoupleClass> classArrayList = new ArrayList<>();
    HomeAdapter homeAdapter;
    @BindView(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;
    long list_cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
        homeAdapter = new HomeAdapter(HomeActivity.this, classArrayList);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeRecycler.setAdapter(homeAdapter);
        settingList();
        settingList(homeAdapter , classArrayList);
        if(getIntent().hasExtra("url")){
            WriteDialog writeDialog = new WriteDialog(this,getIntent().getStringExtra("url"));
            writeDialog.show();
        }

        startService(new Intent(this, ClipboardMonitor.class));

        View headerView = navigationView.getHeaderView(0);
        TextView nameText = headerView.findViewById(R.id.name_text);
        nameText.setText(userLogin.username);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getGroupId()){
//                    case R.id.navigat_category:
//                        break;
                    case R.id.navigat_setting:
                        break;
                    case R.id.navigat_logout:
                        firebaseAuth.signOut();
                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 리스트 셋팅
     */
    private void settingList(){
        getUserQuery(userLogin.couple, COUPLE_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_cnt = dataSnapshot.getChildrenCount();
                classArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CoupleClass coupleClass = postSnapshot.getValue(CoupleClass.class);
                    classArrayList.add(coupleClass);
                }
                DownloadFilesTask downloadFilesTask = new DownloadFilesTask(HomeActivity.this);
                downloadFilesTask.execute(classArrayList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }



    public boolean setCoupleLink(CoupleClass coupleClass){
        getUserQuery(userLogin.couple, COUPLE_UID).getRef().child(list_cnt+"").setValue(coupleClass);
        classArrayList.add(0,coupleClass);
        homeAdapter.notifyDataSetChanged();
        return true;
    }

    @OnClick({R.id.fl_category, R.id.fl_search, R.id.btn_write})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_category:
                if (mainDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mainDrawerLayout.closeDrawer(Gravity.LEFT);
                else
                    mainDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.fl_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putParcelableArrayListExtra("list", classArrayList);
                startActivity(intent);
                break;
            case R.id.btn_write:
                WriteDialog writeDialog = new WriteDialog(this);
                writeDialog.show();
                break;
        }
    }
}
