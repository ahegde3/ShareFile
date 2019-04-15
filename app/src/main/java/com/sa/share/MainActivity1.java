package com.sa.share;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity1 extends Activity implements SensorEventListener,ActivityCompat.OnRequestPermissionsResultCallback  {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Switch gesture;
    int PERMISSION_REQUEST_INTERNET = 0;

    View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mLayout = findViewById(R.id.activity_main1);
        gesture=(Switch)findViewById(R.id.switch1);
        gesture.setChecked(false);
        //declaring Sensor Manager and sensor type
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        onPermission();
    }
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if(!gesture.isChecked()) return;
        if (Math.abs(x) > Math.abs(y)) {
            if (x < -2) {
                //image.setImageResource(R.drawable.right);
                // textView.setText("You tilt the device right");
                Intent b=new Intent(getApplicationContext(),MainActivity_receiver.class);
                startActivity(b);
            }
            if (x > 2) {
                // image.setImageResource(R.drawable.left);
                // textView.setText("You tilt the device left");
                Intent a=new Intent(getApplicationContext(),FileChooser.class);
                startActivity(a);
            }
        } else {
            if (y < 0) {
                // image.setImageResource(R.drawable.up);
                // textView.setText("You tilt the device up");

            }
            if (y > 0) {
                // image.setImageResource(R.drawable.down);
                // textView.setText("You tilt the device down");

            }
        }
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
            // image.setImageResource(R.drawable.center);
            // textView.setText("Not tilt device");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensor,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_INTERNET) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(this,"Internet_permission_granted",Toast.LENGTH_SHORT).show();
                //startCamera();
            } else {
                // Permission request was denied.
                Toast.makeText(this,"Internet_permission_denied",Toast.LENGTH_SHORT).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }


   public void onPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Toast.makeText(this,"Interet_Permission_granted",Toast.LENGTH_SHORT).show();
            //startCamera();
        } else {
            // Permission is missing and must be requested.
            requestInternetPermission();
        }

        }

    private void requestInternetPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Internet access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity1.this,
                            new String[]{Manifest.permission.INTERNET},
                            PERMISSION_REQUEST_INTERNET);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_INTERNET);
        }
    }

   public void onSender(View view){

        Intent i = new Intent(this, FileChooser.class);
        startActivity(i);
   }

   public void onReceiver(View view){
        Intent i = new Intent(this, MainActivity_receiver.class);
        startActivity(i);
   }

}
