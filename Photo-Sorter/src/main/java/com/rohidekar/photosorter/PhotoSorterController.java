package com.rohidekar.photosorter;

import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class PhotoSorterController implements Application {

	PhotoSorter photoSorter = new PhotoSorterImpl();

	public static void main(String[] args) {
		DesktopApplicationContext.main(PhotoSorterController.class, args);
	}

	@Override
	public void startup(Display iDisplay,
			org.apache.pivot.collections.Map<String, String> iProperties) {
		Window window = new Window();
		window.setContent(photoSorter.launch(window));
		window.setTitle("Image Sorter");
		window.setMaximized(true);
		window.open(iDisplay);
	}

	@Override
	public boolean shutdown(boolean optional) {
		photoSorter.close();
		return false;
	}

	@Override
	public void suspend() {
	}

	@Override
	public void resume() {
	}
}
