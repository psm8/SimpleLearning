import java.io.*;
import java.text.DecimalFormat;

public class Driver {
    private static final int NUMB_OF_EPOCHS = 600000;
    private static final int DECREASE_LEARNING_RATE_EVERY_NUM_OF_EPOCHS = 120000;
    private static final double INITIAL_LEARNING_RATE = 0.01;
    private static final double ACCURACY = 0.000005 ;
    private static double roundAtThreshold = 0.9;
    private static final String READ_INPUTS_FROM = "data\\inputs.txt";
    private static final String READ_TARGET_RESULTS_FROM = "data\\targetResults.txt";
    static double TRAINING_INPUTS[][] = DataUtils.read2dimensionalDoubleFile(READ_INPUTS_FROM);
    static double TRAINING_TARGET_RESULTS[] = DataUtils.readSingleColumnFromFile(READ_TARGET_RESULTS_FROM);
    
    public static void main(String args[]) throws IOException {
        Perceptron perceptron = new Perceptron(TRAINING_INPUTS[0].length, INITIAL_LEARNING_RATE);
        double error;
        /*double bias = 0;*/
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = true;
        while (flag) {
            System.out.println("\n> What do you want to do (cer, sc, anneal, train, trainclr, load weights, show results, show weights, exit) ?");
            String[] command = bufferedReader.readLine().split(" ");
            switch (command[0]) {
                case "cer":
                    calculateError(perceptron, TRAINING_INPUTS, TRAINING_TARGET_RESULTS);
                    break;
                case "sc":
                    break;
                case "train":
                    autoTrain(perceptron, TRAINING_INPUTS, TRAINING_TARGET_RESULTS);
                    break;
                case "trainclr":
                    train(perceptron, TRAINING_INPUTS, TRAINING_TARGET_RESULTS);
                    break;
                case "anneal":
                    new SimulatedAnnealing().anneal(perceptron, Double.parseDouble(bufferedReader.readLine()));
                    break;
                case "load weights":
                    String loadWeightsFromFileName = bufferedReader.readLine();
                    double loadedWeights[] = DataUtils.readSingleColumnFromFile("data/saves/weights." + loadWeightsFromFileName + ".txt");
                    perceptron.setWeights(loadedWeights);
                    break;
                case "show results":
                    double cumError2 = 0;
                    for (int j = 0; j < TRAINING_INPUTS.length; j++) {
                        double weightedSum = perceptron.calculateWeightedSum(TRAINING_INPUTS[j]);
                        double result = perceptron.applyActivationFunction(weightedSum);
                        error = getError(TRAINING_TARGET_RESULTS[j], result);
                        cumError2 += Math.abs(error) / TRAINING_TARGET_RESULTS.length;
                        printResult(TRAINING_INPUTS[j], TRAINING_TARGET_RESULTS[j], perceptron.getWeights(), result, weightedSum);
                    }
                    System.out.println(cumError2/MyUtils.mean(TRAINING_TARGET_RESULTS));
                    break;
                case "show weights":
                    for(int i = 0; i < perceptron.getWeights().length; i++){
                        System.out.print(String.format("%.7f",perceptron.getWeights()[i])+",");
                    }
                    break;
                case "exit":
                    flag = false;
                    break;
            }
        }
        System.exit(0);
    }

    private static void autoTrain(Perceptron perceptron, double[][] inputs, double[] targetResults){
        double error;
        double oldCumError = 100;
        for (int i = 1; i < NUMB_OF_EPOCHS + 1; i++) {
            for (int j = 0; j < inputs.length; j++) {
                double weightedSum = perceptron.calculateWeightedSum(inputs[j]);
                double result = perceptron.applyActivationFunction(weightedSum);
                error = getError(targetResults[j], result);
                perceptron.adjustWeights(inputs[j], error);
                /*bias = perceptron.getLearningRate() * error * result + bias;*/
            }
            if (i % 500 == 0) {
                double cumError = calculateError(perceptron, inputs, targetResults);
                cumError = perceptron.adjustLearningRate(oldCumError, cumError);
                if (i % 10000 == 0) {
                    System.out.println("Epoch# " + i);
                    System.out.println(cumError);
                    System.out.println(perceptron.getLearningRate());
                    if (i % DECREASE_LEARNING_RATE_EVERY_NUM_OF_EPOCHS == 0) {
                        perceptron.setLearningRate(perceptron.getLearningRate()/2);
                    }
                }

                /*System.out.println(bias);*/
                oldCumError = cumError;
                if (perceptron.getLearningRate() < ACCURACY) {
                    break;
                }
            }
        }
    }

    private static void train(Perceptron perceptron, double[][] inputs, double[] targetResults) throws IOException{
        System.out.println("Set learning rate:");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String setLearningRate = bufferedReader.readLine();
        if(!setLearningRate.equals("")){
            perceptron.setLearningRate(Double.parseDouble(setLearningRate));
        }
        autoTrain(perceptron, inputs, targetResults);
    }

    private static double calculateError(Perceptron perceptron, double[][] inputs, double[] targetResults){
        double error;
        double cumError = 0;
        for (int j = 0; j < inputs.length; j++) {
            double weightedSum = perceptron.calculateWeightedSum(inputs[j]);
            double result = perceptron.applyActivationFunction(weightedSum);
            error = getError(targetResults[j], result);
            cumError += Math.abs(error) / targetResults.length;
        }
        return cumError;
    }

    static double getError(double targetResult, double result){
        double bottomThreshold = 1 - roundAtThreshold;
        if(bottomThreshold < 0){ bottomThreshold = 0;}
        if(result > roundAtThreshold){
            if(targetResult > roundAtThreshold){
                return 0;
            }else{
                return targetResult - roundAtThreshold;
            }
        } else if(result < bottomThreshold){
            if(targetResult < bottomThreshold){
                return 0;
            }else {
                return targetResult - bottomThreshold;
            }
        }else{
            return targetResult - result;
        }
    }

    private static void printResult(double[] inputs, double trainingTarget, double[] weights , double result, double weightedSum){
        for(int i = 0; i < inputs.length; i++){
            System.out.print("Input "+new DecimalFormat("000").format(i+1) +"|");
        }
        for(int i = 0; i < weights.length; i++){
            System.out.print("Weight"+new DecimalFormat("000").format(i+1) +"|");
        }
        System.out.println(" Weighted Sum | Target Result | Result ");
        for(double input : inputs){
            System.out.print(String.format("%.3f", input)+"    |");
        }
        for(double weight : weights) {
            if (weight >= 0) {
                if(weight >= 10) {
                    System.out.print(String.format("%.5f", weight) + " |");
                }else{
                    System.out.print(String.format("%.6f", weight) + " |");
                }
            } else {
                if(weight <= -10) {
                    System.out.print(String.format("%.4f", weight) + " |");
                }else{
                    System.out.print(String.format("%.5f", weight) + " |");
                }
            }
        }
        if(weightedSum >= 0) {
            System.out.println(String.format("%.7f", weightedSum) + "     |  " + String.format("%.4f",trainingTarget) + "      |" + String.format("%.6f", result));
        }else{
            System.out.println(String.format("%.6f", weightedSum) + "     |  " + String.format("%.4f",trainingTarget) + "      |" + String.format("%.6f", result));
        }
    }
}