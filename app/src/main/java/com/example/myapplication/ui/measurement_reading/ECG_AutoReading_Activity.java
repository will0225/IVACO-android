package com.example.myapplication.ui.measurement_reading;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.vivalnk.sdk.VitalClient;
import com.vivalnk.sdk.common.eventbus.EventBus;
import com.vivalnk.sdk.model.Motion;
import com.vivalnk.sdk.model.SampleData;

import java.util.Random;

public class ECG_AutoReading_Activity extends AppCompatActivity {

    NavController controller;
    protected Toolbar toolbar;
    private Handler handler;
    private Random random = new Random(System.currentTimeMillis());
    private long time = System.currentTimeMillis();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //construct a second sample data
            SampleData sampleData = new SampleData();
            sampleData.setTime(time);
            sampleData.setECG(new int[]{687, 303, 112, 95, 65, 27, -3, -2, -26, -64, -90, -108, -126, -143, -158, -168, -182, -178, -153, -131, -94, -24, 39, 116, 252, 418, 563, 700, 802, 860, 898, 902, 887, 855, 840, 811, 825, 809, 771, 744, 725, 707, 699, 678, 670, 679, 709, 714, 713, 727, 731, 729, 725, 705, 672, 646, 587, 488, 502, 531, 537, 629, 648, 623, 587, 548, 530, 517, 505, 631, 730, -443, -2866, -1965, 1258, 1540, 1083, 816, 431, 291, 284, 238, 201, 147, 96, 64, 35, -9, -76, -152, -227, -284, -338, -383, -398, -417, -446, -447, -443, -435, -411, -333, -227, -119, -29, 7, 10, 78, 87, 55, -4, -78, -137, -193, -245, -314, -373, -398, -418, -453, -504, -536, -573, -606, -611, -612, -610, -622});
            sampleData.setFlash(false);
            sampleData.setHR(80);
            sampleData.setRR(24);
            sampleData.setMagnification(1000);
            Motion[] motions = new Motion[5];
            for (int i = 0; i < 5; i++) {
                int x = random.nextInt(2048);
                int y = random.nextInt(2048);
                int z = random.nextInt(2048);
                motions[i] = new Motion(x, y, z);
            }
            sampleData.setACC(motions);

            EventBus.getDefault().post(sampleData);
            handler.postDelayed(this, 1000);
            time += 1000;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_auto_reading);
        initToolbar();
        controller = Navigation.findNavController(this, R.id.nav_host_fragment);
        controller.setGraph(R.navigation.nav_graph);
        handler = new Handler();
        handler.postDelayed(runnable, 500);
        VitalClient.getInstance().init(this);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.graphics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_switch_view) {
            NavDestination destination = controller.getCurrentDestination();
            int currID = destination.getId();
            if (currID == R.id.ecgFragment) {
                controller.navigate(R.id.accFragment);
            } else if (currID == R.id.accFragment) {
                controller.navigate(R.id.ecgFragment);
            } else if (id == android.R.id.home) {
                onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }
}
