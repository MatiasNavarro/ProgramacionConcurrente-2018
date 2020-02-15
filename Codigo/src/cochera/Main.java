package cochera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.io.IOException;  // Import the IOException class to handle errors




import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import acciones.Accion;
import acciones.ConsolePrinter;
import monitor.Cronometro;
import monitor.Monitor;
import util.ThreadData;


public class Main {
	private static String name_file_console="";
	private static String name_file="";
	private static final int NUMBER_OF_THREADS=26;
	private static final int EXECUTION_TIME=35;
	private static final TimeUnit TIME_UNIT=TimeUnit.SECONDS;
	private static final boolean FLAG_TEST_PRIORITIES=false;
	/*
	 * La politica puede ser:
	 *	0: aleatoria.
	 *	1: primero los que suben.
	 *	2: primero los que bajan.
	*/
	private static final int POLITIC=0;
	
	public static void main(String[] args) throws InterruptedException {
		
		setPath(name_file,name_file_console,FLAG_TEST_PRIORITIES); //seteo los paths de los diferentes archivos a utilizar

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
		monitor.configRdp(name_file); //Configuro la red de petri para el monitor segun el path.
		
		
		
		monitor.setPolitica(POLITIC);
		
		
		int cant_transiciones=monitor.getCantTransiciones();
		if(cant_transiciones==0){
			System.out.println("Error en getTransiciones del monitor");
			System.exit(1);
		}
		
		
		ThreadData data = new ThreadData(fileStream);
		

		

		
		
		//Inicializo ThreadPoolExecutor, maximo de cantidad_de_hilos.
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(NUMBER_OF_THREADS);  //creo un ThreadPoolExecutor de tamaño maximo 26 hilos
		
		//Inicio tren driver - 1 hilos

//		
//		Accion[] acciones_tren=new Accion[14];
//		for(int j=0; j<transiciones_tren.length;j++){
//			acciones_tren[j]=acciones.get(transiciones_tren[j]);
//		}

		
		//Inicio Barreras (generadores de tokens) - 6 hilos
		executor.execute(new FireTransitions(data.get_t_BarreraE1(), monitor, data.get_a_BarreraE1()));
		executor.execute(new FireTransitions(data.get_t_BarreraE2(), monitor, data.get_a_BarreraE2()));
		executor.execute(new FireTransitions(data.get_t_BarreraE3(), monitor, data.get_a_BarreraE3()));
		
		//Inicio Eleccion de piso
		executor.execute(new FireTransitions(data.get_t_EleccionP1(),monitor, data.get_a_EleccionP1()));
		executor.execute(new FireTransitions(data.get_t_EleccionP2(),monitor, data.get_a_EleccionP2()));
		
		//Inicio Rampa-Piso2
		executor.execute(new FireTransitions(data.get_t_Bajada(),monitor, data.get_a_Bajada()));
		executor.execute(new FireTransitions(data.get_t_Subida(),monitor, data.get_a_Subida()));		


		
		//Camino a la caja por piso - 2 hilos
		executor.execute(new FireTransitions(data.get_t_IrACaja1(),monitor, data.get_a_IrAcaja1()));
		executor.execute(new FireTransitions(data.get_t_IrACaja2(),monitor, data.get_a_IrACaja2()));
		
		//Caja - 2 hilos
		executor.execute(new FireTransitions(data.get_t_CobrarPiso1(),monitor, data.get_a_CobrarPiso1()));  
		executor.execute(new FireTransitions(data.get_t_CobrarPiso2(),monitor, data.get_a_CobrarPiso2()));	
		
		
		//Eleccion de salida - 2 hilos
		executor.execute(new FireTransitions(data.get_t_EleccionS1(),monitor, data.get_a_EleccionS1()));	 
		executor.execute(new FireTransitions(data.get_t_EleccionS2(),monitor, data.get_a_EleccionS2()));
		
		//Barreras Salida - 2 hilos
		executor.execute(new FireTransitions(data.get_t_BarreraS1(),monitor, data.get_a_BarreraS1()));	
		executor.execute(new FireTransitions(data.get_t_BarreraS2(),monitor, data.get_a_BarreraS2()));	  
		
		//Calle - 1 Hilo
		executor.execute(new FireTransitions(data.get_t_Calle(),monitor, data.get_a_Calle()));
		
		//Cartel - 1 Hilo
		executor.execute(new FireTransitions(data.get_t_Cartel(),monitor, data.get_a_Cartel()));  


		
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
	
	
	
	
	
	
	
	
	
	public static void setPath(String name_fil,String name_file_consol,boolean flag_prioridad) {
		if(!flag_prioridad){
			if((System.getProperty("os.name")).equals("Windows 10")){	
				 if(System.getProperty("user.name").equals("kzAx")){
					 name_file="..\\src\\RedesParaTest\\TestTren\\excelTren.xls";
					 name_file_console="..\\src\\logueo\\logFileD.txt";
				 }
				 else{
					 name_file="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTren.xls"; //Path para Windows.
					 name_file_console="..\\..\\LeagueOfJustice\\CodigoJava\\src\\logueo\\logFileD.txt"; 
				 }
			}
			else {
				name_file="./src/RedesParaTest/TestTren/excelTren.xls";
				name_file_console="./src/logueo/logFileD.txt";
			}
		}
		else{
			if((System.getProperty("os.name")).equals("Windows 10")){	
				 if(System.getProperty("user.name").equals("kzAx")){
					 name_file="..\\src\\RedesParaTest\\TestTren\\excelTrenPrioridades.xls";
					 name_file_console="..\\src\\logueo\\logFileD.txt";
				 }
				 else{
					 name_file="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTrenPrioridades.xls"; //Path para Windows.
					 name_file_console="..\\..\\LeagueOfJustice\\CodigoJava\\src\\logueo\\logFileD.txt"; 
				 }
			}
			else {
				name_file="./src/RedesParaTest/TestTren/excelTrenPrioridades.xls";
				name_file_console="./src/logueo/logFileD.txt";
			}
		}
	}

}