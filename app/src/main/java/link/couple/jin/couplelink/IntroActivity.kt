package link.couple.jin.couplelink

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

import link.couple.jin.couplelink.utile.Log

/**
 * Created by jin on 2016-11-24.
 */

class IntroActivity : AppCompatActivity() {

    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_activity)
        MobileAds.initialize(this, getString(R.string.ads_app_id))
        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        val mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.start_ad_unit_id)
        mInterstitialAd.adListener = object : AdListener() {

            override fun onAdLoaded() {
                super.onAdLoaded()
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                }
            }

            override fun onAdFailedToLoad(i: Int) {
                super.onAdFailedToLoad(i)
                Log.e(i.toString() + "")
                val intent = Intent(this@IntroActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                val intent = Intent(this@IntroActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val adRequest = AdRequest.Builder().build()
        mInterstitialAd.loadAd(adRequest)
    }

}
