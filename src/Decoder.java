import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Decoder {
    public byte[] decode(String fileName, int windowSize, int lookBehindBits){
        byte[] encodedData = readFile(fileName);
        return toArray(decodeData(encodedData, windowSize, lookBehindBits));
    }


    public String decodeString(byte[] data) {
        return new String(data);
    }


    public void decodeImage(String imageName, byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(bis);
            File file = new File(System.getProperty("user.dir") + "/Results/" + imageName + ".png");
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            System.out.print("Invalid File Name!");
            e.printStackTrace();
        }
    }

    public boolean checkSame(byte[] decoded, byte[] data) {
        if (decoded.length != data.length) {
            System.out.print(data.length + ", " + decoded.length );
            return false;
        }
        boolean noProblem = true;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != decoded[i]) {
                System.out.print(data[i] + ", " + decoded[i] + "\n");
                noProblem = false;
            }
        }
        return noProblem;
    }

    private ArrayList<Byte> decodeData(byte[] data, int windowSize, int lookBehindBits) {
        ArrayList<Byte> bits = new ArrayList<>();
        int lookAheadBits = windowSize * 8 - lookBehindBits;
        int currentPosition = 0;
        while (currentPosition < data.length) {
//            System.out.print("Current Position: " + currentPosition + " (" + data[currentPosition] + ")\n");
            decodeMessage(bits, data, currentPosition, lookBehindBits, lookAheadBits);
            currentPosition += windowSize ;
//            System.out.print("Next Byte Position: " + currentPosition + " (" + data[currentPosition] + ")\n");
            bits.add(data[currentPosition]);
            currentPosition++;
        }
        return bits;
    }


    public void decodeMessage(ArrayList<Byte> bits, byte[] data, int currentPosition, int lookBehindBits, int lookAheadBits) {
        int jump, length;
        jump = getValueFromByteArray(data, currentPosition, lookBehindBits, 7);
        currentPosition += Math.floor(lookBehindBits / 8);
        int startingPosition = 7 - lookBehindBits % 8;
        length = getValueFromByteArray(data, currentPosition, lookAheadBits, startingPosition);
//        System.out.print("Jump: " + jump + "\nLength: " + length + "\n");
        addToBits(bits, jump, length);
    }


    private int getValueFromByteArray(byte[] data, int currentPosition, int bitLength, int startingPosition) {
        int value = 0;
        while (bitLength > 0) {
            value <<= 1;
            value |= (data[currentPosition] & (1 << startingPosition)) > 0 ? 1 : 0;
            startingPosition--;
            if (startingPosition < 0) {
                startingPosition = 7;
                currentPosition++;
            }
            bitLength--;
        }
        return value;
    }


    private void addToBits(ArrayList<Byte> bits, int jump, int length) {
        for (int i = 0; i < length; i++) {
            bits.add(bits.get(bits.size() - jump));
        }
    }


    private byte[] toArray(ArrayList<Byte> bits) {
        byte[] data = new byte[bits.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = bits.get(i);
        }
        return data;
    }


    private byte[] readFile(String fileName) {
        byte[] data = new byte[1];
        File file = new File(System.getProperty("user.dir") + "/Results/" + fileName + ".lz");
        try {
            InputStream inputStream = new FileInputStream(file);
            data = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) throws IOException{
        Decoder decoder = new Decoder();
        byte[] decoded = decoder.decode("Test", 3, 12);
        FileInputStream inputStream = new FileInputStream(new File(System.getProperty("user.dir") + "/Test Data/BW.tif"));
        byte[] data = inputStream.readAllBytes();
        System.out.print(decoder.checkSame(decoded, data));

//        byte[] array = {(byte) 0, (byte) 124, (byte) -53};
//        ArrayList<Byte> bytes = new ArrayList<>();
//        Decoder decoder = new Decoder();
//        decoder.decodeMessage(bytes, array, 0, 16,8);
    }

}
