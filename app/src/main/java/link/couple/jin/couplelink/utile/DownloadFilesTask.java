package link.couple.jin.couplelink.utile;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import link.couple.jin.couplelink.BaseActivity;
import link.couple.jin.couplelink.data.CoupleClass;

/**
 * Created by jeongjin-yong on 2018. 2. 6..
 */

public class DownloadFilesTask extends AsyncTask<ArrayList<CoupleClass>,Void,ArrayList<CoupleClass>> {

    BaseActivity baseActivity;
    Util util;

    public DownloadFilesTask(BaseActivity baseActivity){
        this.baseActivity = baseActivity;
        util = new Util(baseActivity);
    }

    @Override
    protected ArrayList<CoupleClass> doInBackground(ArrayList<CoupleClass>... params) {
        try {
            ArrayList<CoupleClass> coupleClasses = params[0];
            for(int i = 0; i < coupleClasses.size(); i++) {
                HashMap<String, Object> hashMap = util.getImageTag(coupleClasses.get(i).link);
                coupleClasses.get(i).imageList = (ArrayList<String>) hashMap.get("array");
            }
            return  coupleClasses;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        baseActivity.showProgressDialog();
    }

    @Override
    protected void onPostExecute(ArrayList<CoupleClass> coupleClasses) {
        baseActivity.hideProgressDialog();
        baseActivity.notifyDataSetChanged(coupleClasses);
    }

    //    @Override
//    protected HashMap<String,Object> doInBackground(CoupleClass... params) {
//        try {
//            HashMap<String,Object> hashMap = util.getImageTag(params[0].link);
//            hashMap.put("couple",params[0]);
//            return  hashMap;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new HashMap<>();
//    }

//    @Override
//    protected void onPostExecute(final HashMap<String, Object> stringObjectHashMap) {
//        baseActivity.hideProgressDialog();
//        final ArrayList<String> arrayList = (ArrayList<String>) stringObjectHashMap.get("array");
////        TextCrawler textCrawler = new TextCrawler();
////        textCrawler.makePreview(new LinkPreviewCallback() {
////            @Override
////            public void onPre() {}
////
////            @Override
////            public void onPos(SourceContent sourceContent, boolean isNull) {
////                if(!isNull || !sourceContent.getFinalUrl().equals("")){
////                    if(!sourceContent.getImages().isEmpty()) {
////                        arrayList.add(0,sourceContent.getImages().get(0));
////                    }
////                }
////            }
////        }, (String) stringObjectHashMap.get("url"));
//
//        CoupleClass coupleClass = (CoupleClass) stringObjectHashMap.get("couple");
//        coupleClass.imageList = arrayList;
//        baseActivity.notifyDataSetChanged(coupleClass);
//    }
}
