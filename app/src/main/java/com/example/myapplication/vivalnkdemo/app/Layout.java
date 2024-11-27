package com.example.myapplication.vivalnkdemo.app;

import android.view.View;

public class Layout {
  public final static int intType = 1;
  public final static int viewType = 2;
  public int type;
  public Object value;
  private Layout(int type, Object value) {
    this.type = type;
    this.value = value;
  }

  public static Layout createLayoutByID(int id) {
    return new Layout(intType, id);
  }

  public static Layout createLayoutByView(View view) {
    return new Layout(viewType, view);
  }
}
