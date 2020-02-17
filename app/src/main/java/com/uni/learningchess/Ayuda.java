package com.uni.learningchess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class Ayuda extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        pdfView = findViewById(R.id.pdfView);

        LeerPdf(0);
    }

    public void LeerPdf(int... paginas) {
        /*
        pdfView.fromUri(Uri)
        pdfView.fromFile(File)
        pdfView.fromBytes( byte[])
        pdfView.fromStream(InputStream) // stream is written to bytearray - native code cannot use Java Streams
        pdfView.fromSource(DocumentSource)
        */
        try {
            pdfView.fromAsset("instrucciones.pdf")
                    .defaultPage(0)
                    .onPageChange(null)
                    .onLoad(null)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .onPageError(null)
                    .pages(paginas) // all pages are displayed by default
                    //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    // allows to draw something on the current page, usually visible in the middle of the screen
                    //.onDraw(onDrawListener)
                    // allows to draw something on all pages, separately for every page. Called only for visible pages
                    //.onDrawAll(onDrawListener)
                    // .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
                    // .onPageChange(onPageChangeListener)
                    // .onPageScroll(onPageScrollListener)
                    // .onError(onErrorListener)
                    //.onPageError(onPageErrorListener)
                    //.onRender(onRenderListener) // called after document is rendered for the first time
                    // called on single tap, return true if handled, false to toggle scroll handle visibility
                    //.onTap(onTapListener)
                    // .onLongPress(onLongPressListener)
                    .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .swipeHorizontal(false)
                    .autoSpacing(true) // add dynamic spacing to fit each page on its own on the screen
                    // .linkHandler(DefaultLinkHandler)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .pageSnap(true) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    // .nightMode(false) // toggle night mode
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verPdfPagina(View view) {

        ((TextView) findViewById(R.id.textoTituloAyuda)).setText(String.format("Ayuda: %s", ((Button) view).getText().toString()));

        if (view == findViewById(R.id.botonAyudaIntro)) {
            LeerPdf(0);
        }
        if (view == findViewById(R.id.botonAyudaSecc1)) {
            LeerPdf(1, 2);
        }
        if (view == findViewById(R.id.botonAyudaSecc2)) {
            LeerPdf(3,4,5,6,7,8,9,10);
        }
        if (view == findViewById(R.id.botonAyudaSecc3)) {
            LeerPdf(11);
        }
        if (view == findViewById(R.id.botonAyudaSecc4)) {
            LeerPdf(12);
        }
        if (view == findViewById(R.id.botonAyudaSecc5)) {
            LeerPdf(13);
        }
    }
}
