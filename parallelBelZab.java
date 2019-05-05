
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class parallelBelZab extends Thread{
    
    public float [][][] a;
    public float [][][] b;
    public float [][][] c;
    
    public panelGrafico dibuja;
    
    private int dim, nGen;
    private float alfa, beta, gamma;
    private boolean isReady = true;
    private Color [][] color;
    
    private randomGenerator g = new randomGenerator();
    
    static CyclicBarrier barrera = new CyclicBarrier(Runtime.getRuntime().availableProcessors());
    
    public int p = 0, q = 1;
    
    private class tarea implements Runnable
    {
        int inf, sup;
        
        tarea(int i, int s)
        {
            inf = i ; sup = s;
        }
        
        public void run()
        {
            nucSimulacion(inf, sup);
        }
        
    }

    public class panelGrafico extends Canvas
    {
        
        @Override
        public void paint(Graphics g)
        {
            ExecutorService ej = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            while(nGen > 0 && isReady)
            {
                
                int carga = dim/Runtime.getRuntime().availableProcessors();
                int inf = 0, sup = carga;
                
                for(int i = 0 ; i < Runtime.getRuntime().availableProcessors() ; i++)
                {
                    ej.execute(new tarea(inf, sup));
                    inf += carga;
                    sup += carga;
                }
                
                //ej.shutdown();
               // try { ej.awaitTermination(1L, TimeUnit.DAYS); } catch(Exception ex) {}
                
                for(int i = 0 ; i < dim ; i++)
                {
                    for(int j = 0 ; j < dim ; j++)
                    {
                        //System.out.println("i: " + i + " j: " + j + " color: " + color[i][j].getRGB());
                        g.setColor(color[i][j]);
                        g.drawOval(i, j, 1, 1);
                    }
                }
                if(p == 0)
                {
                    p = 1; q = 0;
                }
                else
                {
                    p = 0 ; q = 1;
                }
                nGen--;
                System.out.println(nGen);
            }
                if(nGen == 0)
                {
                    for(int i = 0 ; i < dim ; i++)
                    {
                        for(int j = 0 ; j < dim ; j++)
                        {
                            g.setColor(color[i][j]);
                            g.drawOval(i, j, 1, 1);
                        }
                     }                   
                }
        }
    }
    
    parallelBelZab(int d, int generaciones, float aa, float bb, float cc)
    {
        dim = d; nGen = generaciones;
        alfa = aa;
        beta = bb;
        gamma = cc;
        color = new Color[dim][dim];
        
        System.out.println("generaciones: " + nGen);
        
        dibuja = new panelGrafico();
        dibuja.setPreferredSize(new Dimension(dim, dim));
        dibuja.setBackground(Color.black);
        
        a = new float[dim][dim][2];
        b = new float[dim][dim][2];
        c = new float[dim][dim][2];
        Random r = new Random();
        for(int x = 0 ; x < dim ; x++)
        {
            for(int y = 0 ; y < dim ; y++)
            {
                a[x][y][p] = r.nextFloat();
                b[x][y][p] = r.nextFloat();
                c[x][y][p] = r.nextFloat();
            }
        }
        
        isReady = true; //dibuja.repaint(); System.out.println("ok");
    }
    
   
    private void nucSimulacion(int inf, int sup)
    {
        for(int x = 0 ; x < dim ; x++)
        {
            for(int y = inf ; y < sup ; y++)
            {
                float c_a = 0.0f;
                float c_b = 0.0f;
                float c_c = 0.0f;
                for(int i = x-1; i <= x+1 ; i++)
                {
                    for(int j = y-1; j <= y+1; j++)
                    {
                        c_a += a[(i+dim)%dim][(j+dim)%dim][p];
                        c_b += b[(i+dim)%dim][(j+dim)%dim][p];
                        c_c += c[(i+dim)%dim][(j+dim)%dim][p];
                    }
                }
                c_a /= 9.0f;
                c_b /= 9.0f;
                c_c /= 9.0f;
                
                a[x][y][q] = constrain(c_a + c_a * (alfa*c_b - gamma*c_c));
                b[x][y][q]=constrain(c_b+c_b*(beta*c_c-alfa*c_a));
                c[x][y][q]=constrain(c_c+c_c*(gamma*c_a-beta*c_b));
                color[x][y] = new Color(a[x][y][q], b[x][y][q], c[x][y][q]);
            }
        }
    }
    
    private float constrain(float x){
    if(x<0)return 0;
    else  {
      if(x>1)return 1;
      else return x;
    }
  }
}
