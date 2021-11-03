package view.frame;

import controller.CardGameController;

import javax.swing.*;
import java.util.List;

public class ControllerPanel extends JPanel {

    JToolBar toolBar = new JToolBar();
    JComboBox players = new JComboBox();
    JButton addPlayer = new JButton("add player");
    JButton removePlayer = new JButton("remove player");
    JButton placeBet = new JButton("place a bet");
    JButton cancelBet = new JButton("cancel bet");
    JButton playerDeal = new JButton("Player Deal");

    /**
     * init.
     * @param cardGameController
     */
    public ControllerPanel(CardGameController cardGameController) {
        init(cardGameController);
    }

    /**
     * setup all action for buttons and toolbar.
     */
    private void init(CardGameController cardGameController) {

        players.addItemListener(cardGameController.getSelectPlayerListener());
        addPlayer.addActionListener(cardGameController.getAddPlayerListener());
        removePlayer.addActionListener(cardGameController.getRemovePlayerListener());
        placeBet.addActionListener(cardGameController.getPlaceBetListener());
        cancelBet.addActionListener(cardGameController.getCancelBetListener());
        playerDeal.addActionListener(cardGameController.getPlayerDealListener());

        toolBar.add(players);
        toolBar.add(addPlayer);
        toolBar.add(removePlayer);
        toolBar.add(placeBet);
        toolBar.add(cancelBet);
        toolBar.add(playerDeal);
        this.add(toolBar);
    }

    /**
     * call as we need to update players to toolbar.
     * @param playerNames
     */
    public void updatePlayers(List<String> playerNames) {
        players.removeAllItems();

        for(String p : playerNames)
            players.addItem(p);
    }

    /**
     * call as we need update current player.
     * @param playerName for player name.
     */
    public void updatePlayer(String playerName) {
        players.setSelectedItem(playerName);
    }
}
