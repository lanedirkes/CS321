import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/*
 * This is the Cache class. It is responsible for the creation, maintaining,
 * and clearing of either a single or 2 level cache.
 */
public class Cache {
    boolean l1 = false;
    boolean l2 = false;
    int l1Size = 0;
    int l2Size = 0;
    private LinkedList l1Cache;
    private LinkedList l2Cache;
    // Creating cacheStats object
    cacheStats statistics = new cacheStats();
    /*
     * This is the constructor method for a single level cache.
     * @param size: size of the cache
     * @param filename: name of the file
     */
    public BufferedReader constructor(int size, File filename) {
        // Creating linked list for the cache
        l1Cache = new LinkedList();
        // Declaring a buffered file reader for the text file
        BufferedReader input = null;
        // Buffered File Reader
        try {
            input = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException e){
            System.out.println("The file name entered could not be found.");
        }
        // letting other methods know how many caches there are and the size
        l1 = true;
        l1Size = size;
        return input;
    }

    /*
     * This is the constructor method for a 2 level cache.
     * @param l1size: size of the level 1 cache
     * @param l2size: size of the level 2 cache
     * @param filename: name of the file
     */
    public BufferedReader constructor(int firstSize, int secondSize, File filename) {
        // Creating linked lists for both the level 1 and 2 caches
        l1Cache = new LinkedList();
        l2Cache = new LinkedList();
        // Declaring a buffered file reader for the text file
        BufferedReader input = null;
        // Buffered File Reader
        try {
            input = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException e){
            System.out.println("The file name entered could not be found.");
        }
        // letting other methods know how many caches there are and the sizes
        l2 = true;
        l1Size = firstSize;
        l2Size = secondSize;
        return input;
    }

    /*
     * This is the get method for any cache
     */
    public boolean getObject(String aWord) {
        statistics.l1Refs++;
        if(l1Cache.contains(aWord)){
            statistics.l1Hits++;
            l1Cache.remove(aWord);
            l1Cache.addFirst(aWord);
            if(l2) {
                l2Cache.remove(aWord);
                l2Cache.addFirst(aWord);
            }
            return true;
        }
        else if(l2){
            statistics.l2Refs++;
            if(l2Cache.contains(aWord)){
                statistics.l2Hits++;
                l2Cache.remove(aWord);
                l2Cache.addFirst(aWord);
                fancyAdd(l1Cache,l1Size,aWord);
                return true;
            }
        }
        return false;
    }

    /*
     * This is the add method for any cache
     */
    public void addObject(String aWord) {
        // if the level 1 cache contains the word
        fancyAdd(l1Cache,l1Size,aWord);
        // if there is a second level cache
        if(l2){
            fancyAdd(l2Cache,l2Size,aWord);
        }
    }

    /*
     * Handles adding items to a cache that already exist within the cache
     */
    private void fancyAdd(LinkedList cache, int cacheSize, String aWord) {
        // if the cache is full, execute
        if (cache.size() == cacheSize) {
            // remove the oldest object from the list
            cache.removeLast();
            // add object to the top of the cache
            cache.addFirst(aWord);
        }
        else {
            // add object to the top of the cache
            cache.addFirst(aWord);
        }
    }

    /*
     * This is the remove method for any cache
     */
    public void removeObject(String aWord) {
        l1Cache.remove(aWord);
        if(l2){
            l2Cache.remove(aWord);
        }
    }

    /*
     * This is the clear method that will empty a cache
     */
    public void clearCache() {
        l1Cache.clear();
        if(l2){
            l2Cache.clear();
        }
    }

    public class cacheStats {
        // variables to store number of cache hits
        int l1Hits = 0;
        int l2Hits = 0;
        // variables to store number of cache references
        int l1Refs = 0;
        int l2Refs = 0;

        public double getL1HR() {
            return (double)l1Hits/l1Refs;
        }

        public double getL2HR() {
            return (double)l2Hits/l2Refs;
        }

        public int getTotalHits() {
            return l1Hits + l2Hits;
        }

        public int getTotalRefs() {
            return l1Refs;
        }

        public double getTotalHR() {
            return (double)(statistics.l1Hits + statistics.l2Hits)/statistics.l1Refs;
        }
    }
}