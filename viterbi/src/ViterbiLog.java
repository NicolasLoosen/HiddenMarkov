public class ViterbiLog {

    Node[][] path;
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
        // for all States F,U
        // initialize first Node q0   Fair    E(F,5) * max (q0(0) * M(q0,F)) -> 1/10 * max(1/2)
        //                            Unfair  E(U,5) * max (q0(0) * M(q0,U)) -> 1/6 * max(1/2)
        // Node should start at q0 = 0???
        for (int i = 0; i < states.length; i++) {
            path[0][i] = new Node(-1, Math.log(emmission_probability[i][results[i]-1]) + Math.log(transition_probability[0][i+1]));
        }
        // for prefix length
        for(int i = 1 ; i < results.length; i++){
            // for all states for each prefix
            for (int j = 0; j < states.length; j++) {
                maxProbability(i, j, transition_probability);
                //                 max(op(i-1)*M(p,q)) * E(q,xi)
                path[i][j].value = path[i][j].value + Math.log(emmission_probability[j][results[i]-1]);
            }
        }

        double temp = path[results.length-1][0].value;
        double bestPathValue = temp;
        int best = 0;
        for (int i = 1; i < states.length; i++) {
            temp = path[results.length-1][i].value;
            if(temp>bestPathValue){
                bestPathValue = temp;
                best = i;
            }
        }
        Node bestPath = path[results.length-1][best];
        String result = states[best];

        for(int i = results.length-2; i >= 0; i--){
            result += states[bestPath.prevState];
            bestPath = path[i][bestPath.prevState];
        }

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
        double temp = path[resultIterator-1][0].value + Math.log(transition_probability[1][state+1]);
        double biggest = temp;
        int prevState = 0;
        for (int i = 1; i < path[resultIterator].length; i++){
            temp = path[resultIterator-1][i].value + Math.log(transition_probability[i+1][state+1]);       
            if(temp > biggest){
                biggest = temp;
                prevState = i;
            }
        }
        path[resultIterator][state] = new Node(prevState, biggest); 
    }
}
