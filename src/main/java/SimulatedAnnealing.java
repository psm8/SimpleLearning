class SimulatedAnnealing {
    private static final double RATE_OF_COOLING = 0.00005;
    private static final double MIN_TEMPERATURE = 0.99;

    void anneal(Perceptron perceptron, double temperature){
        double error;
        double[] adjustedWeights = null;
        int i =0;
        while (temperature > MIN_TEMPERATURE) {
            i++;
            double currentError = 0;
            double adjustedError = 0;
            for (int j = 0; j < Driver.TRAINING_INPUTS.length; j++) {
                double weightedSum = perceptron.calculateWeightedSum(Driver.TRAINING_INPUTS[j]);
                double result = perceptron.applyActivationFunction(weightedSum);
                error = Driver.TRAINING_TARGET_RESULTS[j] - result;
                currentError += Math.abs(error) / Driver.TRAINING_TARGET_RESULTS.length;

            }
            Perceptron perceptron2 = new Perceptron(perceptron);
            for (int j = 0; j < Driver.TRAINING_INPUTS.length; j++) {
                adjustedWeights = perceptron2.randomAdjustWeights(perceptron.getWeights());
            }
            for (int j = 0; j < Driver.TRAINING_INPUTS.length; j++) {
                double adjustedWeightedSum = perceptron2.calculateWeightedSum(Driver.TRAINING_INPUTS[j]);
                double adjustedResult = perceptron2.applyActivationFunction(adjustedWeightedSum);
                double aError = Driver.TRAINING_TARGET_RESULTS[j] - adjustedResult;
                adjustedError += Math.abs(aError) / Driver.TRAINING_TARGET_RESULTS.length;
            }
            if (adjustedError < currentError) {
                currentError = adjustedError;
                perceptron.setWeights(adjustedWeights);
            } else if (accept(currentError, adjustedError, temperature, i)) {
                currentError = adjustedError;
                perceptron.setWeights(adjustedWeights);
            }
            temperature *= 1 - RATE_OF_COOLING;
            if(i%1000 == 0) {
                System.out.println("Epoch# " + i);
                System.out.println(temperature);
                System.out.println(currentError / MyUtils.mean(Driver.TRAINING_TARGET_RESULTS));
            }
        }
    }
    private boolean accept(double currentError, double adjustedError, double temperature, int i){
        boolean acceptNNflag = false;
        double acceptanceProbability = Math.exp(-((adjustedError - currentError)*10000)/temperature);
        double randomNumb = Math.random();
        if(i%1000 == 0) {
            System.out.println(acceptanceProbability);
            System.out.println(randomNumb);
        }
        if(acceptanceProbability >= randomNumb){acceptNNflag = true;}
        return acceptNNflag;
    }
}