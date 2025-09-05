

// This class contains methods to build Huffman tree and to encode and decode the input text.

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Huffman {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner (System.in);

        System.out.print("Huffman Coding\n" +
                "Enter the name of the file with letters and probability: ");

        // Reading from the input file
        String filename = in.next();
        File file = new File(filename);
        Scanner inputFile = new Scanner(file);
        in.nextLine();

        ArrayList<BinaryTree<Pair>> list_S = new ArrayList<>();

        //Storing the data in an ArrayList<BinaryTree<Pair>> list_S
        while (inputFile.hasNext()) {
            String line =  inputFile.nextLine();
            String[] arr = (line.split("\\s+"));

            // Extracting the value and probability, to create a Pair object and store it in list_S
            char value = arr[0].charAt(0);
            double prob = Double.parseDouble(arr[1]);

            Pair pair = new Pair(value, prob);

            BinaryTree<Pair> newNode = new BinaryTree<>();

            newNode.makeRoot(pair);
            list_S.add(newNode);
        }

        System.out.println();

        // Calling methods to build Huffman tree
        BinaryTree<Pair> huffmanTree = buildHuffmanTree(list_S);
        String[] encodings = findEncoding(huffmanTree);

        // Calling the methods to encode and decode the input text
        System.out.print("Enter a line of text (uppercase letters only): ");
        String input = in.nextLine();

        String encodedMessage = encode(input, encodings);
        System.out.println("Here’s the encoded line: " + encodedMessage);

        System.out.println("The decoded line is: " + decode(encodedMessage, huffmanTree));
    }

    /*
     * buildHuffmanTree method -  creates and returns a BinaryTree<Pair> based on the probability of each character
     * @param - ArrayList<BinaryTree<Pair>> list_S
     * @return  - BinaryTree<Pair>
     */

    public static BinaryTree<Pair> buildHuffmanTree(ArrayList<BinaryTree<Pair>> list_S) {
        System.out.println("Building the Huffman tree ….");

        ArrayList<BinaryTree<Pair>> list_T = new ArrayList<>();

        while (!list_S.isEmpty() || list_T.size() > 1 ) {
            BinaryTree<Pair> A;
            BinaryTree<Pair> B;

            // if list_T is empty or the smallest item is in list_S, assign the front entry of list_S to A
            if (!list_S.isEmpty() && (list_T.isEmpty() ||list_S.get(0).getData().compareTo(list_T.get(0).getData()) < 0)) {
                A = list_S.get(0);
                list_S.remove(0);
            } else { // if list_T is not empty and the smallest item is in list_T, assign the front entry of list_T to A
                A = list_T.get(0);
                list_T.remove(0);
            }

            // Repeat the same process to assign the value for B
            if (!list_S.isEmpty() && (list_T.isEmpty() || list_S.get(0).getData().compareTo(list_T.get(0).getData()) < 0)) {
                B = list_S.get(0);
                list_S.remove(0);
            } else if (!list_T.isEmpty()) {
                B = list_T.get(0);
                list_T.remove(0);
            } else {
                list_T.add(A); // if no value is assigned to B, the add A to list_T
                break;
            }

            // if Both A and B has values assigned, merge them into a tree and add it to list_T
            Pair mergedPair = new Pair('0', A.getData().getProb() + B.getData().getProb());

            BinaryTree<Pair> newTree = new BinaryTree<>();

            newTree.makeRoot(mergedPair);
            newTree.attachLeft(A);
            newTree.attachRight(B);

            list_T.add(newTree);
        }

        System.out.println("Huffman coding completed.");
        System.out.println();

        return  list_T.get(0);
    }

    /*
     * findEncoding method -  helper method
     * @param - BinaryTree<Pair> bt
     * @return  - String[]
     */

    private static String[] findEncoding(BinaryTree<Pair> bt) {
        String[] result = new String[26];

        // populating result with huffman codes for each character.
        findEncoding(bt, result,
                "");

        return result;
    }

    /*
     * findEncoding method - deriving the Huffman Codes for each character based on the Huffman tree.
     * @param - BinaryTree<Pair> bt, String[] a, String prefix
     * @return  - None
     */

    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix) {

        if (bt.getLeft() == null && bt.getRight() == null){
            a[bt.getData().getValue() - 'A'] = prefix;
        } else { // recursive calling of left and right child and appending "0" and "1" respectively to the prefix
            findEncoding(bt.getLeft(), a, prefix+ "0");
            findEncoding(bt.getRight(), a, prefix+ "1");
        }

    }

    /*
     * encode method - encoding the input text into an encoded line by using the preset huffman codes
     * @param - String text, String[] encodings
     * @return  - String
     */

    public static String encode(String text, String[] encodings) {
        StringBuilder str = new StringBuilder();

        // loop thorough the input text
        for (char ch : text.toCharArray()) {

            if (ch == ' ') {
                str.append(' ');
            } else if (ch >= 'A' && ch <= 'Z') {
                int index = ch - 'A'; // calculating index by subtracting 'A' from ch's ASCII value
                str.append(encodings[index]); // append the corresponding huffman code based on the index
            }

        }

     return str.toString();
    }

    /*
     * decode method - decoding the encoded line back to original form by using the huffman tree
     * @param - String encodedText, BinaryTree<Pair> root
     * @return  - String
     */

    public static String decode(String encodedText, BinaryTree<Pair> root) {
       StringBuilder decode = new StringBuilder();
       BinaryTree<Pair> curr = root;

        if (encodedText == null) {
            return "";
        }

        // looping through the encoded line
        for (char ch : encodedText.toCharArray()) {

            if (ch == ' ') {
                decode.append(' ');
            }

            // moving to left or right of the tree based on the ch
            if (ch == '0') {
                curr = curr.getLeft();
            } else  if (ch == '1'){
                curr = curr.getRight();
            }

            // if the end of the node is reached append the value of the current node and reset the curr back to root.
            if (curr.getLeft() == null && curr.getRight() == null) {
                decode.append(curr.getData().getValue());
                curr = root;
            }

        }

        return decode.toString();
    }

}
