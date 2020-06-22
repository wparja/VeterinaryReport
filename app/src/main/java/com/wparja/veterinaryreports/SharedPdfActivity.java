package com.wparja.veterinaryreports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Jpeg;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.wparja.veterinaryreports.customcomponents.PdfExportLayout;
import com.wparja.veterinaryreports.persistence.entities.ReportEntity;
import com.wparja.veterinaryreports.utils.PhotoUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class SharedPdfActivity extends AppCompatActivity {

    File mPatientMainPhotoFile;
    ScrollView mLinearLayout;
    ImageView mPatientMainPhoto;

    TextView mTextViewPatientName;
    TextView mTextViewPatientOwner;
    TextView mTextViewSpecie;
    TextView mTextViewGender;
    TextView mTextViewBreed;
    TextView mTextViewAge;
    TextView mTextViewWeight;
    TextView mTextViewClinic;
    TextView mTextViewMedicalTeam;
    TextView mTextViewDiagnostics;
    TextView mTextViewAnestheticProcedurePerformed;
    TextView mTextViewAnamnesis;
    TextView mTextViewRecommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_pdf);

        mLinearLayout = findViewById(R.id.pdf);
        mPatientMainPhoto = findViewById(R.id.patient_photo_image_view);
        mTextViewPatientName = findViewById(R.id.patient_name_text_view);
        mTextViewPatientOwner = findViewById(R.id.patient_owner_text_view);
        mTextViewSpecie = findViewById(R.id.specie_text_view);
        mTextViewBreed = findViewById(R.id.breed_text_view);
        mTextViewGender = findViewById(R.id.gender_text_view);
        mTextViewAge = findViewById(R.id.age_text_view);
        mTextViewWeight = findViewById(R.id.weight_text_view);
        mTextViewClinic = findViewById(R.id.clinic_text_view);
        mTextViewMedicalTeam = findViewById(R.id.medical_team_text_view);
        mTextViewDiagnostics = findViewById(R.id.diagnostics_text_view);
        mTextViewAnestheticProcedurePerformed = findViewById(R.id.anesthetic_procedure_performed_text_view);
        mTextViewAnamnesis = findViewById(R.id.anamnesis_text_view);
        mTextViewRecommendations = findViewById(R.id.recommendations_text_view);

        mTextViewPatientName.setText("teste");
        mTextViewPatientOwner.setText("teste");
        mTextViewSpecie.setText("teste");
        mTextViewBreed.setText("teste");
        mTextViewGender.setText("teste");
        mTextViewAge.setText("teste");
        mTextViewWeight.setText("teste");
//        mTextViewClinic.setText("teste");
//        mTextViewMedicalTeam.setText("teste");
//        mTextViewDiagnostics.setText("teste");
//        mTextViewAnestheticProcedurePerformed.setText("teste");
//        mTextViewAnamnesis.setText("teste");
//        mTextViewRecommendations.setText("teste");

        mPatientMainPhotoFile = PhotoUtils.getPhoto(getApplicationContext(), "mainPhoto1.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(mPatientMainPhotoFile.getPath());
        mPatientMainPhoto.setImageBitmap(bitmap);
        mPatientMainPhoto.setRotation(90);
    }
    public static Spanned getSpannedText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

//    private void takeScreenShot() {
//
//        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Signature/");
//
//        if (!folder.exists()) {
//            boolean success = folder.mkdir();
//        }
//
//        path = folder.getAbsolutePath();
//        path = path + "/" + signature_pdf_ + System.currentTimeMillis() + ".pdf";// path where pdf will be stored
//
//        View u = findViewById(R.id.scroll);
//        NestedScrollView z = (NestedScrollView) findViewById(R.id.scroll); // parent view
//        totalHeight = z.getChildAt(0).getHeight();// parent view height
//        totalWidth = z.getChildAt(0).getWidth();// parent view width
//
//        //Save bitmap to  below path
//        String extr = Environment.getExternalStorageDirectory() + "/Signature/";
//        File file = new File(extr);
//        if (!file.exists())
//            file.mkdir();
//        String fileName = signature_img_ + ".jpg";
//        myPath = new File(extr, fileName);
//        imagesUri = myPath.getPath();
//        FileOutputStream fos = null;
//        b = getBitmapFromView(u, totalHeight, totalWidth);
//
//        try {
//            fos = new FileOutputStream(myPath);
//            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        createPdf();// create pdf after creating bitmap and saving
//    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    private void shared3() {
        PdfDocument export = new PdfDocument();

        ScrollView l = findViewById(R.id.pdf);
