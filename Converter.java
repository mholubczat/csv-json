import java.io.*;
import java.util.ArrayList;


public class Converter {
    public static void main(String[] args) throws IOException {
        String inputCsv = "inputCsv.txt";
        String inputJson = "inputJson.txt";
        String outputCsv = "outputCsv.txt";
        String outputJson = "outputJson.txt";

        csvToJson(inputCsv,outputJson);
        jsonToCsv(inputJson,outputCsv);

    }

    private static void csvToJson(String inputFileName, String outputFileName) throws IOException {
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        ArrayList<String> data = new ArrayList<>();

        try {
            inputStream = new BufferedReader(new FileReader(inputFileName));
            outputStream = new PrintWriter(new FileWriter(outputFileName));
            getData(inputStream,data);
                outputStream.write("[");
            String[] keys = extractKeys(data);
            for (String record : data) {
                printRecord(outputStream, data, keys, record);
            }
                outputStream.write(" " + "\n]");
        } finally {
            closeFiles(inputStream, outputStream);
        }
    }

    private static void jsonToCsv(String inputFileName, String outputFileName) throws IOException {
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        ArrayList<String> data = new ArrayList<>();

        try {
            inputStream = new BufferedReader(new FileReader(inputFileName));
            outputStream = new PrintWriter(new FileWriter(outputFileName));
            getData(inputStream, data);
            printKeys(outputStream, data);
            outputStream.write("\n");
            printData(outputStream, data);
        } finally {
            closeFiles(inputStream, outputStream);
        }
    }

    private static String[] extractKeys(ArrayList<String> data) {
        String[] keys = data.get(0).split(";");
        data.remove(0);
        return keys;
    }

    private static void printRecord(PrintWriter outputStream, ArrayList<String> data, String[] keys, String record) {
        outputStream.write("\n" + " {");
        for (int i = 0; i < keys.length; i++) {
            outputStream.write("\n   " + "\"" + keys[i] + "\":");
            if (record.split(";")[i].matches("\\d+")) {
                outputStream.write(record.split(";")[i]);
            } else {
                outputStream.write("\"" + record.split(";")[i] + "\"");
            }
            if (i < keys.length - 1) {
                outputStream.write(",");
            }
        }

        outputStream.write("\n }");
        if (record.equals(data.get(data.size() - 1))) {
        } else {
            outputStream.write(",");
        }
    }


    private static void closeFiles(BufferedReader inputStream, PrintWriter outputStream) throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    private static void getData(BufferedReader inputStream, ArrayList<String> data) throws IOException {
        String line;
        while ((line = inputStream.readLine()) != null) {
            if (line.length() != 0) {
                data.add(line);
            }
        }
    }

    private static void printData(PrintWriter outputStream, ArrayList<String> data) {
        for (String l : data) {
            if (l.contains(":")) {
                outputStream.write(l.substring(l.indexOf(":")).replaceAll("[:\"]", "").replaceAll(",", ";"));
            }
            if (l.contains("}")) {
                outputStream.write("\n");
            }
        }
    }

    private static void printKeys(PrintWriter outputStream, ArrayList<String> data) {
        for (String l : data) {
            if (l.contains(":")) {
                outputStream.write(l.substring(0, l.indexOf(":")).replaceAll("[\":]", "").trim());
                if (l.contains(",")) {
                    outputStream.write(";");
                }
            }
            if (l.contains("}")) {
                break;
            }
        }
    }



}


