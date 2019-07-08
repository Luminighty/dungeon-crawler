/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Luminight
 */
public class ResourceLoader {

	public static InputStream LoadResource(String path) {
		try {
			return new FileInputStream(new File(GetRunFolder(path).getPath().replaceAll("%20", " ")));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static Image LoadImage(String path) {
		try {
			URL url = GetRunFolder(path);
			return ImageIO.read(url);
		} catch (Exception ex) {
			Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static Clip LoadClip(String path) {
		try {
			URL url = GetRunFolder(path);
			AudioInputStream musicStream = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(musicStream);
			return c;
		} catch (Exception ex) {
			Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static URL GetRunFolder(String folder) {
		try {
			File f = new File(ResourceLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if(f.getPath().endsWith(".jar")) {
				return new File(f.getParent() + folder).toURI().toURL();
			}
			return new File(f.getPath() + folder).toURI().toURL();
		} catch (URISyntaxException ex) {
			Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
