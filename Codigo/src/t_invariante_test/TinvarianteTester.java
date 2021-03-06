package t_invariante_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.util.Scanner;




public class TinvarianteTester {
	
	private String disparadas; //las transiciones que se dispararon
	private int i;
	private String [] ecuaciones;
	private int veces []; //vector que contiene cuantas veces se repite cada T-inv
	private int vecesParciales []; //vector que contiene cuantas veces se repite cada T-inv
	private int vectorCorte [];
	private int vectorCorte2 [];
	private String ec0;
	private String ec1;
	private String ec2;
	private String ec3;
	private String ec4;
	private String ec5;
	private String ec6;
	private String ec7;
	private String ec8;
	private String ec9;
	private String ec10;
	private String ec11;
	private String ec12;
	private boolean EncontreUno= false;
	
	public TinvarianteTester() {
		disparadas = new String ("");
		i=0;
		veces = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0}; //cantidad de veces que fue encontrada cada ec. de T-inv
		vecesParciales = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0}; //cantidad de veces que fue encontrada cada ec. de T-inv
		ec0 = new String ("q r i f g j x 2 v b");
		ec1 = new String ("w t i f g j x 2 v b");
		ec2 = new String ("e y i f g j x 2 v b");
		ec3 = new String ("q r u o p a s 1 d h z 2 v b");
		ec4 = new String ("w t u o p a s 1 d h z 2 v b");
		ec5 = new String ("e y u o p a s 1 d h z 2 v b");
		ec6 = new String ("q r i f g j l 3 c b");
		ec7 = new String ("w t i f g j l 3 c b");
		ec8 = new String ("e y i f g j l 3 c b");
		ec9 = new String ("q r u o p a s 1 d h k 3 c b");
		ec10 = new String ("w t u o p a s 1 d h k 3 c b");
		ec11 = new String ("e y u o p a s 1 d h k 3 c b");
		ec12 = new String ("n m");
		vectorCorte = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1}; //cuando sean todos ceros deja de buscar ecuaciones de T-inv
		vectorCorte2 = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1}; //cuando sean todos ceros deja de buscar ecuaciones de T-inv
		
		ecuaciones = new String[] {ec0, ec1, ec2, ec3, ec4, ec5, ec6, ec7, ec8, ec9, ec10, ec11, ec12};
		cargarDisparadas();
		testear();
	}

			
	/**
	 * Metodo busqueda. Busca transicion por transicion la ecuacion de T-inv en el vector de disparadas
	 * @param cad transciones disparadas en el transcurso del programa
	 * @param regex transicion a buscar en cad
	 * @param ec ecuacion de T-inv a buscar en cad
	 */		
	//cad=cadena de disparo / regex=transicion a buscar / ec=ecuacion T-inv
	public void busqueda (String cad, String regex, String ec) {
		Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(cad);
	    
	    if (matcher.find()) {
	    	cad = cad.replaceFirst(regex, " ");
	    	i = i+2;
	    	if(i > ec.length()) {  //si ya encontro todas las q buscaba edito las disparadas posta
	    		disparadas = cad;
	    		i = 0;
	    	}
	    	else {
	    		char c = ec.charAt(i);  //obtengo la prox. transicion
	    		regex = Character.toString(c);  //la paso a formato String
	            busqueda(cad, regex, ec);  //llamada recursiva
	    	}
	    }
	    else i=1;
	}
	
	
	
	public void busqueda2 (String cad, String regex, String ec) {
//		System.out.println(i);
		Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(cad);
	    
	    if (matcher.find()) {// busco la primera ocurrencia de un disparo de la transicion
	    	cad = cad.replaceFirst(regex, " ");
	    	i = i-2;
	    	if(i <0){  //si ya encontro todas las q buscaba edito las disparadas posta
	    		disparadas = cad;
	    		i = 0;
	    	}
	    	else {
	    		char c = ec.charAt(i);  //obtengo la prox. transicion
	    		regex = Character.toString(c);  //la paso a formato String
	            busqueda2(cad, regex, ec);  //llamada recursiva
	    	}
	    }
	    else {//No encontre disparos de la transicion en cuestion
	    	
	    	if(EncontreUno) {//Y ya habia encontrado al menos un disparo de una transicion de la ecuacion 
	    		i=1;//Corto la busqueda
	    	}
	    	else {//Y todavia no habia encontrado
	    		i = i-2;
		    	if(i <0){  //si ya encontro todas las que buscaba reemplazo secuencia de disparadas remanente con la secuencia sin los disparos reemplazados 
		    		disparadas = cad;
		    		i = 1;
		    	}
		    	else {
		    		char c = ec.charAt(i);  //obtengo la prox. transicion
		    		regex = Character.toString(c);  //la paso a formato String
		            busqueda2(cad, regex, ec);  //llamada recursiva
		    	}
	    		
	    	}
	    }
	    
	    
	}
	
	
	/**
	 * Metodo testear. Busca las ecuaciones de T-inv y anot a cuantas veces se repiten
	 */
	public void testear() {
		char c;
		String regex;
		while(!todosCeros(vectorCorte)) { //mientras no sean todos ceros sigue buscando T-invariantes, cuando sean todos ceros deja de buscar 
			for(int j=0; j < ecuaciones.length; j++) {
				c = ecuaciones[j].charAt(0);
				regex = "\\b"+new String (Character.toString(c))+"\\b";
				busqueda(disparadas, regex, ecuaciones[j]);
				if(i == 0) {  //si i=0 encontro la secuencia completa entonces aumento el contador de veces
					veces[j]++;
				}
				else { //si i es distinto de cero significa q no encontro toda la secuencia de la ecuacion o ya no hay transiciones de esa ecuacion
					//System.out.println("La ecuacion "+j+" quedo incompleta");
					vectorCorte[j] = 0; //al ponerle 0 significa que a esa ecuacion ya no la puede encontrar completa
					i=0;
				}
			}
		}

		while(!todosCeros(vectorCorte2)) { //mientras no sean todos ceros sigue buscando T-invariantes parciales, cuando sean todos ceros deja de buscar 
			for(int j=0; j < ecuaciones.length; j++) {
				c = ecuaciones[j].charAt(ecuaciones[j].length()-1);//comienzo la busqueda por la ultima transicion de la ecuacion
				regex = "\\b"+new String (Character.toString(c))+"\\b";
				i= ecuaciones[j].length()-1;// indice de la ultima posicion en el string de la ecuacion
				busqueda2(disparadas, regex, ecuaciones[j]);
				if(i == 0) {  //si i=0 encontro la secuencia completa entonces aumento el contador de veces
					vecesParciales[j]++;
				}
				else { //si i es distinto de cero significa q no encontro toda la secuencia de la ecuacion o ya no hay transiciones de esa ecuacion
					//System.out.println("La ecuacion "+j+" quedo incompleta");
					vectorCorte2[j] = 0; //al ponerle 0 significa que a esa ecuacion ya no la puede encontrar completa
//					i=0;
				}
				
			}
		}
		
		
	}
	
	
	/**
	 * Metodo todosCeros. Permite saber si los valores del arreglo que se le pasa son todos ceros
	 * @param v vector de enteros 
	 * @return boolean true si todos los valores del vector son cero, false en caso contrario
	 */
	public boolean todosCeros(int [] v) {
		int suma = 0;
		for(int k=0; k < v.length; k++) {
			suma = suma + v[k];
		}
		if(suma == 0) return true;
		else return false;
	}
	
	public void imprimoResultados() {
//		System.out.println(disparadas);
		System.out.println();
		for(int k=0; k < veces.length; k++) {			
			System.out.println("Ec"+k+" se repite "+veces[k]+" veces");
		}
		
		System.out.println();
		for(int k=0; k < veces.length; k++) {
			System.out.println("Ec"+k+" se repite parcialmente "+vecesParciales[k]+" veces");
		}
		int q=0;
		int h=0;
		while(disparadas.length() != q) {
			if(disparadas.charAt(q) != ' ') {
				h++;
			}
			q++;
		}
		
		
		System.out.println();
		System.out.println("Sobraron "+h+" transiciones");
	}

	//Metodo que carga las transiciones disparadas en formato String
	public void cargarDisparadas() {
		String path = "";

		if((System.getProperty("os.name")).equals("Windows 10")){
		if(System.getProperty("user.name").equals("usuario")){
		path = "C:\\Users\\usuario\\Desktop\\ProgramacionConcurrente-2018\\Codigo\\src\\logueo\\logFileB.txt";

		}
		}
		else {
		path = "./src/logueo/logFileB.txt";
		}

		File fichero = new File(path);
		Scanner s = null;

		try {
			// Leemos el contenido del fichero
			//System.out.println("... Leemos el contenido del fichero ...");
			s = new Scanner(fichero);

			// Leemos linea a linea el fichero
			while (s.hasNextLine()) {
				String linea = s.nextLine(); 	// Guardamos la linea en un String
				//System.out.println(linea);      // Imprimimos la linea
				switch(linea) {
				case "0": disparadas=disparadas+"q ";break;
				case "1": disparadas=disparadas+"w ";break;
				case "2": disparadas=disparadas+"e ";break;
				case "3": disparadas=disparadas+"r ";break;
				case "4": disparadas=disparadas+"t ";break;
				case "5": disparadas=disparadas+"y ";break;
				case "6": disparadas=disparadas+"u ";break;
				case "7": disparadas=disparadas+"i ";break;
				case "8": disparadas=disparadas+"o ";break;
				case "9": disparadas=disparadas+"p ";break;
				case "10": disparadas=disparadas+"a ";break;
				case "11": disparadas=disparadas+"s ";break;
				case "12": disparadas=disparadas+"d ";break;
				case "13": disparadas=disparadas+"f ";break;
				case "14": disparadas=disparadas+"g ";break;
				case "15": disparadas=disparadas+"h ";break;
				case "16": disparadas=disparadas+"j ";break;
				case "17": disparadas=disparadas+"k ";break;
				case "18": disparadas=disparadas+"l ";break;
				case "19": disparadas=disparadas+"z ";break;
				case "20": disparadas=disparadas+"x ";break;
				case "21": disparadas=disparadas+"c ";break;
				case "22": disparadas=disparadas+"v ";break;
				case "23": disparadas=disparadas+"b ";break;
				case "24": disparadas=disparadas+"n ";break;
				case "25": disparadas=disparadas+"m ";break;
				case "26": disparadas=disparadas+"1 ";break;
				case "27": disparadas=disparadas+"2 ";break;
				case "28": disparadas=disparadas+"3 ";break;
				default: break;
				}
			}

		} catch (Exception ex) {
			System.out.println("Mensaje: " + ex.getMessage());
		} finally {
			// Cerramos el fichero tanto si la lectura ha sido correcta o no
			try {
				if (s != null)
					s.close();
			} catch (Exception ex2) {
				System.out.println("Mensaje 2: " + ex2.getMessage());
			}
		}
	}


}
