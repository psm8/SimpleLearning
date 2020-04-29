import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

class SavingUtils {
    /*version for lists*/
    static void autoSaveSimpleResults(Perceptron perceptron, double[][] inputs, double[] targetResults, List<String> flops, List<String> spots, String fileAddress, String prefix2) throws FileNotFoundException{
        double error;
        List<String> spotTypes = TextUtils.CutMiddleFromAddressAndCreateNewText(spots, "", "",".",".", 3, 1);
        String ssrToFileName = TextUtils.getSmartSaveToName(fileAddress, "SR" + prefix2, ".csv", "ts", "NNResults", 3);
        PrintWriter ssrTo = new PrintWriter(ssrToFileName);
        ssrTo.println("Spot, Flop, Target Result, Result, Difference");
        double cumErrorssr = 0;
        for (int j = 0; j < inputs.length; j++) {
            double weightedSum = perceptron.calculateWeightedSum(inputs[j]);
            double result = perceptron.applyActivationFunction(weightedSum);
            error = Driver.getError(targetResults[j], result);
            cumErrorssr += Math.abs(error) / targetResults.length;
            ssrTo.println(spotTypes.get(j) + "," + flops.get(j) + "," + targetResults[j] + "," + result + "," + error);
        }
        ssrTo.print(cumErrorssr);
        ssrTo.close();
        System.out.println("saved as "+ssrToFileName);
    }

    /*version for Strings*/
    static void autoSaveSimpleResults(Perceptron perceptron, double[][] inputs, double[] targetResults, List<String> flops, String fileAddress, String prefix2) throws FileNotFoundException{
        double error;
        String spotType = TextUtils.CutMiddleFromAddressAndCreateNewText(fileAddress, "", "",".",".", 3, 1);
        String ssrToFileName = TextUtils.getSmartSaveToName(fileAddress, "SR" + prefix2, ".csv", "ts", "NNResults", 3);
        PrintWriter ssrTo = new PrintWriter(ssrToFileName);
        ssrTo.println("Spot, Flop, Target Result, Result, Difference");
        double cumErrorssr = 0;
        for (int j = 0; j < inputs.length; j++) {
            double weightedSum = perceptron.calculateWeightedSum(inputs[j]);
            double result = perceptron.applyActivationFunction(weightedSum);
            error = Driver.getError(targetResults[j], result);
            cumErrorssr += Math.abs(error) / targetResults.length;
            ssrTo.println(spotType + "," + flops.get(j) + "," + targetResults[j] + "," + result + "," + error);
        }
        ssrTo.print(cumErrorssr);
        ssrTo.close();
        System.out.println("saved as "+ssrToFileName);
    }

    /*version for lists*/
    static void autoSaveResults(Perceptron perceptron, double[][] inputs, double[] targetResults, List<String> flops, List<String> spots, String fileAddress, String prefix2) throws FileNotFoundException{
        double error;
        List<String> spotTypes = TextUtils.CutMiddleFromAddressAndCreateNewText(spots, "", "",".",".", 3 , 1);
        String srToFileName = TextUtils.getSmartSaveToName(fileAddress, "R" + prefix2, ".csv", "ts", "NNResults", 3);
        PrintWriter srTo = new PrintWriter(srToFileName);
        saveHeading(srTo, inputs[0].length, perceptron.getWeights().length);
        double cumError = 0;
        for (int j = 0; j < inputs.length; j++) {
            double weightedSum = perceptron.calculateWeightedSum(inputs[j]);
            double result = perceptron.applyActivationFunction(weightedSum);
            error = Driver.getError(targetResults[j], result);
            cumError += Math.abs(error) / targetResults.length;
            saveResult(srTo, spotTypes.get(j), flops.get(j), inputs[j], targetResults[j], perceptron.getWeights(), result, weightedSum);
        }
        srTo.print(cumError);
        srTo.close();
        System.out.println("saved as "+srToFileName);
    }

    /*version for Strings*/
    static void autoSaveResults(Perceptron perceptron, double[][] inputs, double[] targetResults, List<String> flops, String fileAddress, String prefix2) throws FileNotFoundException{
        double error;
        String spotType = TextUtils.CutMiddleFromAddressAndCreateNewText(fileAddress, "", "",".",".", 3 , 1);
        String srToFileName = TextUtils.getSmartSaveToName(fileAddress, "R" + prefix2, ".csv", "ts", "NNResults", 3);
        PrintWriter srTo = new PrintWriter(srToFileName);
        saveHeading(srTo, inputs[0].length, perceptron.getWeights().length);
        double cumError = 0;
        for (int j = 0; j < inputs.length; j++) {
            double weightedSum = perceptron.calculateWeightedSum(inputs[j]);
            double result = perceptron.applyActivationFunction(weightedSum);
            error = Driver.getError( targetResults[j], result);
            cumError += Math.abs(error) / targetResults.length;
            saveResult(srTo, spotType, flops.get(j), inputs[j], targetResults[j], perceptron.getWeights(), result, weightedSum);
        }
        srTo.print(cumError);
        srTo.close();
        System.out.println("saved as " + srToFileName);
    }

    private static void saveHeading(PrintWriter saveTo, double inputsLength, double weightsLength) {
        for (int i = 0; i < inputsLength; i++) {
            saveTo.print("Input " + new DecimalFormat("000").format(i + 1) + ",");
        }
        for (int i = 0; i < weightsLength; i++) {
            saveTo.print("Weight" + new DecimalFormat("000").format(i + 1) + ",");
        }
        saveTo.println(" Weighted Sum , Target Result , Result ");
    }

    private static void saveResult(PrintWriter saveTo, String spotType, String flop, double[] inputs, double trainingTarget, double[] weights , double result, double weightedSum){
        saveTo.print(spotType + "," + flop + ",");
        for(double input : inputs){
            saveTo.print(input + ",");
        }
        for(double weight : weights){
            saveTo.print(weight + ",");
        }
        saveTo.println(weightedSum+ "," + trainingTarget + "," + result);
    }
}
