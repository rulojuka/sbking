package br.com.sbk.sbking.gui.main;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.gui.models.DeckCardImageInformation;

public class AssetLoader {
    private static Map<Card, ImageIcon> imageByCard = new HashMap<Card, ImageIcon>();
    private static ImageIcon cachedBack;

    public static void initAssetLoader() {
        loadCache();
    }

    public static void invalidateAssetLoaderCache() {
        imageByCard.clear();
        loadCache();
    }

    public static ImageIcon getCachedFrontImage(Card card) {
        return imageByCard.get(card);
    }

    public static ImageIcon getCachedBack() {
        return cachedBack;
    }

    private static void loadCache() {
        DeckCardImageInformation deckCardImageInformation = new DeckCardImageInformation();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                
                ImageIcon cardImage = deckCardImageInformation.createFrontImage(card);
                imageByCard.put(card, cardImage);
            }
        }

        cachedBack = deckCardImageInformation.createBackImage();
    }

}
