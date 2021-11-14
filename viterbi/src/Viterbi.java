public class Viterbi {

    Node[][] path;
    public int MULTI = 1;
    /**
     * 
     * @param results                   Würfel Ergebnisse
     * @param states                    F,U Zustände die erreicht werden können
     * @param transition_probability    Übergangswahrscheinlichkeit der Zustände(F->U, U->F..), 1/20, 19/20, 1/2(im ersten Fall)
     *                                  [[[0],[1,2],[1,2]],[[0],[19,20],[1,20]],[[0],[1,20],[19,20]]]
     * @param emmission_probability     Wahrscheinlichkeit eine Augenzahl zu werfen in Beziehung zu Zustand, 1/10, 1/2, 1/6
     *                                  [[[1,6],[1,6],[1,6],[1,6],[1,6],[1,6]],[[1,10],[1,10],[1,10],[1,10],[1,10],[1,2]]]
     */
    public String Vitality(int[] results, String[] states, double[][] transition_probability, double[][] emmission_probability){
        path = new Node[results.length][states.length];
        if(MULTI == 0){
            MULTI = emmission_probability[0].length-1;
        }
        // for all States F,U
        for (int i = 0; i < states.length; i++) {
            // initialize first Node      Fair    E(F,5) * max (q0(0) * M(q0,F)) -> 1/6 * max(1/2)
            //                            Unfair  E(U,5) * max (q0(0) * M(q0,U)) -> 1/10 * max(1/2)
            path[0][i] = new Node(-1, emmission_probability[i][results[i]-1] * transition_probability[0][i+1]);
            // forward Probability
            // initialize first Node      Fair    E(F,5) * max (q0(0) * M(q0,F)) -> 1/6 * max(1/2)
            //                            Unfair  E(U,5) * max (q0(0) * M(q0,U)) -> 1/10 * max(1/2)
            path[0][i].forward = emmission_probability[i][results[i]-1] * transition_probability[0][i+1] * MULTI;
            System.out.println("Initialize Viterbi: "+path[0][i].value);
            System.out.println("Initialize Forward: "+path[0][i].forward);
        }
        // for prefix length
        for(int i = 1 ; i < results.length; i++){
            // for all states for each prefix
            System.out.println("----------------" + i + "----------------");
            for (int j = 0; j < states.length; j++) {
                // viterbi max
                maxProbability(i, j, transition_probability);
                // forward Probability
                sumProbability(i, j, transition_probability);

                if(j == 0){
                    System.out.println("\u03C3" + states[j] + "(" + i + ") = E(" + states[j] + "," + results[i] + ") * max( \u03C3" + states[j] + "(" + results[i-1] + ") * M(" + states[j] + "," + states[j] + ")");
                    System.out.println("\u03C3" + states[j] + "(" + i + ") = E(" + states[j] + "," + results[i] + ") * max( \u03C3" + states[j+1] + "(" + results[i-1] + ") * M(" + states[j+1] + "," + states[j] + ")");
                } else{
                    System.out.println("\u03C3" + states[j] + "(" + i + ") = E(" + states[j] + "," + results[i] + ") * max( \u03C3" + states[j] + "(" + results[i-1] + ") * M(" + states[j] + "," + states[j] + ")");
                    System.out.println("\u03C3" + states[j] + "(" + i + ") = E(" + states[j] + "," + results[i] + ") * max( \u03C3" + states[j-1] + "(" + results[i-1] + ") * M(" + states[j-1] + "," + states[j] + ")");
                }
                //                 max(op(i-1)*M(p,q)) * E(q,xi)
                System.out.println("\u03C3" + states[j] + "(" + i + ") = E(" + emmission_probability[j][results[i]-1] + ") * max( \u03C3" + states[j] + "(" + path[i-1][0].value + ") * M(" + transition_probability[j+1][1] + ")");
                System.out.println("\u03C3" + states[j] + "(" + i + ") = E(" + emmission_probability[j][results[i]-1] + ") * max( \u03C3" + states[j] + "(" + path[i-1][1].value + ") * M(" + transition_probability[j+1][2] + ")");

                path[i][j].value = path[i][j].value * emmission_probability[j][results[i]-1];
                path[i][j].forward = path[i][j].forward * emmission_probability[j][results[i]-1] * MULTI;
                
                System.out.println("Viterbi: " + path[i][j].value);
                System.out.println("Forward: " + path[i][j].forward);
                System.out.println();
                
            }
        }

        // start calc max probability 
        // get the value 0 from the biggest calc prefix (in our case prefix = 300)
        double temp = path[results.length-1][0].value;
        double bestPathValue = temp;
        int best = 0;
        // for all states check if the other prefix calc are bigger or smaller than the other
        for (int i = 1; i < states.length; i++) {
            temp = path[results.length-1][i].value;
            // compare temp with older bestpathvalue
            // if temp is bigger change it to bestpathvalue
            if(temp>bestPathValue){
                bestPathValue = temp;
                best = i;
            }
        }
        Node bestPath = path[results.length-1][best];
        String result = states[best];


        // backward Probability
        for (int i = 0; i < states.length; i++) {
            path[results.length-1][i].backward = 1 * MULTI;
            path[results.length-1][i].posteriori = path[results.length-1][i].backward * path[results.length-1][i].forward;
            System.out.println("---------------------------------");
            System.out.println("Initialize Backward and Posteriori: ");
            System.out.println("Backward: " + path[results.length-1][i].backward);
            System.out.println("Posteriori: " + path[results.length-1][i].posteriori);
        }
        // for prefix length
        for(int i = results.length-2 ; i >= 0; i--){
            // for all states for each prefix
            for (int j = 0; j < states.length; j++) {
                double backward = 0;
                for(int k = 0; k < states.length; k++){
                    backward += emmission_probability[k][results[i+1]-1] * transition_probability[j+1][k+1] * path[i+1][k].backward;
                }

                path[i][j].backward = backward * MULTI;
                path[i][j].posteriori = path[i][j].backward * path[i][j].forward;
                System.out.println("----------------" + i + "----------------");
                System.out.println("backward: "+path[i][j].backward);
                System.out.println("posteriori: "+path[i][j].posteriori);
                System.out.println("Viterbi: " + path[i][j].value);
                System.out.println("Forward: " + path[i][j].forward);
            }
        }


        // build viterbi string
        for(int i = results.length-2; i >= 0; i--){
            result += states[bestPath.prevState];
            bestPath = path[i][bestPath.prevState];
        }

        // build posteriori String
        String posterioriString = "";
        for(int i = 0 ; i < results.length; i++){
            double tempo = path[i][0].posteriori;
            double bestPosteriori = tempo;
            int bestPosterio = 0;
            // for all states check if the other prefix calc are bigger or smaller than the other
            for (int j = 1; j < states.length; j++) {
                tempo = path[i][j].posteriori;
                if(tempo >= bestPosteriori){
                    bestPosteriori = tempo;
                    bestPosterio = j;
                }
            }
            posterioriString += states[bestPosterio];
        }

        System.out.println("PsterioriString("+ posterioriString.length() +"): "+posterioriString);
        StringBuilder sb = new StringBuilder(result);
        sb.reverse();

        return sb.toString();
    }
    /**
     * 
     * @param resultIterator            prefix position
     * @param state                     State (F or U)
     * @param transition_probability    array with values to get the correct transition
     */
    private void maxProbability(int resultIterator, int state, double[][] transition_probability){
        // E(q,xi) * max    ((Wahrscheinlichkeit prevState in F) * M(F,F)) 
        //                  ((Wahrscheinlicjkeit prevState in U) * M(U,F))
        //                     prev Value op(i-1)     * (M(p,q))
        double temp = path[resultIterator-1][0].value * transition_probability[0+1][state+1];
        double biggest = temp;
        int prevState = 0;
        for (int i = 1; i < path[resultIterator-1].length; i++){
            temp = path[resultIterator-1][i].value * transition_probability[i+1][state+1];
            // check if the prev biggest is still bigger than the new temp
            // if its still bigger prev state keeps F, otherwise the state changes to L  
            // lelft to right >   ||   right to left >=  
            if(temp >= biggest){
                biggest = temp;
                prevState = i;
            }
        }
        path[resultIterator][state] = new Node(prevState, biggest); 
    }

    /**
     * 
     * @param resultIterator            prefix position
     * @param state                     State (F or U)
     * @param transition_probability    array with values to get the correct transition
     */
    private void sumProbability(int resultIterator, int state, double[][] transition_probability){
        double sum = 0;
        for (int i = 0; i < path[resultIterator-1].length; i++){
            sum += path[resultIterator-1][i].forward * transition_probability[i+1][state+1];
        }
        path[resultIterator][state].forward = sum;
    }
}
