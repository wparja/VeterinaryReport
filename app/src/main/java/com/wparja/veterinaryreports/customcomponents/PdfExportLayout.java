package com.wparja.veterinaryreports.customcomponents;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.persistence.entities.ReportEntity;

public class PdfExportLayout extends LinearLayout {

    TextView mTextViewPatientName;
    ImageView mImageViewPatientPhoto;
    int w;
    int h;

    public PdfExportLayout(Context context, int w, int h) {
        super(context);
        this.w =w;
        this.h = h;
        init();
    }

    public PdfExportLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PdfExportLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.pdf_export_layout, this);
        setLayoutParams(new LinearLayout.LayoutParams(w, h));
        mTextViewPatientName = findViewById(R.id.patient_name_text_view);
        mImageViewPatientPhoto = findViewById(R.id.patient_photo_image_view);

    }

    public void bind(ReportEntity reportEntity) {
        mTextViewPatientName.setText(reportEntity.getPatientName());
        Bitmap bitmap = BitmapFactory.decodeFile(reportEntity.getPatientMainPhoto());
        mImageViewPatientPhoto.setImageBitmap(bitmap);
        mImageViewPatientPhoto.setRotation(90);
    }
}
