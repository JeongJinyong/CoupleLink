package link.couple.jin.couplelink;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_activity);
        ButterKnife.bind(this);
        applyCoupleInvite = (Button) findViewById(R.id.apply_couple_invite);
        applyCoupleApply = (Button) findViewById(R.id.apply_couple_apply);
        applyCoupleInvite.setOnClickListener(this);
        applyCoupleApply.setOnClickListener(this);
        applyEmail = (EditText) findViewById(R.id.apply_email);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == applyCoupleInvite.getId()) {
            String email = applyEmail.getText().toString();
            // 이메일 입력 확인
            if (TextUtils.isEmpty(email)) {
                applyEmail.setError(util.getStringResources(R.string.edit_email_notinput));
                applyEmail.requestFocus();
                return;
            }
        }
        if (i == applyCoupleInvite.getId()) {

        }
    }
}

