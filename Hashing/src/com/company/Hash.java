package com.company;

import java.math.BigInteger;
import java.util.Arrays;

import static com.company.Constants.*;

public class Hash {

    //fields
    private int[] collisionCount = new int[MAX_INDEX];
    private int totalCollision = 0; // might not need these??
    private boolean probingL = false;
    private boolean probingQ = false;
    private int rehashCount = 0;
    private int[] hashCount = new int[MAX_INDEX];
    private int hashTotal = 0;
    private int hashValue = 0;
    private int x = 0;
    private int currentArraySize = ARRAY_SIZE[x];
    private String[] hashMap = new String[ARRAY_SIZE[MAX_INDEX]];
    private String[] binaries = new String[ARRAY_SIZE[MAX_INDEX]];
    private String[] word = new String[MAX_STRING_AMOUNT];
    private String currentWord;
    private int linearCollisions = 0;

    //methods
    public void setHashMap() { // fills the array with "---"
        Arrays.fill(hashMap, "---");
    }

    public void analysis() { // prints out the analysis options, gives the user a recommendation
        if (linearCollisions < totalCollision){
            System.out.println("""

                    \tAs there are less collisions in Linear Probing compared to Quadratic Probing,\s
                    \tit is recommended to continue with Linear Probing, as the data will be more accurately organised.""");
        } else if (totalCollision < linearCollisions){
            System.out.println("""

                    \tAs there are less collisions in Quadratic Probing compared to Linear Probing,\s
                    \tit is recommended to continue with Quadratic Probing, as the data will be more accurately organised.""");
        } else {
            System.out.println("""

                    \tAs there are are an equal amount of collisions with both methods of probing,\s
                    \tthere is no recommendation to which method should be used. The user can use either.""");
        }
    }

    public void setProbingL(boolean probing) { // runs the linear version of hashing
        probingL = probing;
        if (probingL){
            hash();
            printArray();
        }
        probingL = false;
    }

    public void setProbingQ(boolean probing) { // runs the quadratic version of hashing
        probingQ = probing;
        if (probingQ) {
            hash();
            printArray();
        }
        probingQ = false;
    }

    public void setCurrentArraySize() { // sets the current array size
        x++;
        if (x < MAX_INDEX) {
            currentArraySize = ARRAY_SIZE[x];
        }
    }

    public void splitString(String words) { // Separates each word into a String array,
        words = words.replaceAll("\\s+", " ");
        word = words.split(" ");
    }

    public void hash() { // hash function --> organises the data
        setHashMap();
        for (String s : word) { // loops through each string in the word array
            currentWord = s;
            if (currentWord.equals(" ")) { // in case the word is not there
                continue;
            }
            BigInteger valueHash = binary(currentWord).remainder(BigInteger.valueOf(currentArraySize));
                // brings over the binary value and calculates the remainder
            hashValue = valueHash.intValue();
                // converts the Big Integer value into an integer
            if (hashCount[rehashCount] == (int) (0.7 * currentArraySize)) {
                // checks if the hash count is greater than 70 percent of the array size
                rehash();
                break;
            } else {
                if (hashMap[hashValue].equals("---")) { // if the index of the array is empty,
                    // puts the word and value in the actual index
                    hashMap[hashValue] = ANSI_WHITE + currentWord;
                    binaries[hashValue] = ANSI_WHITE + binary(currentWord);
                } else { // if it collides with some other value, runs the linear or the quadratic probing
                    if (probingL) {
                        linear();
                    }
                    if (probingQ) {
                        quadratic();
                    }
                }
            }
            hashCount[rehashCount]++;
        }
    }

