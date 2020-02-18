package util;


import java.util.HashMap;



public class Data{
	private static int[] t_BarreraE1, t_BarreraE2, t_BarreraE3, t_EleccionP1, t_EleccionP2, t_Bajada, t_Subida, t_IrACaja1, t_IrACaja2, t_CobrarPiso1, t_CobrarPiso2, t_EleccionS1, t_EleccionS2, t_BarreraS2, t_BarreraS1, t_Calle, t_Cartel    ;
	private static String[] e_BarreraE1, e_BarreraE2, e_BarreraE3, e_EleccionP1, e_EleccionP2, e_Bajada, e_Subida, e_IrACaja1, e_IrACaja2, e_CobrarPiso1, e_CobrarPiso2, e_EleccionS1, e_EleccionS2, e_BarreraS2, e_BarreraS1, e_Calle, e_Cartel    ;
	private static HashMap<Integer,String> diccionario;

	
    private static final Data instance = new Data();
	private Data() {
		
//		//Creacion del diccionario
		diccionario = new HashMap<Integer,String>();
		writeDictionary();
		
		//Acciones.
//		HashMap<Integer,Accion> acciones=new HashMap<Integer,Accion>();
//		for(int transicion=0; transicion<29;transicion++){
//			acciones.put(transicion, new ConsolePrinter(diccionario.get(transicion),fileStream));
//		}
		setTransiciones();
		setEventos(diccionario);
		
	}
	
	public static Data getInstance() {
		return instance;
	}

	public static void writeDictionary() {
		diccionario.put(0,"Un auto llega a la barrera de entrada 1");//T1
		diccionario.put(1,"Un auto llega a la barrera de entrada 2");//T2
		diccionario.put(2,"Un auto llega a la barrera de entrada 3");//T3
		diccionario.put(3,"Un auto atraviesa la barrera 1");//T4
		diccionario.put(4,"Un auto atraviesa la barrera 2");//T5
		diccionario.put(5,"Un auto atraviesa la barrera 3");//T6
		diccionario.put(6,"Un auto elige piso 2");//T8
		diccionario.put(7,"Un auto elige piso 1 ");//T11
		diccionario.put(8,"Un auto sube la rampa");//T16
		diccionario.put(9,"Un auto estaciona en el piso 2");//T17
		diccionario.put(10,"Un auto baja la rampa");//T18
		diccionario.put(11,"Un auto desocupo la rampa");//T19
		diccionario.put(12,"Un auto paga (auto piso 2)");//T20
		diccionario.put(13,"Un auto va a la cola de la caja(auto piso 1)");//T21
		diccionario.put(14,"Un auto paga (auto piso 1)");//T22
		diccionario.put(15,"Un auto sale de la caja(auto piso 2)");//T23
		diccionario.put(16,"Un auto sale de la caja(auto piso 1)");//T24
		diccionario.put(17,"Un auto elige salida 2 (auto piso 2)");//T25
		diccionario.put(18,"Un auto elige salida 2 (auto piso 1)");//T26
		diccionario.put(19,"Un auto elige salida 1 (auto piso 2)");//T27
		diccionario.put(20,"Un auto elige salida 1 (auto piso 1)");//T28
		diccionario.put(21,"Un auto salio por salida 2");//T29
		diccionario.put(22,"Un auto sale por salida 1");//T30
		diccionario.put(23,"Un auto en la calle");//T31
		diccionario.put(24,"Prender cartel \"No Hay Lugar\"");//T32
		diccionario.put(25,"Apagar cartel");//T33
		diccionario.put(26,"Un auto va a la cola de la caja(auto piso 2)");//T35
		diccionario.put(27,"Un auto llega a la barrera de la salida 1");//T36
		diccionario.put(28,"Un auto llega a la barrera de la salida 2");//T41

	}

