/**
 * name: Eric Osterman
 * uid: ejo223
 * assignment: PP3
 * Purpose: compare and contrast the preformace/time complexioties of Tree, HashMap with Serial chaining
 * and HashMap with linear probing
 * compare time complexities by analyzing iterations it stakes to find random items in data set
 * also look at number of collisions between both HashMaps at different sizes
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {

    /**
     * list of final variables/constants
     * CAPACITY is the capacity of tree & 2 HashMaps when comparing iterations
     * INCREMENT is used to increment size of HashTables when comparing collisions
     * FILENAME is the first filename "users.txt" that has 100k usernames & passwords
     * SECONDFILENAME is used for choosing 100 random usernames
     * NUMSEARCHES is how many usernames will be randomly chosen when comparing iterations
     */
    public static final int CAPACITY = 100_000;
    public static final int INCREMENT = 50_000;
    public static final String FILENAME = "users.txt";
    public static final String SECONDFILENAME = "user_list.txt";
    public static final int NUM_SEARCHES = 100;

    public static void main(String[] args) {
        /**
         * creating new instance of each data structure to compare iterations
         * when searching for something in each list
         */
        StringComparator sc = new StringComparator();
        TreeMap<String, String> tm = new TreeMap<>(sc);
        HashMapSC<String, String> hmSC = new HashMapSC<>(CAPACITY);
        HashMapLP<String, String> hmLP = new HashMapLP<>(CAPACITY);

        /**
         * adding username and password to each data structure from file
         * may take some time to load
         */
        File file = new File(FILENAME);
        int iters = 0;
        try {
            Scanner read = new Scanner(file);
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] tokens = line.split(" ");
                tm.add(tokens[0], tokens[1]);
                hmSC.put(tokens[0], tokens[1]);
                hmLP.put(tokens[0], tokens[1]);
            }
            read.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error, file not found. here");
            System.exit(0);
        }

        // calling method to print out each structure's amount iterations it takes to find key
        iterationsInStructures(hmSC, hmLP, tm);

        /**
         * comparing each HashMap's collisions as size increases
         * may take some time to load
         * collisions occur when same multiple keys have same hash code and are trying to be inserted at same index
         * serial chaining uses a linked list and adds those keys to list at each index
         * linear probing finds keys a new index using % function
         * As size increases, number of collisions levels off for each HashMap
         * Larger size for HashMap, more spots meaning more places for values to go which leads to less collisions
         * HashMap with Serial chaining starts out with more collisions compared to linear probing,
         * but as size increases, num of collisions declines
         * SC levels out at 300k+ size with 8733 collisions and doesnt increase/decrease
         * HashMap with linear probing start with less collisions compared to serial chaining
         * but as size increases, num of collisions declines, but ends up being more than SC
         * LP levels out with 9313 at 300K+ size 
         */
        System.out.printf("\n%-20s\t%-15s\t%-15s", "size", "HashMapLL", "HashMapLP");
        for (int i = 50_000; i < 500_000; i += 50_000) {
            HashMapSC<String, String> hmsc = new HashMapSC<>(i); // 50k
            HashMapLP<String, String> hmlp = new HashMapLP<>(i); // 50k

            File file1 = new File(FILENAME);
            try {
                Scanner read = new Scanner(file);
                while (read.hasNextLine()) {
                    String line = read.nextLine();
                    String[] tokens = line.split(" ");
                    hmsc.put(tokens[0], tokens[1]);
                    hmlp.put(tokens[0], tokens[1]);
                }
                read.close();
                System.out.printf("\n%-20d\t%-15s\t%-15s", i, hmsc.collisions, hmlp.collisions);

            } catch (FileNotFoundException fnfe) {
                System.out.println("Error, file not found. here");
                System.exit(0);
            }

        }
        System.out.println(); // spacing

    }

    /**
     * method that picks 100 random usernames from file
     * has each data structure attempt to get key(username) from itself
     * record and print out iterations it took every 4 words
     * prints average after
     * @param hmsc HashMap with serial chaining
     * @param hmlp HashMap with linear probing
     * @param tm TreeMap
     * Results: TreeMap had an average of around 20 iterations to find key
     * this supports/visually shows it has time complexity of O(log n) for getting
     * TreeMap uses binary search to find key --> O(log n)
     * HashMap SC & LP have consistent average of 1 iterations to find key
     * this supports/visually shows it has a time complexity of O(1) for getting
     * HashMaps have such fast get() speed because each key has own index (except for collisions, see above)
     * when collisions happen, may take more thasn 1 iterations to find key but still extremely fast compared to TreeMap
     */
    public static void iterationsInStructures(HashMapSC<String, String> hmsc, HashMapLP<String, String> hmlp,
            TreeMap<String, String> tm) {
        // variables to keep track of total iterations
        int tmTotalIters = 0;
        int hmscTotalIters = 0;
        int hmlpTotalIters = 0;

        // initializing ArrayList with usernames from file
        File user_list = new File(SECONDFILENAME);
        ArrayList<String> userName = new ArrayList<>();
        try {
            Scanner read = new Scanner(user_list);
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] tokens = line.split("\\s");
                userName.add(tokens[0]);
            }
            read.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println("Error, file not found.");
            System.exit(0);
        }

        /**
         * choosing 100 random usernames from ArrayList above
         * keeping track of number of iterations it takes to get key
         * every 4 words, prints out iterations for that word
         * after prints out average
         */
        System.out.printf("\n%-20s\t%-15s\t%-15s\t%-15s", "username", "TreeMap", "HashMap Sc", "HashMap LP");
        for (int i = 0; i < NUM_SEARCHES; i++) {
            int rndIndex = (int) (Math.random() * userName.size());
            String rndWord = userName.get(rndIndex);
            tm.get(rndWord);
            int tmIters = tm.iterations;
            tmTotalIters += tmIters;
            hmsc.get(rndWord);
            int hmscIters = hmsc.iterations;
            hmscTotalIters += hmscIters;
            hmlp.get(rndWord);
            int hmlpIters = hmlp.iterations;
            hmlpTotalIters += hmlpIters;
            if (i % 4 == 0) {
                System.out.printf("\n%-20s\t%-15d\t%-15d\t%-15d", rndWord, tmIters, hmscIters, hmlpIters);
            }
        }
        System.out.printf("\n%-20s\t%-15d\t%-15d\t%-15d\n", "Average:", tmTotalIters / NUM_SEARCHES,
                hmscTotalIters / NUM_SEARCHES, hmlpTotalIters / NUM_SEARCHES);

    }

} // end of class