package com.example.tagflowlayout;

import android.content.Context;
import android.util.TypedValue;

public class DisplayUtil {
	 
    private DisplayUtil(){}
     

    /**
     * ׃
     * @param context
     * @param pxValue
     */
     public static int px2dip(Context context, float pxValue) {
             final float scale = context.getResources().getDisplayMetrics().density;  
             return (int) (pxValue / scale + 0.5f);
     }

     /**׃
      * @param dipValue
      * @param scale
      * @return
      */
     public static int dip2px(Context context, float dipValue) {
             final float scale = context.getResources().getDisplayMetrics().density;  
             return (int) (dipValue * scale + 0.5f);
     }

     /**
      *׃
      * @param pxValue
      * @param fontScale
      * @return
      */
     public static int px2sp(Context context, float pxValue) {
             final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
             return (int) (pxValue / fontScale + 0.5f);
     }

     /**
      * @param spValue
      * @param fontScale
      * @return
      */
     public static int sp2px(Context context, float spValue) {
             final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
             return (int) (spValue * fontScale + 0.5f);
     }
     
     public static int dp2px(int dpVal,Context context)  
 	{  
 	    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  
 	            dpVal, context.getResources().getDisplayMetrics());  
 	}  
}