	public static void setTransiciones() {
		t_BarreraE1=new int[2];
		t_BarreraE1[0]=0;
		t_BarreraE1[1]=3;

		t_BarreraE2=new int[2];
		t_BarreraE2[0]=1;
		t_BarreraE2[1]=4;

		t_BarreraE3=new int[2];
		t_BarreraE3[0]=2;
		t_BarreraE3[1]=5;

		t_EleccionP1=new int[1];
		t_EleccionP1[0]=7;

		t_EleccionP2=new int[1];
		t_EleccionP2[0]=6;


		t_Bajada=new int[2];
		t_Bajada[0]=10;
		t_Bajada[1]=11;

		t_Subida=new int[2];
		t_Subida[0]=8;
		t_Subida[1]=9;

		t_IrACaja1=new int[1];
		t_IrACaja1[0]=13;


		t_IrACaja2=new int[1];
		t_IrACaja2[0]=26;


		t_CobrarPiso1=new int[2];
		t_CobrarPiso1[0]=14;
		t_CobrarPiso1[1]=16;

		t_CobrarPiso2=new int[2];
		t_CobrarPiso2[0]=12;
		t_CobrarPiso2[1]=15;

		t_EleccionS1=new int[2];
		t_EleccionS1[0]=19; 
		t_EleccionS1[1]=20;

		t_EleccionS2=new int[2];
		t_EleccionS2[0]=17;
		t_EleccionS2[1]=18;

		t_BarreraS2=new int[2];
		t_BarreraS2[0]=28;
		t_BarreraS2[1]=21;

		t_BarreraS1=new int[2];
		t_BarreraS1[0]=27;
		t_BarreraS1[1]=22;

		t_Calle=new int[1];
		t_Calle[0]=23;


		t_Cartel=new int[2];
		t_Cartel[0]=24;
		t_Cartel[1]=25;
	}
	public static void setEventos(HashMap<Integer, String> eventos) {
		e_BarreraE1=new String[2];
		e_BarreraE1[0]=eventos.get(t_BarreraE1[0]);  
		e_BarreraE1[1]=eventos.get(t_BarreraE1[1]);

		e_BarreraE2=new String[2];
		e_BarreraE2[0]=eventos.get(t_BarreraE2[0]);  
		e_BarreraE2[1]=eventos.get(t_BarreraE2[1]);

		e_BarreraE3=new String[2];
		e_BarreraE3[0]=eventos.get(t_BarreraE3[0]);  
		e_BarreraE3[1]=eventos.get(t_BarreraE3[1]);


		e_EleccionP1=new String[1];
		e_EleccionP1[0]=eventos.get(t_EleccionP1[0]);

		e_EleccionP2=new String[1];
		e_EleccionP2[0]=eventos.get(t_EleccionP2[0]);


		e_Bajada=new String[2];
		e_Bajada[0]=eventos.get(t_Bajada[0]);  
		e_Bajada[1]=eventos.get(t_Bajada[1]);

		e_Subida=new String[2];
		e_Subida[0]=eventos.get(t_Subida[0]);  
		e_Subida[1]=eventos.get(t_Subida[1]);


		e_IrACaja1=new String[1];
		e_IrACaja1[0]=eventos.get(t_IrACaja1[0]);  

		e_IrACaja2=new String[1];
		e_IrACaja2[0]=eventos.get(t_IrACaja2[0]);  


		e_CobrarPiso1=new String[2];
		e_CobrarPiso1[0]=eventos.get(t_CobrarPiso1[0]);  
		e_CobrarPiso1[1]=eventos.get(t_CobrarPiso1[1]);

		e_CobrarPiso2=new String[2];
		e_CobrarPiso2[0]=eventos.get(t_CobrarPiso2[0]);  
		e_CobrarPiso2[1]=eventos.get(t_CobrarPiso2[1]);


		e_EleccionS1=new String[2];
		e_EleccionS1[0]=eventos.get(t_EleccionS1[0]);  
		e_EleccionS1[1]=eventos.get(t_EleccionS1[1]);

		e_EleccionS2=new String[2];
		e_EleccionS2[0]=eventos.get(t_EleccionS2[0]);  
		e_EleccionS2[1]=eventos.get(t_EleccionS2[1]);


		e_BarreraS2=new String[2];
		e_BarreraS2[0]=eventos.get(t_BarreraS2[0]);  
		e_BarreraS2[1]=eventos.get(t_BarreraS2[1]);

		e_BarreraS1=new String[2];
		e_BarreraS1[0]=eventos.get(t_BarreraS1[0]);  
		e_BarreraS1[1]=eventos.get(t_BarreraS1[1]);


		e_Calle=new String[1];
		e_Calle[0]=eventos.get(t_Calle[0]);  


		e_Cartel=new String[2];
		e_Cartel[0]=eventos.get(t_Cartel[0]);  
		e_Cartel[1]=eventos.get(t_Cartel[1]);
	}
	
	public int[] get_t_BarreraE1(){
		return t_BarreraE1;
	}

	public int[] get_t_BarreraE2(){
		return t_BarreraE2;
	}

	public int[] get_t_BarreraE3(){
		return t_BarreraE3;
	}

	public int[] get_t_EleccionP1(){
		return t_EleccionP1;
	}

	public int[] get_t_EleccionP2(){
		return t_EleccionP2;
	}

	public int[] get_t_Bajada(){
		return t_Bajada;
	}

	public int[] get_t_Subida(){
		return t_Subida;
	}

	public int[] get_t_IrACaja1(){
		return t_IrACaja1;
	}

	public int[] get_t_IrACaja2(){
		return t_IrACaja2;
	}

	public int[] get_t_CobrarPiso1(){
		return t_CobrarPiso1;
	}

	public int[] get_t_CobrarPiso2(){
		return t_CobrarPiso2;
	}

	public int[] get_t_EleccionS1(){
		return t_EleccionS1;
	}

	public int[] get_t_EleccionS2(){
		return t_EleccionS2;
	}

	public int[] get_t_BarreraS2(){
		return t_BarreraS2;
	}

	public int[] get_t_BarreraS1(){
		return t_BarreraS1;
	}

	public int[] get_t_Calle(){
		return t_Calle;
	}

	public int[] get_t_Cartel(){
		return t_Cartel;
	}
	
	public HashMap<Integer,String> getDiccionario(){
		return diccionario;
	}


}