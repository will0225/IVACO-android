package com.example.myapplication.vivalnkdemo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.vivalnkdemo.app.BaseFragment;
import com.example.myapplication.vivalnkdemo.app.Layout;
import com.example.myapplication.vivalnkdemo.widget.LiveEcgScreen;
import com.example.myapplication.vivalnkdemo.widget.RTSEcgView;
import com.vivalnk.sdk.common.eventbus.Subscribe;
import com.vivalnk.sdk.model.SampleData;

public class EcgFragment extends BaseFragment {

  RTSEcgView ecgView;

  Button btnSwitchGain;

  Button btnRevert;

  TextView tvHR;

  TextView tvRR;

  LiveEcgScreen mLiveEcgScreen;

  private volatile boolean denoisy;
  private volatile boolean revert;

  @Override
  protected Layout getLayout() {
    return Layout.createLayoutByID(R.layout.fragment_ecg_graphic);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ecgView = view.findViewById(R.id.ecgView);
    btnSwitchGain = view.findViewById(R.id.btnSwitchGain);
    btnRevert = view.findViewById(R.id.btnRevert);
    tvHR = view.findViewById(R.id.tvHR);
    tvRR = view.findViewById(R.id.tvRR);
    btnSwitchGain.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mLiveEcgScreen.switchGain();
      }
    });
    btnRevert.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        revert = !revert;
        mLiveEcgScreen.revert(revert);
        btnRevert.setText(revert ? R.string.tv_de_revert : R.string.tv_revert);
      }
    });
    initView();
  }

  private void initView() {
    mLiveEcgScreen = new LiveEcgScreen(getContext(), ecgView);
    mLiveEcgScreen.setDrawDirection(RTSEcgView.LEFT_IN_RIGHT_OUT);
    mLiveEcgScreen.showMarkPoint(true);
    btnRevert.setText(revert ? R.string.tv_de_revert : R.string.tv_revert);
  }

  @Subscribe
  public void onEcgDataEvent(SampleData ecgData) {
    if (!ecgData.isFlash()) {
      mLiveEcgScreen.update(ecgData);

      if (ecgData.getHR() != null) {
        tvHR.setText("HR: " + ecgData.getHR());
      }

      if (ecgData.getRR() != null && ecgData.getRR() > 0) {
        tvRR.setText("RR: " + ecgData.getRR());
      }

    }
  }

  @Override
  public void onDestroyView() {
    mLiveEcgScreen.destroy();
    super.onDestroyView();
  }
}
