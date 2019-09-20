import static java.lang.Math.pow;
import javax.swing.*;
import java.util.*;

/*
Luke Maher [17223563]
Samuel Foley [17237874]
Carl Frohburg [17237823]
Lennart Mantel [17207266]
*/

public class is17223563 {
    
    static int N = 0;

    public static void main(String[] args) {

        ArrayList<String> validState = new ArrayList<String>();
        final ArrayList<ArrayList<String>> Start = new ArrayList<ArrayList<String>>();
        final ArrayList<ArrayList<String>> End = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> Current = new ArrayList<ArrayList<String>>();
        ArrayList<String> checkState = new ArrayList<String>();
        boolean inputStage1 = true;
        boolean inputStage2 = true;
        boolean errorState = false;
        String startInput = "";
        String endInput = "";
        
        Object[] options = {"15-puzzle", "8-puzzle"};
        
        int type = JOptionPane.showOptionDialog(null, "Plaease choose puzzle type", "Puzzle Solver", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if(type == 0){
            N = 4;
        }
        else if(type == 1){
            N = 3;
        }
        else{
            System.exit(0);
        }
        
        for (int i = 0; i < N; i++) {
            Start.add(new ArrayList<String>());
            End.add(new ArrayList<String>());
            Current.add(new ArrayList<String>());
        }
        
        for (int i = 0; i < pow(N, 2); i++) {
            validState.add(String.valueOf(i));
        }

        while (inputStage1 == true) {

            checkState = new ArrayList<String>();

            startInput = (String) JOptionPane.showInputDialog(null, "Please enter values for initial state", "Puzzle Solver", JOptionPane.INFORMATION_MESSAGE, null, null, null);

            checkState.addAll(Arrays.asList(startInput.split(" ")));
            
            if (checkState.size() == validState.size()) {
                for (int i = 0; i < pow(N, 2); i++) {
                    if (!validState.contains(checkState.get(i))) {
                        errorState = true;
                    }
                }
            } else {
                errorState = true;
            }

            if (errorState == true) {
                JOptionPane.showMessageDialog(null, "Invalid State entered", "Error", JOptionPane.ERROR_MESSAGE);
                errorState = false;
            } else {
                inputStage1 = false;
            }

        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Start.get(i).add(checkState.get(j + (i * N)));
            }
        }

        while (inputStage2 == true) {

            checkState = new ArrayList<String>();

            endInput = (String) JOptionPane.showInputDialog(null, "Please enter values for final state", "Puzzle Solver", JOptionPane.INFORMATION_MESSAGE, null, null, null);

            checkState.addAll(Arrays.asList(endInput.split(" ")));

            if (checkState.size() == validState.size()) {
                for (int i = 0; i < pow(N, 2); i++) {
                    if (!validState.contains(checkState.get(i))) {
                        errorState = true;
                    }
                }
            } else {
                errorState = true;
            }

            if (errorState == true) {
                JOptionPane.showMessageDialog(null, "Invalid State entered", "Error", JOptionPane.ERROR_MESSAGE);
                errorState = false;
            } else {
                inputStage2 = false;
            }

        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                End.get(i).add(checkState.get(j + (i * N)));
            }
        }

        ArrayList<ArrayList<ArrayList<String>>> checkStates = mem(Start, End, 1);

