/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observable;
import java.util.Observer;
import javafx.concurrent.Task;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author Frank Haver
 */
public class GenerateLeft extends Task<Void> implements Observer{
    
    final private KochManager km;
    final private KochFractal koch;
    final private JSF31KochFractalFX application;
    private double edges = 0;
    
    public GenerateLeft(KochManager km, JSF31KochFractalFX application, int level){
        koch = new KochFractal();
        koch.addObserver(this);
        koch.setLevel(level);
        this.km = km;
        this.application = application;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        edges++;
        km.voegEdgeToe((Edge)arg);
        updateProgress(edges,koch.getNrOfEdges());
    }

    @Override
    protected Void call() throws Exception {
        koch.generateLeftEdge();   
        km.IncreaseCounter();
        if(km.getCounter() == 3){
            application.requestDrawEdges();
            km.setCounter(0);
            km.notifyWait();
        }
        return null;
    }


    
}
