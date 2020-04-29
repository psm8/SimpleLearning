import java.util.ArrayList;
import java.util.List;

class MyUtils {
    static <T> List<List<T>> changeDimensions(List<List<T>> iList){
        List<List<T>> oList = new ArrayList<>();
        /*changes in relation to first list*/
        for (int i = 0; i < iList.get(0).size(); i++) {
            List<T> tmpList = new ArrayList<>();
            for (int j = 0; j < iList.size(); j++) {
                tmpList.add(iList.get(j).get(i));
            }
            oList.add(tmpList);
        }
        return oList;
    }

    static <T> List<List<List<T>>> changeDimensions3(List<List<List<T>>> iList){
        List<List<List<T>>> oList = new ArrayList<>();
        for (int i = 0; i < iList.size(); i++) {
            oList.add(changeDimensions(iList.get(i)));
        }
        oList = changeDimensions(oList);
        return oList;
    }

    static String[][] changeDimensions(String[][] iArray){
        String[][] oArray = new String[iArray[0].length][iArray.length];

        for (int i = 0; i < iArray[0].length; i++) {
            for (int j = 0; j < iArray.length; j++) {
                oArray[i][j] = iArray[j][i];
            }
        }
        return oArray;
    }

    static double mean(double[] ms){
        double sum = 0;
        for(double m : ms){
            sum += m;
        }
        return sum/ms.length;
    }
}
