
/**
 * name: Eric Osterman
 * assignment: PP3
 */

import java.util.Comparator;

/**
 * String comparator class used to compare first 3 characters of string passed to it
 * used for TreeMap
 */
public class StringComparator implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        // splitting string into first 3 characters
        String[] o1First3 = o1.split("^.{3}");
        String o13char = o1First3.toString();

        String[] o2First3 = o2.split("^.{3}");
        String o23Char = o2First3.toString();
        
        // comparing the 2 striongs that have first 3 characters from each string passed
        return o13char.compareTo(o23Char);
    }
    
} //end of class