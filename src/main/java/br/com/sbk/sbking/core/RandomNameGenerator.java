package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

public final class RandomNameGenerator {

  private static final String BASE_PATH = "src/main/resources/";
  private static final String ANIMALS_FILE_NAME = "animals.json";
  private static final String ADJECTIVES_FILE_NAME = "adjectives.json";
  private static List<String> animals;
  private static List<String> adjectives;
  private static SecureRandom rand;

  private RandomNameGenerator() {
  }

  static {
    String completeAnimalsFileName = BASE_PATH + ANIMALS_FILE_NAME;
    String completeAdjectivesFileName = BASE_PATH + ADJECTIVES_FILE_NAME;
    Gson gson = new Gson();
    Reader reader;
    rand = new SecureRandom();

    try {
      reader = Files.newBufferedReader(Paths.get(completeAnimalsFileName));
      animals = Stream.of(gson.fromJson(reader, String[].class)).filter(RandomNameGenerator::smallEnough)
          .collect(Collectors.toList());
      reader.close();

      reader = Files.newBufferedReader(Paths.get(completeAdjectivesFileName));
      adjectives = Stream.of(gson.fromJson(reader, String[].class)).map(StringUtils::capitalize)
          .collect(Collectors.toList());
      reader.close();
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  public static String getRandomName() {
    return adjectives.get(rand.nextInt(adjectives.size())) + " " + animals.get(rand.nextInt(animals.size()));
  }

  private static boolean smallEnough(String name) {
    return name != null && name.length() < 10;
  }

}
