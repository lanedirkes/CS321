import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class TestCache extends Cache {
    // variables used to create new caches and reading text files
    static String words;
    static BufferedReader input;
    static File file;
    static int level1Size;
    static int level2Size;
    // initializing both single and double caches for testing
    static Cache testCache;
    public static void main(String[] args){
        level1Size  = Integer.parseInt(args[1]);
        level2Size = 0;
        testCache = new Cache();
        // calling the constructor for both the single and double caches
        if(args[0].equals("1")) {
            file = new File(args[2]);
            input = testCache.constructor(level1Size, file);
            System.out.printf("Single cache with %d entries has been created\n", level1Size);
        }
        else {
            level2Size = Integer.parseInt(args[2]);
            file = new File(args[3]);
            input = testCache.constructor(level1Size, level2Size, file);
            System.out.printf("First level cache with %d entries has been created\n", level1Size);
            System.out.printf("Second level cache with %d entries has been created\n", level2Size);
        }
        System.out.println("..............................\n");
        // testing the cache
        try {
            words = input.readLine();
            StringTokenizer tokenizer;
            while(words != null) {
                tokenizer = new StringTokenizer(words, " \t");
                while(tokenizer.hasMoreTokens()) {
                    String next = (String) tokenizer.nextElement();
                    if (testCache.getObject(next) == false) {
                        testCache.addObject(next);
                    }
                }
                words = input.readLine();
            }
        }
        catch (IOException e){
            System.out.println("There was an IOException");
        }
        // printing the 2 level cache's statistics
        printStats(testCache.statistics);
    }

    private static void printStats(cacheStats statistics){
        System.out.printf("Total number of references: %d\n", statistics.getTotalRefs());
        System.out.printf("Total number of cache hits: %d\n", statistics.getTotalHits());
        System.out.printf("The global hit ratio                  : %f\n", statistics.getTotalHR());
        System.out.printf("Number of 1st-level cache hits: %d\n", statistics.l1Hits);
        System.out.printf("1st-level cache hit ratio             : %f\n", statistics.getL1HR());
        if(level2Size != 0) {
            System.out.printf("Number of 2st-level cache hits: %d\n", statistics.l2Hits);
            System.out.printf("2st-level cache hit ratio             : %f\n", statistics.getL2HR());
        }
    }
}