//        l.setDrawingCacheEnabled(true);
//        Bitmap b = Bitmap.createBitmap(l.getDrawingCache());

        Bitmap b = getBitmapFromView(l, l.getChildAt(0).getHeight(), l.getChildAt(0).getWidth());


        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2480,3508 , 1).create();
        PdfDocument.Page page = export.startPage(pageInfo);

        Bitmap bb = b.createScaledBitmap(b, 2480, 3508, true);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(bb, 0,0, null);


        export.finishPage(page);
        try {
            File pdfDirPath = new File(getFilesDir(), "pdfs");
            pdfDirPath.mkdirs();
            File file = new File(pdfDirPath, "pdfsend.pdf");
            Uri contentUri = FileProvider.getUriForFile(this, "com.wparja.veterinaryreports.fileprovider", file);
            FileOutputStream os = new FileOutputStream(file);
            export.writeTo(os);
            export.close();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating file", e);
        }
    }

    private void shared4() {

        String[] header = new String[] {"Dra Tábata Torres Megda",
                "Graduação Medicina Veterinária UFLA",
                "Residência Anestesiologia de Pequenos Animais UFMG",
                "Mestre ênfase em Anestesiologia UFMG"};

        try {
            Document document = new Document();
            File pdfDirPath = new File(getFilesDir(), "pdfs");
            File file = new File(pdfDirPath, "pdfsendiText.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.setPageSize(PageSize.A4);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            lineSeparator.setOffset(10);

            for (String s : header) {
                Paragraph paragraph = new Paragraph(s);
                paragraph.setAlignment(Element.ALIGN_RIGHT);
                document.add(paragraph);
            }

            Font headerFont = new Font();
            headerFont.setSize(20.0f);
            headerFont.setStyle(Font.BOLD);
            Chunk titleChunk = new Chunk("Relatório de Procedimento Anestésico", headerFont);
            Paragraph titleParagraph = new Paragraph(titleChunk);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(new Chunk(lineSeparator));

            PdfPTable tableAll = new PdfPTable(2);
            tableAll.setSpacingAfter(20);
            PdfPTable tableInformation = new PdfPTable(2);

            PdfPCell cellNameHeader = new PdfPCell(new Phrase("Nome"));
            cellNameHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellNameHeader);

            PdfPCell cellNameValue = new PdfPCell(new Phrase("Merriti xibiu"));
            cellNameValue.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellNameValue);

            PdfPCell cellOwnerHeader = new PdfPCell(new Phrase("Responsável"));
            cellOwnerHeader.setBackgroundColor(BaseColor.WHITE);
            tableInformation.addCell(cellOwnerHeader);

            PdfPCell cellOwnerValue = new PdfPCell(new Phrase("Tábata Popozuda"));
            cellOwnerValue.setBackgroundColor(BaseColor.WHITE);
            tableInformation.addCell(cellOwnerValue);

            PdfPCell cellSpecieHeader = new PdfPCell(new Phrase("Espécie"));
            cellSpecieHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellSpecieHeader);

            PdfPCell cellSpecieValue = new PdfPCell(new Phrase("Canina"));
            cellSpecieValue.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellSpecieValue);

            PdfPCell cellBreedHeader = new PdfPCell(new Phrase("Raça"));
            cellBreedHeader.setBackgroundColor(BaseColor.WHITE);
            tableInformation.addCell(cellBreedHeader);

            PdfPCell cellBreedValue = new PdfPCell(new Phrase("Barraqueira"));
            cellBreedValue.setBackgroundColor(BaseColor.WHITE);
            tableInformation.addCell(cellBreedValue);

            PdfPCell cellGenderHeader = new PdfPCell(new Phrase("Gênero"));
            cellGenderHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellGenderHeader);

            PdfPCell cellGenderValue = new PdfPCell(new Phrase("Fêmea"));
            cellGenderValue.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellGenderValue);

            PdfPCell cellAgeHeader = new PdfPCell(new Phrase("Idade"));
            cellAgeHeader.setBackgroundColor(BaseColor.WHITE);
            tableInformation.addCell(cellAgeHeader);

            PdfPCell cellAgeValue = new PdfPCell(new Phrase("4 anos"));
            cellAgeValue.setBackgroundColor(BaseColor.WHITE);
            tableInformation.addCell(cellAgeValue);

            PdfPCell cellWeightHeader = new PdfPCell(new Phrase("Peso"));
            cellWeightHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellWeightHeader);

            PdfPCell cellWeightValue = new PdfPCell(new Phrase("1 Tonelada"));
            cellWeightValue.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableInformation.addCell(cellWeightValue);

            PdfPCell cellInformation = new PdfPCell();
            cellInformation.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellInformation.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellInformation.addElement(tableInformation);

            tableAll.addCell(cellInformation);
            PdfPCell cellImage = new PdfPCell();
            Image image = Image.getInstance(mPatientMainPhotoFile.getAbsolutePath());
            cellImage.setImage(image);
            cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellImage.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableAll.addCell(cellImage);
            document.add(tableAll);

            Font LabelFont = new Font();
            LabelFont.setSize(12.0f);
            LabelFont.setStyle(Font.BOLD);

            Chunk clinicNameChunk = new Chunk("Clínica", LabelFont);
            Paragraph clinicNameParagraph = new Paragraph(clinicNameChunk);
            clinicNameParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(clinicNameParagraph);
            document.add(new Paragraph("Clínica veterinária"));
            document.add(new Chunk(lineSeparator));

            Chunk medicalTeamChunk = new Chunk("Equipe Médica", LabelFont);
            Paragraph medicalTeamParagraph = new Paragraph(medicalTeamChunk);
            medicalTeamParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(medicalTeamParagraph);
            document.add(new Paragraph("Wagner Patrick Arja, Tábata Torres Megda"));
            document.add(new Chunk(lineSeparator));

            Chunk diagnosticChunk = new Chunk("Diagnóstico", LabelFont);
            Paragraph diagnosticParagraph = new Paragraph(diagnosticChunk);
            diagnosticParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(diagnosticParagraph);
            document.add(new Paragraph("Limpeza de dente, castração e retirada de tumor"));
            document.add(new Chunk(lineSeparator));

            Chunk anestheticProcedurePerformedChunk = new Chunk("Procedimento anestésico realizado", LabelFont);
            Paragraph anestheticProcedurePerformedParagraph = new Paragraph(anestheticProcedurePerformedChunk);
            anestheticProcedurePerformedParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(anestheticProcedurePerformedParagraph);
            document.add(new Paragraph("Inalatória com x mg/kg, Inalatória com x mg/kg, Inalatória com x mg/kg,Inalatória com x mg/kg,Inalatória com x mg/kg Inalatória com x mg/kg, Inalatória com x mg/kg, Inalatória com x mg/kg,Inalatória com x mg/kg,Inalatória com x mg/kg"));
            document.add(new Chunk(lineSeparator));

            Chunk anamnesisChunk = new Chunk("Anamnese (fornecido pelo clínico)", LabelFont);
            Paragraph anamnesisParagraph = new Paragraph(anamnesisChunk);
            anamnesisParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(anamnesisParagraph);
            document.add(new Paragraph("Cão abatido, suspeita doença do carrapato, leish, 3 dias sem comer, deprimido e com dor na barriga"));
            document.add(new Chunk(lineSeparator));

            Chunk recommendationChunk = new Chunk("Recomendações", LabelFont);
            Paragraph recommendationParagraph = new Paragraph(recommendationChunk);
            recommendationParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(recommendationParagraph);
            document.add(new Paragraph("Repouso, alimentação 4 horas apos entrega do animal, passeio só apos 1 semana da cirugia, limitar agua por 3 dias, dipirona de 3 em 3 horas por 7 dias"));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.tabata_signature);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image signature = Image.getInstance(stream.toByteArray());
            signature.setAlignment(Image.RIGHT);
            document.add(signature);

            document.close();

            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            shareDocument(contentUri);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

        private void shareDocument(Uri uri) {
        Intent mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("application/pdf");
        // Assuming it may go via eMail:
        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is a PDF from PdfSend");
        // Attach the PDf as a Uri, since Android can't take it as bytes yet.
        mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(mShareIntent);
        }


    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

