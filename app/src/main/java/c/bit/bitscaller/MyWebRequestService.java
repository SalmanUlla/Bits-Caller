package c.bit.bitscaller;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import c.bit.bitscaller.IntentServiceActivity.MyWebRequestReceiver;

public class MyWebRequestService extends IntentService{

    public static final String REQUEST_STRING = "myRequest";
    public static final String RESPONSE_MESSAGE = "myResponseMessage";

    private String URL = null;
    private static final int REGISTRATION_TIMEOUT = 3*1000;
    private static final int WAIT_TIMEOUT = 300 * 1000;

    public MyWebRequestService() {
        super("MyWebRequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String requestString = intent.getStringExtra(REQUEST_STRING);
        String responseMessage = "";

        try {

            URL = requestString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpParams params = httpclient.getParams();

            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpGet httpGet = new HttpGet(URL);
            HttpResponse response = httpclient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseMessage = out.toString();
            }

            else{
                //Closes the connection.
                Log.w("HTTP1:",statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (ClientProtocolException e) {
            Log.w("HTTP2:",e );
            responseMessage = e.getMessage();
        } catch (IOException e) {
            Log.w("HTTP3:",e );
            responseMessage = e.getMessage();
        }catch (Exception e) {
            Log.w("HTTP4:",e );
            responseMessage = e.getMessage();
        }


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MyWebRequestReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);
        sendBroadcast(broadcastIntent);

    }

}