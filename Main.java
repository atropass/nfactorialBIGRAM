import java.io.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.IOException;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        NameGenerator ng = new NameGenerator("src\\names.txt");
        String newName = ng.generateName();
//      System.out.println(ng.bigramFreq);
        System.out.println(newName);

        //создаю объект класса в котором вызываю метод для генерации
        //имени на рандом на основе биграммов
        //момент с выводом я захардкодил и просто вы



        String fileName = "tableofbigrams.csv";
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("Биграммы", "Шанс появления");
        FileWriter fileWriter = new FileWriter(fileName);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat);
        for (Map.Entry<String, Double> entry : ng.bigramMap.entrySet()) {
            csvPrinter.printRecord(entry.getKey(), entry.getValue());
        }
        csvPrinter.close();
        fileWriter.close();
        //подключил библетеку CSV для создания таблицы и отдельного файла для удобстава
    }

}