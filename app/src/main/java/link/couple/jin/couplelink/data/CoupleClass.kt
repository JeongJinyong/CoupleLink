package link.couple.jin.couplelink.data

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList
import java.util.HashMap

/**
 * 커플페이지에 설정 및 링크들이 들어갈 예정
 * TODO 뭐가 드렁가야할지 생각해봐야댐
 */

class CoupleClass : Parcelable {

    var link: String
    var date: String
    var category: String
    var title: String
    var imageList = ArrayList<String>()

    constructor() {}

    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    /**
     * @param link
     * @param date
     * @param category
     * @param title
     */
    constructor(link: String, date: String, category: String, title: String) {
        this.link = link
        this.date = date
        this.category = category
        this.title = title
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["link"] = link
        result["date"] = date
        result["category"] = category
        result["title"] = title
        return result
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringList(imageList)
        dest.writeString(link)
        dest.writeString(date)
        dest.writeString(category)
        dest.writeString(title)
    }

    private fun readFromParcel(`in`: Parcel) {
        `in`.readStringList(imageList)
        link = `in`.readString()
        date = `in`.readString()
        category = `in`.readString()
        title = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator {
            override fun createFromParcel(`in`: Parcel): CoupleClass {
                return CoupleClass(`in`)
            }

            override fun newArray(size: Int): Array<CoupleClass> {
                return arrayOfNulls(size)
            }
        }
    }

    /**
     * databaseReference.getRef().child("couple").child("진용♡은이20170914104341vnv").addListenerForSingleValueEvent(
     * new ValueEventListener() {
     * @Override
     * public void onDataChange(DataSnapshot dataSnapshot) {
     * CoupleClass coupleClass = new CoupleClass("https://www.google.com/search?q=%EB%B0%94%EB%A1%9C%EA%B8%B4%EA%B1%B0&oq=%EB%B0%94%EB%A1%9C%EA%B8%B4%EA%B1%B0&aqs=chrome..69i57j69i61l2j0l3.1668j0j7&sourceid=chrome&ie=UTF-8","2017-7-7","ㅁㅁㅁ/ㅉㅉ/ㄸㄸㄷ");
     * HashMap<String></String>, Object> result = new HashMap<>();
     * result.put(dataSnapshot.getChildrenCount()+"",coupleClass.toMap());
     * dataSnapshot.getRef().updateChildren(result);
     * }
     * @Override
     * public void onCancelled(DatabaseError databaseError) {
     * Log.e( databaseError.toString()+"//"+databaseError.getCode()+"//"+databaseError.getDetails()+"//"+databaseError.getMessage());
     * }
     * });
     */

}
