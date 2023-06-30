package br.com.sbk.sbking.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SBKingLogger {

  private SBKingLogger() {
    throw new IllegalStateException("Utility class");
  }

  public static final Logger LOGGER = LogManager.getLogger(SBKingLogger.class);

}
