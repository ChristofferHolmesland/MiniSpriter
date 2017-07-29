package application.spritesheet;

import java.io.File;

public class SpriteSheet {

	private String name;
	private File file;
	
	public SpriteSheet(File spriteSheetFile, String name) {
		file = spriteSheetFile;
		this.name = name;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
