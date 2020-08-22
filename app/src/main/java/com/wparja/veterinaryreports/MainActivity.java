package com.wparja.veterinaryreports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.app_name);
    }

    public void callActivity(View view) {

        Intent activity;
        if (view.getId() == R.id.new_report_card) {
            activity = new Intent(this, NewProcedureActivity.class);
        } else {
            activity = new Intent(this, ProceduresActivity.class);
        }

        startActivity(activity);
    }

//    public void export(View view) {
//
//
//        // Create a shiny new (but blank) PDF document in memory
//        // We want it to optionally be printable, so add PrintAttributes
//        // and use a PrintedPdfDocument. Simpler: new PdfDocument().
//
//        PrintAttributes printAttrs = new PrintAttributes.Builder().
//                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
//                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
//                setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, 300, 300)).
//                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
//                build();
//        PdfDocument document = new PrintedPdfDocument(this, printAttrs);
//
//        // crate a page description
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2480, 3508, 1).create();
//
//        // create a new page from the PageInfo
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        // repaint the user's text into the page
//        PdfExportLayout pdfExport = new PdfExportLayout(this);
//        ReportEntity reportEntity = new ReportEntity();
//        reportEntity.setPatientName("Sushi");
//
//        pdfExport.bind();
//        Canvas canvas = page.getCanvas();
//        Paint paint = new Paint();
//        paint.setTextSize(48f);
//        paint.setColor(Color.BLACK);
//        canvas.drawText("Novo teste", 10,200, paint);
//
//
////        Bitmap b = PictureUtils.getScaledBitmap("//android_asset/dog.jpg", 200, 200);
////        canvas.drawBitmap(b, 20,20, paint);
//
//        // do final processing of the page
//        document.finishPage(page);
//
//        // Here you could add more pages in a longer doc app, but you'd have
//        // to handle page-breaking yourself in e.g., write your own word processor...
//
//        // Now write the PDF document to a file; it actually needs to be a file
//        // since the Share mechanism can't accept a byte[]. though it can
//        // accept a String/CharSequence. Meh.
//        try {
//            File pdfDirPath = new File(getFilesDir(), "pdfs");
//            pdfDirPath.mkdirs();
//            File file = new File(pdfDirPath, "pdfsend.pdf");
//            Uri contentUri = FileProvider.getUriForFile(this, "com.wparja.veterinaryreports.fileprovider", file);
//            os = new FileOutputStream(file);
//            document.writeTo(os);
//            document.close();
//            os.close();
//
//            AssetFileDescriptor assetFileDescriptor  = getAssets().openFd("dog.jpg");
//            FileDescriptor fileDescriptor = assetFileDescriptor .getFileDescriptor();
//            InputStream initialStream = new FileInputStream(fileDescriptor);
//            Bitmap b = BitmapFactory.decodeStream(initialStream);
//            canvas.drawBitmap(b, 0,0, null);
//            byte[] buffer = new byte[initialStream.available()];
//            initialStream.read(buffer);
//
//            File targetFile = new File(pdfDirPath, "photo.jpg");
//            OutputStream outStream = new FileOutputStream(targetFile);
//            outStream.write(buffer);
//            outStream.flush();
//            outStream.close();
//
//            //shareDocument(contentUri);
//        } catch (IOException e) {
//            throw new RuntimeException("Error generating file", e);
//        }
//    }
//
//    private void shareDocument(Uri uri) {
//        mShareIntent = new Intent();
//        mShareIntent.setAction(Intent.ACTION_SEND);
//        mShareIntent.setType("application/pdf");
//        // Assuming it may go via eMail:
//        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is a PDF from PdfSend");
//        // Attach the PDf as a Uri, since Android can't take it as bytes yet.
//        mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(mShareIntent);
//        return;
//    }
}
