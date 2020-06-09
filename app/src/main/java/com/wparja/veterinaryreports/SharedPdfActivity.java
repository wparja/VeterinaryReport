package com.wparja.veterinaryreports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wparja.veterinaryreports.customcomponents.PdfExportLayout;
import com.wparja.veterinaryreports.persistence.entities.ReportEntity;
import com.wparja.veterinaryreports.utils.PhotoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SharedPdfActivity extends AppCompatActivity {
    File mPatientMainPhotoFile;
    ImageView mPatientMainPhoto;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_pdf);

        mPatientMainPhoto = findViewById(R.id.photo);
        mTextView = findViewById(R.id.name);

    }

    @Override
    protected void onResume() {
        super.onResume();
        shared();
    }

    private void shared() {
        mPatientMainPhotoFile = PhotoUtils.getPhoto(getApplicationContext(), "mainPhoto1.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(mPatientMainPhotoFile.getPath());
        mPatientMainPhoto.setImageBitmap(bitmap);
        mPatientMainPhoto.setRotation(90);
        mTextView.setText("Sushi mocorongo");
        // Create a shiny new (but blank) PDF document in memory
        // We want it to optionally be printable, so add PrintAttributes
        // and use a PrintedPdfDocument. Simpler: new PdfDocument().
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, width, height)).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();
        PdfDocument document = new PrintedPdfDocument(this, printAttrs);
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        // create a new page from the PageInfo
        PdfDocument.Page page = document.startPage(pageInfo);

        LinearLayout pdf = findViewById(R.id.pdf);
        pdf.measure(width, height);
        pdf.layout(0,0, width, height);
        pdf.draw(page.getCanvas());
        // repaint the user's text into the page
       // mTextView.draw(page.getCanvas());
        // do final processing of the page
        document.finishPage(page);
        // Here you could add more pages in a longer doc app, but you'd have
        // to handle page-breaking yourself in e.g., write your own word processor...
        // Now write the PDF document to a file; it actually needs to be a file
        // since the Share mechanism can't accept a byte[]. though it can
        // accept a String/CharSequence. Meh.
        try {
            File pdfDirPath = new File(getFilesDir(), "pdfs");
            pdfDirPath.mkdirs();
            File file = new File(pdfDirPath, "pdfsend.pdf");
            Uri contentUri = FileProvider.getUriForFile(this, "com.wparja.veterinaryreports.fileprovider", file);
            FileOutputStream os = new FileOutputStream(file);
            document.writeTo(os);
            document.close();
            os.close();
        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
    }
}
