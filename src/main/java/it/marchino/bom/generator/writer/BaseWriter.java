package it.marchino.bom.generator.writer;

import java.io.File;

public abstract class BaseWriter implements Writer {

	protected File outputFolder;

	protected void initOutputFolder(String subfolder) {
		outputFolder = new File("output", subfolder);
		if(outputFolder.exists()) {
			return;
		}
		outputFolder.mkdirs();
	}
}
