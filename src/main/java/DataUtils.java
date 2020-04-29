import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class DataUtils {
    static double[][] read2dimensionalDoubleFile(String fileURI){
        double[][] dArray = new double[0][];

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileURI), StandardCharsets.UTF_8);
            dArray = new double[lines.size()][];

            for(int i = 0; i<lines.size(); i++){
                dArray[i] = convertStringArrayToDoubleArray(lines.get(i).split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dArray;
    }

    static double[] readSingleColumnFromFile(String fileURI){
        double[] iArray = new double[0];

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileURI), StandardCharsets.UTF_8);
            iArray = new double[lines.size()];

            for(int i = 0; i<lines.size(); i++){
                iArray[i] = Double.parseDouble(lines.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iArray;
    }

    static String[][] read2dimensionalStringArray(String fileURI){
        String[][] fArray = new String[0][];
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileURI), StandardCharsets.UTF_8);
            fArray = new String[lines.size()][];

            for(int i = 0; i<lines.size(); i++){
                fArray[i] = (lines.get(i).split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fArray;
    }

    static double[] convertStringArrayToDoubleArray(String[] num){
        if (num != null) {
            double fArray[] = new double[num.length];
            for (int i = 0; i <num.length; i++) {
                fArray[i] = Double.parseDouble(num[i]);
            }
            return fArray;
        }
        return null;
    }

}