package Huffman;

public class Huffman_Node {
    public Huffman_Node left, right;
    public char val;

    public Huffman_Node(Huffman_Node left, Huffman_Node right, char val) {
        this.left = left;
        this.right = right;
        this.val = val;
    }
}
