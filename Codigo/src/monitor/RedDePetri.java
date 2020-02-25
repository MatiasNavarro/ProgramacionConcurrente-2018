package monitor;



import logueo.Logger;
import util.Parser;
import util.Data;
import monitor.Politica;





public class RedDePetri{
	private String path;
	private int cantidad_transiciones_inmediatas;

	private int[][] I; // Matriz de incidencia (Plazas x Transiciones)
	private int[][] M; // Matriz de marcado. 
	private int[][] pinvariantes; //Matriz de P-Invariantes
	private int[][] tinvariantes; //Matriz de T-Invariantes
	private int[] constante_pinvariante;
	private int[][] H; //Matriz H.
	private int[] B; //Matriz B. B= H * Q
	private int[][] Q; //Vector Q
	private int[] L;
	private int[][] R;
	private LogicaTemporal logica_temporal;
	private boolean flagLogica;
	private int[] transiciones_inmediatas; //Un uno indica que la transicion es inmediata.
	private Logger log;

	private String salto_linea;

	//Verificacion T-Invariantes

	private int[][] M0; //Marcado inicial
	private int[][] suma_disparos_transiciones;
	private int contadorTransicionesDisparadas;
	private Data data;
	boolean flagImpresionVerifTInv=false;



	private static final RedDePetri instance =new RedDePetri(); 

	private RedDePetri(){
		this.setMatrices();
		
		salto_linea="\n";

		setCantTransiciones(I[1].length);

		this.B=getMatrizB_Actualizada(); //Calculo Matriz B
		this.L=getMatrizL_Actualizada();

		
		this.M0=this.M.clone(); //Marcado inicial
		
		this.data = Data.getInstance();





		//Sumatoria de todos los disparos efectuados por transicion.
		suma_disparos_transiciones=new int[this.getCantTransiciones()][1]; 
		for(int i=0; i<suma_disparos_transiciones.length;i++){ //Reseteo vector.
			suma_disparos_transiciones[i][0]=0;
		}

		contadorTransicionesDisparadas=0;
//		this.setLogEventos(log);
	}
	
	public static RedDePetri getInstance() {
		return instance;
	}

	/**
	 * Metodo getContadorTransicionesDisparadas.
	 * @return int Cantidad de transiciones disparadas
	 */
	public int getContadorTransicionesDisparadas(){
		return this.contadorTransicionesDisparadas;
	}


	public int[][] getSumaDisparosTransiciones(){
		return this.suma_disparos_transiciones;
	}


	public int[][] getMarcadoInicial(){
		return this.M0;
	}



	public int[] getVectorTransicionesInmediatas(){
		return this.transiciones_inmediatas;
	}


	public void setTransicionesInmediatas(){
		this.transiciones_inmediatas=this.logica_temporal.construirVectorTransicionesInmediatas();
	}
	
	public void setFlagLogica(boolean f) {
		this.flagLogica=f;
	}
	public void configLogicaTemporal() {
		logica_temporal=new LogicaTemporal(this.getCantTransiciones());
		this.logica_temporal.setVectorIntervalos(flagLogica); //Seteo intervalos 
		setTransicionesInmediatas();
		this.logica_temporal.updateTimeStamp(this.getConjuncionEAndBandLandC(), this.getConjuncionEAndBandLandC(),  -1);
	}


	/**
	 * Metodo getMatrizM. Usado unicamente para Test.
	 * @return int[][] Marcado actual de la red.
	 */
	public int[][] getMatrizM(){
		return M;
	}

	/**
	 * Metodo getMatrizH. Usado unicamente para Test.
	 * @return int[][] Matriz de inhibicion.
	 */
//	private int[][] getMatrizH(){
//		return H;
//	}

	public int[][] getPInv(){
		return pinvariantes.clone();
	}

	public int[][] getTInv(){
		return tinvariantes;
	}


	/**
	 * Metodo setCantTransiciones. Permite setear la cantidad de transiciones de la red.
	 * @param cantidad Cantidad de transiciones de la red
	 */
	private void setCantTransiciones(int cantidad){
		this.cantidad_transiciones_inmediatas=cantidad;
	}

	/**
	 * Metodo getCantTransiciones. 
	 * @return int Cantidad de transiciones de la red
	 */
	public int getCantTransiciones(){ //Esto es lo unico que esta OK.
		return cantidad_transiciones_inmediatas;
	}

