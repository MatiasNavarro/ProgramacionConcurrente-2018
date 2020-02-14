package util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	
	private int iIncidencia = 0, iInhibicion = 0, iIncidenciaPrevia = 0,iTInvariant=0,iPInvariant=0;
	private int[] iMarcado = new int[2];
	private Document html;
	private Elements tableRowElements;
	private Elements tableRowElementsInv;
	private HashMap<String, int[][]> ldatos = new HashMap<>();
	private String ubicacion = "./src/util/Red/Matrices.html";
	private String ubicacionInv = "./src/util/Red/invariantes.html";
	
	//	public static void main(String[] args){
//		Parser myParser = new Parser();
////		System.out.println(myParser.leerRed().toString());
//		myParser.leerRed();
//	}

	
	public HashMap<String, int[][]> leerRed()
	{
		try 
		{
			File input = new File(ubicacion);
			html = Jsoup.parse(input, "UTF-8");
//			System.out.println(html);
			Elements tablas = html.select("table");
			tableRowElements = tablas.select("tr");
			
			
			for (int i = 0; i < tableRowElements.size(); i++) 
			{
				Element row = tableRowElements.get(i);
				Elements rowItems = row.select("td");

				for (int j = 0; j < rowItems.size(); j++) 
				{
					switch (rowItems.get(j).text()) 
					{
						case "Backwards incidence matrix I-":
							iIncidenciaPrevia = i;
							break;
						case "Combined incidence matrix I":
							iIncidencia = i;
							break;
						case "Inhibition matrix H":
							iInhibicion = i;
							break;
						case "Marking":
							iMarcado[0] = i;   // N�mero de fila de archivo html
							iMarcado[1] = j;   // Posici�n del �tem en la fila
							break;
						default:
							break;
					}
				}
			}
			
			input = new File(ubicacionInv);
			html = Jsoup.parse(input, "UTF-8");
//			System.out.println(html);
			tablas = html.select("table");
//			System.out.println(tablas);
			tableRowElementsInv = tablas.select("tr");
			int tFlag = 0, pFlag=0;
//			System.out.println(tableRowElementsInv);
			for (int i = 0; i < tableRowElementsInv.size(); i++) 
			{
				Element row = tableRowElementsInv.get(i);
				Elements rowItems = row.select("td");
//				System.out.print(rowItems);

				for (int j = 0; j < rowItems.size(); j++) 
				{
//					System.out.println(rowItems.get(j).text());
//					System.out.println(" ");
					if(rowItems.get(j).text().contains("T")) {

						if(tFlag==0) {
							iTInvariant = i;
							tFlag=1;
							
						}
					
					}
					if(rowItems.get(j).text().contains("P")) {
						if(pFlag==0) {
							iPInvariant = i;
							pFlag=1;
							
						}
					}

				}
			}
			

			
//			
			obtenerMatriz(iIncidencia, "Incidencia",tableRowElements);
			obtenerMatriz(iIncidenciaPrevia, "Incidencia Previa",tableRowElements);
			obtenerMatriz(iInhibicion, "Inhibicion",tableRowElements);
			obtenerMatriz(iMarcado[0], "Marcado",tableRowElements);
			obtenerMatriz(iTInvariant, "Tinvariante",tableRowElementsInv);
			obtenerMatriz(iPInvariant, "Pinvariante",tableRowElementsInv);

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ldatos;
	}
	
	
	// PARSEA LA MATRIZ DE INICDENCIA
	private void obtenerMatriz(int aux, String tag,Elements tableRowElements ) 
	{
		String[] datos; 
		if(!(tag=="Pinvariante" || tag=="Tinvariante")) {
			Element row = tableRowElements.get(aux + 1);
			datos = row.text().split(" ");
			}
		else {
			if(tag=="Tinvariante")
				datos = tableRowElements.text().substring(aux,tableRowElements.text().indexOf("P") ).split(" ");
			else
				datos = tableRowElements.text().substring(tableRowElements.text().indexOf("P"),tableRowElements.text().length()-1 ).split(" ");
			}
		
		int columna = 0;
		int fila = 0;

		
		for (int i = 0; i < datos.length; i++) 
		{

			if (datos[i].contains("T")) 
				columna++;
			
			
			if (datos[i].contains("P")) {

				if(tag=="Tinvariante") {
					break;
				}

				
				if(tag=="Marcado"||tag=="Pinvariante")
					columna++;

				else
					fila++;
			}
			if (datos[i].contains("I") && tag=="Marcado") 
				fila++;				

//			System.out.print(datos[i]);

		}

		if(tag=="Pinvariante" || tag=="Tinvariante") {
			fila =(datos.length-columna)/columna;
		}
//
//		System.out.printf("%n");
//		System.out.print(fila);	
//		System.out.printf("%n");
//		System.out.print(columna);
//		System.out.printf("%n");
		
		int[][] matriz;
		if(tag=="Marcado")
			matriz = new int[columna][fila];
		else	
			matriz = new int[fila][columna];
		

			for(int i = 0; i<datos.length-columna; i++) {
				datos[i] = datos[i+columna];
//				System.out.print(datos[i]);
			}

//			System.out.println("");

		for (int i = 0; i < fila; i++) 
		{

//				filaT = Integer.parseInt(datos[i].replace("P", ""));
				for (int j = 0; j < columna; j++) 
				{
					if(!(tag=="Tinvariante" | tag == "Pinvariante")) {
						if(tag=="Marcado")
							matriz[j][i] = Integer.parseInt(datos[(i)*(fila-1) + (j+1)]);
						else {
							matriz[i][j] = Integer.parseInt(datos[(i)*(columna+1) + (j+1)]);
//							System.out.print(matriz[i][j]);
//							System.out.print(" ");
//							System.out.println((i)*(fila-1) + (j+1));
							}
					}
						
						
					else {
//						System.out.print(datos[(i)*(columna) + (j)]);
						matriz[i][j] = Integer.parseInt(datos[(i)*(columna) + (j)]);}
						
			}
		}
		
		if(tag!="Marcado") {
			for (int i=0;i<fila;i++) {
				for (int j = 0; j<columna;j++) {
					System.out.print(matriz[i][j]);
					if(matriz[i][j]<0) {
						System.out.print(" ");
					}
					else {
						System.out.print("  ");
					}
				}
				System.out.printf("%n");
			}
		}

		System.out.printf("%n");
		ldatos.put(tag, matriz);
	}
	

	public int[][] getInhibicion() {
		return ldatos.get("Inhibicion");
	}

	public int[][] getIncidencia() {
		return ldatos.get("Incidencia");
	}
	public int[][] getMarcado() {
		return ldatos.get("Marcado");
	}

	public int[][] getPInvariante() {
		return ldatos.get("Pinvariante");
	}
	public int[][] getTInvariante() {
		return ldatos.get("Tinvariante");
	}

}