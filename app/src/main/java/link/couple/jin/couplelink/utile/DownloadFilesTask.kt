package link.couple.jin.couplelink.utile

import android.os.AsyncTask

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

import link.couple.jin.couplelink.BaseActivity
import link.couple.jin.couplelink.data.CoupleClass

/**
 * Created by jeongjin-yong on 2018. 2. 6..
 */

class DownloadFilesTask(internal var baseActivity: BaseActivity) : AsyncTask<ArrayList<CoupleClass>, Void, ArrayList<CoupleClass>>() {
    internal var util: Util

    init {
        util = Util(baseActivity)
    }

    override fun doInBackground(vararg params: ArrayList<CoupleClass>): ArrayList<CoupleClass> {
        try {
            val coupleClasses = params[0]
            for (i in coupleClasses.indices) {
                val hashMap = util.getImageTag(coupleClasses[i].link)
                coupleClasses[i].imageList = hashMap["array"] as ArrayList<String>
            }
            return coupleClasses
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ArrayList()
    }

    override fun onPreExecute() {
        super.onPreExecute()
        baseActivity.showProgressDialog()
    }

    override fun onPostExecute(coupleClasses: ArrayList<CoupleClass>) {
        baseActivity.hideProgressDialog()
        baseActivity.notifyDataSetChanged(coupleClasses)
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
