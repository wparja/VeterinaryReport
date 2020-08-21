package com.wparja.veterinaryreports.fragments;

import android.graphics.Bitmap;
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
import com.wparja.veterinaryreports.utils.loadGallery.ThumbnailLoaderPhoto;


import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

    private static final String ARG_PHOTO_FOLDER_PATH = "argPhotoFolderPath";


    private String mPhotoFolderPath;
    private RecyclerView mRecyclerViewPhoto;
    private ThumbnailLoaderPhoto mThumbnailLoaderPhoto;
    private Handler mHandlerResponse;
    private List<Bitmap> mBitmaps = new ArrayList<>();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPhotoAdapter = new PhotoAdapter();
        mHandlerResponse = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bitmap b = (Bitmap) msg.obj;
                mBitmaps.add(b);
                mPhotoAdapter.notifyDataSetChanged();

            }
        };
        mThumbnailLoaderPhoto = new ThumbnailLoaderPhoto(mHandlerResponse);
        mThumbnailLoaderPhoto.start();
        mThumbnailLoaderPhoto.getLooper();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mThumbnailLoaderPhoto.queueThumbnail("Temp");
                return null;
            }
        }.execute();


        if (getArguments() != null) {
            mPhotoFolderPath = getArguments().getString(ARG_PHOTO_FOLDER_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mRecyclerViewPhoto = view.findViewById(R.id.photo_recycler_view);
        mRecyclerViewPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerViewPhoto.setHasFixedSize(true);
        mRecyclerViewPhoto.setItemViewCacheSize(20);
        mRecyclerViewPhoto.setDrawingCacheEnabled(true);
        mRecyclerViewPhoto.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerViewPhoto.setAdapter(mPhotoAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailLoaderPhoto.quit();
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewPhoto;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewPhoto = (ImageView) itemView;
        }

        public void bind(Bitmap photo) {
            mImageViewPhoto.setImageBitmap(photo);
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
            Bitmap photo = mBitmaps.get(position);
            holder.bind(photo);
        }

        @Override
        public int getItemCount() {
            return mBitmaps.size();
        }
    }

    public class Photo {
        private String path;
        private boolean isSelected;

        public Photo(String path) {
            this.isSelected = false;
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}