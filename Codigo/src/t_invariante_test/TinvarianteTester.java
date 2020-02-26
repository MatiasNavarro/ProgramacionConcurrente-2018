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
	
	public TinvarianteTester() {
		disparadas = new String ("");
		i=0;
		veces = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0};
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
		
		ecuaciones = new String[] {ec0, ec1, ec2, ec3, ec4, ec5, ec6, ec7, ec8, ec9, ec10, ec11, ec12};
		cargarDisparadas();
		testear();
		imprimoResultados();
	}

			
			/**
			 * Si algun elemento del array es 1 significa que todavia
			 * falta seguir buscando
			 * @param array
			 * @return
			 */
			/*public boolean verificoArray(int j) {
				for(int k=0; k < array.length; k++) {
					if(array[k] == 1) {
						return true;
					}
				}
				return false;
			}*/
			
			
	//cad=cadena de disparo / regex=transicion a buscar / ec=ecuacion T-inv
	public void general (String cad, String regex, String ec) {
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
	            general(cad, regex, ec);  //llamada recursiva
	    	}
	    }
	    else i=1;
	}
	

	public void testear() {
		char c;
		String regex;
		for(int h=0; h < 200; h++) {
		for(int j=0; j < ecuaciones.length; j++) {
			c = ecuaciones[j].charAt(0);
			regex = "\\b"+new String (Character.toString(c))+"\\b";
			general(disparadas, regex, ecuaciones[j]);
			if(i != 0) {  //si i es distinto de cero significa q no encontro toda la secuencia de la ecuacion
				//System.out.println("La ecuacion "+j+" quedo incompleta");
				i=0;
			}
			else { //entonces si i=0 encontro la secuencia completa y aumento
				veces[j]++;
			}
		}
		}
	}
	
	public void imprimoResultados() {
		System.out.println(disparadas);
		for(int k=0; k < veces.length; k++) {
			System.out.println("Ec"+k+" se repite "+veces[k]+" veces");
		}
		int q=0;
		int h=0;
		while(disparadas.length() != q) {
			if(disparadas.charAt(q) != ' ') {
				h++;
			}
			q++;
		}
		System.out.println("Sobraron "+h+" transiciones");
	}

	//Metodo que carga las transiciones disparadas en formato String
	public void cargarDisparadas() {
		File fichero = new File("C:\\Users\\usuario\\Desktop\\ProgramacionConcurrente-2018\\Codigo\\src\\logueo\\logFileB.txt");
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
