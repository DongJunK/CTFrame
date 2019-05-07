package com.example.adefault;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adefault.Adapters.GridViewAdapter;
import com.example.adefault.Interfaces.SendDataToServer;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Uri photoUri,albumUri = null;
    final int REQUEST_TAKE_PHOTO = 1;
    final int REQUEST_CROP_IMAGE = 2;
    final int REQUEST_DRIVE = 3;
    String mCurrentPhotoPath;
    String loginId = "osb1808@nate.com";
    String upLoadServerUri = "http://27.113.62.168:8080/index.php/insert_image";
    private TextView mTextMessage;

    GridView gridView;
    int icons[] = {R.drawable.background, R.drawable.ic_home_black_24dp,R.drawable.ic_home_black_24dp,R.drawable.ic_home_black_24dp,
            R.drawable.ic_home_black_24dp,R.drawable.ic_home_black_24dp,R.drawable.background,R.drawable.background
    ,R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,
            R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background
    ,R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,
            R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mypage:
                    mTextMessage.setText("Mypage");
                    break;
                case R.id.navigation_pixabay:
                    mTextMessage.setText(R.string.title_pixabay);
                    Intent intent = new Intent(MainActivity.this, Activity_Pixabay.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_googledrive:
                    intent = new Intent(MainActivity.this, Activity_Drive.class);
                    startActivityForResult(intent,REQUEST_DRIVE);
                    break;
                case R.id.navigation_mygallery:
                    makeFolder();
                    albumAction();
                    break;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //*******************************************************************************************/
    // Album to MainActivity cropImage
    //*******************************************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
        {
            if(requestCode == REQUEST_CROP_IMAGE)
            {
                File f = new File(albumUri.getPath());
                f.delete();
            }
        }
        else
        {
            switch (requestCode)
            {
                case REQUEST_TAKE_PHOTO :   //앨범에서 가져오기

                    Log.i("CTtest", "갤러리에서 사진 눌림");
                    File albumFile= null;

                    try
                    {
                        //새 file을 하나 만듬
                        albumFile = createImageFile();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if(albumFile != null)
                    {
                        albumUri = Uri.fromFile(albumFile);     //앨범 이미지 Crop한 결과는 새로운 위치 저장
                    }

                    photoUri = data.getData();  //앨범 이미지의 경f로

                    Log.i("CTFrame","크롭이미지 직전 : "+photoUri.getPath());
                    cropImage(photoUri);

                    break;
                case REQUEST_CROP_IMAGE :
                    //Bitmap photo = BitmapFactory.decodeFile(photoUri.getPath());      //비트맵으로 변환하여 imageView에 띄울떄 사용
                    //album = true;
                    //photoUri = data.getData();  //앨범 이미지의 경로

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);     //동기화
                    mediaScanIntent.setData(albumUri);      //동기화
                    this.sendBroadcast(mediaScanIntent);        //동기화

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendDataToServer sendImage = new SendDataToServer(upLoadServerUri);
                            int serverResponseCode = sendImage.uploadFile(loginId, albumUri.getPath());

                            if(serverResponseCode == 200){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        msgToast("적용되었습니다.");
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        msgToast("실패하셨습니다.");
                                    }
                                });
                            }
                        }

                    }).start();

                    break;
                case REQUEST_DRIVE:
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    albumFile= null;

                    try
                    {
                        //새 file을 하나 만듬
                        albumFile = createImageFile();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if(albumFile != null)
                    {
                        albumUri = Uri.fromFile(albumFile);     //앨범 이미지 Crop한 결과는 새로운 위치 저장
                    }

                    photoUri = Uri.parse(data.getStringExtra("URI"));  //드라이브 저장 이미지의 경로
                    Log.i("CTFrame", photoUri.getPath());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendDataToServer sendImage = new SendDataToServer(upLoadServerUri);
                            int serverResponseCode = sendImage.uploadFile(loginId, photoUri.getPath());

                            if(serverResponseCode == 200){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        msgToast("적용되었습니다.");
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        msgToast("실패하셨습니다.");
                                    }
                                });
                            }
                        }

                    }).start();

                    break;
            }

        }
    }

    //*******************************************************************************************/
    // xml connect source
    //*******************************************************************************************/

    private void Init() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(this, icons));
    }

    //*******************************************************************************************/
    // Create Temp Image File
    //*******************************************************************************************/

    private File createImageFile() throws IOException
    {

        String imageFileName = "CTFrame_" + String.valueOf(System.currentTimeMillis());
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"CTFrame/");

        File file = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = file.getAbsolutePath();
        return file;
    }

    //*******************************************************************************************/
    // cropImage
    //*******************************************************************************************/

    private void cropImage(Uri photoUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(photoUri, "image/*");

        cropIntent.putExtra("aspectX", 16);       //x비율
        cropIntent.putExtra("aspectY", 9);       //y축 비율

        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumUri);
        startActivityForResult(cropIntent, REQUEST_CROP_IMAGE);

    }

    //*******************************************************************************************/
    // MainActivity to Album Activity
    //*******************************************************************************************/

    private void albumAction() {
        Intent albumintent = new Intent(Intent.ACTION_PICK);
        albumintent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(albumintent, REQUEST_TAKE_PHOTO);
    }

    //*******************************************************************************************/
    // Make Folder CTFrame
    //*******************************************************************************************/

    private void makeFolder()
    {
        //휴대폰 내부 저장소에 DownLoad폴더에 CTFrame이라는 폴더를 만든다
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "CTFrame");
        dir.mkdir();
    }

    //*******************************************************************************************/
    // Toast Message
    //*******************************************************************************************/

    void msgToast(String msg){
        Toast.makeText(MainActivity.this, msg,
                Toast.LENGTH_SHORT).show();
    }
}
