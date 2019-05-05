
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class ca1DSimulator implements ca1DSim, Runnable{
    
    /** COMENTARIO SOBRE EL DIBUJADO DEL AUTOMATA 
     * Al tratarse de un automáta 1D, podemos representar la evolución gráfica en 1D (eje x o eje Y)
     * De este modo, dejamos fija una de las coordenadas y evolucionamos la célula en función de la otra coordenada realizando el escalado al tamaño
     * del Canvas empleado.
     */
    
    private static final int numMaxCell = 1000;
    
    private static Object obj = new Object();
    
    private static int [] stringA = new int[numMaxCell];
    private static int [] stringB = new int[numMaxCell];
        
    private int tipoFrontera; // 0 = frontera nula; 1 = cilindrica;
    private String algoritmoSeleccionado;
    private int fTransition;
    private int estadoMax;
    private int rangoVecindad;
    private randomGenerator g;
    private GUI_MC.panelDibujo canva;
    private GUI_MC.panelGrafico graf;
    private GUI_MC.dibujoEntropia dibEntropia;
    private int gen = 0;
    private ExecutorService e; 
    
    private int [] numCellsType;
    
    //static private int [][] celulasGen;    
    static private int [][] resultados;
    
    static private ArrayList<Integer> configuraciones;
    
    //Parametros para hilos
    private int inf, sup;
    static private CyclicBarrier b;
    
    static private int nGen;
    
    static int [] conteo;
    
    static boolean ready;
    
    ca1DSimulator(int e)
    {
        tipoFrontera = 0;
        algoritmoSeleccionado = "generatorCombinated";
        g = new randomGenerator();
        estadoMax = e;
        fTransition = 0;
        rangoVecindad = 1;
        canva = null;
        this.graf = null;
        dibEntropia = null;
        numCellsType = new int[e];
        inf = 0;
        sup = 0;
        configuraciones = new ArrayList<Integer>();
        reglaGeneral();
        ready = false;
    }
    
    ca1DSimulator(int tP, String aS, randomGenerator generador, int e, int trans, int rango, GUI_MC.panelDibujo dib, GUI_MC.panelGrafico graf, GUI_MC.dibujoEntropia entropia, int i, int s)
    {
        tipoFrontera = tP;
        algoritmoSeleccionado = aS;
        g = generador;
        estadoMax = e;
        fTransition = trans;
        rangoVecindad = rango;
        canva = dib;
        this.graf = graf;
        dibEntropia = entropia;
        numCellsType = new int[e];
        inf = i;
        sup = s;
        configuraciones = new ArrayList<Integer>();
        reglaGeneral();
        this.e = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ready = false;
    }
    
    private void iniciaString()
    {
        switch(algoritmoSeleccionado)
        {
            case "generator_26.1a":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.generator261a() * estadoMax);
                }                  
            };break;
            
            case "generator_26.1b":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.generator261b() * estadoMax);
                }
            };break;
            
            case "generator_26.2":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.generator262() * estadoMax);
                }
            };break;
            
            case "generator_26.3":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.generator263() * estadoMax);
                }
            };break;
                        
            case "generatorCombinated":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.generadorCombinado() * estadoMax);
                }
            };break;
            
            case "fishmanMoore_1":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.fishmanMoore_1() * estadoMax);
                }
            };break;
            
            case "fishmanMoore_2":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.fishmanMoore_2() * estadoMax);
                }
            };break;
            
            case "randuGenerator":
            {
                for(int i = 0 ; i < stringA.length ; i++)
                {
                    stringA[i] = (int) (g.Randu() * estadoMax);
                }
            }
            ;break;
            
            default: exit(1); break;
        }
        
        /**
        System.out.println("CONFIGURACION INICIAL: ");
        
        for(int i = 0 ; i < stringA.length ; i++)
            System.out.print("[" + stringA[i] + "] ");
        
        System.out.println();
          */
    }
   
    /** MODIFICAR LAS REGLAS AÑADIENDO INTERVALOS PARA LA EJECUCIÓN EN PARARELO DE LA SIMULACIÓN CELULAR. **/
    
    public void reglaGeneral()
    {
        int n = fTransition;
        ArrayList<Integer> aux = new ArrayList<Integer>();
        while(n != -1)
        {
            if(n==0) 
            {
                n = -1;
                aux.add(0);
            }
            else
            {
                aux.add(n % estadoMax);
                n /= estadoMax;                   
            }
        }
        
        while(aux.size() < 8)
            aux.add(0);

        for(int i = aux.size() - 1 ; i >= 0 ; i--)
        {
            configuraciones.add(aux.get(i));
        }
        
        if(configuraciones.size() > 8)
            configuraciones.remove(0);
        
                
    }
    
    public int numeroEstados()
    {
        return (int) Math.pow(estadoMax, (2*rangoVecindad + 1));
    }
          
    public int numCells(int type)
    {
        return numCellsType[type];
    }
    
    private void copiaVec()
    {
        for(int i = inf ; i < sup ; i++)
        {
            stringA[i] = stringB[i];
        }
    }
    
    @Override
    public void nextGen()
    {   
        switch(tipoFrontera)
        {
            case 0:
            {
                gen++;
                for(int j = inf ; j < sup ; j++)
                {
                    int nVecinos = 2*rangoVecindad;
                    int d = (int) Math.pow(10, nVecinos);
                    int num = 0;    //numero binario
                    int numD = 0;   //numero decimal
                    for(int i = j - rangoVecindad ; i <= j + rangoVecindad ; i++)
                    {
                        if(i >= 0 && i < stringA.length)
                        {
                            num += (stringA[i] * d);
                            d /= 10;
                        }
                        else
                        {
                            //num *= 10;
                            d /= 10;
                        }
                    }

                    numD = Integer.parseInt(String.valueOf(num), estadoMax);
                    //System.out.println("BINARIO: " + num + ", Decimal: " + numD + ". Configuracion: " + configuraciones.get(numD % configuraciones.size()));
                    if(configuraciones.size() != 0) stringB[j] = configuraciones.get(numD % configuraciones.size());
                    synchronized(obj) {conteo[stringB[j]]++;}
                }
                try { b.await(); } catch(Exception e) {}
                copiaVec();
                for(int i = inf ; i < sup ; i++)
                    resultados[gen - 1][i] = stringA[i];
            };break;
            
            case 1:
            {
                gen++;
                for(int j = inf ; j < sup ; j++)
                {
                    int nVecinos = 2*rangoVecindad;
                    int d = (int) Math.pow(10, nVecinos);
                    int num = 0;    //numero binario
                    int numD = 0;   //numero decimal
                    for(int i = j - rangoVecindad ; i <= j + rangoVecindad ; i++)
                    {
                        if(i >= 0 && i < stringA.length)
                        {
                            num += (stringA[i] * d);
                            d /= 10;
                        }
                        else
                        {
                            if(i >= stringA.length) num += (stringA[i % stringA.length] * d);
                            else num += (stringA[i + stringA.length] * d);
                            d /= 10;
                        }
                    }

                    numD = Integer.parseInt(String.valueOf(num), estadoMax);
                    //System.out.println(num + ", " + numD);
                    stringB[j] = configuraciones.get(numD % configuraciones.size());   
                    synchronized(obj) {conteo[stringB[j]]++;}                    
                }
                try { b.await(); } catch(Exception e) {}
                copiaVec();
                for(int i = inf ; i < sup ; i++)
                    resultados[gen - 1][i] = stringA[i];                
            }
        }        
    }    
     
    @Override
    public void caComputation(int nGen)
    {      
        
        //while(!ready) {}
        
        gen = 0;
        resultados = new int[nGen][numMaxCell];
        conteo = new int[estadoMax];
        
        for(int i = 0 ; i < conteo.length ; i++) conteo[i] = 0;
        
        //celulasGen = new int[nGen][estadoMax];
        
        //for(int i = 0 ; i < nGen ; i++)
        //    for(int j = 0 ; j < estadoMax ; j++)
        //        celulasGen[i][j] = 0;
        
        iniciaString();
        
        System.out.print("Configuracion de la regla " + fTransition +": ");
        for(int i = 0 ; i < configuraciones.size() ; i++)
            System.out.print(configuraciones.get(i) + " ");
        
        Thread [] hilos = new Thread[Runtime.getRuntime().availableProcessors()];
        b = new CyclicBarrier(hilos.length);
        canva.nGen(nGen);
        this.nGen = nGen;
        
        int inf = 0, sup = numMaxCell / hilos.length;
        for(int i = 0 ; i < hilos.length ; i++)
        {
           hilos[i] = new Thread(new ca1DSimulator(tipoFrontera, algoritmoSeleccionado, g, estadoMax, fTransition, rangoVecindad, canva,graf, dibEntropia, inf, sup)); 
           inf += (numMaxCell/hilos.length);
           sup += (numMaxCell/hilos.length);
           e.execute(hilos[i]);
        }
            
        e.shutdown();
        try { e.awaitTermination(1L, TimeUnit.DAYS); } catch(Exception e) {}
        
        canva.parse(resultados);
        
        /**
        
        for(int i = 0 ; i < nGen ; i++)
            nextGen();
        **/
        
        new Thread() { public void run() {graf.parseData(resultados, nGen, estadoMax, numMaxCell);}}.start();
        canva.repaint();
    }
    
    @Override
    public void run()
    {
        for(int i = 0 ; i < nGen ; i++)
        {
            //System.out.println(i);
            nextGen();
            try { b.await(); } catch(Exception e){}
        }
    }
    
    
    //** GENERALIZAR PARA +nGen **/
    public double getEntropia(int cell)
    {
        
        int [] ocur = new int[estadoMax];
        double [] p = new double[estadoMax];
        
        //System.out.println("CELL: " + cell);
        
        for(int i = 0 ; i < nGen ; i++)
        {
            ocur[resultados[i][cell]]++;
         }
        
        for(int i = 0 ; i < p.length ; i++)
            p[i] = ocur[i]/(double)nGen;
            //p[i] = conteo[i]/(double)(nGen * numMaxCell);
        
        double sumEntropia = 0;
        for(int i = 0 ; i < nGen ; i++)
        {
            double pxi = p[resultados[i][cell]];
            sumEntropia += (pxi * (Math.log(pxi)/Math.log(estadoMax)));
            //System.out.println((double) (Math.log10(pxi)/Math.log10(2)));
        }
        
        //System.out.print("Conteo: " + ocur[0]);
        //for(int i = 1 ; i < ocur.length ; i++)
        //System.out.print(", " + ocur[i]);
        
        return -1*sumEntropia/nGen;
    }
    
    public void dibujaEntropia()
    {
        dibEntropia.cargaDatosEntropia(estadoMax, numMaxCell, conteo, nGen, resultados);
    }
    
    //Es necesario que empiece por gen = 1;
   public int dHamming(int gen)
   {
       int d = 0;
       
       if(gen >= nGen) 
       {
           d = -1;
       }
       else
       {
           for(int i = 0 ; i < numMaxCell; i++)
           {
               if(resultados[gen-1][i] != resultados[gen][i])
                   d++;
           }
       }
       
       return d;
   }
    
   public int [] generacionSinPintado()
   {
        gen = 0;
        this.nGen = 4000;
        resultados = new int[nGen][numMaxCell];
        conteo = new int[estadoMax];
        Thread [] hilos = new Thread[Runtime.getRuntime().availableProcessors()];
        ExecutorService e = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());    
        b = new CyclicBarrier(hilos.length);
        
        for(int i = 0 ; i < conteo.length ; i++) conteo[i] = 0;
        //iniciaString();
               

        int inf = 0, sup = numMaxCell / hilos.length;
        for(int i = 0 ; i < hilos.length ; i++)
        {
           hilos[i] = new Thread(new ca1DSimulator(tipoFrontera, algoritmoSeleccionado, g, estadoMax, fTransition, rangoVecindad, canva,graf, dibEntropia, inf, sup)); 
           inf += (numMaxCell/hilos.length);
           sup += (numMaxCell/hilos.length);
           e.execute(hilos[i]);
        }
            
        e.shutdown();
        try { e.awaitTermination(1L, TimeUnit.DAYS); } catch(Exception ex) {}
        
        int [] a = new int[nGen];
        for(int i = 0 ; i < a.length ; i++)
            a[i] = resultados[i][(numMaxCell-1)/2];
        
       // for(int i = 0 ; i < 4000 ; i++)
        //    for(int j = 0 ; j < 1000 ; j++)
         //       System.out.print("["+resultados[i][j]+"] ");
        
        
        return a;
        
   }
   
   void changeRule(int r)
   {
       fTransition = r;
   }
   
   double entropiaEspacial()
   {
            double [] p = new double[estadoMax];
            double sumas = 0;
            
            //System.out.println(conteo.length);
            
            for(int i = 0 ; i < conteo.length ; i++)
                p[i] = conteo[i]/(double)(nGen * numMaxCell);
            
            for(int i = 0 ; i < nGen ; i++)
            {
                double sum = 0;
                for(int j = 0 ; j < numMaxCell ; j++)
                {
                    double pxi = p[resultados[i][j]];
                    //System.out.println("pxi: " +pxi);
                    sum += (pxi * (Math.log10(pxi)/Math.log10(2)));
                }
                //System.out.println(sum);
                sumas += (sum * (-1/numMaxCell));
            }            
            
            return (sumas/nGen);
                
            
   }
   
   public void setEstadoInicial(String s)
   {
       int c[] = new int[numMaxCell];
       //int c[] = new int[4000 * 1000];
       
       for(int i = 0 ; i < c.length ; i++)
       {
           if(i < s.length()) c[i] = ((int) s.charAt(i)) % 2;
           else c[i] = 0;
       }
       
       for(int i = 0 ; i < stringA.length ; i++)
       {
           stringA[i] = c[i];
       }
       
   }
   
}
