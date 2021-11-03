package controller;

import model.SimplePlayer;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import view.frame.CanvasPanel;
import view.frame.ControllerPanel;
import view.frame.SummaryPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardGameController {

    private static String HOUSE_NAME = "HOUSE";
    Set<Player> playersAlreadyDeal = new HashSet<>();
    private static final int DEFAULT_DELAY_TIME = 100;
    private Player currentPlayer;

    GameEngine gameEngine;

    CanvasPanel canvas;
    ControllerPanel controllerPanel;
    SummaryPanel summaryPanel;
 
    AddPlayerListener addPlayerListener = new AddPlayerListener();
    RemovePlayerListener removePlayerListener = new RemovePlayerListener();
    PlaceBetListener placeBetListener = new PlaceBetListener();
    CancelBetListener cancelBetListener = new CancelBetListener();
    PlayerDealListener playerDealListener = new PlayerDealListener();
    SelectPlayerListener selectPlayerListener = new SelectPlayerListener();

    public CardGameController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void setCanvas(CanvasPanel canvas) {
        this.canvas = canvas;
    }

    public void setControllerPanel(ControllerPanel controllerPanel) {
        this.controllerPanel = controllerPanel;
        updatePlayers();
    }

    public void setSummaryPanel(SummaryPanel summaryPanel) {
        this.summaryPanel = summaryPanel;
    }

    public AddPlayerListener getAddPlayerListener() {
        return addPlayerListener;
    }

    public RemovePlayerListener getRemovePlayerListener() {
        return removePlayerListener;
    }

    public PlaceBetListener getPlaceBetListener() {
        return placeBetListener;
    }

    public CancelBetListener getCancelBetListener() {
        return cancelBetListener;
    }

    public PlayerDealListener getPlayerDealListener() {
        return playerDealListener;
    }

    public SelectPlayerListener getSelectPlayerListener() {
        return selectPlayerListener;
    }

    private void playerDeal() {
        if(currentPlayer == null) {
            JOptionPane.showMessageDialog(null, "only player can deal. once all players have bet and dealt the House automatically deals.");
        } else if(currentPlayer.getBet() == 0) {
            JOptionPane.showMessageDialog(null, "player " + currentPlayer.getPlayerName() + " doesn't bet.");
        } else {
            dealPlayer(currentPlayer);
        }
    }

    /**
     * deal player.
     * notice that once all players have bet and dealt
     * the House automatically deals.
     * @param player
     */
    private void dealPlayer(Player player) {
        playersAlreadyDeal.add(player);
        new Thread() {
            @Override
            public void run() {
            	if(player.getResult()>0) {
                	JOptionPane.showMessageDialog(null, "A");
                } else {
                	gameEngine.dealPlayer(player, DEFAULT_DELAY_TIME);
                }

                if(playersAlreadyDeal.containsAll(gameEngine.getAllPlayers())) {
                    JOptionPane.showMessageDialog(null, "deal house now");
                    gameEngine.dealHouse(DEFAULT_DELAY_TIME);
                    playersAlreadyDeal.clear();
                }
                
            }
        }.start();
    }

    private void cancelBet() {
        currentPlayer.resetBet();
        update();
    }

    private void placeBet() {
        if(currentPlayer == null) {
            JOptionPane.showMessageDialog(null,
                "house do not need to place bet.",
                "WARNING", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField placeABetText = new JTextField();

        Object[] message = {"place a bet:", placeABetText};

        int option = JOptionPane.showConfirmDialog(null, message, "place a bet", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Integer placeBet;
            try {
                placeBet = Integer.valueOf(placeABetText.getText());
                if (placeBet > currentPlayer.getPoints()) {
                    JOptionPane.showMessageDialog(null,
                        "a player can not bet more than their current points balance, this player points is " + currentPlayer.getPoints(),
                        "WARNING", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(placeBet < 0) {
                    JOptionPane.showMessageDialog(null,
                        "wrong bet of negative value: " + placeBet,
                        "WARNING", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null,
                    "wrong bet of " + placeABetText.getText(),
                    "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }

            gameEngine.placeBet(currentPlayer, placeBet);
            update();
        }
    }

    private void removePlayer() {
        gameEngine.removePlayer(currentPlayer);
        updatePlayers();
    }

    private void updatePlayers() {
        List<String> playerNames = new ArrayList<>();
        playerNames.add(HOUSE_NAME);

        for(Player p : gameEngine.getAllPlayers())
            playerNames.add(p.getPlayerName());

        controllerPanel.updatePlayers(playerNames);
    }

    private void addPlayer() {
        JTextField idText = new JTextField();
        JTextField nameText = new JTextField();
        JTextField pointsText = new JTextField();

        Object[] message = {
            "player id:", idText,
            "player name:", nameText,
            "player points:", pointsText
        };

        int option = JOptionPane.showConfirmDialog(null, message, "add player", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = idText.getText();
            String name = nameText.getText();
            String pointsString = pointsText.getText();

            if (id.trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "wrong player id of 锛�" + id,
                    "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (name.trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "wrong player name of 锛�" + name,
                    "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Integer points = 0;
            try {
                points = Integer.valueOf(pointsString);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "wrong points of 锛�" + pointsString,
                    "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(points < 0) {
                JOptionPane.showMessageDialog(null,
                    "wrong points of negative value: " + points,
                    "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }

            gameEngine.addPlayer(new SimplePlayer(id, name, points));
            updatePlayers();
        }
    }

    /**
     * update currentPlayer and tell to all panels.
     */
    public void update(Player player) {
        currentPlayer = player;
        update(false);
    }

    /**
     * update currentPlayer and tell to all panels.
     */
    public void update(Player player, Boolean updateControlPanel) {
        currentPlayer = player;
        update(updateControlPanel);
    }

    /**
     * update currentPlayer and tell to all panels.
     */
    public void update() {
        update(false);
    }

    /**
     * update currentPlayer and tell to all panels.
     * @param updateControlPanel true as need update control panel.
     */
    public void update(boolean updateControlPanel) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                summaryPanel.updatePlayer(currentPlayer);
                canvas.updatePlayer(currentPlayer);
                if(updateControlPanel) {
                    if(currentPlayer == null)
                        controllerPanel.updatePlayer(HOUSE_NAME);
                    else
                        controllerPanel.updatePlayer(currentPlayer.getPlayerName());
                }
            }
        });
    }

    class AddPlayerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addPlayer();
        }
    }

    class RemovePlayerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            removePlayer();
        }
    }

    class PlaceBetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            placeBet();
        }
    }

    class CancelBetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cancelBet();
        }
    }

    class PlayerDealListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            playerDeal();
        }
    }

    class SelectPlayerListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            selectPlayer((String) e.getItem());
        }

    }
    private void selectPlayer(String playersSelectedItem) {
        if (HOUSE_NAME.equals(playersSelectedItem)) {
            currentPlayer = null;
            update();
        } else {
            for (Player p : gameEngine.getAllPlayers()) {
                if (p.getPlayerName().equals(playersSelectedItem)) {
                    currentPlayer = p;
                    update();
                    return;
                }
            }
        }
    }

}
