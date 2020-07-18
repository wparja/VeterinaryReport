package com.wparja.veterinaryreports.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.utils.FileHelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

    private static final String ARG_PHOTO_FOLDER_PATH = "argPhotoFolderPath";


    private String mPhotoFolderPath;
    private RecyclerView mRecyclerViewPhoto;


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
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            try {
                File photos = FileHelper.gePhotoFolder("Temp");
                mRecyclerViewPhoto.setAdapter(new PhotoAdapter(Arrays.asList(photos.listFiles())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewPhoto;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewPhoto = (ImageView) itemView;
        }

        public void bind(File photo) {
            mImageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<File> mPhotoList;

        public PhotoAdapter(List<File> photoList) {
            mPhotoList = photoList;
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
            File photo = mPhotoList.get(position);
            holder.bind(photo);
        }

        @Override
        public int getItemCount() {
            return mPhotoList.size();
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