package link.couple.jin.couplelink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import link.couple.jin.couplelink.utile.Log;

/**
 * Created by jin on 2016-11-24.
 */

public class IntroActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        MobileAds.initialize(this, getString(R.string.ads_app_id));
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.start_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e(i+"");
                Intent intent = new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Intent intent = new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

}
