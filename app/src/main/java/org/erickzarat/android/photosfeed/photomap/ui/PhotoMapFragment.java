package org.erickzarat.android.photosfeed.photomap.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import org.erickzarat.android.photosfeed.PhotoFeedApp;
import org.erickzarat.android.photosfeed.R;
import org.erickzarat.android.photosfeed.domain.Util;
import org.erickzarat.android.photosfeed.entities.Photo;
import org.erickzarat.android.photosfeed.libs.base.ImageLoader;
import org.erickzarat.android.photosfeed.photomap.PhotoMapPresenter;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoMapFragment extends Fragment implements PhotoMapView, OnMapReadyCallback, GoogleMap.InfoWindowAdapter {
    @Bind(R.id.container)
    FrameLayout container;

    @Inject
    Util utils;

    @Inject
    ImageLoader imageLoader;

    @Inject
    PhotoMapPresenter presenter;

    private GoogleMap map;
    private HashMap<Marker, Photo> markers;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;

    public PhotoMapFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjection();

        markers = new HashMap<Marker, Photo>();

        presenter.onCreate();
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupInjection() {
        PhotoFeedApp app = (PhotoFeedApp) getActivity().getApplication();
        app.getPhotoMapComponent(this, this).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        presenter.subscribe();
        map.setInfoWindowAdapter(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        } else {
            map.setMyLocationEnabled(true);
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (map != null) {
                        map.setMyLocationEnabled(true);
                    }
                }
                return;
            }
        }
    }

    @Override
    public void addPhoto(Photo photo) {
        LatLng location = new LatLng(photo.getLatitutde(), photo.getLongitude());

        Marker marker = map.addMarker(new MarkerOptions().position(location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 6));
        markers.put(marker, photo);

    }

    @Override
    public void removePhoto(Photo photo) {
        for(Map.Entry<Marker, Photo> entry : markers.entrySet()) {
            Photo currentPhoto = entry.getValue();
            Marker currentMarker = entry.getKey();
            if (currentPhoto.getId().equals(photo.getId())) {
                currentMarker.remove();
                markers.remove(currentMarker);
                break;
            }
        }
    }

    @Override
    public void onPhotosError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    private void render(View view, final Photo currentPhoto) {
        CircleImageView imgAvatar = (CircleImageView)view.findViewById(R.id.imgAvatar);
        TextView txtUser = (TextView)view.findViewById(R.id.txtUser);
        final ImageView imgMain = (ImageView)view.findViewById(R.id.imgMain);

        imageLoader.load(imgMain, currentPhoto.getUrl());
        imageLoader.load(imgAvatar, utils.getAvatarUrl(currentPhoto.getEmail()));
        txtUser.setText(currentPhoto.getEmail());
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
        Photo photo = markers.get(marker);

        CircleImageView imgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);
        TextView txtUser = (TextView) view.findViewById(R.id.txtUser);
        ImageView imgMain = (ImageView) view.findViewById(R.id.imgMain);

        imageLoader.load(imgMain, photo.getUrl());
        imageLoader.load(imgAvatar, utils.getAvatarUrl(photo.getEmail()));
        txtUser.setText(photo.getEmail());

        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}