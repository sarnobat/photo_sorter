import com.google.common.base.Preconditions;

import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.Modifier;

import java.io.File;

// Copyright 2012 Google Inc. All Rights Reserved.
/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class MyImageTagAction extends MyImageAction {

	private String aFolderName;

	public MyImageTagAction(PhotoSorter app, String folderName) {
		super(app, folderName);
		aFolderName = folderName;
	}

	@Override
	public File invoke(File imageFile) {
		boolean modifierPressed = Keyboard.isPressed(Modifier.ALT) || Keyboard.isPressed(Modifier.META);
		if (modifierPressed) {
			return super.moveImageToSubfolder(Preconditions.checkNotNull(imageFile), aFolderName);
		}
		return imageFile;
	}
}
