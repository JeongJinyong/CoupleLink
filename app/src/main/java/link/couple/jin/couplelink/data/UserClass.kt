package link.couple.jin.couplelink.data

import java.util.HashMap

/**
 * 회원가입시 데이터베이스에 이메일과 이름 저장
 */

class UserClass {

    var email: String
    var username: String
    var couple = ""
    var isCouple = false
    var isCoupleConnect = false
    var uid: String? = null
    var fcm = ""

    constructor() {}

    constructor(email: String, username: String, couple: String, isCouple: Boolean, isCoupleConnect: Boolean) {
        this.email = email
        this.username = username
        this.couple = couple
        this.isCouple = isCouple
        this.isCoupleConnect = isCoupleConnect
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["email"] = email
        result["username"] = username
        result["couple"] = couple
        result["isCouple"] = isCouple
        result["isCoupleConnect"] = isCoupleConnect
        result["fcm"] = fcm
        return result
    }

}
