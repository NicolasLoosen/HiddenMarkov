import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utils {

    /**
     * Loads Data in a List<String> from given Filename 
     * @param url
     * @return
     */
    static List<String> loadData(String url){
        List<String> input = new ArrayList<>();
        try {
            String currentPath = new java.io.File(".").getCanonicalPath();
            Scanner sc = new Scanner(new File(currentPath + "/src/data/wuerfel2021.txt"));
            while(sc.hasNextLine()){
                input.add(sc.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * reversed an given int[] array
     * @param intArray
     * @return
     */
    public static int[] reverseArray(int intArray[]) 
    {   
        int[] temparray = intArray.clone();
        int i, temp; 
        int size = intArray.length;
        for (i = 0; i < size / 2; i++) { 
            temp = temparray[i]; 
            temparray[i] = temparray[size - i - 1]; 
            temparray[size - i - 1] = temp; 
        }
        return temparray;
    } 

    /**
     * returns the 0 String from a List<String>
     * @param input
     * @return
     */
    public static int[] getResults(List<String> input){
        String results = input.get(0).toString();
        int[] resultArr = new int[results.length()];
        for(int i = 0; i < results.length(); i++){
            int temp = results.charAt(i);
            resultArr[i] = Character.getNumericValue(temp);
        }
        return resultArr;
    }

    /**
     * imports an 2D Array from an JSON File
     * @param jsonUrl
     * @return
     */
    public static double[][] import2DArray(String jsonUrl){
        JSONParser parser = new JSONParser();
        try {
            
            JSONArray array = (JSONArray) parser.parse(new FileReader("./src/data/"+jsonUrl));
            JSONArray arraylength = (JSONArray) array.get(0);

            double [][] darray = new double[array.size()][arraylength.size()];

            for(int i = 0; i < array.size(); i++){
                JSONArray l = (JSONArray) array.get(i);
                for(int j = 0; j< l.size(); j++){
                    String temp = l.get(j).toString().replaceAll("\\[", "").replaceAll("\\]","");
                    double left = Double.parseDouble(temp.split(",")[0]);
                    double right = Double.parseDouble(temp.split(",")[1]);
                    if(right == 0.0){
                        darray[i][j]= 0.0;
                    }else{
                        darray[i][j] = left/right;
                    }
                }
            }
            return darray;

        } catch (IOException |ParseException  e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Imports an Array from an JSON File
     * @param jsonUrl
     * @return
     */
    public static String[] importArray(String jsonUrl) {
        JSONParser parser = new JSONParser();
        try{
            JSONArray array = (JSONArray) parser.parse(new FileReader("./src/data/"+jsonUrl));
            String[] returnArr = new String[array.size()];
            for(int i = 0; i<array.size(); i++){
                returnArr[i] = array.get(i).toString();
            }
            return returnArr;
        }catch (IOException |ParseException  e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Dialog for input file-names
     * @param msgName
     * @return
     */
    static String dialog(String msgName){
        Scanner sc = new Scanner(System.in);
        Boolean run = true;
        String input = null;
        do{
            System.out.println("Enter"+msgName+"(name.json):");
            if(sc.hasNextLine()){
                input = sc.nextLine();
                if(!input.isEmpty()){
                    run = false;
                }
            } else {
                System.out.println("False Input!");
                sc.next();
            }
        }while(run);

        sc.close();
        return input;
    }

}
