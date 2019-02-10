import java.io.*;
import java.util.Arrays;

public class Testing {
    private static int[][] parameters = {{1, 2}, {1, 4}, {1, 6}, {2, 4}, {2, 6}, {2, 8}, {2, 10}, {2, 12}, {2, 14}, {3, 4}
                , {3, 6}, {3, 8}, {3, 10}, {3, 12}, {3, 14}, {3, 16}, {3, 18}, {3, 20}};
    private static Decoder decoder = new Decoder();
    private static Encoder encoder = new Encoder();

    public static void main(String[] args) {
//        String[] files = {"BW.tif", "Color.tif", "Chinese.txt", "Large.txt", "Medium.txt", "Small.txt", "video.mp4", "LargeVideo.mp4"};
        String[] files = {"LargeVideo.mp4"};
        int fileNum = 0;
        try {
            BufferedWriter writer = getWriter();
            writer.write("Purpose: Large Video Test\n********************************\n\n");
            for (String fileName : files) {
                System.out.print("File: " + fileName + "\n");
                writer.write("File Name: " + fileName +"\n.........................\n");
                File file = new File(System.getProperty("user.dir") + "/Test Data/" + fileName);
                InputStream inputStream = new FileInputStream(file);
                byte[] data = inputStream.readAllBytes();
                for (int j = 0; j < parameters.length; j++) {
                    System.out.print("Parameters: " + Arrays.toString(parameters[j]) +"\n");
                    writeToFile(writer, parameters[j], fileName, "Test" + (fileNum > 0 ? fileNum : ""),  data);
                    fileNum++;
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        removeFiles();
    }

    private static void writeToFile(BufferedWriter writer, int[] parameter, String inputFile, String outputFile, byte[] correctData) throws IOException{
        writer.write("Parameters:\nWindow Size: " + parameter[0] + "\nLook Behind Bits: " + parameter[1] + " (" + ((1 << parameter[1]) - 1) + ")");
        writer.write("\nLook Ahead Bits: " + (8 * parameter[0] - parameter[1]) + " (" + ((1 << (8 * parameter[0] - parameter[1])) - 1)+ ")\n");

//        Encode
        long startTime = System.currentTimeMillis();
        encoder.encode(outputFile, inputFile, parameter[0], parameter[1]);
        writer.write("Time Taken To Encode: " + (System.currentTimeMillis() - startTime)  + " milliseconds\n");

//        Decode
        startTime = System.currentTimeMillis();
        byte[] data = decoder.decode(outputFile, parameter[0], parameter[1]);
        writer.write("Time Taken To Decode: " + (System.currentTimeMillis() - startTime) + " milliseconds\n");
        writer.write("Correctly Decoded: " + decoder.checkSame(data, correctData) + "\n");
//        Get Compression Rate
        writer.write("Compression Rate: "  + getFileLength(outputFile) / correctData.length + "\n");
        writer.write("-----------------------------\n\n");
    }


    private static float getFileLength(String fileName) {
        try {
            File encoded = new File(System.getProperty("user.dir") + "/Results/" + fileName + ".lz");
            InputStream encodedStream = new FileInputStream(encoded);
            return encodedStream.readAllBytes().length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void removeFiles() {
        File file = new File(System.getProperty("user.dir") + "/Results/Test.lz");
        int i = 1;
        while(file.exists()) {
            file.delete();
            file = new File(System.getProperty("user.dir") + "/Results/Test" + i + ".lz");
            i++;
        }
    }

    public static BufferedWriter getWriter() throws IOException{
        String startingPath = System.getProperty("user.dir") + "/Parameter Testing/PA Test";
        File file = new File(startingPath + ".txt");
        for (int i = 1; file.exists(); i++) {
            file = new File(startingPath + i + ".txt");
        }
        return new BufferedWriter(new FileWriter(file));
    }



}
