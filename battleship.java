import java.util.*;
import java.io.*;

public class battleship {
        //declare and initialize the variables 
        private int numOfGuesses = 0; 
        private GameHelper helper = new GameHelper();
        private ArrayList<DotCom> dotComsList = new ArrayList<DotCom>();
        private void setUpGame(){

            // make three dot coms objects and stick them in the array 
            DotCom one = new DotCom();
            one.setName("Pets.com");
            DotCom two = new DotCom();
            two.setName("eToys.com");
            DotCom three = new DotCom();
            three.setName("Go2.com");

            dotComsList.add(one);
            dotComsList.add(two);
            dotComsList.add(three);

            // brief introductions fpr the user 
            System.out.println("Your goal is to sink three dot coms");
            System.out.println("Pets.com, eToys.com, Go2.com"); 
            System.out.println("Try to Sink them all in the fewest number of guesses"); 
            // repeat with each dot com in the list 
            for (DotCom dotComToSet: dotComsList){
                // ask the helper for a DotCom location
                ArrayList<String> newLocation = helper.placeDotCom(3);
                //call the setter method in this DotCom to give it location you got from the helper 
                dotComToSet.setCellsLocation(newLocation);
            }

        } // close setUpGame method 

        private void startPlaying(){
            // as long as the dot com list is not empty
            while (!dotComsList.isEmpty()){
                // get user input 
                String userGuess = helper.getUserInput();
                // call the checkUserGuess method 
                checkUserGuess(userGuess); 
            }
            // call the finish game method 
            finishGame();
        }
        private void checkUserGuess(String UserGuess){
            numOfGuesses++;
            String result = "miss";
            for(DotCom dotComToTest : dotComsList){
                result = dotComToTest.checkGuess(UserGuess);
                if(result.equals("hit")){
                    break;
                }
                if(result.equals("kill")){
                    dotComsList.remove(dotComToTest);
                    break;
                }
            } // close for loop 
            System.out.println(result);

        } 
        private void finishGame(){
            System.out.println("All Dot Coms are dead! Your Stock is now worthless.");
            if(numOfGuesses <= 18){
                System.out.println("It only took you " + numOfGuesses + " guesses");
                System.out.println("You got out before your options sank!");

            }
            else {
                System.out.println("Took you long enough. " + numOfGuesses + " guesses");
                System.out.println("Fish are Dancing with your options.");
            }
        }
       
    public static void main(String[] args) {
        battleship game = new battleship();
        game.setUpGame();
        game.startPlaying();
    }
}
class DotCom {
    // int [] locationCells; 
    // int numOfHits = 0;
    private ArrayList<String> locationCells; 
    private String name;

    public void setCellsLocation(ArrayList<String> loc){
       locationCells = loc;
    }
    public void setName (String n){
        name = n;
    }
    public String checkGuess(String stringGuess){
        // int guess = Integer.parseInt(stringGuess); // convert string into an int 
        // variable to hold the result 
        String result = "miss"; 
        //Find out if the user guess is in the ArrayList, by asking for its index.
        // If it’s not in the list, then indexOf() returns a -1
        int index = locationCells.indexOf(stringGuess);
         // if the index is greater than 0 or equal to 0 then the user guess is in the list so remove it 
        if( index >= 0 ){ 
            locationCells.remove(index);
            if(locationCells.isEmpty()){
                result = "kill";
                System.out.println("Ouch! you sunk " + name + ":( ");
            }
            else {
                result = "hit";
            }
        }
        return result;
    }
}
// read input from stdin 
class GameHelper {
    private static final String alphabet = "abcdefg";
    private int gridLength = 7; 
    private int gridSize = 49; 
    private int [] grid = new int[gridSize];
    private int comCount = 0;
 
    public String getUserInput(){
        String inputLine = null;
        System.out.print("Enter a guess: ");
        try{
            BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
            inputLine = is.readLine();
            if( inputLine.length() == 0) return null;
        }
        catch (IOException e){
            System.out.println("Ioexception: " + e);
        }
        return inputLine;
    }
    public ArrayList<String> placeDotCom (int comSize){
        ArrayList<String> alphaCells = new ArrayList<String>();
        String [] alphacoords = new String [comSize]; // holds f6 type coords
        String temp = null;  // temporary string for concat 
        int[] coords = new int[comSize];// current candidate coords
        int attempt = 0; //current attempt counter 
        boolean success = false; // flag = found a good location?
        int location = 0; // current starting location

        comCount++;
        int incr = 1; //set horizantal increment
        if((comCount % 2) == 1){ // if odd dot com (place vertically)
            incr = gridLength; //set vertical length  
        }
        while ( !success & attempt++ < 200){
            location = (int) (Math.random() * gridSize); // get random starting point
                // System.out.println ("try " + location);
                int x = 0; // nth position in dotcom to place 
                success = true;// assume success 
                while ( success && x < comSize){ // look for adjacent unused spots 
                    if(grid[location] == 0){ // if not already used 
                        coords [x++] = location; //save location 
                        location +=incr; // try 'next' adjacent
                        if(location >= gridSize){ // out of bounds - 'bottom'
                            success = false; //failure 
                        }
                        if( x > 0 && (location % gridLength == 0)){ // out of bounds - right edge 
                           success = false; 


                        }
                    
                    }
                    else { // found already used location
                        // System.out.println("used " + location);
                        success = false;
                    }

                }

            
        } // end while 
        int x = 0;
        int row = 0;
        int column = 0;

        while ( x < comSize){
            grid[coords[x]] = 1; // mark master grid pts. as 'used'
            row = (int) (coords[x] /gridLength); // get row value
            column = coords[x] % gridLength; // get numeric column value
            temp = String.valueOf(alphabet.charAt(column)); //convert to alpha
           alphaCells.add(temp.concat(Integer.toString(row)));
        // the sentence to use to get the location of the dot com cells
        //    System.out.print(" coord " + x + " = " + alphaCells.get(x));


            x++;
        }
        // System.out.println("\n");
        return alphaCells;

    }


    }
