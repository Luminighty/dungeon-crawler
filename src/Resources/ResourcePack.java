/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.TreeMap;
import java.util.Vector;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Luminight
 */
public class ResourcePack {

	public ResourcePack(Vector<FrameResource> frames, Vector<ClipResource> clips) {
		this.FrameResources = frames;
		this.ClipResources = clips;
	}

	public static ResourcePack FromFolder(String folder) {
		Vector<FrameResource> frames = new Vector();
		Vector<ClipResource> clips = new Vector();

		String path = "/Resources/Packs/" + folder;
		LoadFrameFolder(frames, path + "/Player/", "player");
		LoadFrameFolder(frames, path + "/Tiles/", "tiles");
		LoadFrameFolder(frames, path + "/UI/", "ui");
		LoadFrameFolder(frames, path + "/UI/Numbers/", "ui");
		LoadFrameFolder(frames, path + "/UI/Items/", "ui");
		LoadFrameFolder(frames, path + "/Item/", "item");
		LoadFrameFolder(frames, path + "/Enemies/", "enemy");
		LoadFrameFolder(frames, path + "/Enemies/Green/", "green");
		LoadFrameFolder(frames, path + "/Enemies/Teal/", "teal");
		LoadFrameFolder(frames, path + "/Enemies/Pink/", "pink");
		LoadFrameFolder(frames, path + "/Enemies/Yellow/", "yellow");
		LoadFrameFolder(frames, path + "/Enemies/Blue/", "blue");

		path = "/Resources/Packs/" + folder + "/Sounds/";
		LoadClipFolder(clips, path);

		return new ResourcePack(frames, clips);
	}

	public static void LoadFrameFolder(Vector<FrameResource> frames, String path, String player) {
		Vector<String> exclude = new Vector();
		LoadFrameFolder(frames, path, player, exclude);
	}

	public static void LoadFrameFolder(Vector<FrameResource> frames, String folder, String sprite, Vector<String> exclude) {
		File fold = new File(ResourceLoader.GetRunFolder(folder).getPath());
		File[] files = fold.listFiles();
		for (File f : files) {
			if (f.isDirectory() || exclude.contains(f.getName())) {
				continue;
			}
			String file = f.getName();
			int extIndex = file.lastIndexOf(".");
			String fileWithoutExtension = file.substring(0, extIndex);
			frames.add(new FrameResource(folder + file, sprite, fileWithoutExtension.toLowerCase()));
		}
	}

	public static void LoadClipFolder(Vector<ClipResource> clips, String folder) {
		Vector<String> exclude = new Vector();
		LoadClipFolder(clips, folder, exclude);
	}

	public static void LoadClipFolder(Vector<ClipResource> clips, String folder, Vector<String> exclude) {
		File fold = new File(ResourceLoader.GetRunFolder(folder).getPath());
		File[] files = fold.listFiles();
		for (File f : files) {
			if (f.isDirectory() || exclude.contains(f.getName())) {
				continue;
			}
			String file = f.getName();
			int extIndex = file.lastIndexOf(".");
			String fileWithoutExtension = file.substring(0, extIndex);
			clips.add(new ClipResource(folder + file, fileWithoutExtension.toLowerCase()));
		}
	}

	public void LoadResources() throws IOException, UnsupportedAudioFileException, UnsupportedAudioFileException, LineUnavailableException, URISyntaxException {
		for (FrameResource res : FrameResources) {
			if (Sprites.containsKey(res.Name)) {
				TreeMap sprites = Sprites.get(res.Name);
				sprites.put(res.Frame, ResourceLoader.LoadImage(res.Path));
			} else {
				TreeMap newTree = new TreeMap();
				newTree.put(res.Frame, ResourceLoader.LoadImage(res.Path));
				Sprites.put(res.Name, newTree);
			}
		}

		for (ClipResource res : ClipResources) {
			Clip clip = ResourceLoader.LoadClip(res.Path);
			Clips.put(res.Clip, clip);
		}
	}

	public void UnloadResources() {
		Sprites.clear();
		Clips.clear();
	}

	private Vector<FrameResource> FrameResources = new Vector();
	private Vector<ClipResource> ClipResources = new Vector();

	private TreeMap<String, TreeMap<String, Image>> Sprites = new TreeMap();
	private TreeMap<String, Clip> Clips = new TreeMap();

	public Image GetImage(String sprite, String frame) {
		if (!Sprites.containsKey(sprite) || !Sprites.get(sprite).containsKey(frame)) {
			System.err.println("'" + sprite + " - " + frame + "' frame not found!");
		}
		return Sprites.get(sprite).get(frame);
	}

	public Clip GetClip(String clip) {
		if (!Clips.containsKey(clip)) {
			System.err.println("'" + clip + "' clip not found!");
		}
		return Clips.get(clip);
	}

}
