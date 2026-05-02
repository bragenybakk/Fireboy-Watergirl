package inf112.fireboys.app;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import inf112.fireboys.controller.GameController;
import inf112.fireboys.model.GameModel;
import inf112.fireboys.view.AudioManager;
import inf112.fireboys.view.GameView;

/** Entry point — wires together the model, view, controller, and audio, then opens the game window. */
public class Main {
	public static final String GAME_TITLE = "Fireboy & Watergirl";

	/**
	 * Starts the game: initialises audio, model, view, and controller, then shows the main window.
	 *
	 * @param args
	 *            command-line arguments (not used)
	 */
	public static void main(String[] args) {
		AudioManager audio = new AudioManager();
		audio.playMusic("/Dentaneosuchus Hunt.mp3");
		GameModel model = new GameModel();
		GameView view = new GameView(model);
		new GameController(model, view, audio);
		JFrame frame = new JFrame(GAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
	}
}
