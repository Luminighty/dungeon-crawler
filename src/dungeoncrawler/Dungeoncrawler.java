/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeoncrawler;

import Model.Controls;
import View.MainMenu;
import View.MainWindow;

/**
 *
 * @author Luminight
 */
public class Dungeoncrawler {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Controls.SetDefaultControls();
		new MainMenu();
	}

}
