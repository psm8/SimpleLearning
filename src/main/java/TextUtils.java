import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    public static String getSaveToName(String fileAddress, String prefix, String suffix, String delimiter, String directoryName, int shift){
        StringBuilder saveToBuilder = new StringBuilder();
        int fileNameStart = fileAddress.lastIndexOf('\\');
        for (int i = 0; i <= fileNameStart; i++) {
            saveToBuilder.append(fileAddress.charAt(i));
        }
        if(!directoryName.equals("")) {
            saveToBuilder.append(directoryName).append("\\");
        }
        new File(saveToBuilder.toString()).mkdir();
        saveToBuilder.append(prefix).append(".");
        for (int i = indexOfRegex(fileAddress, delimiter, fileNameStart) + shift; i < fileAddress.length() - 4; i++) {
            saveToBuilder.append(fileAddress.charAt(i));
        }
        String saveTo = saveToBuilder.toString();
        return saveTo + suffix;
    }

    public static String getSmartSaveToName(String fileAddress, String prefix, String suffix, String delimiter, String directoryName, int shift){
        StringBuilder saveToBuilder = new StringBuilder();
        int fileNameStart = fileAddress.lastIndexOf('\\');
        for (int i = 0; i <= fileNameStart; i++) {
            saveToBuilder.append(fileAddress.charAt(i));
        }
        if(!directoryName.equals("")) {
            saveToBuilder.append(directoryName).append("\\");
        }
        new File(saveToBuilder.toString()).mkdir();
        saveToBuilder.append(prefix).append(".");
        for (int i = indexOfRegex(fileAddress, delimiter, fileNameStart) + shift; i < fileAddress.length() - 4; i++) {
            saveToBuilder.append(fileAddress.charAt(i));
        }
        String saveTo = saveToBuilder.toString();
        String saveToTmp = saveTo;
        boolean fileExistFlag = new File(saveToTmp + suffix).exists();
        int fileNum = 1;
        while(fileExistFlag){
            fileNum++;
            saveTo = saveToTmp + "(" + fileNum + ")";
            fileExistFlag = new File(saveTo + suffix).exists();
        }
        return saveTo + suffix;
    }

    static String CutEndAndCreateNewText(String originalText, String prefix, String suffix, String delimiter){
        return prefix + originalText.substring(originalText.lastIndexOf(delimiter) + 1) + suffix;
    }

    static String CutMiddleFromAddressAndCreateNewText(String address, String prefix, String suffix, String delimiter1, String delimiter2, int n1, int n2){
        String name = address.substring(address.lastIndexOf("\\") + 1);
        return prefix + name.substring(nthIndexOf(name, delimiter1, n1) + 1,nthLastIndexOf(name, delimiter2, n2)  - 1)+ suffix;
    }

    static String CutMiddleFromAddressAndCreateNewText(String address, String prefix, String suffix, String delimiter1, String delimiter2, int n1, int n2, int shift1, int shift2){
        String name = address.substring(address.lastIndexOf("\\") + 1);
        return prefix + name.substring(nthIndexOf(name, delimiter1, n1) + shift1,nthLastIndexOf(name, delimiter2, n2)  + shift2) + suffix;
    }

    static List<String> CutMiddleFromAddressAndCreateNewText(List<String> addresses, String prefix, String suffix, String delimiter1, String delimiter2, int n1, int n2){
        List<String> names = new ArrayList<>();
        for(String address : addresses) {
            names.add(CutMiddleFromAddressAndCreateNewText(address, prefix, suffix, delimiter1, delimiter2, n1, n2));
        }
        return names;
    }

    static String CutMiddleFromAddressAndCreateNewTextRegexII(String address, String prefix, String suffix, String delimiterIndexOf1, String delimiterIndexOf2){
        String name = address.substring(address.lastIndexOf("\\") + 1);
        int test1 = indexOfRegex(name, delimiterIndexOf1, 0);
        int test2 = indexOfRegex(name, delimiterIndexOf2, 0);
        /*tmp way*/
        if(test2 - test1 > 1) {
            return prefix + name.substring(indexOfRegex(name, delimiterIndexOf1, 0) + 1, indexOfRegex(name, delimiterIndexOf2, 0) - 1) + suffix;
        }
        else{
            return  prefix + suffix;
        }
    }


    static String CutMiddleFromAddressAndCreateNewTextRegex(String address, String prefix, String suffix, String delimiter1, String delimiter2){
        String name = address.substring(address.lastIndexOf("\\") + 1);
        return prefix + name.substring(indexOfRegex(name, delimiter1, 0) + 1,lastIndexOfRegex(name, delimiter2, 0)  - 1)+ suffix;
    }


    static List<String> CutMiddleFromAddressAndCreateNewTextRegex(List<String> addresses, String prefix, String suffix, String delimiter1, String delimiter2){
        List<String> names = new ArrayList<>();
        for(String address : addresses) {
            names.add(CutMiddleFromAddressAndCreateNewTextRegex(address, prefix, suffix, delimiter1, delimiter2));
        }
        return names;
    }

    static String CutBeginOfNameFromAddress(String address, String regex, int shift){
        String name = address.substring(address.lastIndexOf("\\") + 1);
        return name.substring(0, TextUtils.indexOfRegex(name, regex, 0) + shift);
    }

    static String getParentDirectory(String address){
        return address.substring(0, address.lastIndexOf("\\"));
    }

    static int nthIndexOf(String originalText, String delimiter, int n){
        int index = 0;
        for (int i = 0; i < n; i++) {
            index = originalText.indexOf(delimiter, index + 1);
        }
        index = originalText.indexOf(delimiter, index);
        return index;
    }

    static int nthLastIndexOf(String originalText, String delimiter, int n){
        int index = originalText.length();
        for (int i = 0; i < n; i++) {
            index = originalText.lastIndexOf(delimiter, index - 1);
        }
        index = originalText.lastIndexOf("", index);
        return index;
    }

    public static int indexOfRegex(String text, String regex, int start){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text.substring(start));
        if(m.find()){
            return start + m.start();
        }
        else{
            return -1;
        }
    }

    public static int lastIndexOfRegex(String text, String regex, int start){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text.substring(start));
        int lastIndex = -1;
        /*not sure if that works*/
        while(m.find()){
            lastIndex =  start + m.start();
        }
        return lastIndex;
    }
}
