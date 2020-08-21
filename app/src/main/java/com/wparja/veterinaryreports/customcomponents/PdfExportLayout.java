package com.wparja.veterinaryreports.customcomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.persistence.entities.Report;

public class PdfExportLayout extends LinearLayout {

    TextView mTextViewPatientName;
    ImageView mImageViewPatientPhoto;

    public PdfExportLayout(Context context) {
        super(context);
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
//        mTextViewPatientName = findViewById(R.id.patient_name_text_view);
//        mImageViewPatientPhoto = findViewById(R.id.patient_photo_image_view);

    }

    public void bind(Report report) {
//        mTextViewPatientName.setText(report.getPatientName());
//        Bitmap bitmap = BitmapFactory.decodeFile(report.getPatientMainPhoto());
//        mImageViewPatientPhoto.setImageBitmap(bitmap);
//        mImageViewPatientPhoto.setRotation(90);
    }
}
