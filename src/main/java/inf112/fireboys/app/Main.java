package inf112.fireboys.app;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.lwjgl.system.Configuration;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import inf112.fireboys.controller.GameController;
import inf112.fireboys.model.GameModel;
import inf112.fireboys.view.GameView;

public class Main {
	public static final String GAME_TITLE = "Fireboy and Watergirl";
	public static void main(String[] args) {
		GameModel model = new GameModel();
		GameView view = new GameView(model);
		new GameController(model, view);
		JFrame frame = new JFrame(GAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
	}
}
