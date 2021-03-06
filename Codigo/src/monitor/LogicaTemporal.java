
package monitor;






public class LogicaTemporal {
	
	/*
	 * Atributos de la clase LogicaTemporal
	 */
	private int cantidad_de_transiciones;
	private int[][] vector_de_intervalos; //Por cada transicion, contiene el alfa y beta
	private Cronometro[] vector_de_time_stamps;
	private int [] vector_z; //Contiene un uno si el contador esta entre alfa y beta. De lo contrario un cero.
	
	
    /**
	 * Constructor de LogicaTemporal, se inicializan las variables (Arreglos y Matrices) segun la cantidad de transiciones de la red.
	 * @param cantidad_de_transiciones cantidad de transiciones de la red.
	 */
	public LogicaTemporal(int cantidad_de_transiciones){
		this.cantidad_de_transiciones=cantidad_de_transiciones;
		this.vector_de_time_stamps=new Cronometro[cantidad_de_transiciones];
		for (int i= 0; i < this.cantidad_de_transiciones; i++) {
			this.vector_de_time_stamps[i] = new Cronometro();
        }
		this.vector_z=new int[this.cantidad_de_transiciones];
		
	}
	
	
	/**
	 * Metodo setVectorIntervalos.
	 * se setea el vector que contienen los valores de alfa y beta de las transiciones.
	 * @param flag, boolean que nos indicara si el programa sera considerando logica temporal o no. 
	 */ 
	public void setVectorIntervalos(boolean flag){
		 this.vector_de_intervalos = new int[this.cantidad_de_transiciones][2];
		 for (int i = 0; i < this.cantidad_de_transiciones; i++) {
		     for (int j = 0; j < 2; j++) {
		    	 if(j==0)
		    		 this.vector_de_intervalos [i][j]=0;
		    	 else
		    		 this.vector_de_intervalos [i][j]=-1;
		     }
		 }
		 if(flag) {
		 this.vector_de_intervalos[15][0]=3000;
	     this.vector_de_intervalos[15][1]=-1;
	     this.vector_de_intervalos[16][0]=3000;
	     this.vector_de_intervalos[16][1]=-1;
		 }
	}
	
	/**
	 * Metodo getVectorDeIntervalos
	 * @return int[][] Copia del vector de intervalos de tiempo.
	 */
	public int[][] getVectorDeIntervalos(){
		return this.vector_de_intervalos.clone();
	}
	
	
	/**
	 * Metodo updateTimeStamp
	 * Actualiza el vector de timeStamps o contadores.
	 * @param t_sensibilizadas_antes_disparar Vector de transiciones sensibilizadas antes de disparar la transicion elegida
	 * @param t_sensibilizadas_despues_disparar Vector de transiciones sensibilizadas despues de disparar la transicion elegida ( E AND B and L and C)
	 * @param t_a_disparar Transicion elegida para disparar
	 */
	public void updateTimeStamp(int[] t_sensibilizadas_antes_disparar, int[] t_sensibilizadas_despues_disparar,  int t_a_disparar){
		
		/*
		 * Con este primer if, lo que se hace es analizar cuales transiciones estan sensibilizadas al iniciar la red (sin haber disparado
		 * ni una transicion aun). Aquellas que se encuentren sensibilizadas (por tokens en plaza), se inicia el contador de 
		 * la transicion con setNuevoTimeStamp().
		 */
		if(t_a_disparar==-1) { // Se indica el nicio de red con -1 en el parametro transicion a disparar (no se dispara ninguna t)
			for (int transicion = 0; transicion < this.cantidad_de_transiciones; transicion++) {
				if(t_sensibilizadas_antes_disparar[transicion]==1) {
					this.vector_de_time_stamps[transicion].setTimeStamp();
				}

			}
		}
		
		/*
		 * De lo contrario, si la red tiene un marcado diferente al inicial (se disparo alguna transicion)
		 * se realiza lo siguiente:
		 */
		for (int transicion = 0; transicion < this.cantidad_de_transiciones; transicion++) { //Se recorren todas las transiciones
			
			if(t_sensibilizadas_antes_disparar[transicion]==1 && t_sensibilizadas_despues_disparar[transicion]==1) { //Si la transicion se encontraba sensibilizada antes del disparo y sigue sensibilizada despues de disparar, no se resetea el contador de la misma 
				if(!(transicion==t_a_disparar)) { //A MENOS QUE justamente sea esa la transicion disparada (tiene que reinizializar su cronometro)
					this.vector_de_time_stamps[transicion].setTimeStamp(); //continua la cuenta
				}
			}
			
			//Si la transicion estaba sensibilizada antes de disparar, y despues del disparo no se encuentra sensibilizada, el contador de la misma se pone a 0
			// (tiene que esperar sensibilizarse nuevamente para iniciar la cuenta).
			else if(t_sensibilizadas_antes_disparar[transicion]==1 && t_sensibilizadas_despues_disparar[transicion]==0) {
				this.vector_de_time_stamps[transicion].resetearContador();
			}
			
			//Si no estaba sensibilizada y despues de disparar si lo esta, se inicia la cuenta del cronometro alfa con setNuevoTimeStamp.
			else if(t_sensibilizadas_antes_disparar[transicion]==0 && t_sensibilizadas_despues_disparar[transicion]==1) {
				this.vector_de_time_stamps[transicion].setTimeStamp();
			}
			
			//Si no estaba sensibilizada y sigue sin estarlo despues del disparo, no se empieza la cuenta del cronometro alfa.
			else if(t_sensibilizadas_antes_disparar[transicion]==0 && t_sensibilizadas_despues_disparar[transicion]==0) {
				this.vector_de_time_stamps[transicion].resetearContador();
			}
		
		}
		this.updateVectorZ(t_sensibilizadas_despues_disparar); // (E AND B AND L AND C)
	}
	
	
	/**
	 * Metodo updateVectorZ
	 * Actualiza el vector Z.
	 * @param q Vector resultante de hacer E AND B and L and C
	 */
	public void updateVectorZ(int[] q){
		
		for (int i= 0; i < this.cantidad_de_transiciones; i++) { //recorro todas las transiciones y hago and entre las sensibilizadas por E AND B y su ventana de tiempo
			if(isInWindowsTime(i) & q[i]==1) { 
				vector_z[i]=1;
			}
			else {
				vector_z[i]=0;
			}
		}
	}
	
