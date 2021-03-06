package com.uni.learningchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.uni.learningchess.Pieza.Color.BLANCO;
import static com.uni.learningchess.interf_general.RESOURCE_SDCARD_PATH;

public class MetodosGenerales {

    private Context mContext;
    private Activity mActivity;
    Vector<Pieza> vectorPiezas;

    public MetodosGenerales(Context context) {
        mContext = context;
        mActivity = (Activity) context;
        vectorPiezas= new Vector<>();
    }

    public EjercicioBaseActivity.Validador validadorGenerico = new EjercicioBaseActivity.Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean movimientoValidoBlancas = false;
            if (hayPieza(colOrigen, filaOrigen) && casillaDisponible(colOrigen, filaOrigen, colDestino, filaDestino)) {
                switch (getTipoPieza(colOrigen, filaOrigen)) {
                    case PEON:
                        movimientoValidoBlancas = new MoverPeonActivity().validadorPeon.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case CABALLO:
                        movimientoValidoBlancas = new MoverCaballoActivity().validadorCaballo.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case ALFIL:
                        movimientoValidoBlancas = new MoverAlfilActivity().validadorAlfil.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case TORRE:
                        movimientoValidoBlancas = new MoverTorreActivity().validadorTorre.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case DAMA:
                        movimientoValidoBlancas = new MoverDamaActivity().validadorDama.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case REY:
                        movimientoValidoBlancas = new MoverReyActivity().validadorRey.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                }
            }
            return (movimientoValidoBlancas);
        }
    };


    public void setVectorPiezas(Vector<Pieza> vpiezas) {
        vectorPiezas.removeAllElements();
        vectorPiezas = vpiezas;
    }

    public boolean saltaPiezas(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        boolean saltaPiezas = false;
        int distanciaCol = Math.abs(colOrigen - colDestino);
        int distanciaFila = Math.abs(filaOrigen - filaDestino);
        int distancia = Math.max(distanciaCol, distanciaFila);
        int col = colOrigen;
        int fila = filaOrigen;
        for (int i = 1; i < distancia && !saltaPiezas; i++) {
            if (col < colDestino) col += 1;
            if (col > colDestino) col -= 1;
            if (fila < filaDestino) fila += 1;
            if (fila > filaDestino) fila -= 1;
            saltaPiezas = hayPieza(col, fila);
        }
        return saltaPiezas;
    }

    public boolean capturaPieza(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Pieza piezaOrigen = getPieza(colOrigen, filaOrigen);
        Pieza piezaDestino = getPieza(colDestino, filaDestino);
        return (piezaOrigen != null && piezaDestino != null && piezaOrigen.getColor() != piezaDestino.getColor());
    }

    public boolean casillaDisponible(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        return (!hayPieza(colDestino, filaDestino) || capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
    }

    public Pieza getPieza(int columna, int fila) {
        Pieza pieza = null;
        if (vectorPiezas.size() > 0) {

            for (int i = 0; i < vectorPiezas.size() && pieza == null; i++) {
                if (vectorPiezas.get(i).getColumna() == columna
                        && vectorPiezas.get(i).getFila() == fila) {
                    pieza = vectorPiezas.get(i);
                }
            }

        } else {
            //Toast.makeText(mActivity,"No hay vector de piezas",Toast.LENGTH_LONG).show();
        }
        return pieza;
    }

    public boolean hayPieza(int columna, int fila) {
        Pieza pieza = getPieza(columna, fila);
        return (pieza != null);
    }

    public Pieza.Tipo getTipoPieza(int columna, int fila) {
        Pieza.Tipo tipo = null;
        Pieza pieza = getPieza(columna, fila);
        if (pieza != null) tipo = pieza.getTipo();
        return tipo;
    }

    public Pieza.Color getColorPieza(int columna, int fila) {
        Pieza.Color color = null;
        Pieza pieza = getPieza(columna, fila);
        if (pieza != null) color = pieza.getColor();
        return color;
    }

    public void colocaPieza(Pieza pieza) {
        int idImageView = mActivity.getResources().getIdentifier(pieza.getCoordenada(), "id", mContext.getPackageName());
        ImageView imageView = mActivity.findViewById(idImageView);
        int idDrawablePieza = getDrawablePieza(pieza);
        if (imageView.getVisibility() == View.VISIBLE) {
            // Its visible
            imageView.setImageResource(idDrawablePieza);
        } else {
            Log.w("ColocarPieza", "El id del image view  no esta visible.");
            // Either gone or invisible
        }

        Log.d("Ajedrez", "tipo=" + pieza.getTipo() + " color=" + pieza.getColor() + " coordenada=" + pieza.getCoordenada() + " getResourceName(idImageView)=" + mActivity.getResources().getResourceName(idImageView));
    }

    public int getDrawablePieza(Pieza pieza) {
        int idDrawable = 0;
        if (pieza.getColor() == BLANCO) {
            switch (pieza.getTipo()) {
                case PEON:
                    idDrawable = R.drawable.peon_blanco;
                    break;
                case CABALLO:
                    idDrawable = R.drawable.caballo_blanco;
                    break;
                case ALFIL:
                    idDrawable = R.drawable.alfil_blanco;
                    break;
                case TORRE:
                    idDrawable = R.drawable.torre_blanca;
                    break;
                case DAMA:
                    idDrawable = R.drawable.dama_blanca;
                    break;
                case REY:
                    idDrawable = R.drawable.rey_blanco;
                    break;
            }
        } else {
            switch (pieza.getTipo()) {
                case PEON:
                    idDrawable = R.drawable.peon_negro;
                    break;
                case CABALLO:
                    idDrawable = R.drawable.caballo_negro;
                    break;
                case ALFIL:
                    idDrawable = R.drawable.alfil_negro;
                    break;
                case TORRE:
                    idDrawable = R.drawable.torre_negra;
                    break;
                case DAMA:
                    idDrawable = R.drawable.dama_negra;
                    break;
                case REY:
                    idDrawable = R.drawable.rey_negro;
                    break;
            }
        }
        return idDrawable;
    }


    public void MostrarDialogoVideo_Practica(final String URL, final String Activit) {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogo_movim_piezas_video_practica);

        View.OnClickListener PracticarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NombrePaquete = mContext.getPackageName() + "." + Activit;
                try {
                    Class<?> c = Class.forName(NombrePaquete);
                    Intent intent = new Intent(mContext, c);
                    mContext.startActivity(intent);
                    dialog.dismiss();
                } catch (ClassNotFoundException ignored) {
                }
            }
        };

        TextView tvPracticar = dialog.findViewById(R.id.tvPracticar_dlgmpvp);
        ImageButton Practicar = dialog.findViewById(R.id.ibtnPracticar_dlgmpvp);
        Practicar.setOnClickListener(PracticarListener);
        tvPracticar.setOnClickListener(PracticarListener);


        View.OnClickListener VerVideoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, VerVideo.class);
                i.putExtra("video_id", URL);
                mContext.startActivity(i);
                dialog.dismiss();
            }
        };

        ImageButton VerVideo = dialog.findViewById(R.id.ibtnVerVideo_dlgmpvp);
        TextView tvVerVideo = dialog.findViewById(R.id.tvVerVideo_dlgmpvp);
        VerVideo.setOnClickListener(VerVideoListener);
        tvVerVideo.setOnClickListener(VerVideoListener);

        ImageButton dialogButton = dialog.findViewById(R.id.ibtnCerrar_dlgmpvp);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void MostrarAlertaError(String titulo, String mensaje){

        new AlertDialog.Builder(mActivity)
                .setTitle(titulo)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(mensaje)
                .show();
    }

    private void ParseXML() {

        File yourFile = new File(RESOURCE_SDCARD_PATH, "preferences.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(yourFile);
            doc.getDocumentElement().normalize();

            Log.e("Root_element" , doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("OKV");

            //modelArrayList = new ArrayList<Model>();


            for (int i = 0; i < nodeList.getLength(); i++) {

                Element element = (Element) nodeList.item(i);

                for (int j = 0 ; j < element.getAttributes().getLength(); j++){

                    Log.w("Nodo", element.getAttributes().item(j).getNodeName()  + ": " + element.getAttributes().item(j).getNodeValue());
                }

            }
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }

    }

}
