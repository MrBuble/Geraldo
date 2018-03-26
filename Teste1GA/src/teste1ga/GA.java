/*
 * GA.java
 *
 * Created on 12 de Setembro de 2009, 18:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package teste1ga;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JTextArea;
import teste1ga.Cromossomo;

/**
 *
 * @author geraldinho
 */
public class GA {

    private int cromoLen = 10;
    private int nIndividuos = 10; // deve ser par pq cruz gera 2 filhos
    private double taxMut = 0.1;
    private int nGer = 30;
    private double a = 0, b = 4;
    private ArrayList pop, popInicial;
    private JTextArea out;
    private Graphics graf;
    private Cromossomo melhorDeTodos;
    private boolean elite = false;

    /**
     * Creates a new instance of GA
     */
    public GA() {
    }

    public void printPop(int geracao) {
        plotaFitness();
        plotaPopIni();
        graf.setColor(Color.BLUE);
        graf.fillOval(200, 20, 10, 10);
        graf.drawString("Pop Inicial", 220, 30);
        graf.setColor(Color.RED);
        graf.fillOval(200, 50, 10, 10);
        graf.drawString("Pop Atual", 220, 60);
        out.append("\nGeracao: " + geracao + "\n");
        DecimalFormat ft = new DecimalFormat("##.###");
        for (int i = 0; i < nIndividuos; i++) {
            Cromossomo c = (Cromossomo) pop.get(i);
            out.append(
                    c.getString()
                    + "\tvalor="
                    + ft.format(getValor(c))
                    + "\tfit="
                    + ft.format(c.getFit())
                    + "\n");
            plotaX(getValor(c), Color.RED);
        }
        out.append("best: "
                + melhorDeTodos.getString()
                + "     valor="
                + ft.format(getValor(melhorDeTodos))
                + "     fit="
                + ft.format(melhorDeTodos.getFit())
                + "\n");
    }

    public void plotaPopIni() {
        for (int i = 0; i < nIndividuos; i++) {
            Cromossomo c = (Cromossomo) popInicial.get(i);
            plotaX(getValor(c), Color.BLUE);
        }
    }

    private void plotaX(double x, Color cor) {
        int grafSz = 180; // painel de 180 x 180
        int marg = 15; // margem
        int w = 150, h = 150; // area desenho
        double dx = (b - a) / 150;
        int i = (int) Math.round(x / dx);
        int j = 180 - marg;
        graf.setColor(cor);
        graf.drawOval(marg + i - 2, j - 2, 4, 4);
        graf.setColor(cor);
    }

    private void plotaFitness() {
        int grafSz = 180; // painel de 180 x 180
        int marg = 15; // margem
        int w = grafSz - 2 * marg, h = grafSz - 2 * marg; // area desenho
        double dx = (b - a) / (grafSz - 2 * marg);
        double dy = 4.0 / (grafSz - 2 * marg); // 0 ate max fit
        double x = a, y;
        int i, j, k, i_ant = 0, j_ant = 0;
        graf.setColor(Color.BLACK);
        graf.clearRect(marg / 2, marg / 2, grafSz, grafSz);
        graf.drawLine(marg, grafSz - marg, grafSz - marg, grafSz - marg); // eixo x
        graf.drawLine(marg, marg, marg, grafSz - marg);  // eixo y
        for (k = 0; k < 150; k++) {
            y = fitness(x);
            i = marg + k;
            j = (int) Math.round(y / dy);
            j = grafSz - marg - j;
            if (k == 0) {
                graf.drawOval(i, j, 1, 1);
            } else {
                graf.drawLine(i_ant, j_ant, i, j);
            }
            i_ant = i;
            j_ant = j;
            x += dx;
        }
    }

    public void run() {

        initPop();
        for (int i = 0; i < nGer; i++) {
            avaliaPop();
            printPop(i);
            ArrayList newPop = new ArrayList();
            if (elite) {
                newPop.add(melhorDeTodos);
            }
            while (newPop.size() < nIndividuos) {
                Cromossomo p1 = seleciona();
                Cromossomo p2 = seleciona();
                Cromossomo f1 = new Cromossomo(cromoLen);
                Cromossomo f2 = new Cromossomo(cromoLen);
                p1.cruzamento(p2, f1, f2);
                if (Math.random() < taxMut) {
                    f1.mutacao();
                }
                if (Math.random() < taxMut) {
                    f2.mutacao();
                }
                if (newPop.size() < nIndividuos) {
                    newPop.add(f1);
                }
                if (newPop.size() < nIndividuos) {
                    newPop.add(f2);
                }
            }
            pop = newPop;
        }
    }

    public Cromossomo seleciona() {
        double totAval = 0;
        int i;
        for (i = 0; i < nIndividuos; i++) {
            totAval += ((Cromossomo) pop.get(i)).getFit();
        }
        double limite = Math.random() * totAval;
        double aux = 0;
        for (i = 0; aux < limite; i++) {
            aux += ((Cromossomo) pop.get(i)).getFit();
        }
        Cromossomo resp = (Cromossomo) pop.get((i>0?(i-1):0));
        return resp;
    }

    public void initPop() {
        pop = new ArrayList();
        while (pop.size() < nIndividuos) {
            Cromossomo c = new Cromossomo();
            c.inicializa(cromoLen);
            pop.add(c);
        }
        popInicial = pop;
    }

    public void avaliaPop() {
        double f;
        for (int i = 0; i < nIndividuos; i++) {
            Cromossomo c = (Cromossomo) pop.get(i);
            f = fitness(getValor(c));
            c.setFit(f);
            if (i == 0) {
                melhorDeTodos = c;
            } else if (f > melhorDeTodos.getFit()) {
                melhorDeTodos = c;
            }
        }
    }

    public void setOut(JTextArea out) {
        this.out = out;
    }

    public void setGraf(Graphics g) {
        this.graf = g;
    }

    private double getValor(Cromossomo c) {
        int i, j;
        double xDec, x, doisAn;

        xDec = c.getDecimal();
        doisAn = 1;
        for (j = 1; j <= cromoLen; j++) {
            doisAn *= 2;
        }
        x = a + xDec * (b - a) / (doisAn - 1);
        return x;
    }

    private double fitness(double x) {
        
        return 1.0 + x * Math.sin(2*3.15*x); // f = x * seno(2*pi*x) + 1
    }

    public void setElite(boolean vf) {
        elite = vf;
    }
}
