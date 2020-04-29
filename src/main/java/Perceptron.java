class Perceptron {
    private double[] weights;
    private double[] recoveryWeights;
    private double learningRate;

    Perceptron(int dataLength, double initialLearningRate){
        this.weights = new double[dataLength];
        this.recoveryWeights = new double[dataLength];
        for(int x=0; x<this.weights.length; x++){
            this.weights[x] = 0.5 - Math.random();
        }
        this.learningRate = initialLearningRate;
    }

    Perceptron(Perceptron perceptron){
        this.weights = perceptron.getWeights();
        this.learningRate = perceptron.getLearningRate();
    }

    double calculateWeightedSum(double[] data){
        double weightedSum = 0;
        for(int i=0; i<data.length; i++){
            weightedSum += data[i] * this.weights[i];
        }
        return weightedSum;
    }

    double applyActivationFunction(double weightedSum){
        return weightedSum;
    }

    double adjustLearningRate(double oldCumError, double cumError){
        if(oldCumError - cumError < 0){
            this.learningRate /= 2;
            this.weights = this.recoveryWeights.clone();
            return oldCumError;
        }
        else{
            this.recoveryWeights = this.weights.clone();
            return cumError;
        }
    }
/*    public void adjustLearningRateGradually(){
            this.learningRate *= 0.95;
    }*/

    void adjustWeights(double[] data, double error){
        for(int i=0; i<this.weights.length; i++){
            this.weights[i] += this.learningRate * error * data[i];
        }
    }

    double[] randomAdjustWeights(double[] weights){
        double[] adjustedWeights = new double[weights.length];
        for(int i=0; i<weights.length; i++){
            adjustedWeights[i] = 0.5 - Math.random() + weights[i];
        }
        return adjustedWeights;
    }

    double[] getWeights() { return this.weights; }

    double getLearningRate() { return this.learningRate; }

    void setWeights(double[] loadedWeights) {
        for(int i=0; i<loadedWeights.length; i++){
            this.weights[i] = loadedWeights[i];
        }
    }

    void setLearningRate(double _learningRate) { this.learningRate = _learningRate; }
}
