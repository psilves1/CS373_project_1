import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String[] arg = {"src/sample_1.txt" , "000"};

        List<State> listOfStates = new ArrayList<>();

        File file = new File(arg[0]);
        Scanner inFile = new Scanner(file);

        String tkn;

        tkn = inFile.next();

        //Constructs the states that have
        while(tkn.equals("state")){
            int num = inFile.nextInt();
            String type = inFile.next();

            if(type.equals("start")){
                listOfStates.add(new State(num, true, false));
            }
            if(type.equals("accept")){
                boolean alreadyExists = false;
                for(int i = 0; i < listOfStates.size(); i++){
                    if(listOfStates.get(i).getNumber() == num) {
                        listOfStates.get(i).setAccept(true);
                        alreadyExists = true;
                        break;
                    }
                }
                if(!alreadyExists){
                    listOfStates.add(new State(num, false, true));
                }

            }

            tkn = inFile.next();
        }


    }
}

class State{

    private int number;
    private List<Transition> validTransitions;
    private boolean isStartState;
    private boolean isAcceptState;

    public State(int number, boolean isStartState, boolean isAcceptState) {
        this.number = number;
        this.validTransitions = new ArrayList<>();
        this.isStartState = isStartState;
        this.isAcceptState = isAcceptState;

    }

    public void setValidTransitions(Transition t){
        validTransitions.add(t);
    }

    public boolean isReachable(Transition t){
        return validTransitions.contains(t);
    }

    public int getNumber(){
        return number;
    }

    public boolean isStartState() {
        return isStartState;
    }

    public boolean isAcceptState() {
        return isAcceptState;
    }

    public void setAccept(boolean b) {
        isAcceptState = b;
    }
}

class Transition{

    private List<State> listOfStates;
    private String value;

    public Transition(String value){
        this.listOfStates = new ArrayList<>();
        this.value = value;
    }

    public List getListOfStates(){
        return listOfStates;
    }
}
