package view.frame;

import model.interfaces.Player;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SummaryPanel extends JPanel {

    JLabel playerSummary = new JLabel("Player:");
    JLabel resultSummary = new JLabel("the final score of the dealers (house) hand: "); //  the final score of the hand
    HashMap<Player, Integer> playerToResult = new HashMap();

    /**
     * init,set a BorderLayout and add summary labels.
     */
    public SummaryPanel() {
        this.setBackground(Color.CYAN);
        this.setLayout(new BorderLayout());

        this.add(playerSummary, BorderLayout.WEST);
        this.add(resultSummary, BorderLayout.EAST);
    }

    /**
     * update for player.
     * @param player for player, if update HOUSE, then p is null.
     */
    public void updatePlayer(Player player) {
        if(player == null) {
            playerSummary.setText("HOUSE");
        } else {
            playerSummary.setText("player: " + player.toString());
        }
        resultSummary.setText("the final score of the dealers (house) hand: " + playerToResult.getOrDefault(player, 0));
    }

    /**
     * for player, if update HOUSE, then p is null.
     * @param player for player, if update HOUSE, then p is null.
     * @param result for result point of current player.
     */
    public void result(Player player, int result) {
        playerToResult.put(player, result);
        updatePlayer(player);
    }
}
