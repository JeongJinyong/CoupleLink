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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import link.couple.jin.couplelink.data.UserClass;
import link.couple.jin.couplelink.dialog.CoupleInvite;

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

    JSONObject user_data;
    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_activity);
        ButterKnife.bind(this);
        applyCoupleInvite.setOnClickListener(this);
        applyCoupleApply.setOnClickListener(this);
        applyEmail.setText("image5956@naver.com");

        databaseReference.child("user").child(util.getBase64decode(applyEmail.getText().toString())).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            UserClass userClass = dataSnapshot.getValue(UserClass.class);
                            util.log("e", dataSnapshot.getValue().toString());
                        } catch (Exception e) {
                            util.log("e", e.getMessage());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        util.log("e", databaseError.toString());
                    }
                }
        );

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == applyCoupleApply.getId()) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if(permissionCheck== PackageManager.PERMISSION_DENIED){
                new TedPermission(this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage(R.string.permission_phone)
                        .setDeniedMessage(R.string.permission_phone_setting)
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .check();
                return;
            }
            email = applyEmail.getText().toString();
            // 이메일 입력 확인
            if (TextUtils.isEmpty(email)) {
                applyEmail.setError(util.getStringResources(R.string.edit_email_notinput));
                applyEmail.requestFocus();
                return;
            }
            try {
                if(userLogin.getString("email").equals(email)){
                    //아쉽게도 본인과 연애는 구현되지 않았습니다.
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //에처처리
            }
            coupleConnect();
        }
        if (i == applyCoupleInvite.getId()) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if(permissionCheck== PackageManager.PERMISSION_DENIED){
                new TedPermission(this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage(R.string.permission_sms)
                        .setDeniedMessage(R.string.permission_sms_setting)
                        .setPermissions(Manifest.permission.SEND_SMS)
                        .check();
            }else{
                new CoupleInvite(this).show();
            }
        }
    }


    private void coupleConnect(){
        showProgressDialog();
        databaseReference.child("user").child(util.getBase64encode(email)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {

                            UserClass userClass = dataSnapshot.getValue(UserClass.class);
                            String data = dataSnapshot.getValue().toString().replace("=",":");
                            data = "{"+data.substring(1,data.length()-1).replace("{","[{").replace("}","}]")+"}";
                            util.log("e",data);
                            util.log("e",new JSONObject(data).toString());
                            Iterator i = user_data.keys();
                            boolean isCoupleApply = false;
                            JSONObject keyObject = new JSONObject();
                            String key = "";
                            while (i.hasNext()) {
                                key = i.next().toString();
                                if(user_data.getJSONObject(key).getString("email").contains(email)){
                                    keyObject = user_data.getJSONObject(key);
                                    isCoupleApply = true;
                                    break;
                                }
                            }
                            if(isCoupleApply){
                                if(keyObject.getBoolean("isCouple")){
                                    Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_couple, Toast.LENGTH_SHORT).show();
                                }else{
                                    if(keyObject.isNull("couple")){
                                        keyObject.put("couple" , util.getNowTime()+util.getDevicesUUID());
                                        util.log("e", util.toMap(keyObject).toString());
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("/user/"+key , util.toMap(keyObject));
                                        databaseReference.updateChildren(childUpdates);
                                    }else{
                                        Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_apply, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_email, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            util.log("e", e.getMessage());
                        }
                        hideProgressDialog();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        util.log("e", databaseError.toString());
                    }
                }
        );
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(CoupleconnectActivity.this,"다시버튼클릭하시오",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            switch (deniedPermissions.get(0)){
                case Manifest.permission.READ_PHONE_STATE:
                    Toast.makeText(CoupleconnectActivity.this,"승낙안하면 진행못함",Toast.LENGTH_SHORT).show();
                    break;
                case Manifest.permission.SEND_SMS:
                    new CoupleInvite(CoupleconnectActivity.this).show();
                    break;
            }
        }


    };



}

