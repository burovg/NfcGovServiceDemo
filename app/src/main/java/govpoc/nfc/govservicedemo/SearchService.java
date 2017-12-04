package govpoc.nfc.govservicedemo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SearchService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "govpoc.nfc.govservicedemo.action.FOO";
    public static final String ACTION_BAZ = "govpoc.nfc.govservicedemo.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "govpoc.nfc.govservicedemo.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "govpoc.nfc.govservicedemo.extra.PARAM2";

    public SearchService() {
        super("SearchService");
    }

    public static void search(Context context, String data ){
        Intent intent = new Intent(context,SearchService.class);
        intent.setAction("search");
        intent.putExtra("result",data);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(action.equals("search")){
                String data = intent.getStringExtra("result");
                try{
                    String requestURL = "https://esb.gov.il/GovServiceList/VeterinaryInfo/GetDogs?searchData=" + data;
                    URL url = null;
                    url = new URL(requestURL);
                    HttpURLConnection conn = null;
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    int responseCode = 0;
                    responseCode = conn.getResponseCode();
                    if(responseCode != HttpURLConnection.HTTP_OK)
                        return;
                    String result = "false";
                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        String line = "";
                        StringBuilder sb = new StringBuilder();
                        BufferedReader reader = null;

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                        while ((line = reader.readLine()) != null) {
                            //Log.e("con",line);
                            sb.append(line);
                        }


                        Intent brd = new Intent("result");
                        brd.putExtra("result", sb.toString());
                        LocalBroadcastManager.getInstance(this).sendBroadcast(brd);
                    }
                }
                catch(Exception ex){
                    Log.e("search",ex.toString());
                }

            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
