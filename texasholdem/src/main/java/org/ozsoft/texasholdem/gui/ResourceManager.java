package org.ozsoft.texasholdem.gui;

import java.net.URL;

import javax.swing.ImageIcon;

import org.ozsoft.texasholdem.Card;

/**
 * Utility class responsible for retrieving resource files.
 * 
 * @author Oscar Stigter
 */
public abstract class ResourceManager {
    
    private static final String IMAGE_PATH_FORMAT = "/images/card_%s.png"; 
    
    /**
     * Returns the image of a specific card.
     * 
     * @param card
     *            The card.
     * 
     * @return The image.
     */
    public static ImageIcon getCardImage(Card card) {
        // Use image order, which is different from value order.
        int sequenceNr = card.getSuit() * Card.NO_OF_RANKS + card.getRank();
        String sequenceNrString = String.valueOf(sequenceNr);
        if (sequenceNrString.length() == 1) {
            sequenceNrString = "0" + sequenceNrString;
        }
        String path = String.format(IMAGE_PATH_FORMAT, sequenceNrString);
        return getIcon(path);
    }
    
    /**
     * Returns an image resource.
     * 
     * @param path
     *            The path on the classpath.
     * 
     * @return The image resource.
     * 
     * @throws RuntimeException
     *             If the resource could not be found.
     */
    public static ImageIcon getIcon(String path) {
        URL url = ResourceManager.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            throw new RuntimeException("Resource file not found: " + path);
        }
    }
    
}
