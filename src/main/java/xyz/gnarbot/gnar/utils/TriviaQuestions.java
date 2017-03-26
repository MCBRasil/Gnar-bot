package xyz.gnarbot.gnar.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class TriviaQuestions {
    private static final String[] categories = {"2015", "Anime", "Cars", "Clash Royale", "Disney", "Dota 2 " +
            "Abilities", "Dota 2 Items", "Games", "General", "General Extended", "Greek Mythology", "Harry Potter",
            "League of Legends", "League of Legends Ults", "Pokemon", "Pokémon", "Slogans", "Star Wars", "U.S. " +
            "Capitals", "World Capitals", "World Flags", "World of Warcraft"};

    private static final ArrayList<String> questions = new ArrayList<>();

    private static final ArrayList<String> answers = new ArrayList<>();

    public static void init() {
        File f = new File("_DATA/trivia");
        if (!f.exists()) {
            System.out.println("No trivia folder detected.");
        }

        File[] files = f.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line;
                while ((line = br.readLine()) != null) {
                    String[] split = line.split("`");
                    if (split.length > 1) {
                        questions.add(split[0] + "\n\n**Category: " + file.getName() + "**");
                        answers.add(split[1]);
                    } else {
                        System.out.println(line);
                    }
                }
            } catch (Exception e) {e.printStackTrace();}
        }
    }

    public static boolean isSetup() {
        return questions.size() > 0;
    }

    public static String getRandomQuestion() {
        Random rand = new Random();
        int randNum = rand.nextInt(questions.size());
        return questions.get(randNum) + "\n\n**For the answer, type _answer " + randNum + "**";
    }

    public static String getAnswer(int key) {
        if (key <= questions.size() && key > 0) {
            return answers.get(key);
        } else {
            return "Invalid Question Key";
        }
    }

}
