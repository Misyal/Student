package com.example.student.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.student.R;
import com.example.student.zxing.android.CaptureActivity;


import static android.app.Activity.RESULT_OK;

public class ScanFragment extends Fragment implements View.OnClickListener {
    private Button scancode;
    private Button scanface;
    private Button sign;
    private Button ver;
    private TextView tseat, tvcourse,tvenable;

    // 定位相关
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClient locationClient;//定位监听
    boolean isFirstLoc = true;// 是否首次定位
    private LatLng mDestion;
    private double mDistance;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //在使用百度地图SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.activity_scan, container, false);
        scancode = view.findViewById(R.id.btn_scancode);
        scancode.setOnClickListener(this);
        scanface = view.findViewById(R.id.btn_scanface);
        scanface.setOnClickListener(this);
        sign = view.findViewById(R.id.btn_sign);
        sign.setOnClickListener(this);
        ver = view.findViewById(R.id.btn_vercation);
        ver.setOnClickListener(this);
        tseat = view.findViewById(R.id.tseat);
        tvcourse = view.findViewById(R.id.tvcourse);
        tvenable=view.findViewById(R.id.enable);

        //地图初始化
        mapView = view.findViewById(R.id.bmapView);
        mBaiduMap=mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }else {
        initLocation();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_scancode) {
            //动态权限申请
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 0);
            }
        }
        if (v.getId() == R.id.btn_scanface) {
            //扫脸
        }
        if (v.getId() == R.id.btn_sign) {
            //签到
        }
        if (v.getId() == R.id.btn_vercation) {
            //上传请假图片
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(getActivity(), "你拒绝了权限申请，无法打开相机扫码！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initLocation();
                }else {
                    Toast.makeText(getActivity(),"你拒绝了定位权限，无法正常使用！",Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra("codedContent");
                //返回的BitMap图像
                //Bitmap bitmap = data.getParcelableExtra("codedBitmap");
                tseat.setText("座位是：" + content);
            }
        }
    }

    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");//返回定位结果坐标系，百度地图为ba0911
        option.setScanSpan(2000);//时间间隔 5s
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        locationClient=new LocationClient(getActivity().getApplicationContext());
        locationClient.setLocOption(option);//保存定位参数
        MyLocationListener myListener = new MyLocationListener();
        locationClient.registerLocationListener(myListener);
        locationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            if(isFirstLoc){
               LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);//更新坐标
               mBaiduMap.animateMapStatus(u);
               u=MapStatusUpdateFactory.zoomTo(18);
               mBaiduMap.animateMapStatus(u);
               //isFirstLoc=false;
            }
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
            Message message=new Message();
            message.obj=location;
            mHandler.sendMessage(message);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BDLocation location= (BDLocation) msg.obj;
            LatLng locationPoint=new LatLng(location.getLatitude(),location.getLongitude());
            mDestion=new LatLng(location.getLatitude(),location.getLongitude());
            //setCircleOptions();
            mDistance= DistanceUtil. getDistance(locationPoint,mDestion);
            if(mDistance>50){
                sign.setEnabled(false);
                tvenable.setText("不在范围内，无法签到");
                mBaiduMap.setMyLocationEnabled(true);
            }else {
                sign.setEnabled(true);
                tvenable.setText("在范围内，可以签到");
                mBaiduMap.setMyLocationEnabled(true);
            }
        }
    };

    //设置打卡目标范围圈
    private void setCircleOptions() {
        if (mDestion == null) return;
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x4057FFF8)
                .center(mDestion).stroke(new Stroke(1, 0xB6FFFFFF)).radius(50);
        mBaiduMap.addOverlay(ooCircle);
    }

}


