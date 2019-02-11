package Huffman;

import java.util.HashMap;
import java.util.LinkedList;

public class Huffman_Encoder {
    HashMap<Character, Integer> occurrences = new HashMap<>();
    public void encode(String characters){
        getOccurrences(characters.toCharArray());
    }

    private void getOccurrences(char[] characters) {
        for (char c : characters) occurrences.put(c, occurrences.getOrDefault(c, 0) + 1);
    }

    private void getLinkedList() {
        LinkedList<Huffman_Node> nodes = new LinkedList<>();
    }

    private void addToLinkedList() {

    }

    private void constructTree() {

    }

    private void getDictionary() {

    }
}
