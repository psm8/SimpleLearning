import Jama.Matrix;
import Jama.QRDecomposition;


public class MultipleLinearRegression {
    private final Matrix beta;  // regression coefficients
    private double SSE;         // sum of squared
    private double SST;         // sum of squared

    MultipleLinearRegression(double[][] x, double[] y) throws RuntimeException{
        if (x.length != y.length) throw new RuntimeException("dimensions don't agree");
        int N = y.length;

        Matrix X = new Matrix(x);
        // create matrix from vector
        Matrix Y = new Matrix(y, N);

        // find least squares solution
        QRDecomposition qr = new QRDecomposition(X);
        beta = qr.solve(Y);

        // mean of y[] values
        double mean = MyUtils.mean(y);

        // total variation to be accounted for
        for (int i = 0; i < N; i++) {
            double dev = y[i] - mean;
            SST += dev*dev;
        }

        // variation not accounted for
        Matrix residuals = X.times(beta).minus(Y);
        SSE = residuals.norm2() * residuals.norm2();
    }

    double getBeta(int j) {
        return beta.get(j, 0);
    }

    Matrix getBeta() {
        return beta;
    }

    double R2() {
        return 1.0 - SSE/SST;
    }

    public static void main(String[] args) {
        double[][] x = { {  1,  10,  20 },
                {  1,  20,  40 },
                {  1,  40,  15 },
                {  1,  80, 100 },
                {  1, 160,  23 },
                {  1, 200,  18 } };
        double[] y = { 243, 483, 508, 1503, 1764, 2129 };
        MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

        System.out.printf("%.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n",
                regression.getBeta(0), regression.getBeta(1), regression.getBeta(2), regression.R2());
    }
}