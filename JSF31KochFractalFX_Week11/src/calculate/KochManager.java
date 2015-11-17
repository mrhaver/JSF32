/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author Frank Haver
 */
public class KochManager{
    
    private JSF31KochFractalFX application;
    private KochFractal koch;
    private ArrayList<Edge> edges;
    private int counter = 0;
    
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        edges = new ArrayList<>();
        koch = new KochFractal();
        counter = 0;
    }
    
    synchronized public void changeLevel(int nxt) {
        //application.getProgressbarRight().unbind();
        //application.getProgressbarLeft().unbind();
        //application.getProgressbarBottom().unbind();
        koch.setLevel(nxt);
        edges.clear();
        TimeStamp tsb = new TimeStamp();
        tsb.setBegin("Begin Berekenen");
        Task<Void> taskRight = new GenerateRight(this,application,nxt);
        Task<Void> taskLeft = new GenerateLeft(this,application,nxt);
        Task<Void> taskBottom = new GenerateBottom(this,application,nxt);
        Thread tRight = new Thread(taskRight);
        Thread tLeft = new Thread(taskLeft);
        Thread tBottom = new Thread(taskBottom);
        //application.getProgressBarRight().progressProtperty().bind(taskRight.progressProperty());
        //application.getProgressBarLeft().progressProtperty().bind(taskLeft.progressProperty());
        //application.getProgressBarBottom().progressProtperty().bind(taskBottom.progressProperty());
        tLeft.start();
        tRight.start();
        tBottom.start();
        System.out.println("Effuh Wachtuhh");
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(String.valueOf(edges.size()));
        tsb.setEnd("Fractal berekend");      
        application.setTextCalc(tsb.toString());
        application.setTextNrEdges(String.valueOf(koch.getNrOfEdges()));
    }
    
    public void drawEdges() {
        application.clearKochPanel();
        TimeStamp tst = new TimeStamp();
        tst.setBegin("Begin Tekenen");
        for(Edge e : edges){
            application.drawEdge(e);
        }
        tst.setEnd("Fractal Getekend");
        application.setTextDraw(tst.toString());
    }
    
    synchronized public void IncreaseCounter(){
        counter++;
    }
    
    synchronized public int getCounter(){
        return counter;
    }
    
    synchronized public void setCounter(int value){
        counter = value;
    }
    
    synchronized public void voegEdgeToe(Edge edge){
        edges.add(edge);
    }
    
    synchronized public void notifyWait(){
        notify();
    }


}
