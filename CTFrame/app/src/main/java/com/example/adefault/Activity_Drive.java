package com.example.adefault;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.adefault.Interfaces.SendDataToServer;

public class Activity_Drive extends Activity {
    WebView wv;
    String fileURI = null;
    String loginId;
    String upLoadServerUri = "http://27.113.62.168:8080/index.php/insert_image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        Intent intent = getIntent();
        loginId = intent.getStringExtra("loginID");
        wv = (WebView) findViewById(R.id.wv);
        wv.loadUrl("https://drive.google.com/drive/my-drive");
        wv.setWebViewClient(new client());
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.clearCache(true);
        wv.clearHistory();

        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadingFile(url,userAgent, contentDisposition,mimetype,contentLength);
            }
        });

    }

    private void downloadingFile(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
    {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
        req.allowScanningByMediaScanner(); // Must
        req.setMimeType(mimetype); // Must
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setDescription("사진 다운로드중");
        String cookie = CookieManager.getInstance().getCookie(url);
        if (cookie != null) {
            req.addRequestHeader("cookie", cookie);
        }

        String imageFileName = "CTFrame_" + String.valueOf(System.currentTimeMillis());
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/CTFrame", imageFileName + ".jpg");
        req.setTitle(imageFileName + ".jpg");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE); // Must
        fileURI = "/storage/emulated/0/Download/CTFrame/" + imageFileName + ".jpg";
        dm.enqueue(req); // Must
        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        SendDataToServer sendImage = new SendDataToServer(upLoadServerUri);
        int serverResponseCode = sendImage.uploadFile(loginId, fileURI);

        Log.i("CTFrame",Integer.toString(serverResponseCode));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();

    }
    private class client extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}