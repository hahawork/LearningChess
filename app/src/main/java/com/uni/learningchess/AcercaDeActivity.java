package com.uni.learningchess;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AcercaDeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acerca_de);

        Typeface fuente = Typeface.createFromAsset(getAssets(),"fonts/CabinSketch-Bold.ttf");
        Typeface fuente2 = Typeface.createFromAsset(getAssets(),"fonts/IndieFlower.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(),"fonts/LoveYaLikeASister.ttf");
        Typeface fuente3 = Typeface.createFromAsset(getAssets(),"fonts/Dekko-Regular.ttf");


        TextView acercaDeDialog = findViewById(R.id.acercade);
        acercaDeDialog.setTypeface(fuente3);

        TextView acercaDe2Dialog = findViewById(R.id.acercade2);
        acercaDe2Dialog.setTypeface(fuente3);

        TextView linkCanal = findViewById(R.id.linkCanal);
        linkCanal.setTypeface(fuente3);
        linkCanal.setMovementMethod(LinkMovementMethod.getInstance());



        TextView tituloCreditosApp = findViewById(R.id.tituloCreditosApp);
        tituloCreditosApp.setTypeface(fuente1);

        TextView tituloCreditosAppProgramacion = findViewById(R.id.tituloCreditosAppProgramacion);
        tituloCreditosAppProgramacion.setTypeface(fuente);

        TextView tituloCreditosAppDiseño = findViewById(R.id.tituloCreditosAppDiseño);
        tituloCreditosAppDiseño.setTypeface(fuente);

        TextView tituloCreditosAppIdea = findViewById(R.id.tituloCreditosAppIdea);
        tituloCreditosAppIdea.setTypeface(fuente);

        TextView tituloCreditosAppGuion = findViewById(R.id.tituloCreditosAppGuion);
        tituloCreditosAppGuion.setTypeface(fuente);

        TextView tituloCreditosAppDireccion = findViewById(R.id.tituloCreditosAppDireccion);
        tituloCreditosAppDireccion.setTypeface(fuente);
        TextView textoCreditosAppDireccion = findViewById(R.id.textoCreditosAppDireccion);
        textoCreditosAppDireccion.setTypeface(fuente2);

        TextView tituloCreditosVideo = findViewById(R.id.tituloCreditosVideo);
        tituloCreditosVideo.setTypeface(fuente1);

        TextView tituloCreditosVideoGuion = findViewById(R.id.tituloCreditosVideoGuion);
        tituloCreditosVideoGuion.setTypeface(fuente);

        TextView tituloCreditosVideoIdea= findViewById(R.id.tituloCreditosVideoIdea);
        tituloCreditosVideoIdea.setTypeface(fuente);

        TextView tituloCreditosVideoDiseño= findViewById(R.id.tituloCreditosVideoDiseño);
        tituloCreditosVideoDiseño.setTypeface(fuente);

        TextView tituloCreditosVideoAnimacion= findViewById(R.id.tituloCreditosVideoAnimacion);
        tituloCreditosVideoAnimacion.setTypeface(fuente);

        TextView tituloCreditosVideoVoces= findViewById(R.id.tituloCreditosVideoVoces);
        tituloCreditosVideoVoces.setTypeface(fuente);

        TextView tituloCreditosVideoSupervision= findViewById(R.id.tituloCreditosVideoSuperVision);
        tituloCreditosVideoSupervision.setTypeface(fuente);
        TextView textoCreditosVideoSupervision= findViewById(R.id.textoCreditosVideoSuperVision);
        textoCreditosVideoSupervision.setTypeface(fuente2);
    }
}
