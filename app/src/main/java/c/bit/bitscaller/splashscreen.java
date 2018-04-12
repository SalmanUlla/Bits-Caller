package c.bit.bitscaller;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        //to play video
        VideoView videoView = (VideoView)findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashscreen));
        videoView.requestFocus();
        videoView.start();

        //delay to start homepage activity
        final Thread mThread = new Thread(){
            @Override
            public void run() {
                try
                {
                    sleep(2000);
                    Intent intent = new Intent(splashscreen.this,phone.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }
}

