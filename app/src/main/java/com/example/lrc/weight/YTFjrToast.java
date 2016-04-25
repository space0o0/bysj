package com.example.lrc.weight;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by space on 16/3/18.
 */
public class YTFjrToast {

    protected static Toast theYTFjrToast = null;

    private static void setMsg(Context context, String msg,int duration) {

        if (theYTFjrToast == null) {
            theYTFjrToast=Toast.makeText(context,msg,duration);
        }else
        {
            theYTFjrToast.setText(msg);
        }

        theYTFjrToast.show();
    }

    /**
     * toast默认--Toast.LENGTH_LONG
     * @param context
     * @param msg
     */
    public static void show(Context context,String msg){
        setMsg(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 可设置toast的显示时常
     * @param context
     * @param msg
     * @param duration
     */
    public static void show(Context context,String msg,int duration){
        setMsg(context,msg,duration);
    }

}
