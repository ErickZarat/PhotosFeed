package org.erickzarat.android.photosfeed.main.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import org.erickzarat.android.photosfeed.PhotoFeedApp;
import org.erickzarat.android.photosfeed.photolist.ui.PhotoListFragment;
import org.erickzarat.android.photosfeed.photomap.ui.PhotoMapFragment;
import org.erickzarat.android.photosfeed.R;
import org.erickzarat.android.photosfeed.login.ui.LoginActivity;
import org.erickzarat.android.photosfeed.main.MainPresenter;
import org.erickzarat.android.photosfeed.main.ui.adapters.MainSectionsPagerAdapter;

import javax.inject.Inject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Inject
    MainPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    MainSectionsPagerAdapter adapter;

    private static final int REQUEST_PICTURE = 1;
    private static final int REQUEST_RESOLVE_ERROR = 0;
    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;

    private String photoPath;
    private PhotoFeedApp app;
    private GoogleApiClient apiClient;
    private Location lastKnownLocation;
    private boolean resolvingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        app = (PhotoFeedApp) getApplication();
        setupInjection();
        setupNavigation();
        setupGoogleAPIClient();

        presenter.onCreate();
    }

    private void setupGoogleAPIClient() {
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void setupNavigation() {
        String email = sharedPreferences.getString(app.getEmailKey(), getString(R.string.app_name));
        toolbar.setTitle(email);
        setSupportActionBar(toolbar);

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupInjection() {
        String[] titles = new String[]{getString(R.string.main_title_list),
                getString(R.string.main_title_map)};

        Fragment[] fragments = new Fragment[]{new PhotoListFragment(),
                new PhotoMapFragment()};

        app.getMainComponent(this, getSupportFragmentManager(),fragments,titles).inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        apiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) logout();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR){
            resolvingError = false;
            if (resultCode == RESULT_OK){
                if(!apiClient.isConnecting() && !apiClient.isConnected()){
                    apiClient.connect();
                }
            }
        } else if (requestCode == REQUEST_PICTURE){
            if (resultCode == RESULT_OK){
                boolean fromCamera = (data == null || data.getData() == null);

                if (fromCamera) {
                    addPicToGallery();
                } else {
                    photoPath = getRealPathFromURI(data.getData());
                }

                presenter.uploadPhoto(lastKnownLocation, photoPath);

            }
        }
    }

    private void logout() {
        presenter.logout();
        sharedPreferences.edit().clear().commit();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            }
            return;
        }
        if(LocationServices.FusedLocationApi.getLocationAvailability(apiClient).isLocationAvailable()){
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        } else {
            showSnackbar(R.string.main_error_location_notavailable);
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(LocationServices.FusedLocationApi.getLocationAvailability(apiClient).isLocationAvailable()){
                        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                    } else {
                        showSnackbar(R.string.main_error_location_notavailable);
                    }
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(resolvingError){
            return;
        }else if (connectionResult.hasResolution()){
            resolvingError = true;
            try {
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            resolvingError = true;
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), REQUEST_RESOLVE_ERROR).show();
        }
    }

    @Override
    public void onUploadInit() {
        showSnackbar(R.string.main_notice_upload_init);
    }

    @Override
    public void onUploadComplete() {
        showSnackbar(R.string.main_notice_upload_complete);
    }

    @Override
    public void onUploadError(String error) {
        showSnackbar(error);
    }

    @OnClick(R.id.fab)
    public void takePicture() {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("return-data", true);
        File photoFile = getFile();

        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                intentList = addIntentsToList(intentList, cameraIntent);
            }
        } else {
            Snackbar.make(viewPager, "Debe conceder los permisos", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            intentList = addIntentsToList(intentList, pickIntent);
        }

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    getString(R.string.main_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        if (chooserIntent != null) {
            startActivityForResult(chooserIntent, REQUEST_PICTURE);
        }
    }

    private File getFile(){
        File photoFile = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            }
            return null;
        }
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            photoPath = photoFile.getAbsolutePath();
        } catch (IOException ex) {
            showSnackbar(R.string.main_error_dispatch_camera);
        }
        return photoFile;
    }

    private List<Intent> addIntentsToList(List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    private void addPicToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            if (contentURI.toString().contains("mediakey") || contentURI.toString().contains("mediaKey")){
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(contentURI);
                    OutputStream output = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4 * 1024];
                        int read;

                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                    } finally {
                        output.close();
                        input.close();
                    }

                } catch (Exception e) {
                }
            } else {
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }

        }
        return result;
    }

    private void showSnackbar(String msg){
        Snackbar.make(viewPager, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(int strResouce){
        Snackbar.make(viewPager, getString(strResouce), Snackbar.LENGTH_SHORT).show();
    }
}
