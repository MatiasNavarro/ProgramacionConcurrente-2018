import java.io.IOException;
import java.nio.channels.ReadPendingException;

public class GestorDeMonitor {
    private Mutex       mtx;
    private RdP         rdp;
    private Cola[]      colas;
    private Politicas   politicas;
    
    private boolean     DEBUG = false;
    
    public GestorDeMonitor(Rdp rdp){
        this.mtx = new Mutex();
        this.rdp = rdp;
        this.politica = new Politica(this.rdp.getN_t());
        this.colas = new Cola[this.rdp.getN_t()];

        for(int i=0; i < colas.length, i++)
        {
            colas[i] = new Cola();
        }
    }

     
}
