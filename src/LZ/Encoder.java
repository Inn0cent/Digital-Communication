package LZ;

import java.io.*;

public class Encoder {
    public void encode(String outputFileName, String fileToRead, int windowSize, int lookBehindBits) {
        File file = new File(System.getProperty("user.dir") + "/Test Data/" + fileToRead);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = inputStream.readAllBytes();
            encode(outputFileName, data, windowSize, lookBehindBits);
        } catch (IOException e) {
            System.out.print("Incorrect File Name!\n\n");
            e.printStackTrace();
        }
    }


    private void encode(String outputFileName, byte[] data,  int windowSize, int lookBehindBits){
        int lookAheadBits = windowSize * 8 - lookBehindBits;
        int lookBehind = (1 << lookBehindBits) - 1, lookAhead = (1 << lookAheadBits) - 1;
        File file = new File(System.getProperty("user.dir") + "/Results/" + outputFileName + ".lz");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            int currentPosition = 0;
            while (currentPosition < data.length) {
                Tuple tuple = getTuple(data, currentPosition, lookBehind, lookAhead);
                currentPosition += tuple.length + 1;
                fileOutputStream.write(toByteArray(tuple, windowSize, lookBehindBits));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Tuple getTuple(byte[] bits, int currentPosition, int lookBehind, int lookAhead){
        Tuple tuple = new Tuple(0,  0, bits[currentPosition]);
        for (int i = currentPosition > lookBehind ? currentPosition - lookBehind : 0; i < currentPosition; i++) {
            if (bits[i] == bits[currentPosition]) {
                int length = lookBehind(bits, lookAhead, currentPosition, i);
                if (length > tuple.length) {
                    tuple = new Tuple(currentPosition - i, length, bits[currentPosition + length]);
                }
                i += length;
            }
        }
//        System.out.print(tuple + "\n");
        return tuple;
    }


    /*
    Behaviour: returns the length at which there is no match.
     */
    private int lookBehind(byte[] bits, int lookAhead, int currentPosition, int pastPosition){
        int length = 0;
        for (int i = 1; i <= Math.min(bits.length - currentPosition - 1, lookAhead); i++) {
            length++;
            if (bits[pastPosition + i] != bits[currentPosition + i]) return length;
        }
        if (currentPosition + length == bits.length) return length - 1;
        return length;
    }


    private byte[] toByteArray(Tuple tuple, int windowSize, int lookBehindBits) {
        byte[] encoded = new byte[windowSize + 1];
        int currentPosition = 0, lookAheadBits = windowSize * 8 - lookBehindBits;
        currentPosition = addToByteArray(encoded, tuple, currentPosition, lookBehindBits, lookAheadBits);
        encoded[currentPosition] = tuple.nextByte;
        return encoded;
    }


    private static int addToByteArray(byte[] bits, Tuple tuple, int currentPosition, int lookBehindBits, int lookAheadBits) {
        while (lookBehindBits >= 8) {
            bits[currentPosition] = (byte) (tuple.jump >> (lookBehindBits - 8));
            currentPosition++;
            lookBehindBits -= 8;
        }
        if (lookBehindBits > 0) bits[currentPosition] = (byte) (tuple.jump << (8 - lookBehindBits));
        if (lookAheadBits > 8) {
            bits[currentPosition] |= tuple.length >> (lookAheadBits - (8 - lookBehindBits));
            currentPosition++;
            lookAheadBits -= (8 - lookBehindBits);
            while (lookAheadBits > 0) {
                bits[currentPosition] = (byte) (tuple.length >> (lookAheadBits - 8));
                lookAheadBits -= 8;
                currentPosition++;
            }
        } else {
            bits[currentPosition] |= tuple.length;
            currentPosition++;
        }
        return currentPosition;
    }


    public static void main(String[] args) {
//        LZ.Tuple tuple = new LZ.Tuple(124, 203, (byte) 0);
//        byte[] bits = new byte[3];
//        addToByteArray(bits, tuple, 0, 16, 8);
//        System.out.print(Arrays.toString(bits));

        Encoder encoder = new Encoder();
        encoder.encode("Test", "BW.tif", 3, 12);
//        encoder.encode("Test5", "BW.tif", 1, 2);
    }
}
