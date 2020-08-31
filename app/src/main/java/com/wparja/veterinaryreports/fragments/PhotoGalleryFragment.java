package com.wparja.veterinaryreports.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.model.Photo;
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
        RecyclerView recyclerViewPhoto = view.findViewById(R.id.photo_recycler_view);
        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewPhoto.setHasFixedSize(true);
        recyclerViewPhoto.setItemViewCacheSize(20);
        recyclerViewPhoto.setDrawingCacheEnabled(true);
        recyclerViewPhoto.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerViewPhoto.setAdapter(mPhotoAdapter);
        return view;
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

    private static class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewPhoto;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewPhoto = (ImageView) itemView;
        }

        public void bind(Photo photo) {
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