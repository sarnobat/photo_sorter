package com.rohidekar.photosorter.model;

import java.io.File;

import com.rohidekar.photosorter.MyTextInput;
import com.rohidekar.photosorter.NoImagesFoundException;

public interface ImmutableMyModel {

	boolean configurationNotInitialized();

	void setConfiguration(ServerVisibleConfiguration configuration);

	String getRootDirPath();

	Iterable<Character> getActionChars();

	void eraseKeyConfigPairs();

	void moveCurrentImageToSubfolder(String aFolderName);

	void copyCurrentImageToFolder(String destinationFolderPath);

	void moveCurrentImageToParentFolder();

	void addKeyBinding(MyTextInput keyTextInputWidget, MyTextInput tagTextInputWidget);

	File getCurrentImage();

	boolean recursive();

	boolean ignoreTaggedImages();

	void setImageList(MyImageList imageList);

	boolean extensionPermitted(String ext);

	void showNextUnhandled() throws NoImagesFoundException;

	void advanceUntilImageFound() throws NoImagesFoundException;

	void validate();

	void retreatPointer();

}
