package link.couple.jin.couplelink;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jin on 2016-12-06.
 */

public class CoupleconnectActivity extends MainClass implements View.OnClickListener {

    @BindView(R.id.apply_email)
    EditText applyEmail;
    @BindView(R.id.apply_couple_apply)
    Button applyCoupleApply;
    @BindView(R.id.apply_couple_invite)
    Button applyCoupleInvite;

    JSONObject user_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_activity);
        ButterKnife.bind(this);
        applyCoupleInvite.setOnClickListener(this);
        applyCoupleApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == applyCoupleApply.getId()) {
            final String email = applyEmail.getText().toString();
            // 이메일 입력 확인
            if (TextUtils.isEmpty(email)) {
                applyEmail.setError(util.getStringResources(R.string.edit_email_notinput));
                applyEmail.requestFocus();
                return;
            }
            showProgressDialog();
            databaseReference.child("user").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            user_data = new JSONObject(dataSnapshot.getValue().toString());
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
                            /**
                             * TODO 우선 로그인하고 넘어오면 로그인 정보를 저장해두어야 겠음 커플 신청할때 자기 이메일은 등록하지 못하도록 변경해줘야겠어요
                             *
                             */

                            if(isCoupleApply){
                                if(keyObject.getBoolean("isCouple")){
                                    Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_couple, Toast.LENGTH_SHORT).show();
                                    //커플입니다. 라고 에러처리
                                }else{
                                    if(keyObject.isNull("couple")){
                                        keyObject.put("couple" , util.getNowTime()+util.getDevicesUUID());
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("/user/"+key , util.toMap(keyObject));
                                        databaseReference.updateChildren(childUpdates);
                                    }else{
                                        Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_apply, Toast.LENGTH_SHORT).show();
                                        //커플신청이 되어있다고 확인하라고 에러처리.
                                    }
                                }
                            }else{
                                Toast.makeText(CoupleconnectActivity.this, R.string.error_connect_email, Toast.LENGTH_SHORT).show();
                                //없는 이메일이라고 에러처리
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
        if (i == applyCoupleInvite.getId()) {

        }
    }
}

