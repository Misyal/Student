package com.example.student.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.student.R;
import com.example.student.zxing.android.CaptureActivity;

import static android.app.Activity.RESULT_OK;

public class ScanFragment extends Fragment implements View.OnClickListener {
    private Button scancode;
    private Button scanface;
    private Button sign;
    private Button ver;
    private TextView tseat, tvcourse;

    // 定位相关
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClientOption mOption;//定位属性
    private LocationClient client;//定位监听
    boolean isFirstLoc = true;// 是否首次定位


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

        //地图初始化
        mapView = view.findViewById(R.id.bmapView);
        mBaiduMap=mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);//开启图层



        //开启定位
        getLocationClientOption();


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
                    Toast.makeText(getActivity(), "你拒绝了权限申请，可能无法打开相机扫码哟！", Toast.LENGTH_SHORT).show();
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

    //开启定位
    public void getLocationClientOption(){
        mOption=new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(false);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        client = new LocationClient(getActivity().getApplicationContext());
        client.registerLocationListener(BDAblistener);
        client.setLocOption(mOption);
        client.start();
    }

    private BDAbstractLocationListener BDAblistener=new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null)
                return;
            //构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 设置地图中心点
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(new MapStatus.Builder().target(latLng)
                            .overlook(-15).rotate(180).zoom(17).build());
            mBaiduMap.setMapStatus(mapStatusUpdate);
            if (isFirstLoc) {
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                if (mBaiduMap.getLocationData()!=null)
                    if (mBaiduMap.getLocationData().latitude==location.getLatitude()
                            &&mBaiduMap.getLocationData().longitude==location.getLongitude()) {
                        isFirstLoc = false;
                    }
            }
            StringBuilder sb = new StringBuilder(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());//获取维度
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());//获取经度
            sb.append("\nradius : ");
            sb.append(location.getRadius());//获取定位精度半径，单位是米
            if (location.getLocType() == BDLocation.TypeGpsLocation){//获取error code
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
        }
    };

}


