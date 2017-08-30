package link.couple.jin.couplelink;

import android.os.Bundle;
import android.util.Log;
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
        databaseReference.child("user").child(userLogin.couple).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {

                            UserClass userClass = dataSnapshot.getValue(UserClass.class);
                            util.log("e",dataSnapshot.getKey());
                            applyNickname.setText(userClass.username);
                            applyEmail.setText(userClass.email);
                        } catch (Exception e) {
                            e.printStackTrace();
                            util.log("e", e.getMessage());
                        }
                        hideProgressDialog();
                    }
        
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(CoupleapplyActivity.this, R.string.error_connect_email, Toast.LENGTH_SHORT).show();
                        util.log("e", databaseError.toString());
                        hideProgressDialog();
                    }
                }
        );
    }

    @OnClick({R.id.btn_refuse, R.id.btn_consent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refuse:
                break;
            case R.id.btn_consent:
                break;
        }
    }
}
