import Huffman.AdaptiveHuffmanEncoder;
import Huffman.AdaptiveHuffmanDecoder;
import LZ.Decoder;
import LZ.Encoder;

import java.io.*;
import java.util.Arrays;

public class Testing {
    private static int[][] parameters = {{1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}, {2, 2}, {2, 3}, {2, 4}, {2, 5}
            , {2, 6}, {2, 7}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {2, 12}, {2, 13}, {2, 14}, {2, 15},
            {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 11}, {3, 12},
            {3, 13}, {3, 14}, {3, 15},  {3, 16},  {3, 17},  {3, 18}, {3, 19},  {3, 20}};
//    private static int[][] parameters = {{1, 2}};
    private static Decoder decoder = new Decoder();
    private static Encoder encoder = new Encoder();
    private static AdaptiveHuffmanEncoder huffman_compress = new AdaptiveHuffmanEncoder();
    private static AdaptiveHuffmanDecoder huffmanDecompress = new AdaptiveHuffmanDecoder();

//    Add Logging Files To Process
    public static void main(String[] args) {
        String[] files = {"LargePicture.RAF"};
//        String[] files = {"coursework.pdf"};
        int fileNum = 0;
        try {
            BufferedWriter parameterWriter = getWriter("Parameter Testing/PA Test.txt");
            parameterWriter.write("Purpose: Windows test\n********************************\n\n");
            for (String fileName : files) {
                System.out.print("File: " + fileName + "\n");
                BufferedWriter logWriter = getWriter("Log Files/Windows" + fileName.split("\\.")[0] + ".log");
                parameterWriter.write("File Name: " + fileName +"\n.........................\n");
                File file = new File(System.getProperty("user.dir") + "/Test Data/" + fileName);
                InputStream inputStream = new FileInputStream(file);
                byte[] data = inputStream.readAllBytes();
                for (int j = 0; j < parameters.length; j++) {
                    System.out.print("Parameters: " + Arrays.toString(parameters[j]) +"\n");
                    writeToFile(parameterWriter, logWriter, parameters[j], fileName, "Test" + (fileNum > 0 ? fileNum : ""),  data);
                    fileNum++;
                }
                logWriter.close();
            }
            parameterWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        removeFiles();
    }

    private static void writeToFile(BufferedWriter parameterWriter, BufferedWriter logWriter, int[] parameter, String inputFile, String outputFile, byte[] correctData) throws IOException{
        parameterWriter.write("Parameters:\nWindow Size: " + parameter[0] + "\nLook Behind Bits: " + parameter[1] + " (" + ((1 << parameter[1]) - 1) + ")");
        parameterWriter.write("\nLook Ahead Bits: " + (8 * parameter[0] - parameter[1]) + " (" + ((1 << (8 * parameter[0] - parameter[1])) - 1)+ ")\n");
        logWriter.write(parameter[1] + "," + (parameter[0] * 8 - parameter[1]) + ",");
//        Encode
        long startTime = System.currentTimeMillis();
        encoder.encode(outputFile, inputFile, parameter[0], parameter[1]);
        parameterWriter.write("Time Taken To Encode (LZ): " + (System.currentTimeMillis() - startTime)  + " milliseconds\n");
        logWriter.write((System.currentTimeMillis() - startTime) + ",");

//        Decode
        startTime = System.currentTimeMillis();
        byte[] data = decoder.decode(outputFile, parameter[0], parameter[1]);
        parameterWriter.write("Time Taken To Decode (LZ): " + (System.currentTimeMillis() - startTime) + " milliseconds\n");
        logWriter.write((System.currentTimeMillis() - startTime) + ",");

//        Get Compression Rate
        parameterWriter.write("Correctly Decoded: " + decoder.checkSame(data, correctData) + "\n");
        parameterWriter.write("Compression Rate (LZ): "  + correctData.length / getFileLength(outputFile + ".lz") + "\n");

        logWriter.write(getFileLength(outputFile + ".lz") / correctData.length + "\n");
        parameterWriter.write("-----------------------------\n\n");
    }


    private static float getFileLength(String fileName) {
        try {
            File encoded = new File(System.getProperty("user.dir") + "/Results/" + fileName);
            InputStream encodedStream = new FileInputStream(encoded);
            return encodedStream.readAllBytes().length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void removeFiles() {
        File[] files =  new File(System.getProperty("user.dir") + "/Results").listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    public static BufferedWriter getWriter(String fileName) throws IOException{
        String startingPath = System.getProperty("user.dir") + "/" + fileName;
        File file = new File(startingPath);
        String[] names = startingPath.split("\\.");
        for (int i = 1; file.exists(); i++) {
            file = new File(names[0] + i + "." + names[1]);
        }
        return new BufferedWriter(new FileWriter(file));

    }



}
