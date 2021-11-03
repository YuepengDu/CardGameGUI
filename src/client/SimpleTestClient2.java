package client;

import controller.CardGameController;
import model.GameEngineImpl;
import model.SimplePlayer;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import view.GameEngineCallbackGUI;
import view.GameEngineCallbackImpl;

/**
 * Simple console client for FP assignment 2, 2020
 * NOTE: This code will not compile until you have implemented code for the supplied interfaces!
 * 
 * You must be able to compile your code WITHOUT modifying this class.
 * Additional testing should be done by copying and adding to this class while ensuring this class still works.
 * 
 * The provided Validator.jar will check if your code adheres to the specified interfaces!
 * 
 * @author Caspar Ryan
 * 
 */
public class SimpleTestClient2
{
   public static void main(String args[])
   {
      final GameEngine gameEngine = new GameEngineImpl();

      // create two test players
      Player[] players = new Player[] { new SimplePlayer("2", "The Shark", 1000), new SimplePlayer(
         "1", "The Loser", 500) };

      // main loop to add players
      for (Player player : players)
      {
         gameEngine.addPlayer(player);
      }

      // controller
      CardGameController cardGameController = new CardGameController(gameEngine);

      // GUI
      GameEngineCallbackGUI gameEngineCallback = new GameEngineCallbackGUI(cardGameController);

      // add logging callback
      gameEngine.addGameEngineCallback(new GameEngineCallbackImpl());
      gameEngine.addGameEngineCallback(gameEngineCallback);

   }
}
