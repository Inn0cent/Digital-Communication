import Huffman.AdaptiveHuffmanDecoder;
import LZ.Decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Deflate_Decoder {
    private AdaptiveHuffmanDecoder huffman_decoder = new AdaptiveHuffmanDecoder();
    private Decoder decoder = new Decoder();

    public void decompress(String inputFile, int windowSize, int lookAheadBits, byte[] data){
        decoder.decodeDeflate(inputFile, windowSize, lookAheadBits);
        huffman_decoder.decompressDeflate(inputFile);
        decoder.decodeDeflate(inputFile, windowSize, lookAheadBits);
    }

    public static void main(String[] args) {
        Deflate_Encoder encoder = new Deflate_Encoder();
        Deflate_Decoder decoder = new Deflate_Decoder();
        byte[] data = readFile("Small.txt");
        encoder.compress("Small.txt", 2, 8);
        decoder.decompress("Small.txt", 2, 8, data);

    }

    private static byte[] readFile(String fileName) {
        byte[] data = new byte[1];
        File file = new File(System.getProperty("user.dir") + "/Test Data/" + fileName);
        try {
            InputStream inputStream = new FileInputStream(file);
            data = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
