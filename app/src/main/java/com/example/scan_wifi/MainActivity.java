package com.example.scan_wifi;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WifiAdmin mWifiAdmin = new WifiAdmin(MainActivity.this);

        Button button_start = (Button) findViewById(R.id.start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiAdmin.openNetCard();
                final TextView tv=(TextView) findViewById(R.id.allNetWork);
                tv.setText("现在时间---请看你的手机！！");
            }
        });
        Button button_clear = (Button) findViewById(R.id.stop);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiAdmin.closeNetCard();
                final TextView tv=(TextView) findViewById(R.id.allNetWork);
                tv.setText("NO");
            }
        });




        Button button_out = (Button) findViewById(R.id.scan);
        button_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mScanResult = mWifiAdmin.getScanResult();
                final TextView tv=(TextView) findViewById(R.id.allNetWork);
                showGPSContacts();
                if(mScanResult.length()==0){
                    tv.setText("没呀");
                }else
                {
                    tv.setText("sdsd"+mScanResult);
                }
            }
        });
//        ScanResult scanResult = new ScanResult(MainActivity.this);
//        mscanResult.setText(mScanResult);
    }
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    private LocationManager lm;
    /**
     * 检测GPS、位置权限是否开启
     */

    public void showGPSContacts() {

        //得到系统的位置服务，判断GPS是否激活
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions(this, LOCATIONGPS, BAIDU_READ_PHONE_STATE);
                } else {
                    initLocationOption();//有权限，进行相应的处理
                }
            } else {
                initLocationOption();//有权限，进行相应的处理
            }
        } else {
            Toast.makeText(this, "系统检测到未开启GPS定位服务,请开启", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, PRIVATE_CODE);
        }
    }

    /**
     * Android6.0申请权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
//             requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                //如果用户取消，permissions可能为null.
                if (grantResults[0] == PERMISSION_GRANTED && grantResults.length > 0) { //有权限
                    // 获取到权限，作相应处理
                    initLocationOption();//有权限，进行相应的处理
                } else {
                    /*
                     * 无权限
                     * */

                    Toast.makeText(this, "你未开启定位权限!", Toast.LENGTH_SHORT).show();

                }
                break;
            default:
                break;
        }
    }
    public void initLocationOption(){

    }
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

}
