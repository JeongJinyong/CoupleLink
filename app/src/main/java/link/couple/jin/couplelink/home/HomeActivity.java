package link.couple.jin.couplelink.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.BaseActivity;
import link.couple.jin.couplelink.LoginActivity;
import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.dialog.WriteDialog;
import link.couple.jin.couplelink.service.ClipboardMonitor;
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
                    final CoupleClass coupleClass = postSnapshot.getValue(CoupleClass.class);
                    DownloadFilesTask downloadFilesTask = new DownloadFilesTask(HomeActivity.this);
                    downloadFilesTask.execute(coupleClass);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    /**
     * 데이터 수집 후 어댑터 체인
     * @param coupleClass
     */
    public void notifyDataSetChanged(CoupleClass coupleClass){
        classArrayList.add(coupleClass);
        if(classArrayList.size() == list_cnt) {
            homeAdapter.notifyDataSetChanged();
        }
    }

    private class DownloadFilesTask extends AsyncTask<CoupleClass,Void,HashMap<String,Object>> {

        HomeActivity homeActivity;

        public DownloadFilesTask(HomeActivity homeActivity){
            this.homeActivity = homeActivity;
        }

        @Override
        protected HashMap<String,Object> doInBackground(CoupleClass... params) {
            try {
                HashMap<String,Object> hashMap = util.getImageTag(params[0].link);
                hashMap.put("couple",params[0]);
                return  hashMap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new HashMap<>();
        }

        @Override
        protected void onPostExecute(final HashMap<String, Object> stringObjectHashMap) {
            final ArrayList<String> arrayList = (ArrayList<String>) stringObjectHashMap.get("array");
            TextCrawler textCrawler = new TextCrawler();
            textCrawler.makePreview(new LinkPreviewCallback() {
                @Override
                public void onPre() {}

                @Override
                public void onPos(SourceContent sourceContent, boolean isNull) {
                    if(!isNull || !sourceContent.getFinalUrl().equals("")){
                        if(!sourceContent.getImages().isEmpty()) {
                            arrayList.add(0,sourceContent.getImages().get(0));
                        }
                    }
                    CoupleClass coupleClass = (CoupleClass) stringObjectHashMap.get("couple");
                    coupleClass.imageList = arrayList;
                    homeActivity.notifyDataSetChanged(coupleClass);
                }
            }, (String) stringObjectHashMap.get("url"));
        }
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
                break;
            case R.id.btn_write:
                WriteDialog writeDialog = new WriteDialog(this);
                writeDialog.show();
                break;
        }
    }
}
