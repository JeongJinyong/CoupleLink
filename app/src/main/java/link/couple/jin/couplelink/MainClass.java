package link.couple.jin.couplelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by jin on 2016-11-29.
 */

public class MainClass extends Activity implements MainInterface {

    public ProgressDialog mProgressDialog;
    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(this);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("로딩중입니다.");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void test() {

    }
    public MainClass(){

    }
}