        System.out.println("Completed in: " + checkStates.size() + " move(s).");	
				
    }
		//POSSIBLE SOLUTIONS: 
		//Check literally every single path, find a way to ignore some if possible
		//Change open to not remove everything every loop and find a way to add a score with the corresponding depth level of when it was first encountered
		//e.g 4d array, which includes the states and score: OPEN: 1st level = which puzzle, 2nd and 3rd = puzzle state, 4th = score.
    public static ArrayList<ArrayList<ArrayList<String>>> mem(ArrayList<ArrayList<String>> Start, ArrayList<ArrayList<String>> End, int print) {

        ArrayList<ArrayList<ArrayList<String>>> Closed = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> Current = clone(Start);

        if (Current.equals(End)) {
            Closed.add(Start);
            return Closed;
        }

        while (true) {

            ArrayList<ArrayList<ArrayList<String>>> Open = new ArrayList<ArrayList<ArrayList<String>>>();
            ArrayList<ArrayList<ArrayList<String>>> possible = new ArrayList<ArrayList<ArrayList<String>>>();
            final ArrayList<ArrayList<String>> finCurrent = clone(Current);

            for (int i = 0; i < mvmts(zeroPos(finCurrent)).size(); i++) {
                possible.add(state(mvmts(zeroPos(finCurrent)).get(i), finCurrent));
            }

            for (int i = 0; i < possible.size(); i++) {
                if (!Closed.contains(possible.get(i))) {
                    Open.add(possible.get(i));
                }
            }
						
            if(Open.contains(Current)){
                Open.remove(Current);
            }

            Closed.add(Current);
            if(print == 1)
                System.out.println(toFormat(Current));
            Current = bestPath(Open, End);
            if (Current.equals(End)) {
            if(print == 1)
                System.out.println(toFormat(End));
                break;
            }

        }

        return Closed;

    }

    public static ArrayList<String> mvmts(String zeroPos) {
        ArrayList<String> movements = new ArrayList<>();
        if(N == 3){
            if ("11".equals(zeroPos)) {
                movements.add("01s");
                movements.add("10e");
                movements.add("12w");
                movements.add("21n");
            } else if (zeroPos.charAt(0) == '0') {
                switch (zeroPos.charAt(1)) {
                    case '1':
                        movements.add("11n");
                        movements.add("00e");
                        movements.add("02w");
                        break;
                    case '0':
                        movements.add("10n");
                        movements.add("01w");
                        break;
                    default:
                        movements.add("12n");
                        movements.add("01e"); 
                        break;
                }
            } else if (zeroPos.charAt(0) == '2') {
                switch (zeroPos.charAt(1)) {
                    case '1':
                        movements.add("11s");
                        movements.add("20e");
                        movements.add("22w");
                        break;
                    case '0':
                        movements.add("10s");
                        movements.add("21w");
                        break;
                    default:
                        movements.add("12s");
                        movements.add("21e"); 
                        break;
                }
            } else if (zeroPos.charAt(0) == '1') {
                switch (zeroPos.charAt(1)) {
                    case '0':
                        movements.add("00s");
                        movements.add("20n");
                        movements.add("11w"); 
                        break;
                    default:
                        movements.add("02s");
                        movements.add("22n");
                        movements.add("11e"); 
                        break;
                }
            }
        }
        else{
            switch (zeroPos.charAt(0)) {
                case '0':
                    switch (zeroPos.charAt(1)) {
                        case '3':
                            movements.add("12w");
                            movements.add("13n");
                        case '1':
                            movements.add("11n");
                            movements.add("00e");
                            movements.add("02w");
                            break;
                        case '0':
                            movements.add("10n");
                            movements.add("01w");
                            break;
                        default:
                            movements.add("12n");
                            movements.add("03e");
                            movements.add("01w");
                            break;
                    }   break;
                case '2':
                    switch (zeroPos.charAt(1)) {
                        case '1':
                            movements.add("11s");
                            movements.add("20e");
                            movements.add("22w");
                            movements.add("31n");
                            break;
                        case '0':
                            movements.add("10s");
                            movements.add("21w");
                            movements.add("30n");
                            break;
                        default:
                            movements.add("12s");
                            movements.add("21e");
                            movements.add("32n");
                            movements.add("23w");
                            break;
                        case '3':
                            movements.add("13s");
                            movements.add("22e");
                            movements.add("33n");
                    }   break;
                case '1':
                    switch (zeroPos.charAt(1)) {
                        case '1':
                            movements.add("01s");
                            movements.add("10e");
                            movements.add("12w");
                            movements.add("21n");
                        case '0':
                            movements.add("00s");
                            movements.add("20n");
                            movements.add("11w");
                            break;
                        default:
                            movements.add("02s");
                            movements.add("22n");
                            movements.add("11e");
                            movements.add("13w");
                            break;
                        case '3':
                            movements.add("03s");
                            movements.add("12e");
                            movements.add("23n");
                    }   switch (zeroPos.charAt(1)) {
                        case '3':
                            movements.add("32w");
                            movements.add("23s");
                        case '1':
                            movements.add("21s");
                            movements.add("30e");
                            movements.add("32w");
                            break;
                        case '0':
                            movements.add("20s");
                            movements.add("31w");
                            break;
                        default:
                            movements.add("22s");
                            movements.add("31e");
                            movements.add("33w");
                            break;
                    }   break;
                default:
                    break;
            }
        }
        return movements;
    }

    public static ArrayList<ArrayList<String>> state(String movement, ArrayList<ArrayList<String>> Current) {


        int y = Integer.parseInt(String.valueOf(zeroPos(Current).charAt(0)));
        int z = Integer.parseInt(String.valueOf(zeroPos(Current).charAt(1)));
        int w = Integer.parseInt(String.valueOf(movement.charAt(0)));
        int x = Integer.parseInt(String.valueOf(movement.charAt(1)));
        ArrayList<ArrayList<String>> clone = clone(Current);
        clone.get(y).set(z, clone.get(w).get(x));
        clone.get(w).set(x, "0");

        return clone;

    }

    public static String zeroPos(ArrayList<ArrayList<String>> state) {
        String pos = "";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ("0".equals(state.get(i).get(j))) {
                    pos = String.valueOf(i) + String.valueOf(j);
                }
            }
        }
        return pos;
    }
		
    public static ArrayList<ArrayList<String>> bestPath(ArrayList<ArrayList<ArrayList<String>>> selection, ArrayList<ArrayList<String>> target) {

        int bestVal = 10000;
        int bestValIndex = 0;
        int score = 0;
        for (int i = 0; i < selection.size(); i++) {
            score = getScore(selection.get(i), target);
            if (score < bestVal) {
                bestVal = score;
                bestValIndex = i;
            }
        }

        return selection.get(bestValIndex);

    }

    public static ArrayList<ArrayList<String>> clone(ArrayList<ArrayList<String>> target) {

        ArrayList<ArrayList<String>> clone = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < N; i++) {
            clone.add(new ArrayList<String>());
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                clone.get(i).add(target.get(i).get(j));
            }
        }

        return clone;
    }

    public static int getScore(ArrayList<ArrayList<String>> Start, ArrayList<ArrayList<String>> End) {
        int m = 0, n = 0, distance = 0, score = 0;
        boolean nonZero = true;
        for (int k = 0; k < pow(N, 2); k++) {
            nonZero = true;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (Start.get(m).get(n).equals("0")) {
                        nonZero = false;
                    }
                    if ((Start.get(m).get(n).equals(End.get(i).get(j))) && (nonZero)) {
                        distance = (Math.max(m, i) - Math.min(m, i)) + (Math.max(n, j) - Math.min(n, j));
                        score += distance;
                    }
                }
            }
            n++;
            if (n > N-1) {
                n = 0;
                m++;
            }
        }
        return score;
    }
		
    public static String toFormat(ArrayList<ArrayList<String>> state){
            String output = "";
            for(int i = 0; i < N; i++){
                    for(int j = 0; j < N; j++){
                            output += state.get(i).get(j) + " ";
                    }
                    output += "\n";
            }
            return output;
    }

}