	/**
	 * Metodo getSensibilizadas(). Permite obtener el vector de transiciones sensibilizadas. (Vector E)
	 * @return ArrayList<Integer> lista con enteros 1 y 0 indicando transiciones sensibilizadas o no, respectivamente.
	 */
	public int[] getSensibilizadas() {
		int[] transicionesSensibilizadas = new int[getCantTransiciones()];

		for (int transicion = 0; transicion < getCantTransiciones(); transicion++) {
			try {
				if (esDisparoValido(getMarcadoSiguiente(transicion))) {
					transicionesSensibilizadas[transicion]=1;
				}
				else{
					transicionesSensibilizadas[transicion]=0;
				}
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.out.println("Transicion u operaciones matriciales invalidas");
			}
			catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("Marcado null");
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error en getSensibilizadas()");
			}

		}


		return transicionesSensibilizadas;
	}


	/**
	 * Metodo disparar. Permite el disparo de una determina transicion.
	 * @param transicion Transicion a disparar
	 * @return boolean true si la transicion se disparo. False en caso contrario.
	 */
	public boolean disparar(int transicion){ 

		int[][] marcado_siguiente = this.getMarcadoSiguiente(transicion);
		//System.out.println(this.getSensibilizadasExtendido()[transicion]==1);
		if (this.getSensibilizadasExtendido()[transicion]==1) {
			int[] transSensAntesDisparo=this.getConjuncionEAndBandLandC();
			M = marcado_siguiente; //Asignacion del nuevo marcado
			
			//imprimo en consola lo que guardo en logD
			System.out.println(data.getDiccionario().get(transicion) + " nro: "+ transicion + salto_linea);
			
			this.log.addMessage(data.getDiccionario().get(transicion), 3);
			this.log.addMessage(salto_linea, 3);
			this.logica_temporal.updateTimeStamp(transSensAntesDisparo, this.getConjuncionEAndBandLandC(),  transicion);
			try{
				this.verificarPInvariantes(); // En cada disparo se verifica que se cumplan las ecuaciones del P-Invariante

				//this.transicionesDisparadas.add(transicion);
				suma_disparos_transiciones[transicion][0]++;
				this.contadorTransicionesDisparadas++;
				//Logueo el contador de transiciones disparadas
				this.log.createMessage("Cantidad de transiciones disparadas:"+salto_linea+String.valueOf(this.contadorTransicionesDisparadas), 2);
				//Logueo la transicion disparada
				this.log.addMessage(String.valueOf(transicion)+ new String("/n"), 1);

			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Error en invariantes");
				System.exit(1);
			}

			this.log.addMessage(salto_linea+"Transicion a disparar: " + transicion+ salto_linea, 0); //Log de transicion a disparar

			//Logueo valor de K y marcado resultante
			this.log.addMessage(new String("Valor de K: true"+salto_linea), 0);
			this.log.addMessage(new String("Marcado PostDisparo:"+salto_linea), 0);
			for(int plaza=0;plaza<this.M.length;plaza++){
				this.log.addMessage(String.valueOf(this.M[plaza][0])+"-", 0);
			}
			this.log.addMessage(salto_linea, 0);
			return true;

		}

		this.log.addMessage(salto_linea+"Transicion a disparar: " + transicion+ salto_linea, 0); //Log de transicion a disparar
		//Logueo valor de K y marcado resultante
		//System.out.println(transicion);
		this.log.addMessage("Valor de K: false"+salto_linea, 0);
		this.log.addMessage("Marcado PostDisparo: "+salto_linea, 0);
		for(int plaza=0;plaza<this.M.length;plaza++){
			this.log.addMessage(String.valueOf(this.M[plaza][0])+"-", 0);
		}
		this.log.addMessage(salto_linea, 0);
		return false;


	}


	/**
	 * Metodo verificarPInvariantes.
	 *  El objetivo es verificar que la cantidad de marcas se mantiene constante en las plazas P-invariantes, para ello
	 * 	compara el valor que retorna gerMarcadoPinvariante() con el marcado inicial "constante_pinvariante" obtenida al momento de configurar la red de petri
	 * @throws IllegalStateException
	 */

	private void verificarPInvariantes() throws IllegalStateException{
		for (int i = 0; i < pinvariantes.length; i++) {	//Recorro todas las filas del pinvariantes
			if(getMarcadoPinvariante()[i]!=this.constante_pinvariante[i]) { //verifico que cada solucion del P-invariante con el marcado actual sea igual a la solucion arrojada al momento de configurar la red de petri en "constante_pinvariante"
				throw new IllegalStateException("Error en los pinvariantes");	//en otras palabras, si el marcado en esas plazas resulta diferente, se dispara IllegalStateException
			}
		}
	}


	/**
	 * Metodo gerMarcadoPinvariante.
	 * @return int[] Vector que contiene las soluciones a las ecuaciones de los P-invariantes
	 */
	private int[] getMarcadoPinvariante() {
		int[] resultado=new int[pinvariantes.length];			//Inicializacion del vector resultado
		for (int i = 0; i < pinvariantes.length; i++) {			//Recorro las filas del vector pinvariantes
			for (int j = 0; j < pinvariantes[0].length; j++) {	// 'j' representa cada columna del vector pinvariantes[fila]
				resultado[i]=resultado[i]+(this.pinvariantes[i][j]*this.M[j][0]); //Multiplico pinvariantes[fila][columna] con marcado[fila][columna]
			}
		}

		return resultado; //retorno resultado, que contiene las soluciones a las ecuaciones de los pinvariantes
	}


	/**
	 * Metodo setMatricesFromExcel. Encargado de cargar las matrices que forman parte de los atributos a partir de un archivo de Excel. 
	 * Matrices colocadas en paginas de Excel:
	 *
	 * Hoja 1:  I+
	 * Hoja 2:  I-
	 * Hoja 3:  I (matriz de incidencia)
	 * Hoja 4:  H
	 * Hoja 5:  M (matriz de marcado)
	 * Hoja 6:  T-invariantes
	 * Hoja 7:  P-invariantes
	 * Hoja 8:  Intervalos [alfa,beta]
	 * Hoja 9:  Prioridades subida
	 * Hoja 10: Prioridades bajada
	 * 
	 * @param path Direccion donde se encuentra el archivo de Excel
	 * 
	 */
	private void setMatrices() {
		Parser myParser = new Parser();
		myParser.leerRed();

		try {
				
				I = myParser.getIncidencia();


				H = myParser.getInhibicion();

				M = myParser.getMarcado();

				tinvariantes = myParser.getTInvariante();

				pinvariantes = myParser.getPInvariante();


				this.constante_pinvariante=getMarcadoPinvariante(); //Obtiene el resultado de las ecuaciones del P-invariante
				//System.out.println(constante_pinvariante.length);


				R = new int [I.length][I[0].length];//Matriz de arcos Lectores
				
				R[27][25]=1;
				
					
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en metodo setMatrices");

		}


	}


	/**
	 * Metodo getMatrizB_Actualizada. 
	 * Matriz B= Matriz H * Vector Q.
	 * @return int[][] Matriz B. Utilizada en la ecuacion de estado.
	 */
	private int[] getMatrizB_Actualizada(){
		this.Q=getVectorQ_Actualizado();
		int Htranspuesta[][]=OperacionesMatricesListas.transpuesta(this.H);
		int aux[][]=OperacionesMatricesListas.productoMatrices(Htranspuesta, this.Q);
		for(int i=0; i<aux.length;i++){
			if(aux[i][0]==0){
				aux[i][0]=1;
			}
			else{
				aux[i][0]=0;
			}
		}

		int Baux[]=new int[aux.length];
		for(int i=0; i<aux.length;i++){
			Baux[i]=aux[i][0];
		}
		
		this.B=Baux;
		

		
		return this.B.clone();
	}
	
	
	
	/**
	 * Metodo getMatrizB_Actualizada. 
	 * Matriz B= Matriz H * Vector Q.
	 * @return int[][] Matriz B. Utilizada en la ecuacion de estado.
	 */
	private int[] getMatrizL_Actualizada(){
		this.Q=getVectorQ_Actualizado();
		int[][] w =new int[this.Q.length][this.Q[0].length];
		for(int i=0;i<this.Q.length;i++) {
			if(this.Q[i][0]==0)
				w[i][0]=1;
			else
				w[i][0]=0;
		}

			
		
		int Rtranspuesta[][]=OperacionesMatricesListas.transpuesta(this.R);
		int aux[][]=OperacionesMatricesListas.productoMatrices(Rtranspuesta, w);
		for(int i=0; i<aux.length;i++){
			if(aux[i][0]==0){
				aux[i][0]=1;
			}
			else{
				aux[i][0]=0;
			}
		}
		
		int Laux[]=new int[aux.length];
		for(int i=0; i<aux.length;i++){
			Laux[i]=aux[i][0];
		}
		


		this.L=Laux;
		return this.L.clone();
	}

	/**
	 * Metodo getVectorQ_Actualizado. 
	 * El vector Q contiene un cero si la marca de la plaza es cero. 
	 * De lo contrario posee un uno en esa posicion. 
	 * @return int[][] Vector Q. 
	 */
	private int[][] getVectorQ_Actualizado(){
//		System.out.print(this.M.length);
		int aux[][]=new int[this.M.length][1];
		for(int i=0;i<this.M.length;i++){
			if(M[i][0]!=0){
				aux[i][0]=1;
			}
			else{
				aux[i][0]=0;
			}
		}
		return aux;
	}

	/**
	 * Metodo getMarcadoSiguiente. Permite obtener el marcado siguiente al disparar una determinada transicion.
	 * @param transicion transicion a disparar para obtener el marcado siguiente
	 * @return int[][] Matriz del marcado siguiente
	 * @throws IllegalArgumentException si el numero de transicion es invalido
	 */
	public int[][] getMarcadoSiguiente(int transicion) throws IllegalArgumentException{
		if (transicion >= this.getCantTransiciones()) {
			throw new IllegalArgumentException("Transicion invalida.");
		}

		//Vector de disparo.

		int[][] deltaDisparo = new int[I[1].length][1];
		deltaDisparo[transicion][0] = 1;

		return OperacionesMatricesListas.sumaMatrices(M,OperacionesMatricesListas.productoMatrices(I,deltaDisparo));
	}




	/**
	 * Metodo esDisparoValido. Permite conocer si el marcado siguiente no tiene ningun elemento negativo, lo cual denota un disparo invalido
	 * @param marcado_siguiente matriz de marcado siguiente obtenido al disparar una determinada transicion
	 * @return boolean true si el arreglo marcado_siguiente pasado como parametro no tiene ningun elemento negativo
	 * @throws NullPointerException si el parametro es null
	 */

	public boolean esDisparoValido(int[][] marcado_siguiente) throws NullPointerException{

		if (marcado_siguiente==null){throw new NullPointerException("Marcado null.");}

		for (int i = 0; i < marcado_siguiente.length; i++) {
			for (int j = 0; j < marcado_siguiente[0].length; j++) {
				if (marcado_siguiente[i][j] < 0) { //Valor negativo. Disparo invalido.
					return false;
				}
			}
		}
		return true;
	}


