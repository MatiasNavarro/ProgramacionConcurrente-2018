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
	 * Metodo politicaAleatoria. Implementa la decision de cual disparar en base a la aleatoriedad.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @param flagInmediatas boolean que indica si existen transiciones inmediatas con posibilidad de dispararse. 
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	public int politicaAleatoria(int[] lista_m){
	
			int indice=0;
			boolean flag=true;
			if(lista_m[24] == 1) {
				indice = 24;
				return indice;
			}
			if(lista_m[23] == 1) {
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
	
	/**
	 * Metodo politica1. Implementa la decision de cual disparar en base a de si el piso 1 esta lleno o no.
	 * en caso de que el La transicion que prende el cartel este sensibilizada y en la cola, tiene prioridad sobre toda la red.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	/*private int politica1(int[] lista_m){
		
		int indice;	
		if(lista_m[24] == 1) {
				indice = 24;
				return indice;
			}
			
			if((lista_m[6]==1) && (lista_m[7]==1)){ //si la transicion 6 y la 7 estan sensibilizadas
				int lista_aux[] = new int [lista_m.length];
				lista_aux = lista_m.clone();
				lista_aux[6] = 0; //la transicion 6 deja de estar sensibilizada asi van al piso 1
				return this.politicaAleatoria(lista_aux);
			}

			
		    return this.politicaAleatoria(lista_m); //Si no esta definida la prioridad, se utiliza la aleatoriedad.
			
	
	}*/
	
	/**
	 * Metodo politica2. Implementa la decision de cual disparar en base a darle prioridad a la salida 2 sobre la 1.
	 * en caso de que el La transicion que prende el cartel este sensibilizada y en la cola, tiene prioridad sobre toda la red.	 
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	/*private int politica2(int[] lista_m){
		System.out.println("-------------------------------------------------------------------------------------------------------------");
		int indice;	
		if(lista_m[24] == 1) {
				indice = 24;
				return indice;
			}
		if((lista_m[17]==1 || lista_m[18]==1) && (lista_m[19]==1 || lista_m[20]==1)) {
			int lista_aux[] = new int [lista_m.length];
			lista_aux = lista_m.clone();
			lista_aux[19] = 0; //la transicion 44 deja de estar sensibilizada asi eligen salida 2
			lista_aux[20] = 0; //la transicion 45 deja de estar sensibilizada asi eligen salida 2
			return this.politicaAleatoria(lista_aux);
		}
		
		return this.politicaAleatoria(lista_m); 
	}*/
	
	
	
	/**
	 * Metodo cualDisparar. Metodo que indica cual transicion se dispara segun el modo de politica elegido.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 * @throws IndexOutOfBoundsException en caso de que lista_m sea una lista vacia.
	 */
	/*public int cualDisparar(int[] lista_m) throws IndexOutOfBoundsException{
		if(lista_m.length>0){
			if(this.modo_politica==0){ //Politica aleatoria.
				return politicaAleatoria(lista_m);
			}
			else if(this.modo_politica==1){ //Politica 1: autos eligen piso 1
				return politica1(lista_m);
			}
			else if(this.modo_politica==2){ //Politica 2: autos eligen salida 2
				return politica2(lista_m);
			}
			else{
				return 0;
			}
		}
		else{
			throw new IndexOutOfBoundsException("Lista M vacia.");
		}
		
		
		
	}*/
	
	/**
	 * Metodo checkDisparo. Metodo que devuelve false en caso de que la transicion a disparar sea sobre la cual se desensibiliza mediante la politica.
	 * @param Vs es el vector de transiciones sensibilizadas
	 * @return int transicion que representa a la transicion a disparar del vector de transiciones
	 */
	public boolean  checkDisparo(int[]vs, int transicion) {
		
		if(this.modo_politica==0){ //Politica aleatoria.
			return true;
		}
		else if(this.modo_politica==1){ //Politica 1: autos eligen piso 1
			if(vs[6]==1 && vs[7]==1 && transicion == 6)
				return false;
			else
				return true;
		}
		else{ //Politica 2: autos eligen salida 2
			if(transicion == 19 || transicion ==20 )
				return false;
			else
				return true;	
		}


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