    public void printArray() { // prints out the arrays
        if (probingL){
            System.out.println("\nLinear Probing Array:\n");
        }
        if (probingQ){
            System.out.println("\nQuadratic Probing Array:\n");
        }
        for (int i = 0; i < currentArraySize; i++) { // word array
            System.out.print(hashMap[i]);
            if (i != currentArraySize - 1) {
                System.out.print(", ");
            }
            if ((i + 1) % 20 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        for (int i = 0; i < currentArraySize; i++) { // binary array
            if (binaries[i] == null) {
                System.out.print(ANSI_WHITE + "---");
            } else {
                System.out.print(binaries[i]);
            }
            if (i != currentArraySize - 1) {
                System.out.print(", ");
            }
            if ((i + 1) % 20 == 0) {
                System.out.println();
            }
        }
        data();
    }

    public void linear() { // linear collision method
        collisionCount[rehashCount]++;
        for (int i = 0; i <= ARRAY_SIZE[8]; i++) { // increases i by 1 each time
            int placeholder = (hashValue + i) % currentArraySize;
            if (hashMap[placeholder].equals("---")) {
                hashMap[placeholder] = ANSI_WHITE + currentWord;
                binaries[placeholder] = ANSI_BLUE + binary(currentWord); // blue if it collides
                collisionCount[rehashCount]++;
                break; // breaks if it finds a place in the array
            }
        }
    }

    public void quadratic() { // quadratic collision method
        collisionCount[rehashCount]++;
        for (int i = 0; (i * i) <= ARRAY_SIZE[8]; i++) { // increases hash value by i^2
            int placeholder = (hashValue + (i * i)) % currentArraySize;
            if (hashMap[placeholder].equals("---")) {
                hashMap[placeholder] = ANSI_WHITE + currentWord;
                binaries[placeholder] = ANSI_BLUE + binary(currentWord); // blue if it collides
                collisionCount[rehashCount]++;
                break; // breaks if it finds a place in the array
            }
        }
    }

    public BigInteger binary(String word) { // binary method, converts word to binary then to decimal value
        int length = word.length();
        BigInteger decimal = BigInteger.valueOf(0); // big integer values as they exceed java's max integer size limits
        for (int i = 0; i < length; i++) {
            if (Character.getNumericValue(word.charAt(i)) >= 0) {
                BigInteger binary = BigInteger.valueOf(Character.getNumericValue(word.charAt(i)));
                decimal = decimal.add(binary);
                if ((i != length - 1) && (Character.getNumericValue(word.charAt(i + 1)) != -1)) {
                    decimal = decimal.multiply(BigInteger.valueOf(32));
                }
                // formula for converting letters to decimal binary values, source: Mr. Hubert
            }
        }
        return decimal; // returns as Big Integer value
    }

    public void rehash() { // rehash function if the number of hashed values exceeds 70 percent of the currentArraySize
        rehashCount++;
        setHashMap(); // resets hashmap
        for(int i = 0; i < currentArraySize; i++){ // resets the binary array
            binaries[i] = null;
        }
        setCurrentArraySize(); // increases current array size
        hash(); // runs the hash function again with new array size
    }

    public void data() { // collects all the collision and hash data
        for (int i = 0; i < MAX_INDEX; i++) {
            totalCollision += collisionCount[i];
            hashTotal += hashCount[i];
        }
        System.out.println(ANSI_WHITE + "\nTotal Collisions: " + totalCollision + "\nTotal Times Hashed: " + hashTotal);
        // prints out the total collisions and hashes
        if (probingL) { // if it is linear, then saves the data in case the user wants analysis
            linearCollisions = totalCollision;
            totalCollision = 0;
            hashTotal = 0;
        }
    }

    public void reset(){ // resets all data before the next hashMap is created
        setHashMap();
        for (int i = 0; i < currentArraySize; i++){
            binaries[i] = null; // resets binary array
            if (i < 8){ // resets collision and hash counts
                collisionCount[i] = 0;
                hashCount[i] = 0;
            }
        }
        x = 0; // resets x
        currentArraySize = ARRAY_SIZE[x]; // resets currentArraySize
    }


}
