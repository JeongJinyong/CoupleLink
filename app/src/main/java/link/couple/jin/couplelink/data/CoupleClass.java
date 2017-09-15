package link.couple.jin.couplelink.data;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 커플페이지에 설정 및 링크들이 들어갈 예정
 * TODO 뭐가 드렁가야할지 생각해봐야댐
 */

public class CoupleClass {

    public String link;
    public String date;
    public String category;

    public CoupleClass(){}

    public CoupleClass(@NonNull String link, @NonNull String date, @NonNull String category){
        this.link = link;
        this.date = date;
        this.category = category;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("link", link);
        result.put("date", date);
        result.put("category", category);
        return result;
    }

    /**
     *  databaseReference.getRef().child("couple").child("진용♡은이20170914104341vnv").addListenerForSingleValueEvent(
     new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
    CoupleClass coupleClass = new CoupleClass("https://www.google.com/search?q=%EB%B0%94%EB%A1%9C%EA%B8%B4%EA%B1%B0&oq=%EB%B0%94%EB%A1%9C%EA%B8%B4%EA%B1%B0&aqs=chrome..69i57j69i61l2j0l3.1668j0j7&sourceid=chrome&ie=UTF-8","2017-7-7","ㅁㅁㅁ/ㅉㅉ/ㄸㄸㄷ");
    HashMap<String, Object> result = new HashMap<>();
    result.put(dataSnapshot.getChildrenCount()+"",coupleClass.toMap());
    dataSnapshot.getRef().updateChildren(result);
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
    Log.e( databaseError.toString()+"//"+databaseError.getCode()+"//"+databaseError.getDetails()+"//"+databaseError.getMessage());
    }
    });
     */

}