//    int MARGIN_RIGHT = 30;
//    int HEIGHT_SPACE = 80;
//    private void shared2() {
//        PdfDocument export = new PdfDocument();
//        Paint paint = new Paint();
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2480 ,3508 , 1).create();
//        PdfDocument.Page page = export.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//
//        paint.setTextSize(48f);
//        paint.setColor(Color.rgb(122,119,119));
//        String string1 = "Dra Tábata Torres Megda";
//        float x = paint.measureText(string1) + MARGIN_RIGHT;
//        float y = HEIGHT_SPACE;
//        canvas.drawText(string1, pageInfo.getPageWidth() - x, y, paint);
//
//        String string2 = "Graduação Medicina Veterinária UFLA";
//        x = paint.measureText(string2) + 10;
//        y+=HEIGHT_SPACE;
//        canvas.drawText(string2, pageInfo.getPageWidth() - x, y, paint);
//
//        String string3 = "Residência Anestesiologia de Pequenos Animais UFMG";
//        x = paint.measureText(string3) + 10;
//        y+=HEIGHT_SPACE;
//        canvas.drawText(string3, pageInfo.getPageWidth() - x, y, paint);
//
//        String string4 = "Mestre ênfase em Anestesiologia UFMG";
//        x = paint.measureText(string4) + 10;
//        y+=HEIGHT_SPACE;
//        canvas.drawText(string4, pageInfo.getPageWidth() - x, y, paint);
//
//        String string5 = "Pós-graduação Dor e Bem-estar Animal FAMESP (em andamento)";
//        x = paint.measureText(string5) + 10;
//        y+=HEIGHT_SPACE;
//        canvas.drawText(string5, pageInfo.getPageWidth() - x, y, paint);
//
//        paint.setColor(Color.BLACK);
//        String string6 = "Relatório de Procedimento Anestésico";
//        paint.setTextSize(100f);
//        paint.setTextAlign(Paint.Align.CENTER);
//        y = y + HEIGHT_SPACE + HEIGHT_SPACE;
//        canvas.drawText(string6, pageInfo.getPageWidth()/2, y, paint);
//
//        paint.setTextSize(64f);
//        paint.setColor(Color.rgb(122,119,119));
//
//        export.finishPage(page);
//        try {
//            File pdfDirPath = new File(getFilesDir(), "pdfs");
//            pdfDirPath.mkdirs();
//            File file = new File(pdfDirPath, "pdfsend.pdf");
//            Uri contentUri = FileProvider.getUriForFile(this, "com.wparja.veterinaryreports.fileprovider", file);
//            FileOutputStream os = new FileOutputStream(file);
//            export.writeTo(os);
//            export.close();
//            os.close();
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating file", e);
//        }
//    }
//
//    private void shared() {
//
//        // Create a shiny new (but blank) PDF document in memory
//        // We want it to optionally be printable, so add PrintAttributes
//        // and use a PrintedPdfDocument. Simpler: new PdfDocument().
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = 3508;
//        int width =  2480;
//        PrintAttributes printAttrs = new PrintAttributes.Builder().
//                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
//                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
//                setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, width, height)).
//                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
//                build();
//                PdfDocument document = new PrintedPdfDocument(this, printAttrs);
//        // crate a page description
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
//        // create a new page from the PageInfo
//        PdfDocument.Page page = document.startPage(pageInfo);
//        LinearLayout pdf = findViewById(R.id.pdf);
//        pdf.measure(width, height);
//        pdf.layout(0,0, width, height);
//        pdf.draw(page.getCanvas());
//        int w = pdf.getWidth();
//        int h = pdf.getHeight();
//        int r = w + h;
//
//        // repaint the user's text into the page
//       // mTextView.draw(page.getCanvas());
//        // do final processing of the page
//        document.finishPage(page);
//        // Here you could add more pages in a longer doc app, but you'd have
//        // to handle page-breaking yourself in e.g., write your own word processor...
//        // Now write the PDF document to a file; it actually needs to be a file
//        // since the Share mechanism can't accept a byte[]. though it can
//        // accept a String/CharSequence. Meh.
//        try {
//            File pdfDirPath = new File(getFilesDir(), "pdfs");
//            pdfDirPath.mkdirs();
//            File file = new File(pdfDirPath, "pdfsend.pdf");
//            Uri contentUri = FileProvider.getUriForFile(this, "com.wparja.veterinaryreports.fileprovider", file);
//            FileOutputStream os = new FileOutputStream(file);
//            document.writeTo(os);
//            document.close();
//            os.close();
//        } catch (IOException e) {
//            throw new RuntimeException("Error generating file", e);
//        }
//    }

    public void export(View view) {
        shared4();
    }
}
