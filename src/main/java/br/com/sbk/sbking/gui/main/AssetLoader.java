package br.com.sbk.sbking.gui.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.awt.Image;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.gui.models.DeckCardImageInformation;

public class AssetLoader {

	final static Logger logger = LogManager.getLogger(AssetLoader.class);
	private static Map<Card, Image> ImageByCard = new HashMap<Card, Image>();

	private static Image cachedBack;

	public static void initAssetLoader() {
		logger.info("initAssetLoader");

		DeckCardImageInformation deckCardImageInformation = new DeckCardImageInformation();

		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				
				Image cardImage = deckCardImageInformation.createFrontImage(card);
				ImageByCard.put(card, cardImage);
			}
		}

		cachedBack = deckCardImageInformation.createBackImage();
	}

	public static Image getCachedFrontImage(Card card) {
		return ImageByCard.get(card);
	}

	public static Image getCachedBack() {
		return cachedBack;
	}

}
