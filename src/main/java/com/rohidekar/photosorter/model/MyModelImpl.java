package com.rohidekar.photosorter.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pivot.wtk.ApplicationContext;

import com.google.common.base.Preconditions;
import com.rohidekar.photosorter.MyTextInput;
import com.rohidekar.photosorter.NoImagesFoundException;

/**
 * Holds application state
 */
 public class MyModelImpl implements ImmutableMyModel {
	// TODO: make this final
	private MyImageList imageList;
	private final Map<MyTextInput, MyTextInput> keyConfigInputPairs = new HashMap<MyTextInput, MyTextInput>();
	// note this can be changed
	private ServerVisibleConfiguration configuration;

	public void setImageList(MyImageList myImageList) {
		imageList = myImageList;
	}

	// Demeter - unavoidable
	public File getCurrentImage() {
		return imageList.getCurrentImage();
	}

	public void eraseKeyConfigPairs() {
		keyConfigInputPairs.clear();
	}

	public void setConfiguration(ServerVisibleConfiguration configuration) {
		this.configuration = checkNotNull(configuration);
	}

	ServerVisibleConfiguration getConfiguration() {
		return this.configuration;
	}

	public void validate() {
		checkNotNull(getCurrentImage());
	}

	public boolean recursive() {
		return getConfiguration().recursive();
	}

	public String getRootDirPath() {
		return getConfiguration().getRootDirPath();
	}

	public boolean configurationNotInitialized() {
		return getConfiguration() == null;
	}

	public boolean extensionPermitted(String ext) {
		return configuration.extensionPermitted(ext);
	}

	public boolean ignoreTaggedImages() {
		return configuration.ignoreTaggedImages();
	}

	public void showNextUnhandled() throws NoImagesFoundException {
		File currentImage = getCurrentImage();

		if (currentImage != null) {
			if (currentImage.toURI() != null) {
				try {
					ApplicationContext.getResourceCache().remove(currentImage.toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		imageList.advancePointer();
	}

	public void advanceUntilImageFound() throws NoImagesFoundException {
		while (getCurrentImage() == null || !getCurrentImage().exists()) {
			imageList.advancePointer();
		}
	}

	public void copyCurrentImageToFolder(String destinationFolderPath) {
		File imageFile = imageList.getCurrentImage();
		verifySourceImageExists(imageFile);
		File subfolder = getOrCreateDestinationFolder(destinationFolderPath);
		File destinationFile = allocateFile(imageFile, subfolder);
		try {
			FileUtils.copyFile(imageFile, destinationFile);
		} catch (IOException e) {
			throw new IllegalAccessError("Moving did not work");
		}
	}

	private static void verifySourceImageExists(File imageFile) throws IllegalAccessError {
		Preconditions.checkNotNull(imageFile);
		if (!imageFile.exists()) {
			try {
				throw new IllegalAccessError("Developer Error. File doesn't exist:"
						+ imageFile.getCanonicalPath());
			} catch (IllegalAccessError e) {
				throw new IllegalAccessError("Developer Error. File doesn't exist:");
			} catch (IOException e) {
				throw new IllegalAccessError("Developer Error. File doesn't exist:");
			}
		}
	}

	private static File getOrCreateDestinationFolder(String destinationFolderPath)
			throws IllegalAccessError {
		File subfolder = new File(destinationFolderPath);

		// if the subfolder does not exist, create it
		if (!subfolder.exists()) {
			boolean createdSubfolder = subfolder.mkdir();
			if (!createdSubfolder) {
				throw new IllegalAccessError("Developer Error");
			}
		}
		if (!subfolder.isDirectory()) {
			throw new IllegalAccessError("Developer Error: not a directory - "
					+ subfolder.getAbsolutePath());
		}
		return subfolder;
	}

	private static File allocateFile(File imageFile, File subfolder) throws IllegalAccessError {
		System.out.println("allocateFile() - free memory before: "
				+ Runtime.getRuntime().freeMemory());
		// if destination file exists, rename the file to be moved(while loop)
		String destinationFilePath;
		try {
			destinationFilePath = subfolder.getCanonicalPath() + "/" + imageFile.getName();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalAccessError("Developer Error");
		}
		// TODO: make sure the extension is copied.
		// if (!(destinationFilePath.endsWith("jpg") ||
		// destinationFilePath.endsWith("JPG"))) {
		// throw new IllegalAccessError("Developer Error");
		// }

		String destinationFilePathWithoutExtension = destinationFilePath.substring(0,
				destinationFilePath.lastIndexOf('.'));
		String extension = FilenameUtils.getExtension(destinationFilePath);
		File destinationFile = new File(destinationFilePath);
		while (destinationFile.exists()) {
			destinationFilePathWithoutExtension += "1";
			destinationFilePath = destinationFilePathWithoutExtension + "." + extension;
			destinationFile = new File(destinationFilePath);
		}
		if (destinationFile.exists()) {
			throw new IllegalAccessError("an existing file will get overwritten");
		}
		System.out.println("allocateFile() - free memory after: "
				+ Runtime.getRuntime().freeMemory());
		return destinationFile;
	}

	public void moveCurrentImageToSubfolder(String folderName) {
		System.out.println("moveCurrentImageToSubfolder() - free memory before: "
				+ Runtime.getRuntime().freeMemory());
		File imageFile = getCurrentImage();
		verifySourceImageExists(imageFile);
		if (imagePathAlreadyContainsFolder(imageFile, folderName)) {
			System.out.println("Path already contains " + folderName);
		}

		// if the subfolder exists, do nothing
		String parentDirPath = imageFile.getParent();
		String destinationFolderPath = parentDirPath + "/" + folderName;
		if (folderName.equals(imageFile.getParentFile().getName())) {
			System.out.println("Not moving to identical subfolder");
			return;
		}
		File subfolder = getOrCreateDestinationFolder(destinationFolderPath);
		File destinationFile = allocateFile(imageFile, subfolder);
		try {
			FileUtils.moveFile(imageFile, destinationFile);
		} catch (IOException e) {
			throw new IllegalAccessError("Moving did not work");
		}

		System.out.println("moveCurrentImageToSubfolder() - free memory during: "
				+ Runtime.getRuntime().freeMemory());
		replaceImageWith(destinationFile);
		if (!destinationFile.equals(getCurrentImage())) {
			throw new IllegalAccessError("Try to keep these 2 in sync");
		}

		System.out.println("moveCurrentImageToSubfolder() - free memory after: "
				+ Runtime.getRuntime().freeMemory());
	}

	public void moveCurrentImageToParentFolder() {
		File imageFile = getCurrentImage();
		File destinationFile = allocateFile(imageFile, imageFile.getParentFile().getParentFile());
		try {
			FileUtils.moveFile(imageFile, destinationFile);
		} catch (IOException e) {
			throw new IllegalAccessError("Moving did not work");
		}
		replaceImageWith(destinationFile);
		if (!destinationFile.equals(getCurrentImage())) {
			throw new IllegalAccessError("Try to keep these 2 in sync");
		}
	}

	private boolean imagePathAlreadyContainsFolder(File imageFile, String folderName) {
		if (imageFile == null) {
			return false;
		}
		if (imageFile.getName().equals(folderName)) {
			return true;
		} else {
			return imagePathAlreadyContainsFolder(imageFile.getParentFile(), folderName);
		}
	}

	public void replaceImageWith(File newFile) {
		if (newFile.equals(getCurrentImage())) {
			throw new IllegalAccessError("This is pointless");
		}
		imageList.replaceImage(newFile, getCurrentImage());
	}

	public void retreatPointer() {
		imageList.retreatPointer();
	}

	public Collection<Character> getActionChars() {
		Collection<Character> actionChars = new LinkedList<Character>();
		for (MyTextInput aKeyInputWidget : keyConfigInputPairs.keySet()) {
			char[] charsInTextBox = aKeyInputWidget.getText().toCharArray();
			if (charsInTextBox.length > 1) {
				System.err.println("Ignored: " + aKeyInputWidget.getText());
				continue;
			}
			actionChars.add(charsInTextBox[0]);
		}
		return actionChars;
	}

	public void addKeyBinding(MyTextInput keyTextInputWidget, MyTextInput tagTextInputWidget) {
		keyConfigInputPairs.put(keyTextInputWidget, tagTextInputWidget);
	}
}
