package com.wparja.veterinaryreports.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.wparja.veterinaryreports.BuildConfig;
import com.wparja.veterinaryreports.NewProcedureActivity;
import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.logging.LoggerHelper;
import com.wparja.veterinaryreports.model.Photo;
import com.wparja.veterinaryreports.utils.FileHelper;
import com.wparja.veterinaryreports.utils.loadGallery.ThumbnailLoaderPhoto;


import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

    private static final String ARG_PHOTO_FOLDER_PATH = "argPhotoFolderPath";

    private String mPhotoFolderPath;
    private ThumbnailLoaderPhoto mThumbnailLoaderPhoto;
    private List<Photo> mPhotos = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;


    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    public static PhotoGalleryFragment newInstance(String photoFolderPath) {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_FOLDER_PATH, photoFolderPath);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            mPhotoFolderPath = getArguments().getString(ARG_PHOTO_FOLDER_PATH);
        }

        mPhotoAdapter = new PhotoAdapter();
        Handler handlerResponse = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Photo newPhoto = (Photo) msg.obj;
                for (Photo photo : mPhotos) {
                    if (photo.getName().equals(newPhoto.getName())) return;
                }
                mPhotos.add(newPhoto);
                mPhotoAdapter.notifyDataSetChanged();

            }
        };
        mThumbnailLoaderPhoto = new ThumbnailLoaderPhoto(handlerResponse);
        mThumbnailLoaderPhoto.start();
        mThumbnailLoaderPhoto.getLooper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        view.findViewById(R.id.owner);
        view.findViewById(R.id.open_folder_card).setOnClickListener(v -> {
            try {
                open();
            } catch (Exception e) {
                LoggerHelper.getInstance().logError(e.getMessage());
            }
        });

        view.findViewById(R.id.new_photo_card).setOnClickListener(v ->
        {
            try {
                ((NewProcedureActivity)getActivity()).takePhoto(NewProcedureActivity.REQUEST_PHOTO);
            } catch (Exception e) {
                LoggerHelper.getInstance().logError(e.getMessage());
            }

        });

        RecyclerView recyclerViewPhoto = view.findViewById(R.id.photo_recycler_view);
        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewPhoto.setHasFixedSize(true);
        recyclerViewPhoto.setItemViewCacheSize(20);
        recyclerViewPhoto.setDrawingCacheEnabled(true);
        recyclerViewPhoto.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerViewPhoto.setAdapter(mPhotoAdapter);
        return view;
    }


    private void open() throws Exception {

        Uri selectedUri = Uri.parse(FileHelper.gePhotoFolder(mPhotoFolderPath).getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");

        if (intent.resolveActivityInfo(getActivity().getPackageManager(), 0) != null)
        {
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getActivity(), "Not file manager installed", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onResume() {
        super.onResume();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mThumbnailLoaderPhoto.queueThumbnail(mPhotoFolderPath);
                return null;
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailLoaderPhoto.quit();
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewPhoto;
        private Photo mPhoto;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewPhoto = (ImageView) itemView;
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(mPhoto.getAbsolutePath());
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            });
        }

        public void bind(Photo photo) {
            mPhoto = photo;
            mImageViewPhoto.setImageBitmap(photo.getBitmap());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        public PhotoAdapter() {
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_gallery, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
            Photo photo = mPhotos.get(position);
            holder.bind(photo);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
    }
}