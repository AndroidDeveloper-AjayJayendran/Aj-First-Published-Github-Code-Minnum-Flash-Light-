package ajay.com.ledflash;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
public class MainActivity extends AppCompatActivity {
    Button b,c;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;
    MediaPlayer mp;
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button)findViewById(R.id.button);
        c = (Button)findViewById(R.id.blink);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setLogo(R.drawable.l3);
            ab.setDisplayUseLogoEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("I Understand", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.setIcon(R.drawable.help);
            alert.show();
            return;
        }
        getCamera();
        toggleButtonImage();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setBackgroundResource(R.drawable.m4);
                BlinkFlash();
            }
        });
    }
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Failed to Open.", e.getMessage());
            }
        }
    }
    private void turnOnFlash() {
        if (!isFlashOn){
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
            toggleButtonImage();
        }
    }
    private void turnOffFlash(){
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
            // changing button/switch image
            toggleButtonImage();
        }
    }
    private void toggleButtonImage(){
        if(isFlashOn){
            b.setBackgroundResource(R.drawable.m4);
        } else{
            b.setBackgroundResource(R.drawable.bulb);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        turnOffFlash();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(hasFlash)
            turnOnFlash();
    }
    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(camera != null) {
            camera.release();
            camera = null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_about){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Oliyamin (V.1.0)\n\n"+"Build by Ajay Jayendran\nCo-founder & CTO at Pagelizt\n\n"+"Contact: tillmybest@gmail.com")
                    .setCancelable(false)
                    .setNegativeButton("WOW!!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setIcon(R.drawable.l3);
            alert.setTitle("About Oliyamin (V.1.0)");
            alert.show();
        }
        if(id == R.id.action_exit){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you  really want to close this application ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes,I do it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No,I don\'t", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setIcon(R.drawable.exit);
            alert.setTitle("Exit");
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void BlinkFlash(){
        String myString = "010101010101010101010101010101010101";
        long blinkDelay =150; //Delay in ms
        int k = myString.length();
      //  k *= 20;
        for (int i = 0; i < k; i++) {
            if (myString.charAt(i) == '0') {
                b.setBackgroundResource(R.drawable.m4);
                params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(params);
                camera.startPreview();
                isFlashOn = true;
                toggleButtonImage();
            } else {
                b.setBackgroundResource(R.drawable.bulb);
                params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
                isFlashOn = false;
                toggleButtonImage();
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
