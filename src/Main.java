public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing();
        Problem p = new Problem(){
            @Override
            public double fit(double x) {
                return -x*x*x*x + 4*x*x*x + 36*x*x + 16;
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f0 < f1;
            }
        };

        double x = sa.solve(p, 0.99);
        System.out.println(x);
        System.out.println(p.fit(x));

    }
}
