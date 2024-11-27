package com.example.myapplication.vivalnkdemo.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.vivalnkdemo.app.BaseFragment;
import com.example.myapplication.vivalnkdemo.app.Layout;
import com.example.myapplication.vivalnkdemo.widget.AccView;
import com.vivalnk.sdk.common.eventbus.Subscribe;
import com.vivalnk.sdk.model.SampleData;

public class AccFragment extends BaseFragment {

  AccView accView;

  @Override
  protected Layout getLayout() {
    return Layout.createLayoutByID(R.layout.fragment_acc_graphic);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    accView = view.findViewById(R.id.accView);
  }

  @Subscribe
  public void onEcgDataEvent(SampleData ecgData) {
    if (!ecgData.isFlash()) {
      accView.addAccData(ecgData.getACC());
    }
  }

}
