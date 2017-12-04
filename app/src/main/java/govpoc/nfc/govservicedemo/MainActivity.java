package govpoc.nfc.govservicedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void showSender(View view) {

        showActivity(SenderActivity.class);
    }

    public void showReceiver(View view){
        showActivity(ReceiverActivity.class);
    }

    private void showActivity(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}
