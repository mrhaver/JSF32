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
    private Task<Void> taskRight;
    private Task<Void> taskLeft;
    private Task<Void> taskBottom;
    
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        edges = new ArrayList<>();
        koch = new KochFractal();
        counter = 0;
    }
    
    synchronized public void changeLevel(int nxt) {
        unbindObjects();
        resetObjects();
        koch.setLevel(nxt);
        edges.clear();
        TimeStamp tsb = new TimeStamp();
        tsb.setBegin("Begin Berekenen");
        taskRight = new GenerateRight(this,application,nxt);
        taskLeft = new GenerateLeft(this,application,nxt);
        taskBottom = new GenerateBottom(this,application,nxt);
        Thread tRight = new Thread(taskRight);
        Thread tLeft = new Thread(taskLeft);
        Thread tBottom = new Thread(taskBottom);
        application.getProgressBarRight().progressProperty().bind(taskRight.progressProperty());
        application.getProgressBarLeft().progressProperty().bind(taskLeft.progressProperty());
        application.getProgressBarBottom().progressProperty().bind(taskBottom.progressProperty());
        application.getlabelCountLeft().textProperty().bind(taskLeft.messageProperty());
        application.getlabelCountBottom().textProperty().bind(taskBottom.messageProperty());
        application.getlabelCountRight().textProperty().bind(taskRight.messageProperty());

        tLeft.start();
        tRight.start();
        tBottom.start();
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
            application.drawEdge(e, false);
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
    
    private void unbindObjects(){
        application.getProgressBarRight().progressProperty().unbind();
        application.getProgressBarLeft().progressProperty().unbind();
        application.getProgressBarBottom().progressProperty().unbind();
        application.getlabelCountLeft().textProperty().unbind();
        application.getlabelCountBottom().textProperty().unbind();
        application.getlabelCountRight().textProperty().unbind();
    }
    
    private void resetObjects(){
        application.getProgressBarRight().setProgress(0);
        application.getProgressBarLeft().setProgress(0);
        application.getProgressBarBottom().setProgress(0);
        application.getlabelCountLeft().setText("Nr edges: ");
        application.getlabelCountBottom().setText("Nr edges: ");
        application.getlabelCountRight().setText("Nr edges: ");
    }


}
