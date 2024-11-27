package com.example.myapplication.vivalnkdemo.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

/**
 * Created by JakeMo on 18-5-3.
 */
public class BaseUtils {

  public static void showToast(Context context, @StringRes int resId, int showType) {
    Toast.makeText(context, resId, showType).show();
  }

  public static void showToast(Context context, CharSequence text, int showType) {
    Toast.makeText(context, text, showType).show();
  }

  public static <T extends Activity> void navToActivity(Context context, Bundle extras,
      Class<T> clazz) {
    Intent intent = new Intent();
    if (null != extras) {
      intent.putExtras(extras);
    }
    intent.setClass(context, clazz);
    context.startActivity(intent);
  }

  public static <T extends Fragment> T createFragment(Bundle extras, Class<T> clazz) {
    T fragment = null;
    try {
      fragment = clazz.newInstance();
      if (null != extras) {
        fragment.setArguments(extras);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return fragment;
  }

}
