package govpoc.nfc.govservicedemo;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.Charset;

public class SenderActivity extends AppCompatActivity implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {


    private TextView mStatus;
    private EditText mName;
    private NdefMessage mMessage;
    private NfcAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        mStatus = (TextView)findViewById(R.id.tvStatus);
        mName = (EditText) findViewById(R.id.txtName);

        Button msg = (Button)findViewById(R.id.cmdMessage);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                NdefRecord[] records = new NdefRecord[2];
                byte[] buff = name.getBytes(Charset.forName("UTF-8"));
                records[0] = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT,new byte[0],buff);
                records[1] = NdefRecord.createApplicationRecord(getPackageName());
                mMessage = new NdefMessage(records);
                mStatus.setText("Waiting for sending...");
                mName.setText("");
            }
        });

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mAdapter.setOnNdefPushCompleteCallback(this,this);
        mAdapter.setNdefPushMessageCallback(this,this);
    }

    /**
     * Called to provide a {@link NdefMessage} to push.
     * <p>
     * <p>This callback is usually made on a binder thread (not the UI thread).
     * <p>
     * <p>Called when this device is in range of another device
     * that might support NDEF push. It allows the application to
     * create the NDEF message only when it is required.
     * <p>
     * <p>NDEF push cannot occur until this method returns, so do not
     * block for too long.
     * <p>
     * <p>The Android operating system will usually show a system UI
     * on top of your activity during this time, so do not try to request
     * input from the user to complete the callback, or provide custom NDEF
     * push UI. The user probably will not see it.
     *
     * @param event {@link NfcEvent} with the {@link NfcEvent#nfcAdapter} field set
     * @return NDEF message to push, or null to not provide a message
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return mMessage;
    }


    @Override
    public void onNdefPushComplete(NfcEvent event) {
        mStatus.setText("Completed");
    }
}
