/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observable;
import java.util.Observer;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author Frank Haver
 */
public class GenerateLeft implements Observer, Runnable{
    
    private KochManager km;
    private KochFractal koch;
    private JSF31KochFractalFX application;
    
    public GenerateLeft(KochManager km, JSF31KochFractalFX application, int level){
        koch = new KochFractal();
        koch.addObserver(this);
        koch.setLevel(level);
        this.km = km;
        this.application = application;
    }

    @Override
    synchronized public void run() {
        koch.generateLeftEdge();   
        km.IncreaseCounter();
        if(km.getCounter() == 3){
            application.requestDrawEdges();
            km.setCounter(0);
            km.notifyWait();
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        km.voegEdgeToe((Edge)arg);
    }


    
}
