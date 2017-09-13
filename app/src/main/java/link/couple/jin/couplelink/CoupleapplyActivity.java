package link.couple.jin.couplelink;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.data.UserClass;
import link.couple.jin.couplelink.utile.Log;

import static link.couple.jin.couplelink.utile.Constant.QUERY_COUPLE;
import static link.couple.jin.couplelink.utile.Constant.QUERY_UID;

/**
 * Created by image on 2017-08-15.
 */

public class CoupleapplyActivity extends MainClass {


    @BindView(R.id.maintop_title)
    TextView maintopTitle;
    @BindView(R.id.apply_nickname)
    TextView applyNickname;
    @BindView(R.id.apply_email)
    TextView applyEmail;
    @BindView(R.id.btn_refuse)
    Button btnRefuse;
    @BindView(R.id.btn_consent)
    Button btnConsent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_activity);
        ButterKnife.bind(this);
        getCoupleInfo();
    }

    private void getCoupleInfo() {
        showProgressDialog();
        getEmailQuery(userLogin.couple,QUERY_UID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            UserClass userClass = dataSnapshot.getValue(UserClass.class);
                            applyNickname.setText(userClass.username);
                            applyEmail.setText(userClass.email);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e( e.getMessage());
                        }
                        hideProgressDialog();
                    }
        
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(CoupleapplyActivity.this, R.string.error_connect_email, Toast.LENGTH_SHORT).show();
                        Log.e(databaseError.toString());
                        hideProgressDialog();
                    }
                }
        );
    }

    @OnClick({R.id.btn_refuse, R.id.btn_consent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refuse:
                getEmailQuery(userLogin.couple,QUERY_COUPLE).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            UserClass post = postSnapshot.getValue(UserClass.class);
                            post.couple = "";
                            postSnapshot.getRef().updateChildren(post.toMap());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.btn_consent:
                userLogin.isCouple = true;
                getEmailQuery(userLogin.uid,QUERY_UID).getRef().setValue(userLogin);
                break;
        }
    }
}
