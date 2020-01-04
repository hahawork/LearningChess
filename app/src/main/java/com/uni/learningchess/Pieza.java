package com.uni.learningchess;

import android.util.Log;

public class Pieza {

    private Tipo tipo;
    private Color color;
    private String coordenada,coordenadacorrecta="A0";
    private boolean MoverDarJaqueMate;

    public enum Tipo {PEON, CABALLO, ALFIL, TORRE, DAMA, REY}

    public enum Color {BLANCO, NEGRO}

    public Pieza(Tipo tipo, Color color, String coordenada) {
        this.coordenada = coordenada;
        this.tipo = tipo;
        this.color = color;
        this.MoverDarJaqueMate = false;
    }

    public Pieza(Tipo tipo, Color color, String coordenada, boolean moverDarJaqueMate,String coordenadacorrecta) {
        this.coordenada = coordenada;
        this.tipo = tipo;
        this.color = color;
        this.MoverDarJaqueMate = moverDarJaqueMate;
        this.coordenadacorrecta = coordenadacorrecta;
    }

    public String getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(int columna, int fila) {
        coordenada = "" + (char) ((int) ('A') + columna)
                + (char) ((int) ('1') + fila);
        Log.d("Ajedrez", "coordenada=" + coordenada);
    }

    public int getColumna() {
        return coordenada.charAt(0) - 'A';
    }

    public int getFila() {
        return coordenada.charAt(1) - '1';
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMoverDarJaqueMate() {
        return MoverDarJaqueMate;
    }

    public void setMoverDarJaqueMate(boolean moverDarJaqueMate) {
        MoverDarJaqueMate = moverDarJaqueMate;
    }
    public int getColumnaCorrecta() {
        return coordenadacorrecta.charAt(0) - 'A';
    }

    public int getFilaCorrecta() {
        return coordenadacorrecta.charAt(1) - '1';
    }

}
