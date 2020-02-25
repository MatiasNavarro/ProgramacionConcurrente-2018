package cochera;


import monitor.Monitor;
import java.io.PrintStream;
import util.Data;


public class FireTransitions implements Runnable {
    private int[] transitions_to_fire;
    private Monitor monitor;
	private PrintStream log;
	private Data data;
    
/**
 * 
 * @param transiciones_viaje arreglo de enteros que representan las transiciones que disparara el hilo  (viajes en el tren)
 * @param monitor
 */
    public FireTransitions(
    		int[] transiciones, 
    		Monitor monitor,
    		PrintStream log
    		) {
        this.transitions_to_fire = transiciones;
        this.monitor = monitor;
        this.log = log;
        this.data = Data.getInstance();

        
        
    }

    @Override
    public void run() {
    	while(monitor.getCondicion()) {
    		for(int i=0; i<this.transitions_to_fire.length; i++) {
    			monitor.dispararTransicion(this.transitions_to_fire[i]);
    			//System.out.println(data.getDiccionario().get(transitions_to_fire[i]));
    			this.log.println(data.getDiccionario().get(transitions_to_fire[i]));
    	       
    		}
    	}
    }

}

