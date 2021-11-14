import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String args[]){
 
        int[] results = Utils.getResults(Utils.loadData(Utils.dialog("Result")));
        String[] states = Utils.importArray(Utils.dialog("States"));
        double[][] transition_probability = Utils.import2DArray(Utils.dialog("transition probability"));
        double[][] emmission_probability =  Utils.import2DArray(Utils.dialog("emmission probability"));

    //     // int[] results = Utils.getResults(Utils.loadData("wuerfel2021.txt"));
    //     // String[] states = Utils.importArray("states.json");
    //     // double[][] transition_probability = Utils.import2DArray("transition_probability.json");
    //     // double[][] emmission_probability =  Utils.import2DArray("emmission_probability.json");

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("|                                               Your Inputs                                              |");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("Your Results are: "+Arrays.toString(results));
        System.out.println("Your States are: "+Arrays.toString(states));
        System.out.println("Your Transition Probabilitys are: "+Arrays.deepToString(transition_probability));
        System.out.println("Your Emmission Probabilitys are: "+Arrays.deepToString(emmission_probability));
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("|                                           Start Calculation Viterbi                                     |");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        Viterbi v = new Viterbi(); 
        String resultViterbi = v.Vitality(results,states,transition_probability,emmission_probability);
        System.out.println("Final Result of Viterbi(Length: "+resultViterbi.length()+"):  " + "\n" + resultViterbi);
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("|                                           Start Calculation Viterbi Reverse                             |");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        Viterbi vreverse = new Viterbi(); 
        String resultViterbireverse = vreverse.Vitality(Utils.reverseArray(results),states,transition_probability,emmission_probability);
        System.out.println("Final Result of Viterbi(Length: "+resultViterbireverse.length()+"):  " + "\n" + resultViterbireverse);
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("|                                           Start Calculation Viterbi Log                                 |");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        ViterbiLog vlog = new ViterbiLog(); 
        String resultViterbiLog = vlog.Vitality(results,states,transition_probability,emmission_probability);
        System.out.println("Final Result of Viterbi(Length: "+resultViterbiLog.length()+"):  " + "\n" + resultViterbiLog);
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("|                                           Start Calculation Viterbi Log Reverse                         |");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        ViterbiLog vlogreverse = new ViterbiLog(); 
        String resultViterbiLogreverse = vlogreverse.Vitality(Utils.reverseArray(results),states,transition_probability,emmission_probability);
        System.out.println("Final Result of Viterbi(Length: "+resultViterbiLogreverse.length()+"):  " + "\n" + resultViterbiLogreverse);     
    }
}
