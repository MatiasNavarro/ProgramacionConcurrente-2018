import java.util.concurrent.Semaphore;

public class Mutex {
	public Semaphore semaforo;
	
	/*
	 * Crea un semaforo binario, con cola FIFO
	 */
	public Mutex() {
		semaforo = new Semaphore(1,true);
	}
	
	/*
	 * Intenta tomar el acceso al recurso. Si no lo obtiene, espera en la cola (se duerme).
	 */
	public boolean acquiere() {
		try {
			semaforo.acquire();
		} catch(InterruptedException e) {
			e.printStackTrace();	
		}
		return true;
	}
	
	/*
	 * Libera el acceso al recurso
	 */
	public boolean release() {
		semaforo.release();
		return true;
	}
}
