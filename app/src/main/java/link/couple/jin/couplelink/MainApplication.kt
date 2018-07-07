package link.couple.jin.couplelink

import android.app.Application
import android.content.Context

import com.kakao.auth.ApprovalType
import com.kakao.auth.AuthType
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.ISessionConfig
import com.kakao.auth.KakaoAdapter
import com.kakao.auth.KakaoSDK

/**
 * Created by jeongjin-yong on 2018. 2. 26..
 */

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSDK.init(KakaoSDKAdapter())
    }

    private inner class KakaoSDKAdapter : KakaoAdapter() {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }

                override fun isSecureMode(): Boolean {
                    return false
                }

                override fun getApprovalType(): ApprovalType {
                    return ApprovalType.INDIVIDUAL
                }

                override fun isSaveFormData(): Boolean {
                    return true
                }
            }
        }

        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig { this@MainApplication }
        }
    }
}
