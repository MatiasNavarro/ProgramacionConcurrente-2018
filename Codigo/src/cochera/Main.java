package cochera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;





import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import monitor.Cronometro;
import monitor.LogicaTemporal;
import monitor.Monitor;
import monitor.RedDePetri;
import util.Data;


public class Main {
	private static String name_file_console="";
	private static String name_file="";
	private static final int NUMBER_OF_THREADS=17;
	private static final int EXECUTION_TIME=35;
	private static final TimeUnit TIME_UNIT=TimeUnit.SECONDS;
	private static final boolean FLAG_TEST_PRIORITIES=true;
	private static final boolean FLAG_LOGICA=false;
	
	/*
	 * La politica puede ser:
	 *	0: aleatoria.
	 *	1: Primero Piso1. Salida indistinta
	 *	2: Salida por calle 2.Piso indistinto
	*/
	
	private static final int POLITIC=1;
	
	public static void main(String[] args) throws InterruptedException {
		
		setPath(); //seteo los paths de los diferentes archivos a utilizar

		/*
		 * Seteo el log de acciones al directorio donde indique name_file_console
		 */
		PrintStream fileStream = null;
		try {
			
			fileStream=new PrintStream(new File(name_file_console));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		/*
		 * tiempo_transcurrido variable Cronometro para imprimir tiempo transcurrido desde el inicio del programa.
		 */
		Cronometro tiempo_transcurrido=new Cronometro();
		tiempo_transcurrido.setNuevoTimeStamp();
		
		
		Monitor monitor=Monitor.getInstance(); //Patron Singleton
		RedDePetri rdp = RedDePetri.getInstance();
		rdp.setFlagLogica(FLAG_LOGICA);
		
		monitor.configRdp(name_file); //Configuro la red de petri para el monitor segun el path.
		
		
		
		monitor.setPolitica(POLITIC);
		
		int cant_transiciones=monitor.getCantTransiciones();
		if(cant_transiciones==0){
			System.out.println("Error en getTransiciones del monitor");
			System.exit(1);
		}
		
		
		Data data = Data.getInstance();
		

		

		
		
		//Inicializo ThreadPoolExecutor, maximo de cantidad_de_hilos.
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(NUMBER_OF_THREADS);  //creo un ThreadPoolExecutor de tamaño maximo 26 hilos
		




		
		//Inicio Eleccion de piso
		executor.execute(new FireTransitions(data.get_t_EleccionP1(),monitor,fileStream));
		executor.execute(new FireTransitions(data.get_t_EleccionP2(),monitor, fileStream));
		
		//Inicio Rampa-Piso2
		executor.execute(new FireTransitions(data.get_t_Bajada(),monitor, fileStream));
		executor.execute(new FireTransitions(data.get_t_Subida(),monitor, fileStream));		


		
		//Camino a la caja por piso - 2 hilos
		executor.execute(new FireTransitions(data.get_t_IrACaja1(),monitor, fileStream));
		executor.execute(new FireTransitions(data.get_t_IrACaja2(),monitor, fileStream));
		
		//Caja - 2 hilos
		executor.execute(new FireTransitions(data.get_t_CobrarPiso1(),monitor, fileStream));  
		executor.execute(new FireTransitions(data.get_t_CobrarPiso2(),monitor, fileStream));	
		
		
		//Eleccion de salida - 2 hilos
		executor.execute(new FireTransitions(data.get_t_EleccionS1(),monitor, fileStream));	 
		executor.execute(new FireTransitions(data.get_t_EleccionS2(),monitor, fileStream));
		
		//Barreras Salida - 2 hilos
		executor.execute(new FireTransitions(data.get_t_BarreraS1(),monitor, fileStream));	
		executor.execute(new FireTransitions(data.get_t_BarreraS2(),monitor, fileStream));	  
		
		//Calle - 1 Hilo
		executor.execute(new FireTransitions(data.get_t_Calle(),monitor, fileStream));
		
		//Cartel - 1 Hilo
		executor.execute(new FireTransitions(data.get_t_Cartel(),monitor, fileStream));  
		
		
		//Inicio Barreras (generadores de tokens) - 6 hilos
		executor.execute(new FireTransitions(data.get_t_BarreraE1(), monitor, fileStream));
		executor.execute(new FireTransitions(data.get_t_BarreraE2(), monitor, fileStream));
		executor.execute(new FireTransitions(data.get_t_BarreraE3(), monitor,fileStream));


		
        executor.shutdown();// no se van a aceptar mas tareas, pero espera finalizarse las que se encuentran en ejecucion    
		

		
        /*
         * 		TimeUnit.MINUTES     -		TimeUnit.SECONDS
         * 		Especifica el tiempo que espera el hilo (main) antes de continuar con la siguiente instruccion.				
         */
		executor.awaitTermination(EXECUTION_TIME, TIME_UNIT);
		
		monitor.setCondicion(false);
		
		/*
		 * Mientras no terminen todas las tareas, no sale del while. Evita que algun hilo se quede con el semaforo.
		 */
		while(!executor.isTerminated()) {
			
		}
		
		/*
		 * Finalizo completamente el executor
		 */
		executor.shutdownNow();
        
		/*
		 * Escribo todos los logs del sistema.
		 */
        monitor.writeLogFiles();
        
        
        
        /*
         * Fin del programa
         */
        fileStream.format("Finalizo la ejecucion del simulador en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        System.out.format("Finalizo la ejecucion del simulador en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        
        fileStream.format("\nQuedaron %d tareas por finalizar.",executor.getActiveCount());
        System.out.format("\nQuedaron %d tareas por finalizar.",executor.getActiveCount());
        System.exit(0);
	}
	
	
	
	
	
	
	
	
	
	public static void setPath() {

			if((System.getProperty("os.name")).equals("Windows 10")){	
				 if(System.getProperty("user.name").equals("usuario")){
					 name_file_console="C:\\Users\\usuario\\Desktop\\ProgramacionConcurrente-2018\\Codigo\\src\\logueo\\logFileZ.txt";
				 }
			}
			else {
				name_file_console="./src/logueo/logFileZ.txt";
			}
		

		
		
	}

}
