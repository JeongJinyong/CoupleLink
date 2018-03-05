package link.couple.jin.couplelink.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.BaseActivity;
import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.home.HomeAdapter;
import link.couple.jin.couplelink.utile.DownloadFilesTask;

import static link.couple.jin.couplelink.utile.Constant.TITLE;

/**
 * Created by jeongjin-yong on 2018. 2. 5..
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.recycler_search)
    RecyclerView recyclerSearch;
    ArrayList<CoupleClass> coupleClasses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        settingList(new HomeAdapter(this,coupleClasses),coupleClasses);
        recyclerSearch.setAdapter(adapter);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    coupleClasses.clear();
                    getUserQuery(v.getText().toString(),TITLE).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (postSnapshot.child("title").getValue().toString().contains(v.getText().toString())) {
                                    CoupleClass coupleClass = postSnapshot.getValue(CoupleClass.class);
                                    coupleClasses.add(coupleClass);
                                }
                                DownloadFilesTask downloadFilesTask = new DownloadFilesTask(SearchActivity.this);
                                downloadFilesTask.execute(coupleClasses);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            databaseError.toException().printStackTrace();
                        }
                    });
                }
                return false;
            }
        });

    }

    @OnClick(R.id.btn_delete)
    public void onViewClicked() {
        editSearch.setText("");
    }
}
