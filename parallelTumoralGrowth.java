
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class parallelTumoralGrowth {
    
    private float Ps, Pm, Pp;
    private int NP, nCells, gen;
    
    private Random r;
    
    private static CyclicBarrier barrera = new CyclicBarrier(Runtime.getRuntime().availableProcessors());
    private static CyclicBarrier barrera2 = new CyclicBarrier(Runtime.getRuntime().availableProcessors());
       
    private static int [][] cellsA;
    private static int [][] cellsB;
    private static int [][] cellsPH;
    
    public dibujadoTumor lienzo;
    
    private ExecutorService exec = Executors.newCachedThreadPool();
    
    private class tarea implements Runnable
    {
        
        int inf, sup;
        
        tarea(int i, int s)
        {
            inf = i; sup = s;
        }
        
        @Override
        public void run()
        {
            simulacion(inf, sup);
            try { barrera2.await();} catch(Exception e){} 
        }
        
    }
    
    parallelTumoralGrowth(float Ps, float Pm, float Pp, int NP, int n, int generaciones, String tipoGen)
    {
        r = new Random();
        this.Ps = Ps;
        this.Pm = Pm;
        this.Pp = Pp;
        this.NP = NP;
        this.nCells = n;
        gen = generaciones;
        lienzo = new dibujadoTumor();
        lienzo.setPreferredSize(new Dimension(nCells, nCells));
        lienzo.setBackground(Color.white);
        
        cellsA = new int[nCells][nCells];
        cellsB = new int[nCells][nCells];
        cellsPH = new int[nCells][nCells];
                
        //Inciando la matriz de celulas vacia.
        for(int i = 0 ; i < nCells ; i++)
        {
            for(int j = 0 ; j < nCells ; j++)
            {
                cellsA[i][j] = 0;
                cellsB[i][j] = 0;
                cellsPH[i][j] = 0;
            }
        }
        
        int pos;
        switch(tipoGen)
        {
            case "Central": pos = (nCells - 1)/2; cellsA[pos][pos] = 1; cellsB[pos][pos] = 1; break;
            case "Aleatorio": int posX = r.nextInt(nCells); int posY = r.nextInt(nCells); cellsA[posX][posY] = 1; cellsB[posX][posY] = 1; break;
        }
        
        
    }
    
    private void simulacion(int inf, int sup)
    {
       
        //Comenzando etapas de la simulación...
        //System.out.println("simulacion...");
        for(int i = inf ; i < sup ; i++)
        {
            for(int j = 0 ; j < nCells ; j++)
            {   
                if(cellsA[i][j] == 1 && survival(i, j))
                {   System.out.println("SURVIVAL");
                    if(!proliferacion(i, j));
                    {   if(r.nextFloat() < Pm)
                        {
                            System.out.println("MIGRATION");
                            migration(i, j);
                        }
                    }
                                        
                }
               
            }
        }
        
        try {barrera.await();}catch(Exception e) {}
        
        for(int i = 0 ; i < nCells ; i++)
            for(int j = 0 ; j < nCells ; j++)
                cellsA[i][j] = cellsB[i][j];
        

    }
    
    private boolean survival(int i, int j)
    {
        
        boolean survive = false;
        if(r.nextFloat() < Ps)
        {
            survive = true;
        }
        else
        {
            cellsB[i][j] = 0;
        }

        return survive;
        
    }
    
    private boolean proliferacion(int i, int j)
    {
        if(r.nextFloat() < Pp)
        {
            cellsPH[i][j]++;
            if(cellsPH[i][j] >= NP)
            {
                        
                //Calculate Proliferation Probabilities
                return isProliferating(r.nextFloat(),i, j);
 
            }
            else
            {
                return false;
            }
        }
        return false;
    }
    
private boolean isProliferating(float rr, int i, int j)
    {
       
        float P1, P2, P3, P4;
        if(i >= 1 && j >= 1 && i < nCells - 1 && j < nCells - 1)
        {
            P1 = (1 - cellsA[i-1][j]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
            P2 = (1 - cellsA[i+1][j]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
            P3 = (1 - cellsA[i][j-1]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
            P4 = (1 - cellsA[i][j+1]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
        }
        else
        {
            //Suponemos frontera nula
            if(i == 0)
            {
                if(j == 0)
                {
                    P1 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                    P2 = (1 - cellsA[i+1][j]) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                    P3 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                    P4 = (1 - cellsA[i][j+1]) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                }
                else
                {
                    P1 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));
                    P2 = (1 - cellsA[i+1][j]) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));
                    P3 = (1 - cellsA[i][j-1]) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));
                    P4 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));                    
                }
            }
            else
            {
                if(j == 0)
                {
                    P1 = (1 - cellsA[i-1][j]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P2 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P3 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P4 = (1 - cellsA[i][j+1]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));  
                }
                else
                {
                    P1 = (1 - cellsA[i-1][j]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P2 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P3 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P4 = (1 - cellsA[i][j+1]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));                      
                }
            }
            
        }
        
        boolean prolifera = false;
        System.out.println("rr: " + rr +", P1: " + P1);
        
        if(i != 0 && 0 <= rr && rr <= P1 && cellsA[i-1][j] == 0)    // primera fila no evalua hacia arriba
        {
            prolifera = true;
            cellsB[i-1][j] = 1;
            cellsB[i][j] = 0;
        }
        
        if(!prolifera && i != nCells - 1 && cellsA[i+1][j] == 0 && P1 < rr && rr <= (P1 + P2))   // ultima fila no evalua hacia abajo
        {
            prolifera = true;
            cellsB[i+1][j] = 1;
            cellsB[i][j] = 0;
        }
        
        if(!prolifera && j != 0 && cellsA[i][j-1] == 0 &&(P1 + P2) < rr && rr <= (P1 + P2 + P3)) // primera columna no evalua hacia izquierda
        {
            prolifera = true;
            cellsB[i][j-1] = 1;
            cellsB[i][j] = 0;
        }
        
        if(!prolifera && j != nCells - 1 && cellsA[i][j+1] == 0 && (P1 + P2 + P3) < rr && rr <= 1) // ultima columna no evalua hacia derecha
        {
            prolifera = true;
            cellsB[i][j+1] = 1;
            cellsB[i][j] = 0;
        }
        
        if(prolifera) System.out.println("PROLIFERATING!");
        
        return prolifera;
    }
    
    private void migration(int i, int j)
    {
        float P1, P2, P3, P4;
        if(i >= 1 && j >= 1 && i < nCells - 1 && j < nCells - 1)
        {
            P1 = (1 - cellsA[i-1][j]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
            P2 = (1 - cellsA[i+1][j]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
            P3 = (1 - cellsA[i][j-1]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
            P4 = (1 - cellsA[i][j+1]) / (float) (4 - (cellsA[i+1][j] + cellsA[i-1][j] + cellsA[i][j+1] + cellsA[i][j-1]));
        }
        else
        {
            //Suponemos frontera nula
            if(i == 0)
            {
                if(j == 0)
                {
                    P1 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                    P2 = (1 - cellsA[i+1][j]) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                    P3 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                    P4 = (1 - cellsA[i][j+1]) / (float) (4 - (cellsA[i+1][j] + 0 + cellsA[i][j+1] + 0));
                }
                else
                {
                    P1 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));
                    P2 = (1 - cellsA[i+1][j]) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));
                    P3 = (1 - cellsA[i][j-1]) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));
                    P4 = (1 - 0) / (float) (4 - (cellsA[i+1][j] + 0 + 0 + cellsA[i][j-1]));                    
                }
            }
            else
            {
                if(j == 0)
                {
                    P1 = (1 - cellsA[i-1][j]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P2 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P3 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P4 = (1 - cellsA[i][j+1]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));  
                }
                else
                {
                    P1 = (1 - cellsA[i-1][j]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P2 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P3 = (1 - 0) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));
                    P4 = (1 - cellsA[i][j+1]) / (float) (4 - (0 + cellsA[i-1][j] + cellsA[i][j+1] + 0));                      
                }
            }
            
        }
        
        boolean migra = false;
        
        float rr = r.nextFloat();
        
        if(i != 0 && 0 <= rr && rr <= P1 && !migra)    // primera fila no evalua hacia arriba
        {
            migra = true;
            cellsB[i-1][j] = 1;
            cellsB[i][j] = 0;
        }
        
        if(!migra && i != nCells - 1 && P1 < rr && rr <= (P1 + P2))   // ultima fila no evalua hacia abajo
        {
            migra = true;
            cellsB[i+1][j] = 1;
            cellsB[i][j] = 0;
        }
        
        if(!migra && j != 0 &&(P1 + P2) < rr && rr <= (P1 + P2 + P3)) // primera columna no evalua hacia izquierda
        {
            migra = true;
            cellsB[i][j-1] = 1;
            cellsB[i][j] = 0;
        }
        
        if(!migra && j != nCells - 1 && (P1 + P2 + P3) < rr && rr <= 1) // ultima columna no evalua hacia derecha
        {
            migra = true;
            cellsB[i][j+1] = 1;
            cellsB[i][j] = 0;
        }
                
    }

       
    public class dibujadoTumor extends Canvas
    {
        
        @Override
        public void paint(Graphics g)
        {
                      
            for(int i = 0 ; i < gen ; i++)
            {
                //System.out.println(gen);
                g.setColor(Color.blue);
                for(int j = 0 ; j < nCells ; j++)
                {    for(int k = 0 ; k < nCells ; k++)
                    {
                        if(cellsA[j][k] == 1) g.drawOval(j, k, 1, 1);
                    }
                }
                
                // Simulacion de calculo...
                int inf = 0 ; int sup = nCells/Runtime.getRuntime().availableProcessors();
                
                for(int x = 0 ; x < Runtime.getRuntime().availableProcessors() ; x++)
                {
                    exec.execute(new tarea(inf, sup));
                    inf += nCells/Runtime.getRuntime().availableProcessors();
                    sup += nCells/Runtime.getRuntime().availableProcessors();
                }              
                
            }
            
                // Estado final
                g.setColor(Color.blue);
                for(int j = 0 ; j < nCells ; j++)
                {    for(int k = 0 ; k < nCells ; k++)
                    {
                        if(cellsA[j][k] == 1) g.drawOval(j, k, 1, 1);
                    }
                }
            
        }
    }
    
    
}
