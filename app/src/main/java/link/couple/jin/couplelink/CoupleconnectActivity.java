package link.couple.jin.couplelink;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import link.couple.jin.couplelink.data.UserClass;
import link.couple.jin.couplelink.dialog.CoupleInvite;
import link.couple.jin.couplelink.utile.Log;

import static link.couple.jin.couplelink.utile.Constant.QUERY_EMAIL_ALL;
import static link.couple.jin.couplelink.utile.Constant.QUERY_UID;

/**
 * 커플 신청 및 커플 초대
 */

public class CoupleconnectActivity extends MainClass implements View.OnClickListener {

    @BindView(R.id.apply_email)
    EditText applyEmail;
    @BindView(R.id.apply_couple_apply)
    Button applyCoupleApply;
    @BindView(R.id.apply_couple_invite)
    Button applyCoupleInvite;
    @BindView(R.id.apply_couplenm)
    EditText applyCouplenm;

    JSONObject user_data;
    String email = "",couple_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        ButterKnife.bind(this);
        applyCoupleInvite.setOnClickListener(this);
        applyCoupleApply.setOnClickListener(this);
        applyEmail.setText("image5956@naver.com");
        applyCouplenm.setText("진용 은이");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == applyCoupleApply.getId()) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                new TedPermission(this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage(R.string.permission_phone)
                        .setDeniedMessage(R.string.permission_phone_setting)
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .check();
                return;
            }
            email = applyEmail.getText().toString();
            couple_name = applyCouplenm.getText().toString();
            if (TextUtils.isEmpty(email)) {
                applyEmail.setError(util.getStringResources(R.string.edit_email_notinput));
                applyEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(couple_name) || couple_name.length() > 10) {
                applyCouplenm.setError(util.getStringResources(R.string.edit_couplenm_notinput));
                applyCouplenm.requestFocus();
                return;
            }
            checkCouple();
        }
        if (i == applyCoupleInvite.getId()) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                new TedPermission(this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage(R.string.permission_sms)
                        .setDeniedMessage(R.string.permission_sms_setting)
                        .setPermissions(Manifest.permission.SEND_SMS)
                        .check();
            } else {
                new CoupleInvite(this).show();
            }
        }
    }

    private void checkCouple() {
        getUserQuery(userLogin.uid, QUERY_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                if (!userClass.isCoupleConnect) coupleConnect();
                else
                    Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_overlap, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void coupleConnect() {
        showProgressDialog();

        getUserQuery(email, QUERY_EMAIL_ALL).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            DataSnapshot tmp_data = dataSnapshot.getChildren().iterator().next();
                            //데이터를 어떻게 가공할지 생각해보면됨
                            UserClass userClass = tmp_data.getValue(UserClass.class);
                            Log.e(userClass.email);
                            if (userClass.email.equals(userLogin.email)) {
                                Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_this, Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                                return;
                            }
                            if (userClass.isCouple) {
                                Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_couple, Toast.LENGTH_SHORT).show();
                            } else {
                                if (userClass.couple.isEmpty()) {
                                    String couple_nm = couple_name + util.getNowTime() + util.getRandomAlphabet();
                                    userClass.couple = couple_nm;
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("/user/" + tmp_data.getKey(), userClass.toMap());
                                    databaseReference.updateChildren(childUpdates);
                                    childUpdates = new HashMap<>();
                                    userLogin.isCoupleConnect = true;
                                    userLogin.couple = couple_nm;
                                    childUpdates.put("/user/" + userLogin.uid, userLogin.toMap());
                                    databaseReference.updateChildren(childUpdates);
                                    Toast.makeText(CoupleconnectActivity.this, R.string.toast_couple_connect, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_apply, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e(e.getMessage());
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_email, Toast.LENGTH_SHORT).show();
                        Log.e(databaseError.toString());
                        hideProgressDialog();
                    }
                }
        );
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(CoupleconnectActivity.this, "다시버튼클릭하시오", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            switch (deniedPermissions.get(0)) {
                case Manifest.permission.READ_PHONE_STATE:
                    Toast.makeText(CoupleconnectActivity.this, "승낙안하면 진행못함", Toast.LENGTH_SHORT).show();
                    break;
                case Manifest.permission.SEND_SMS:
                    new CoupleInvite(CoupleconnectActivity.this).show();
                    break;
            }
        }


    };


}