	/**
	 * Metodo getVectorZ_Actualizado
	 * Permite obtener la ultima version del vector Z
	 * @param  q Vector resultante de hacer E AND B and L and C
	 * @return int[] Ultima version del vector Z.
	 */
	public int[] getVectorZ_Actualizado(int[] q){
		this.updateVectorZ(q);
		return this.vector_z;
	}
	
	
	/**
	 * Metodo isInWindowsTime
	 * @param transicion transicion a determinar si su contador se encuentra dentro de la ventana de tiempo
	 * @return boolean true si se encuentra dentro de la ventana, de lo contrario false.
	 * @throws IllegalArgumentException En caso de transicion invalida
	 */
	public boolean isInWindowsTime(int transicion) throws IllegalArgumentException{
		
		if(transicion>this.cantidad_de_transiciones) {
			throw new IllegalArgumentException("Transicion invalida");	
		}
		
		//Comparaciones necesarias para verificar si una transicion esta en su intervalo de disparo
		boolean comparacion1=this.vector_de_time_stamps[transicion].getMillis()>=(long)vector_de_intervalos[transicion][0];  //es el tiempo actual mayor que alfa?
		boolean comparacion2=this.vector_de_time_stamps[transicion].getMillis()<=(long)vector_de_intervalos[transicion][1];  //es el tiempo actual menor que beta?
		
		//Comparaciones necesarias para saber si es una transicion inmediatas
		boolean comparacion4=(long)vector_de_intervalos[transicion][0]==0;			//es alfa 0?
		boolean comparacion3=(long)vector_de_intervalos[transicion][1]==(long)-1;  //es beta -1?
		
		if((comparacion1&&(comparacion2||comparacion3))||construirVectorTransicionesInmediatas()[transicion]==1||comparacion4) { //si el tiempo actual es mayor que alfa y beta -1 o si es inmediata o alfa cero
			return true;
		}
		else {
			return false;
		}
	
	}
	
	
	/**
	 * Metodo construirVectorTransicionesInmediatas.
	 * Genera un vector de transiciones indicando con un uno si la transicion es inmediata o con un cero si no lo es.
	 * Lo que hace es fijarse si la transicion correspondiente tiene alfa 0 y beta -1
	 * @return int[] vector de transiciones inmediatas
	 */
	public int[] construirVectorTransicionesInmediatas(){
		int aux[]=new int[this.cantidad_de_transiciones];
		for(int i=0; i < this.cantidad_de_transiciones;i++){
			if(this.vector_de_intervalos[i][0]==0 & this.vector_de_intervalos[i][1]==-1){
				aux[i]=1;
			}
			else{
				aux[i]=0;
			}
				
		}
		return aux;
	}
	
	
	/**
	 * Metodo getTiempoFaltanteParaAlfa. 
	 * @param transicion 
	 * @return long Tiempo faltante que posee el contador de la transicion para alcanzar el valor de alfa. 
	 * Devuelve cero en caso de haber alcanzado o superado dicho valor.
	 * @throws IllegalArgumentException En caso de transicion invalida
	 */
	public long getTiempoFaltanteParaAlfa(int transicion) throws IllegalArgumentException{
		
		if(transicion>this.cantidad_de_transiciones) {
			throw new IllegalArgumentException("Transicion invalida");	
		}
		
		boolean comparacion1=this.vector_de_time_stamps[transicion].getMillis()>=(long)vector_de_intervalos[transicion][0];
		boolean comparacion2=this.vector_de_time_stamps[transicion].getMillis()<=(long)vector_de_intervalos[transicion][1];
		
		boolean comparacion3=(long)vector_de_intervalos[transicion][1]==(long)-1;
		boolean comparacion4=(long)vector_de_intervalos[transicion][0]==0;
		
		if((comparacion1&&(comparacion2||comparacion3))||construirVectorTransicionesInmediatas()[transicion]==1||comparacion4) {
			return 0;
		}
		else {
			if((this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis())<=0) {
				return (long)0;
			}
			else {
				return ((long)this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis());
			}
		}
	
	}
}