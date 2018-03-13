package vs.com.batteryknock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {
    private TextView batteryTxt,statusTxt,chargeTxt,batlevel,temp_status;
    private int temp = 0;
    private ProgressBar mProgressBar;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public  void onReceive(Context ctxt, Intent intent) {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = ctxt.registerReceiver(null, ifilter);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText("Percentage : "+String.valueOf(level) + "%");

            //for information
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            if(isCharging == true)
            {
                chargeTxt.setText("Status : Charging");
            }
            else
            {
                chargeTxt.setText("Status : Charged");
            }
            // How are we charging?
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            if(usbCharge == true)
            {
                statusTxt.setText("Type : USB Charging");

            }
            if (acCharge == true)
            {
                statusTxt.setText("Type  : AC Charging");
            }
            //----------------------------
            batlevel.setText(String.valueOf(level) + "%");
            mProgressBar.setProgress(level);
           // for sound -----
            ringsound(level,isCharging);
            //--end
            //temp
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            temp_status.setText("Temperature : "+(float)temp/10+"*c");
            //------------
        }
    };

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        batteryTxt = (TextView)this.findViewById(R.id.level);
        chargeTxt = (TextView)this.findViewById(R.id.status1);
        statusTxt = (TextView)this.findViewById(R.id.status);
        temp_status = (TextView)this.findViewById(R.id.temp);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        batlevel = (TextView)this.findViewById(R.id.bat_level);

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }
    public void ringsound(int level,boolean isCharging)
    {
        if(isCharging == true){
        if (level ==100)
        {
            MediaPlayer ring = MediaPlayer.create(getApplicationContext(),R.raw.bep);
            ring.start();
        }}

    }
}
