package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

public final class RandomNameGenerator {

  private static final String BASE_PATH = "/randomNames/";
  private static final String ANIMALS_FILE_NAME = "animais.json";
  private static final String ADJECTIVES_FILE_NAME = "adjetivos.json";
  private static List<String> animals;
  private static List<String> adjectives;
  private static SecureRandom rand;

  private RandomNameGenerator() {
  }

  static {
    String completeAnimalsFileName = BASE_PATH + ANIMALS_FILE_NAME;
    URL completeAnimalsURL = RandomNameGenerator.class.getResource(completeAnimalsFileName);
    String completeAdjectivesFileName = BASE_PATH + ADJECTIVES_FILE_NAME;
    URL completeAdjectivesURL = RandomNameGenerator.class.getResource(completeAdjectivesFileName);
    Gson gson = new Gson();
    Reader reader;
    rand = new SecureRandom();

    try {
      reader = new BufferedReader(new InputStreamReader(completeAnimalsURL.openStream()));
      animals = Stream.of(gson.fromJson(reader, String[].class)).filter(RandomNameGenerator::smallEnough)
          .collect(Collectors.toList());
      reader.close();

      reader = new BufferedReader(new InputStreamReader(completeAdjectivesURL.openStream()));
      adjectives = Stream.of(gson.fromJson(reader, String[].class)).map(StringUtils::capitalize)
          .collect(Collectors.toList());
      reader.close();

    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  public static String getRandomName() {
    return animals.get(rand.nextInt(animals.size())) + " " + adjectives.get(rand.nextInt(adjectives.size()));
  }

  private static boolean smallEnough(String name) {
    return name != null && name.length() < 10;
  }

}