//	private int[][] getTinvariant() {
//		return this.tinvariantes;
//	}

	public LogicaTemporal getLogicaTemporal() {
		return this.logica_temporal;
	}



	/**
	 * Metodo getSensibilizadasExtendido.
	 * @return int[] Vector Ex= vector Z and vector B and vector E
	 */
	public int[] getSensibilizadasExtendido(){
		int Ex[];
		int E[]= this.getSensibilizadas();

		this.getMatrizL_Actualizada();
		this.getMatrizB_Actualizada();



		int Z[]= logica_temporal.getVectorZ_Actualizado(this.getConjuncionEAndBandLandC());

		Ex=OperacionesMatricesListas.andVector(Z,this.getConjuncionEAndBandLandC());

		
		return Ex;
	}


	/**
	 * Metodo getConjuncionEAndBandL
	 * @return int[] Vector resultante de:  vector E and vector B.
	 */
	public int[] getConjuncionEAndBandLandC(){
		int E[]= this.getSensibilizadas();


		int aux[] = OperacionesMatricesListas.andVector(this.B,E);

		aux= OperacionesMatricesListas.andVector(aux,this.L);
		
		int q[] = OperacionesMatricesListas.andVector(aux, Politica.getGuardas(aux));

		return q;
	}


	/**
	 * Metodo setLogEventos. Permite loguear el marcado inicial de la red.
	 * @param log Logger
	 */
	public void setLogEventos(Logger log){
		this.log=log;
		this.log.createMessage("Evolucion del marcado: "+salto_linea+salto_linea+"Marcado M0: "+salto_linea, 0);
		for(int plaza=0;plaza<M0.length;plaza++){
			this.log.addMessage(String.valueOf(this.M0[plaza][0])+"-", 0);
		}
		this.log.addMessage(salto_linea, 0);
	}


}