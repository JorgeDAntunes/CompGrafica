/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto2d;

/**
 *
 * @author Jorge
 */
public class Movimento {
    
    private int peca;
    private int torreOrigen;
    private int torreDestino;

    public Movimento(int peca, int torreOrigen, int torreDestino) {
        this.peca = peca;
        this.torreOrigen = torreOrigen;
        this.torreDestino = torreDestino;
    }

    public int getPeca() {
        return peca;
    }

    public void setPeca(int peca) {
        this.peca = peca;
    }

    public int getTorreDestino() {
        return torreDestino;
    }

    public void setTorreDestino(int torreDestino) {
        this.torreDestino = torreDestino;
    }

    public int getTorreOrigen() {
        return torreOrigen;
    }

    public void setTorreOrigen(int torreOrigen) {
        this.torreOrigen = torreOrigen;
    }
}

