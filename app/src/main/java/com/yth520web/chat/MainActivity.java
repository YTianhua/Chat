package com.yth520web.chat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;//管理传感器
    private Vibrator vibrator;//反馈
    private final int SENSOR_SHAKE = 10;
    private static  String strs[]={"A","B","C"};
    private  static  int  pics[] = {R.mipmap.p1,R.mipmap.p2,R.mipmap.p3};
    private TextView textView;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textviewLable);
        img = (ImageView) findViewById(R.id.imageView);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sensorManager!=null){
            //第一个参数为listener，第二个为传感器类型，第三个获取传感器信息 的频率
            sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager!=null){
            sensorManager.unregisterListener(sensorEventListener);
        }

    }

    /**
     * 重力感应监听
     */
    //注册监听器
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //传感器改变时执行
            float []values = event.values;
            //分别代表xyz轴的重力加速度，
            float x = values[0];//向右为正
            float y = values[1];//向前为正
            float z = values[2];//向上为正
            Log.i("传感器：","x["+x+"],y["+"]z["+z+"]");
            //一般三个方向的重力加速度达到40就达到摇晃手机的状态
            int mendumValue = 10;//临界值
            //绝对值，重力加速度有正负
            if (Math.abs(x)>mendumValue||Math.abs(y)>mendumValue||Math.abs(z)>mendumValue){
                vibrator.vibrate(200);
                Message message = new Message();
                message.what = SENSOR_SHAKE;
                handler.sendMessage(message);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    Log.i("摇晃ing：","摇晃手机");
                    java.util.Random r =new java.util.Random();
                    int num  = Math.abs(r.nextInt())%3;
                    textView.setText(strs[num]);
                    img.setImageResource(pics[num]);
                    break;
            }
        }
    };

}
