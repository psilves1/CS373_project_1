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

        while(tkn.equals("transition")){
            //System.out.println(inFile.next());
            int originalStateNum = inFile.nextInt();
            String transition = inFile.next();
            int newStateNum = inFile.nextInt();

            /*
            List of things we need to do here:
            1) Make sure originalStateNum exists in listOfStates. If not add it
            2) Make sure newStateNum exists in listOfStates. If not add it
            3) Create transition object
            4) Add transition object to the orignalState in the listOfStates
            5) Add newState to listOfStates the transition contains
             */
            generateIfNotExist(originalStateNum, listOfStates);
            State s = generateIfNotExist(newStateNum, listOfStates);

            for(int i = 0; i < listOfStates.size(); i++){
                if(listOfStates.get(i).getNumber() == originalStateNum){
                    listOfStates.get(i).setValidTransitions(new Transition(transition, s));
                }
            }

            if(!inFile.hasNext()){
                break;
            }
            tkn = inFile.next();
        }
        /*
        for(int i = 0; i < listOfStates.size(); i++){
            System.out.println(listOfStates.get(i).getNumber());
            listOfStates.get(i).printTransitions();
            System.out.println("");
        }
         */

        //TODO
        //I have the FA built, now I just need to read the string in and follow its path

        //file = new File(arg[1]);
        //inFile = new Scanner(file);

        //This reads in the string and turns it into a char array
        //tkn = inFile.next();
        tkn = arg[1];


        List<CurrentState> listOfIterations = new ArrayList<>();

        //adds all start states to list of iterations
        for (int i = 0; i < listOfStates.size(); i++) {
            if (listOfStates.get(i).isStartState()){
                listOfIterations.add(new CurrentState(listOfStates.get(i), tkn));
            }
        }

        /*
        for (int i = 0; i < listOfStates.size(); i++) {
            System.out.println(listOfStates.get(i).getNumber());
            listOfStates.get(i).printTransitions();
        }
         */

        List<State> reachedAcceptStates = new ArrayList<>();

        while(listOfIterations.size() != 0){

            List<State> branches = listOfIterations.get(0).transition();

            if(branches == null){
                //System.out.println(listOfIterations.get(0).getCurrentState().getNumber());
                if(listOfIterations.get(0).getCurrentState().isAcceptState()){
                    reachedAcceptStates.add(listOfIterations.get(0).getCurrentState());
                }
                listOfIterations.remove(0);
                continue;
            }
            if(branches.size() == 1){
                //System.out.println("branches == 1");
            }
            if(branches.size() > 1){
                //System.out.println("branches > 1");
                for (int i = 1; i < branches.size(); i++) {
                    listOfIterations.add(new CurrentState(branches.get(i),
                            listOfIterations.get(0).getRemainingChars()));
                }
            }

        }

        //System.out.println(reachedAcceptStates.size());

        for (int i = 0; i < reachedAcceptStates.size(); i++) {
            System.out.println(reachedAcceptStates.get(i).getNumber());
        }


    }
    //Generates a state if it does not exist
    static State generateIfNotExist(int n, List<State> listOfStates){
        for(int i = 0; i < listOfStates.size(); i++) {
            if (listOfStates.get(i).getNumber() == n) {
                return listOfStates.get(i);
            }
        }
        listOfStates.add(new State(n, false, false));
        for(int i = 0; i < listOfStates.size(); i++) {
            if (listOfStates.get(i).getNumber() == n) {
                return listOfStates.get(i);
            }
        }
        return listOfStates.get(0);
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

    public List<State> transition(String str){
        List<State> list = new ArrayList<>();
        for (int i = 0; i < validTransitions.size(); i++) {
            if(validTransitions.get(i).getValue().equals(str)){
                list.add(validTransitions.get(i).getEndState());
            }
        }
        return list;
    }

    public void printTransitions(){
        for(int i = 0; i < validTransitions.size(); i++){
            System.out.println(validTransitions.get(i).getValue());
            System.out.println("is the transition value to go to: ");
            System.out.println(validTransitions.get(i).getEndState().getNumber());
            System.out.println("");
        }
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

class CurrentState{

    private State currState;
    String remainingChars;

    public CurrentState(State s, String remainingChars){
        this.currState = s;
        this.remainingChars = remainingChars;
    }

    public State getCurrentState(){
        return currState;
    }

    public void setCurrState(State s){
        currState = s;
    }

    public String getRemainingChars(){
        return remainingChars;
    }

    public List<State> transition(){

        if(remainingChars.length() == 0){
            return null;
        }

        List<State> list = currState.transition(remainingChars.substring(0,1));

        if(list.size() == 0) {
            currState = null;
        }
        else{
            currState = list.get(0);
        }
        remainingChars = remainingChars.substring(1);
        return list;
    }
}

class Transition{

    private State endState;
    private String value;

    public Transition(String value, State endState){
        this.endState = endState;
        this.value = value;
    }

    public State getEndState() {
        return endState;
    }

    public void setEndState(State endState) {
        this.endState = endState;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
