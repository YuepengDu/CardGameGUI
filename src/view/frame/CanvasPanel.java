package view.frame;

import model.PlayingCardImpl;
import model.interfaces.Player;
import model.interfaces.PlayingCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CanvasPanel extends Canvas {

    Player currentPlayer;
    Map<Player, ArrayList<PlayingCard>> cache = new HashMap<>();

    /**
     * init.
     */
    public CanvasPanel() {
        this.setBackground(Color.magenta);
    }

    /**
     * update player and card, then repaint.
     * @param player for player, if update HOUSE, then p is null.
     * @param card for card of current player.
     */
    public void updateCards(Player player, PlayingCard card) {
        currentPlayer = player;

        if(!cache.containsKey(currentPlayer)) {
            cache.put(currentPlayer, new ArrayList<>());
        }

        ArrayList<PlayingCard> playingCards = cache.get(currentPlayer);
        if(playingCards.size() == 5)
            playingCards.clear();
        playingCards.add(card);

        repaint();
    }

    /**
     * update card, then repaint.
     * @param card for card of current player.
     */
    public void updateCards(PlayingCard card) {
        currentPlayer = null;
        if(!cache.containsKey(currentPlayer)) {
            cache.put(currentPlayer, new ArrayList<>());
        }

        ArrayList<PlayingCard> playingCards = cache.get(currentPlayer);
        if(playingCards.size() == 5)
            playingCards.clear();
        playingCards.add(card);

        repaint();
    }

    /**
     * update player, then repaint.
     * @param currentPlayer for player, if update HOUSE, then p is null.
     */
    public void updatePlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        int eachCardWidth = width / 5;

        int pandding = 20;
        int panddingInCard = 20;

        g.clearRect(0, 0, width, height);
        ArrayList<PlayingCard> playingCards = cache.getOrDefault(currentPlayer, new ArrayList<>());
        try {

            for (int i = 0; i < playingCards.size(); i++) {
                PlayingCardImpl playingCard = (PlayingCardImpl) playingCards.get(i);
                String suitString = getSuitString(playingCard.getSuit());
                String valueString = getValueString(playingCard.getValue());

                int x = i * eachCardWidth + pandding;
                int y = pandding;
                int cardWidth = eachCardWidth - 2 * pandding;
                int cardHeight = height - 2 * pandding;
                g.setColor(Color.WHITE);
                g.fillRoundRect(x, y, cardWidth, cardHeight, 2, 2);

                if(playingCard.getSuit() == PlayingCard.Suit.DIAMONDS || playingCard.getSuit() == PlayingCard.Suit.HEARTS) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.drawString(valueString, x + panddingInCard, y + panddingInCard);
                g.drawString(valueString, x + cardWidth - panddingInCard, y + cardHeight - panddingInCard);

                BufferedImage image = ImageIO.read(new File(suitString + ".png"));
                g.drawImage(image, x + (cardWidth / 2) - 15, y + (cardHeight / 2) - 15, 30, 30, null);
            }
        } catch (Exception ignored) {

        }
    }

    public static String getValueString(PlayingCard.Value value) {
        if(value == PlayingCard.Value.EIGHT) return "8";
        if(value == PlayingCard.Value.NINE) return "9";
        if(value == PlayingCard.Value.TEN) return "10";
        if(value == PlayingCard.Value.JACK) return "J";
        if(value == PlayingCard.Value.QUEEN) return "Q";
        if(value == PlayingCard.Value.KING) return "K";
        if(value == PlayingCard.Value.ACE) return "A";
        return "";
    }

    public static String getSuitString(PlayingCard.Suit suit) {
        if(suit == PlayingCard.Suit.HEARTS)	return "hearts";
        if(suit == PlayingCard.Suit.SPADES)	return "spades";
        if(suit == PlayingCard.Suit.CLUBS)	return "clubs";
        if(suit == PlayingCard.Suit.DIAMONDS)	return "diamonds";
        return "";
    }
}

