package govpoc.nfc.govservicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReceiverActivity extends AppCompatActivity {

    BroadcastReceiver mReceiver;
    private boolean mRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra("result");
                TextView tvStatus = (TextView)findViewById(R.id.tvMessage);
                tvStatus.setText(result);
            }
        };

        registerReceiver();
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("result"));
        mRegistered = true;
    }

    @Override
    public void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    @Override
    public void onPause(){
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mRegistered = false;
    }

    private void handleIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] receivedArray =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(receivedArray != null){
                NdefMessage msg = (NdefMessage) receivedArray[0];
                NdefRecord[] records = msg.getRecords();
                String string = "";
                for(NdefRecord record : records){
                    String str = new String(record.getPayload());
                    if(string.equals(getPackageName()))
                        continue;
                    else{
                        string = str;
                        break;
                    }
                }
                SearchService.search(this,string);

            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(!mRegistered)
            registerReceiver();
        handleIntent(getIntent());
    }
}
