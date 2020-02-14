package cochera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.io.IOException;  // Import the IOException class to handle errors
/*
 Trabajo Practico Integrador Programacion Concurrente 2017. (Ferrocarril).
 Autores: Casabella Martin, Kleiner Matias, Lopez Gaston.
 */



import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import acciones.Accion;
import acciones.ConsolePrinter;
import monitor.Cronometro;
import monitor.Monitor;


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
		
		
		//Diccionario.
		HashMap<Integer,String> dictionary=new HashMap<Integer,String>();
		
		//Se escribe la referencia de transicion/accion en el diccionario
		writeDictionary(dictionary);
		
		//Acciones.
		HashMap<Integer,Accion> acciones=new HashMap<Integer,Accion>();
		for(int transicion=0; transicion<cant_transiciones;transicion++){
			acciones.put(transicion, new ConsolePrinter(dictionary.get(transicion),fileStream));
		} 
		
		
		//Inicializo ThreadPoolExecutor, maximo de cantidad_de_hilos.
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(NUMBER_OF_THREADS);  //creo un ThreadPoolExecutor de tamaño maximo 26 hilos
		
		//Inicio generadores - 6 hilos
		executor.execute(new FireSingleTransition(0,monitor, acciones.get(0)));
		executor.execute(new FireSingleTransition(1,monitor, acciones.get(1)));
		executor.execute(new FireSingleTransition(2,monitor, acciones.get(2)));
		executor.execute(new FireSingleTransition(3,monitor, acciones.get(3)));
		executor.execute(new FireSingleTransition(15,monitor, acciones.get(15)));
		executor.execute(new FireSingleTransition(20,monitor, acciones.get(20)));
		
		//Inicio control de bajada - 1 hilos
		executor.execute(new FireSingleTransition(24,monitor, acciones.get(24)));
		
		//Inicio circulacion de autos por barrera - 2 hilos
		executor.execute(new FireSingleTransition(22,monitor, acciones.get(22)));
		executor.execute(new FireSingleTransition(17,monitor, acciones.get(17)));
		
		//Inicio pasajeros subiendo al tren/vagon - 8 hilos
		executor.execute(new FireSingleTransition(10,monitor, acciones.get(10)));  // "Estacion A","Maquina"
		executor.execute(new FireSingleTransition(7,monitor, acciones.get(7)));	 // "Estacion A","Vagon"
		executor.execute(new FireSingleTransition(9,monitor, acciones.get(9)));	 // "Estacion B","Maquina"
		executor.execute(new FireSingleTransition(13,monitor, acciones.get(13)));	 // "Estacion B","Vagon"
		executor.execute(new FireSingleTransition(23,monitor, acciones.get(23)));  // "Estacion C","Maquina"
		executor.execute(new FireSingleTransition(12,monitor, acciones.get(12)));  // "Estacion C","Vagon"
		executor.execute(new FireSingleTransition(6,monitor, acciones.get(6)));   // "Estacion D","Maquina"
		executor.execute(new FireSingleTransition(11,monitor, acciones.get(11)));  // "Estacion D","Vagon"
		
		//Inicio pasajeros bajando al tren/vagon - 8 hilos
		executor.execute(new FireSingleTransition(29,monitor, acciones.get(29)));	 // "Estacion A","Maquina"
		executor.execute(new FireSingleTransition(31,monitor, acciones.get(31)));  // "Estacion A","Vagon"
		executor.execute(new FireSingleTransition(32,monitor, acciones.get(32)));  // "Estacion B","Maquina"
		executor.execute(new FireSingleTransition(33,monitor, acciones.get(33)));  // "Estacion B","Vagon"
		executor.execute(new FireSingleTransition(25,monitor, acciones.get(25)));  // "Estacion C","Maquina"
		executor.execute(new FireSingleTransition(26,monitor, acciones.get(26)));  // "Estacion C","Vagon"
		executor.execute(new FireSingleTransition(27,monitor, acciones.get(27)));  // "Estacion D","Maquina"
		executor.execute(new FireSingleTransition(28,monitor, acciones.get(28)));  // "Estacion D","Vagon"

		//Inicio tren driver - 1 hilos
		int[] transiciones_tren=new int[14];
		transiciones_tren[0]=36; //temporal
		transiciones_tren[1]=35;
		transiciones_tren[2]=34; //temporal
		transiciones_tren[3]=18;
		transiciones_tren[4]=21; //temporal
		transiciones_tren[5]=21; //temporal
		transiciones_tren[6]=30;
		transiciones_tren[7]=19; //temporal
		transiciones_tren[8]=8;
		transiciones_tren[9]=4; //temporal
		transiciones_tren[10]=14;
		transiciones_tren[11]=16; //temporal
		transiciones_tren[12]=16; //temporal
		transiciones_tren[13]=5;
		
		
		Accion[] acciones_tren=new Accion[14];
		for(int j=0; j<transiciones_tren.length;j++){
			acciones_tren[j]=acciones.get(transiciones_tren[j]);
		}
		executor.execute(new FireMultipleTransition(transiciones_tren,monitor, acciones_tren));
		
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
        fileStream.format("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        System.out.format("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        
        fileStream.format("\nQuedaron %d tareas por finalizar.",executor.getActiveCount());
        System.out.format("\nQuedaron %d tareas por finalizar.",executor.getActiveCount());
        System.exit(0);
	}
	
	
	
	
	
	
	
	
	public static void writeDictionary(HashMap<Integer,String> diccionario) {
		diccionario.put(0,"Un auto llega a la barrera de entrada 1");		//T1
		diccionario.put(1,"Un auto llega a la barrera de entrada 2");		//T2
		diccionario.put(2,"Un auto llega a la barrera de entrada 3");		//T3
		diccionario.put(3,"Un auto atraviesa la barrera 1");				//T4
		diccionario.put(4,"Un auto atraviesa la barrera 2");				//T5
		diccionario.put(5,"Un auto atraviesa la barrera 3");				//T6
		diccionario.put(6,"Un auto elige piso 2");							//T8
		diccionario.put(7,"Un auto elige piso 1 ");							//T11
		diccionario.put(8,"Un auto sube la rampa");							//T16
		diccionario.put(9,"Un auto estaciona en el piso 2");				//T17
		diccionario.put(10,"Un auto baja la rampa");						//T18
		diccionario.put(11,"Un auto desocupo la rampa");					//T19
		diccionario.put(12,"Un auto paga (auto piso 2)");					//T20
		diccionario.put(13,"Un auto va a la cola de la caja(auto piso 1)");	//T21
		diccionario.put(14,"Un auto paga (auto piso 1)");					//T22
		diccionario.put(15,"Un auto sale de la caja(auto piso 2)");			//T23
		diccionario.put(16,"Un auto sale de la caja(auto piso 1)");			//T24
		diccionario.put(17,"Un auto elige salida 2 (auto piso 2)");			//T25
		diccionario.put(18,"Un auto elige salida 2 (auto piso 1)");			//T26
		diccionario.put(19,"Un auto elige salida 1 (auto piso 2)");			//T27
		diccionario.put(20,"Un auto elige salida 1 (auto piso 1)");			//T28
		diccionario.put(21,"Un auto salio por salida 2");					//T29
		diccionario.put(22,"Un auto sale por salida 1");					//T30
		diccionario.put(23,"Un auto en la calle");							//T31
		diccionario.put(24,"Prender cartel \"No Hay Lugar\"");				//T32
		diccionario.put(25,"Apagar cartel");								//T33
		diccionario.put(26,"Un auto va a la cola de la caja(auto piso 2)");	//T35
		diccionario.put(27,"Un auto llega a la barrera de la salida 1");	//T36
		diccionario.put(28,"Un auto llega a la barrera de la salida 2");	//T41

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