/*
 * Cromossomo.java
 *
 * Created on 12 de Setembro de 2009, 18:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package teste1ga;

/**
 *
 * @author geraldinho
 */
public class Cromossomo {
    private int[] bitString;
    private int nBits;
    private double fit;
    
    /** Creates a new instance of Cromossomo */
    public Cromossomo() {
    }
    public Cromossomo(int n) {
        nBits=n;
        bitString = new int[nBits];
    }
    
    public double getDecimal()
    {
        double xDec = 0;
        for (int i=nBits-1; i>=0; i--)
        {
            if (bitString[i]==1) 
                xDec += ((long) 1) << (nBits-1-i);
        }
        return xDec;
    }
    
    public void inicializa(int bits)
    {
        nBits = bits;
        bitString = new int[nBits];
        for (int i=0; i<bits; i++)
            if (Math.random()<0.5) bitString[i] = 0; else bitString[i] = 1;
    }
    
    public void setFit(double f)
    {
       fit = f;
    }

    public String getString() {
        String resp = "";
        for(int i=0; i<nBits; i++) resp += bitString[i];
        return resp;
    }

    public double getFit() {
        return fit;
    }
    
    public void mutacao()
    {
        int i = (int) (Math.random()*nBits);
        if (bitString[i]==0) bitString[i] = 1;  else bitString[i] = 0;
    }
    
    public void cruzamento(Cromossomo p, Cromossomo f1, Cromossomo f2)
    {
        int i = (int) (Math.random()*nBits);
        int j;
        for(j=0; j<i; j++) {
            f1.bitString[j] = bitString[j];
            f2.bitString[j] = p.bitString[j];
        }
        for(j=i; j<nBits; j++) {
            f1.bitString[j] = p.bitString[j];
            f2.bitString[j] = bitString[j];
        }
    }
}
