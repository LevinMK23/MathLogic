package levin.ru.cm;

/*
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

public class HTTPwork extends Thread {
    /*String answerHTTP = "";
    MainMenuLoader ex;

    private class LongOperation extends AsyncTask<String, Void, String> {
        private LongOperation() {
        }

        protected String doInBackground(String... params) {
            Log.e("HTTPwork", "Starting");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://d7ss.com/ml/get.php");
            HttpResponse response = null;
            Log.i("MainMenuLoader", "httppost=" + httppost.getAllHeaders().toString());
            try {
                response = httpclient.execute(httppost);
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e12) {
                e12.printStackTrace();
            }
            onProgressUpdate(new Void[0]);
            if (response == null || response.getEntity() == null) {
                HTTPwork.this.ex.end("");
                Log.e("HTTPwork", "Connection Timeout");
            } else {
                HttpEntity entity = response.getEntity();
                try {
                    HTTPwork.this.answerHTTP = EntityUtils.toString(entity);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                onProgressUpdate(new Void[0]);
                Log.e("HTTPwork", "ANSW:" + HTTPwork.this.answerHTTP);
                HTTPwork.this.ex.end(HTTPwork.this.answerHTTP);
            }
            return "Executed";
        }

        protected void onPostExecute(String result) {
            Log.e("HTTPwork", "ANSW:" + HTTPwork.this.answerHTTP);
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
            Log.e("HTTPwork", "Busy..");
        }
    }

    public void run() {
        do {
        } while (System.currentTimeMillis() - System.currentTimeMillis() <= 5000);
        Log.e("HTTPwork", "Me");
    }

    public void start(MainMenuLoader ex) {
        new LongOperation().execute(new String[]{""});
        this.ex = ex;
    }*/
}
