import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NameGenerator {
    // мапа где храним наши биграмы и их вероятности
    public Map<String, Double> bigramMap = new HashMap<>();
    // Создаю мапу для хранения биграмов и частот
    public Map<String, Double> bigramFreq = new HashMap<>();
    int aveNameLength = 0;
    
    public NameGenerator(String fileName) throws IOException {
        Scanner scanner = new Scanner(new File(fileName));
        Scanner scanner1 = new Scanner(new File(fileName));
        double totalCount = 0;
        while (scanner1.hasNext()) {
            aveNameLength+= scanner1.nextLine().length();
            scanner1.next();
        }
        while (scanner.hasNextLine()) {
            totalCount+=1.0;
            String name = "^" + scanner.nextLine() + "$"; // Добавляем символы начала и конца слова
            // мапа с частотой
            for (int i = 0; i < name.length() - 1; i++) {
                String bigram = name.substring(i, i + 2);
                bigramMap.put(bigram, bigramMap.getOrDefault(bigram, 0.0) + 1.0);
            }
        }
        aveNameLength /= totalCount;
        bigramFreq = bigramMap; // сохраняю мапу с частотой и создаю мапу с шансами для таблицы
        for (Map.Entry<String, Double> entry : bigramMap.entrySet()) {
            double count = entry.getValue();
            bigramMap.put(entry.getKey(), count / totalCount);
        }
//        System.out.println(bigramMap);
    }

    // сам метод для генерации имени
    public String generateName() throws FileNotFoundException {
        // создаю начальную биграму ^ и стрингбилдер
        List<String> possibleBigrams = bigramFreq.keySet().stream()
                .filter(bigram -> bigram.startsWith("^"))
                .toList();
        String firstBigram = getNextBigram(possibleBigrams, bigramFreq);
        StringBuilder generatedName = new StringBuilder();
        generatedName.append(firstBigram);
//        System.out.println(generatedName);
        List<String> possibleBigrams1 = bigramFreq.keySet().stream().toList();
        String currentBigram = "  ";
        // генерю имя, пока не достигнем конечной биграммы $
        while (currentBigram.charAt(1) != '$'){
            // чекаю все биграммы которые начинаются с этой буквы
            currentBigram = getNextBigram(possibleBigrams1,bigramFreq);
            if(currentBigram.charAt(0) == '^'){
                continue;
            }
//            System.out.println(currentBigram);
            // некст буква на рандоме
            generatedName.append(currentBigram); // Добавляем выбранную букву к имени
        }

        return generatedName.substring(1,generatedName.length()-1);
    }

    private String getNextBigram(List<String> possibleBigrams, Map<String, Double> bigramFreq) {
        // список биграммов и частоты
        List<String> freqList = new ArrayList<>();
        for (String bigram : possibleBigrams) {
            double freq = bigramFreq.getOrDefault(bigram, (double)0);
            for (int i = 0; i < freq; i++) {
                freqList.add(bigram);
            }
        }
        // рандом на основе шансыа
        int randomIndex = (int) (Math.random() * freqList.size());
        return freqList.get(randomIndex);
    }
}