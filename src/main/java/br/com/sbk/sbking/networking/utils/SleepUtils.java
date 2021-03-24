package br.com.sbk.sbking.networking.utils;

import org.apache.logging.log4j.Logger;

public class SleepUtils {

  public static void sleepFor(int miliseconds, Logger logger) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      logger.debug(e);
    }
  }

  public static void sleepForWithInfo(int miliseconds, Logger logger, String info) {
    try {
      logger.info("Sleeping for " + miliseconds + "ms. " + info);
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      logger.debug(e);
    }
  }

}
