package com.trigma.tiktok.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;
import com.trigma.tiktok.C1020R;

public class VideoPlayerActivity extends BaseActivity {
    DisplayMetrics dm;
    MediaController media_Controller;
    SurfaceView sur_View;
    String uriPath = "android.resource://com.trigma.tiktok.activity/VideoPlayerActivity/2131230721";
    VideoView video_player_view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1020R.layout.video_player);
        getInit();
    }

    public void getInit() {
        this.video_player_view = (VideoView) findViewById(C1020R.id.video_player_view);
        this.media_Controller = new MediaController(this);
        this.dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.dm);
        int height = this.dm.heightPixels;
        this.video_player_view.setMinimumWidth(this.dm.widthPixels);
        this.video_player_view.setMinimumHeight(height);
        this.video_player_view.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + C1020R.raw.video));
        this.video_player_view.setMediaController(this.media_Controller);
        this.video_player_view.start();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.media_Controller != null) {
            this.media_Controller.hide();
        }
    }
}
