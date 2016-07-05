package org.erickzarat.android.photosfeed.photolist.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import org.erickzarat.android.photosfeed.PhotoFeedApp;
import org.erickzarat.android.photosfeed.R;
import org.erickzarat.android.photosfeed.entities.Photo;
import org.erickzarat.android.photosfeed.photolist.PhotoListPresenter;
import org.erickzarat.android.photosfeed.photolist.ui.adapters.OnItemClickListener;
import org.erickzarat.android.photosfeed.photolist.ui.adapters.PhotoListAdapter;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoListFragment extends Fragment implements PhotoListView, OnItemClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.container)
    FrameLayout container;

    @Inject
    PhotoListPresenter presenter;
    @Inject
    PhotoListAdapter adapter;


    public PhotoListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInection();
        presenter.onCreate();
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupInection() {
        PhotoFeedApp app = (PhotoFeedApp) getActivity().getApplication();
        app.getPhotoListComponent(this, this, this).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        presenter.subscribe();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showList() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void addPhoto(Photo photo) {
        adapter.addPhoto(photo);
    }

    @Override
    public void removePhoto(Photo photo) {
        adapter.removePhoto(photo);
    }

    @Override
    public void onPhotosError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPlaceClick(Photo photo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + photo.getLatitutde() +"," + photo.getLongitude()));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onShareClick(Photo photo, ImageView img) {
        Bitmap bitmap = ((GlideBitmapDrawable)img.getDrawable()).getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null);
        Uri imageUri =  Uri.parse(path);

        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, getString(R.string.photolist_message_share)));
    }

    @Override
    public void onDeleteClick(Photo photo) {
        presenter.removePhoto(photo);
    }
}
