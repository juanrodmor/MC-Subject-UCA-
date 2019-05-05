
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class randomGenerator {
    
    int seed_261a;
    int seed_261b;
    int seed_262;
    BigInteger seed_263, seed_FM1, seed_FM2, seed_Randu;
    int W0, X0, Y0;
    
    randomGenerator()
    {
        seed_261a = 1;
        seed_261b = 1;
        seed_262 = 1;
        seed_263 = BigInteger.ONE;
        seed_FM1 = BigInteger.ONE;
        seed_FM2 = BigInteger.ONE;
        seed_Randu = BigInteger.ONE;
        W0 = 1;
        X0 = 1;
        Y0 = 1;
    }
    
    public double generator261a()
    {
        seed_261a = (5 * seed_261a) % (int) Math.pow(2, 5);
        return (double) seed_261a / Math.pow(2, 5);
    }
    
    public double generator261b()
    {
        seed_261b = (7 * seed_261b) % (int) Math.pow(2, 5);
        return (double) seed_261b / Math.pow(2, 5);
    }
    
    public double generator262()
    {
        seed_262 = (3 * seed_262) % 31;
        return (double) seed_262 / 31;
    }
    
    public double generator263()
    {

        BigInteger prod = seed_263.multiply(BigDecimal.valueOf(Math.pow(7, 5)).toBigInteger());
        BigInteger pot = BigDecimal.valueOf(Math.pow(2, 31)).toBigInteger().subtract(BigInteger.ONE);
        seed_263 = prod.mod(pot);
        
        BigDecimal d = new BigDecimal(seed_263);
        BigDecimal mod = new BigDecimal(pot);
                
        return d.divide(mod, 10, RoundingMode.HALF_UP).doubleValue();
    }
    
    public double generadorCombinado()
    {
        int Wn = (157 * W0) % 32363;  W0 = Wn;
        int Xn = (146 - X0) % 31727; X0 = Xn;
        int Yn = (142 - Y0) % 31657; Y0 = Yn;
        
        double numPseudo = (double) (Wn - Xn + Yn) % 32362;
        
        return numPseudo / 32362.0;
        
    }
    
    public double fishmanMoore_1()
    {
        BigInteger m = BigDecimal.valueOf(Math.pow(2, 31)).toBigInteger().subtract(BigInteger.ONE);
        
        seed_FM1 = seed_FM1.multiply(BigDecimal.valueOf(48271).toBigInteger()).mod(m);
        
        BigDecimal d = new BigDecimal(seed_FM1);
        BigDecimal mod = new BigDecimal(m);
                               
        return d.divide(mod, 10, RoundingMode.HALF_UP).doubleValue();
        
    }
    
    public double fishmanMoore_2()
    {
        BigInteger m = BigDecimal.valueOf(Math.pow(2, 31)).toBigInteger().subtract(BigInteger.ONE);
        
        seed_FM2 = seed_FM2.multiply(BigDecimal.valueOf(69621).toBigInteger()).mod(m);
        
        BigDecimal d = new BigDecimal(seed_FM2);
        BigDecimal mod = new BigDecimal(m);
                               
        return d.divide(mod, 10, RoundingMode.HALF_UP).doubleValue();        
    }
    
    public double Randu()
    {
        BigInteger m = BigDecimal.valueOf(Math.pow(2, 31)).toBigInteger();
        BigInteger a = BigDecimal.valueOf(Math.pow(2, 16)).toBigInteger().add(BigDecimal.valueOf(3).toBigInteger());
        
        seed_Randu = seed_Randu.multiply(a).mod(m);
        
        BigDecimal d = new BigDecimal(seed_Randu);
        BigDecimal mod = new BigDecimal(m);
        
        return d.divide(mod, 10, RoundingMode.HALF_UP).doubleValue();
        
    }
}