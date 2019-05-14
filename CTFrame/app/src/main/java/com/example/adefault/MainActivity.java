package com.example.adefault;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adefault.Adapters.GalleryImageAdapter_mainpage;
import com.example.adefault.Interfaces.IRecyclerViewClickListener;
import com.example.adefault.Interfaces.SendDataToServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String loginId = null;
    Uri photoUri,albumUri = null;
    boolean selectMode = false;
    final int REQUEST_TAKE_PHOTO = 1;
    final int REQUEST_CROP_IMAGE = 2;
    final int REQUEST_DRIVE = 3;
    String mCurrentPhotoPath;
    String upLoadServerUri = "http://27.113.62.168:8080/index.php/insert_image";
    private TextView mTextMessage;
    ArrayList<String> imageArray = new ArrayList<>();
    ArrayList<String> deleteImageArray = new ArrayList<>();
    RecyclerView recyclerView;
    GalleryImageAdapter_mainpage galleryImageAdapter;

    Intent intent;
    ImageView btn_cancel,btn_delete;
    ArrayList<ImageView> imageViews = new ArrayList<>();

    GridView gridView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mypage:
                    //mTextMessage.setText("Mypage");
                    intent = new Intent(MainActivity.this, Activity_MyPage.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_pixabay:
                    //mTextMessage.setText(R.string.title_pixabay);
                    intent = new Intent(MainActivity.this, Activity_Pixabay.class);
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

        Intent intent = getIntent();

        //로그인 정보 가져오기
        MainActivity.loginId = intent.getStringExtra("email");
        Init();
        btn_cancel = (ImageView)findViewById(R.id.btn_cancel);
        btn_delete = (ImageView)findViewById(R.id.btn_delete);
        btn_cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                btn_cancel.setVisibility(View.INVISIBLE);
                btn_delete.setVisibility(View.INVISIBLE);
                selectMode = false;
                deleteImageArray.clear();
                imageView_all_clear_filter();

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView_all_clear_filter();
                btn_cancel.setVisibility(View.INVISIBLE);
                btn_delete.setVisibility(View.INVISIBLE);
                msgToast("삭제되었습니다!");
                for(int i=0;i<deleteImageArray.size();++i)
                {
                    Log.i("CTFrame",deleteImageArray.get(i));
                }
                int success = send_to_server_delete_image();
                galleryImageAdapter.notifyDataSetChanged();

                Log.i("CTFrame",String.valueOf(success));
                selectMode = false;
            }
        });
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
                                        msgToast("서버 저장 완료");
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
        //gridView = (GridView)findViewById(R.id.gridView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        image_list_view();

        final IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {


            @Override
            public void onClick(View view, int position, ImageView imageView) {

                if(selectMode)
                {

                    try{
                        Log.i("CTFrame",imageView.getColorFilter().toString());
                        imageView.clearColorFilter();
                        for(int i=0;i<deleteImageArray.size();++i)
                        {
                            if(deleteImageArray.get(i).equals(imageArray.get(position)))
                            {
                                deleteImageArray.remove(i);
                            }
                        }
                    }catch (Exception e){
                        imageView.setColorFilter(Color.argb(140,0,0,255));
                        deleteImageArray.add(imageArray.get(position));
                        if(!imageViews.contains(imageView)){
                            imageViews.add(imageView);
                        }

                    }

                }
                else
                {
                    //open full screen activity with omage clicked
                    Intent i = new Intent(MainActivity.this, Activity_Fullscreen_mainpage.class);
                    i.putExtra("IMAGES", imageArray);
                    i.putExtra("POSITION", position);
                    startActivity(i);
                }

            }
            @Override
            public void onLongClick(View view, int position, ImageView imageView){
                selectMode = true;
                btn_cancel.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                imageViews.add(imageView);
                deleteImageArray.add(imageArray.get(position));
            }

        };

        // this대신 getActivity 사용
        galleryImageAdapter = new GalleryImageAdapter_mainpage(this, imageArray, listener);
        recyclerView.setAdapter(galleryImageAdapter);
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

    //*******************************************************************************************/
    // List View
    //*******************************************************************************************/
    void image_list_view()
    {
        JSONObject obj = new JSONObject();
        SendDataToServer sendDataToServer = new SendDataToServer();

        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("email" , loginId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (post_dict.length() > 0) {
            try
            {
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict),"list_view").get());
                Log.i("CTFrame","여기까지");

                try
                {
                    JSONArray imgArray = obj.getJSONArray("responseMsg");
                    for(int i=0;i<imgArray.length();i++)
                    {
                        JSONObject tempobj = imgArray.getJSONObject(i);
                        String picURL = tempobj.getString("pic");
                        imageArray.add("http://"+picURL);
                    }
                }
                catch (JSONException e)
                {
                    Log.i("CTFrame", "JSONError : " + e.toString());
                }
            }
            catch (Exception e)
            {
                Log.i("CTFrame",e.toString());
            }
        }

    }
    //*******************************************************************************************/
    // Delete_image_to_server Function
    //*******************************************************************************************/
    int send_to_server_delete_image()
    {
        int responseMsg=0;
        JSONObject obj = new JSONObject();
        SendDataToServer sendDataToServer = new SendDataToServer();

        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("email", loginId);
            post_dict.put("image",deleteImageArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (post_dict.length() > 0) {
            try {
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict), "delete_image").get());
                Log.i("CTFrame", "여기까지");

                responseMsg = obj.getInt("responseMsg");

            } catch (Exception e) {
                Log.i("CTFrame", e.toString());
            }
        }
        return responseMsg;
    }
    //*******************************************************************************************/
    // ImageView Filter all clear function
    //*******************************************************************************************/
    void imageView_all_clear_filter()
    {
        try{
            for(ImageView iv : imageViews)
            {
                iv.clearColorFilter();
            }
        }catch (Exception e)
        {

        }
    }
}