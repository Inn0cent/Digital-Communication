import Huffman.AdaptiveHuffmanEncoder;
import LZ.Encoder;

public class Deflate_Encoder {
    private AdaptiveHuffmanEncoder huffman_encoder = new AdaptiveHuffmanEncoder();
    private Encoder lz_encoder = new Encoder();

//    Full Name of Inputfile
    public void compress(String inputFile, int windowSize, int lookAheadBits) {
        huffman_encoder.compressForDeflate(inputFile);
        lz_encoder.encodeDeflate(inputFile, windowSize, lookAheadBits);
    }
}
