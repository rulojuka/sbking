package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class RandomNameGenerator {

  private static final String BASE_PATH = "/randomNames/";
  private static final String ANIMALS_FILE_NAME = "animais.json";
  private static final String ADJECTIVES_FILE_NAME = "adjetivos.json";
  private static List<String> animals;
  private static List<String> adjectives;
  private static RandomUtils randomUtils;

  private RandomNameGenerator() {
  }

  static {
    String completeAnimalsFileName = BASE_PATH + ANIMALS_FILE_NAME;
    URL completeAnimalsURL = RandomNameGenerator.class.getResource(completeAnimalsFileName);
    String completeAdjectivesFileName = BASE_PATH + ADJECTIVES_FILE_NAME;
    URL completeAdjectivesURL = RandomNameGenerator.class.getResource(completeAdjectivesFileName);
    randomUtils = new RandomUtils();

    ObjectMapper mapper = new ObjectMapper();
    try (Reader animalReader = new BufferedReader(new InputStreamReader(completeAnimalsURL.openStream()));
        Reader adjectiveReader = new BufferedReader(new InputStreamReader(completeAdjectivesURL.openStream()))) {
      animals = mapper.readValue(animalReader, new TypeReference<ArrayList<String>>() {
      }).stream().filter(RandomNameGenerator::smallEnough)
          .collect(Collectors.toList());
      adjectives = mapper.readValue(adjectiveReader, new TypeReference<ArrayList<String>>() {
      }).stream().map(StringUtils::capitalize)
          .collect(Collectors.toList());
    } catch (IOException e) {
      LOGGER.error(e);
    }
  }

  public static String getRandomName() {
    return animals.get(randomUtils.nextInt(animals.size())) + " "
        + adjectives.get(randomUtils.nextInt(adjectives.size()));
  }

  private static boolean smallEnough(String name) {
    return name != null && name.length() < 10;
  }

}
