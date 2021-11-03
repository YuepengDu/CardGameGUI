package view;

import controller.CardGameController;
import model.interfaces.PlayingCard;
import view.frame.CanvasPanel;
import model.interfaces.Player;
import model.interfaces.GameEngine;
import view.frame.ControllerPanel;
import view.frame.SummaryPanel;
import view.interfaces.GameEngineCallback;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GUI implement of GameEngineCallback
 */
public class GameEngineCallbackGUI implements GameEngineCallback {

    CardGameController cardGameController;

    CanvasPanel canvas;
    ControllerPanel controllerPanel;
    SummaryPanel summaryPanel;

    /**
     * init and store gameEngine
     */
    public GameEngineCallbackGUI(CardGameController cardGameController) {
        this.cardGameController = cardGameController;
        init(); 
    }

    /**
     * create all panels and menubar and then setup frame.
     */
    private void init() {

        canvas = new CanvasPanel();
        summaryPanel = new SummaryPanel();
        controllerPanel = new ControllerPanel(cardGameController);

        cardGameController.setCanvas(canvas);
        cardGameController.setSummaryPanel(summaryPanel);
        cardGameController.setControllerPanel(controllerPanel);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("Game");
        frame.add(controllerPanel, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(summaryPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "this is a card game.");
            }
        });
        file.add(about);
        menuBar.add(file);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
        frame.setBounds(0,0,800,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * <pre>called for each card as the house is dealing to a Player, use this to
     * update your display for each card or log to console
     * @param player
     *            the Player who is receiving cards
     * @param card
     *            the next card that was dealt
     * @param engine
     *            a convenience reference to the engine so the receiver can call
     *            methods if necessary
     * </pre>
     */
    @Override
    public void nextCard(Player player, PlayingCard card, GameEngine engine) {

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                cardGameController.update(player, true);
                canvas.updateCards(player, card);
            }
        });

    }

    /**<pre>
     * called when the card causes the player to bust
     * this method is called instead of {@link #nextCard(Player, PlayingCard, GameEngine)}
     * this method is called before {@link #result(Player, int, GameEngine)}
     * use this to update your display for each card or log to console
     *
     * NOTE: If player gets 42 exactly then this method IS NOT called
     *
     * @param player
     *             the Player who is receiving cards
     * @param card
     *             the bust card that was dealt
     * @param engine
     *             a convenience reference to the engine so the receiver can call
     *             methods if necessary
     * @see model.interfaces.GameEngine
     * </pre>
     */
    @Override
    public void bustCard(Player player, PlayingCard card, GameEngine engine) {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                canvas.updateCards(player, card);
            }
        });
    }

    @Override
    public void result(Player player, int result, GameEngine engine) {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                summaryPanel.result(player, result);
            }
        });
    }

    @Override
    public void nextHouseCard(PlayingCard card, GameEngine engine) {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                cardGameController.update(null, true);
                canvas.updateCards(card);
            }
        });
        
    }

    @Override
    public void houseBustCard(PlayingCard card, GameEngine engine) {

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                canvas.updateCards(card);
            }
        });
    }

    @Override
    public void houseResult(int result, GameEngine engine) {
        summaryPanel.result(null, result);
    }

}
