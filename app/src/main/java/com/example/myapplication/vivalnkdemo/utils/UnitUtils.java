package com.example.myapplication.vivalnkdemo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by JakeMo on 17-11-21.
 */

public class UnitUtils {

  /**
   * 获取px值
   *
   * @param cmNUM 多少厘米
   */
  public static int getCM(Context context, float cmNUM) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    //1cm的像素点
    return (int) (cmNUM * (dm.ydpi / 2.54f));
  }

}
