package monitor; 


public class Politica { 
	
	private static int modo_politica;
	
	//Modo 0: aleatoria.
    //Modo 1: Primero lleno piso 1
    //Modo 2: Elijo salida 2.
	
	
	
	public Politica(int modo){
		setModo(modo);
			
	}
	

	
	/**
	 * Metodo getModo.
	 * @return int atributo modo_politica
	 */
	public int getModo(){
		return this.modo_politica;
	}
	
	/**
	 * Metodo setModo
	 * @param modo Modo con el que va a decidir la politica
	 */
	public void setModo(int modo){
		this.modo_politica=modo;
	}
	
	/**
	 * Metodo cualDisparo. Implementa la decision de cual disparar en base a la aleatoriedad.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	public int cualDisparo(int[] lista_m){
	
			int indice=0;
			boolean flag=true;
			if(lista_m[24] == 1) { //se le da prioridad absoluta al prendido del cartel
				indice = 24;
				return indice;
			}
			if(lista_m[23] == 1) { //se le da prioridad absoluta al apagado del cartel
				indice = 23;
				return indice;
			}
			while(flag){
				int numero_aleatorio = (int) (Math.random()*100);
				indice=numero_aleatorio % lista_m.length; //Defino una posicion aleatoria.
				
				if(lista_m[indice]!=0){ //Verifico si el numero aleatorio no coincide con una transicion que no se puede disparar
					
						flag=false;
					
				}
				
			}
			return indice;
		
	}
	
	
	
	public static int[] getGuardas(int[] Vs) {
		int[]c = new int[Vs.length];
		for (int i=0;i<c.length;i++)
			c[i]=1;
		if(modo_politica==0){ //Politica aleatoria.
//			return true;
		}
		else if(modo_politica==1){ //Politica 1: autos eligen piso 1
			if(Vs[6]==1 && Vs[7]==1)
				c[6]=0;

		}
		else{ //Politica 2: autos eligen salida 2
			if(Vs[19]== 1 || Vs[20] ==1 ) {
				c[19]=0;
				c[20]=0;
			}
	
		}
		return c;
		
	}
	
}
