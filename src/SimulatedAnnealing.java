import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing{

    public ArrayList<Double> hist;

    public double solve(Problem p, double a) {

        hist = new ArrayList<>();
        Random r = new Random();
        double x0 = r.nextDouble() * 100;
        return solve(p,a,x0);
    }


    public double solve(Problem p, double a, double x0) {

        Random r = new Random();
        double f0 = p.fit(x0);
        hist.add(f0);

        // REPEAT
        for (double t = 100; t > 1; t = t * a){
            int kt = (int) t;
            for(int j=0; j<kt; j++){
                double upper = x0 + 10;
                double lower = x0 - 10;
                double x1 = r.nextDouble()  * (upper - lower) + lower;
                double f1 = p.fit(x1);

                if(p.isNeighborBetter(f0,f1)){
                    x0 = x1;
                    f0 =f1;
                    hist.add(f0);
                }else{
                    double d = Math.abs(f1 - f0);
                    double p0 = Math.exp(-d/t);
                    if(r.nextDouble() < p0) {
                        x0 = x1;
                        f0 = f1;
                        hist.add(f0);
                    }
                }
            }
        }
        return x0;
    }
}
