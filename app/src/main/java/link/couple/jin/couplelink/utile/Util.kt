package link.couple.jin.couplelink.utile

import android.content.Context
import android.content.SharedPreferences
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.telephony.TelephonyManager
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.leocardz.link.preview.library.Regex

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.helper.W3CDom
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.Random
import java.util.UUID
import java.util.regex.Matcher
import java.util.regex.Pattern

import jp.wasabeef.glide.transformations.CropCircleTransformation
import link.couple.jin.couplelink.R

import com.bumptech.glide.Glide.with


/**
 * 이곳저곳 쓰게될 메서드들은 모아두자
 */

class Util(internal var context: Context) {
    internal var sharedPreferences: SharedPreferences

    /**
     * 자동로그인 설정 가져옴
     * @return
     */
    /**
     * 자동로그인 설정 저장
     * @param isAuto
     */
    var isAutoLogin: Boolean
        get() = sharedPreferences.getBoolean("auto_login", false)
        set(isAuto) {
            val editor = sharedPreferences.edit()
            editor.putBoolean("auto_login", isAuto)
            editor.commit()
        }

    /**
     * 로그인 정보를 가져옴
     * @return
     */
    val loginInfo: Array<String>
        get() {
            val login_info = arrayOfNulls<String>(1)
            login_info[0] = sharedPreferences.getString("login_id", "")
            login_info[1] = sharedPreferences.getString("login_pw", "")
            return login_info
        }

    /**
     * yyyyMMdd 형식
     * @return time
     */
    val nowTime: String
        get() {
            val sdfNow = SimpleDateFormat("yyyyMMddhhmmss")
            return sdfNow.format(Date(System.currentTimeMillis()))
        }

    /**
     * yyyy-MM-dd 형식
     * @return time
     */
    val ymdTime: String
        get() {
            val sdfNow = SimpleDateFormat("yyyy-MM-dd")
            return sdfNow.format(Date(System.currentTimeMillis()))
        }

    /**
     * 디바이스 유니크 아이디 가져오기
     * @return deviceId
     */
    val devicesUUID: String
        get() {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmDevice: String
            val tmSerial: String
            val androidId: String
            tmDevice = "" + tm.deviceId
            tmSerial = "" + tm.simSerialNumber
            androidId = "" + android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
            val deviceUuid = UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode())
            return deviceUuid.toString()
        }

    /**
     * 랜덤 알파벳 3자리를 만듬 대소문자 구분없이
     * @return temp
     */
    // a-z
    // A-Z
    val randomAlphabet: String
        get() {
            val temp = StringBuffer()
            val rnd = Random()
            for (i in 0..2) {
                val rIndex = rnd.nextInt(2)
                when (rIndex) {
                    0 -> temp.append((rnd.nextInt(26) + 97).toChar())
                    1 -> temp.append((rnd.nextInt(26) + 65).toChar())
                }
            }
            return temp.toString()
        }

    init {
        sharedPreferences = context.getSharedPreferences("link", Context.MODE_PRIVATE)
    }

    /**
     * 로그인정보 저장 자동로그인이 아닐경우엔 비밀번호는 공백으로 처리 = ""
     * @param login_id
     * @param login_pw
     */
    fun setLoginInfo(login_id: String, login_pw: String) {
        val editor = sharedPreferences.edit()
        editor.putString("login_id", login_id)
        editor.putString("login_pw", login_pw)
        editor.commit()
    }

    /**
     * string.xml에 있는 리소스를 가져옴
     * @param resId
     * @return
     */
    fun getStringResources(resId: Int): String {
        return context.resources.getString(resId)
    }

    /**
     * Json을 Map형태로 변경
     * @param object
     * @return
     * @throws JSONException
     */
    @Throws(JSONException::class)
    fun toMap(`object`: JSONObject): Map<String, Any> {
        val map = HashMap<String, Any>()

        val keysItr = `object`.keys()
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            var value = `object`.get(key)

            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            map[key] = value
        }
        return map
    }

    /**
     * JsonArray를 MapList형태로 변경
     * @param array
     * @return
     * @throws JSONException
     */
    @Throws(JSONException::class)
    fun toList(array: JSONArray): List<Any> {
        val list = ArrayList<Any>()
        for (i in 0 until array.length()) {
            var value = array.get(i)
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            list.add(value)
        }
        return list
    }

    @Throws(IOException::class)
    fun getImageTag(url: String): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        val rawData = Jsoup.connect(url)
                .timeout(5000)
                .get()
        val matches = Regex.pregMatchAll(rawData.head().toString(), "<meta(.*?)>", 1)
        val var4 = matches.iterator()

        val imgs = rawData.select("img")
        if (imgs.size == 0) {
            val elements = rawData.select("frame")
            for (img in elements) {
                for (i in 0 until img.attributes().asList().size) {
                    val key = img.attributes().asList()[i].key
                    if (key.contains("src")) {
                        var s = img.attributes().asList()[i].value
                        if (!s.contains("http")) {
                            val u = URL(url)
                            s = u.protocol + "://" + u.host + s
                        }
                        return getImageTag(s)
                    }
                }
            }
            //            String reUrl = rawData.head().data().toString();
            //            reUrl = reUrl.substring(reUrl.lastIndexOf("top.location.replace(\"")+"top.location.replace(\"".length(),reUrl.indexOf("\")"));
            //            return getImageTag(reUrl);
        }
        hashMap["url"] = url
        val imageUrls = ArrayList<String>()
        while (var4.hasNext()) {
            val match = var4.next() as String
            if (match.contains("property=\"og:image\"") || match.contains("property=\'og:image\'") || match.contains("name=\"image\"") || match.contains("name=\'image\'")) {
                imageUrls.add(Regex.pregMatch(match, "content=\"(.*?)\"", 1))
            }
        }
        element@ for (img in imgs) {
            for (i in 0 until img.attributes().asList().size) {
                val key = img.attributes().asList()[i].key
                try {
                    if (key.contains("src") && (Integer.parseInt(img.attr("width")) > 100 || img.attr("width") == "")) {
                        val `val` = img.attributes().asList()[i].value
                        if (imageUrls.contains(`val`))
                            continue
                        imageUrls.add(`val`)
                    }
                } catch (e: Exception) {
                }

                if (imageUrls.size == 4) break@element
            }
        }
        hashMap["array"] = imageUrls
        return hashMap
    }

    companion object {
        /**
         * 글라이드 사용 간단하게 이미지만 불러옴
         * @param imageView
         * @param imageUrl
         */
        @BindingAdapter("bind:imageUrl")
        fun loadImage(imageView: ImageView, imageUrl: String) {
            var imageUrl = imageUrl
            if (!imageUrl.contains("https"))
                imageUrl = "http://" + imageUrl.substring(imageUrl.indexOf("//") + 2, imageUrl.length)
            Glide.with(imageView.context)
                    .load(imageUrl)
                    .apply(RequestOptions().error(R.drawable.kakaotalk_icon)) //TODO 에러아이콘 바꿔야함
                    .into(imageView)
        }

        /**
         * 글라이드 사용 Circle Crop
         * @param imageView
         * @param imageUrl
         */
        @BindingAdapter("bind:loadImageCircle")
        fun loadImageCircle(imageView: ImageView, imageUrl: String) {
            with(imageView.context)
                    .load(imageUrl)
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(imageView)
        }
    }
}
