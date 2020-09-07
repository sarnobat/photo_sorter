package com.rohidekar.photosorter.actions;


import org.apache.pivot.wtk.TextInput;

import com.rohidekar.photosorter.NoImagesFoundException;
import com.rohidekar.photosorter.model.ImmutableMyModel;

import com.rohidekar.photosorter.model.MyModelManipulator;

// Copyright 2012 Google Inc. All Rights Reserved.

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public abstract class MyImageAction {

	private final String folderName;
	protected final MyModelManipulator modelManipulator;
	protected final TextInput aFilePathTextInput;

	public MyImageAction(MyModelManipulator app, String folderName, TextInput aFilePathTextInput) {
		this.modelManipulator = app;
		this.folderName = folderName;
		this.aFilePathTextInput = aFilePathTextInput;
	}

	public abstract void invoke(ImmutableMyModel session) throws NoImagesFoundException;

	public static void copyToFolder(String destinationFolderPath, ImmutableMyModel session) {
		session.copyCurrentImageToFolder(destinationFolderPath);
	}

	public String getFolderName() {
		return folderName;
	}

	public void moveCurrentImageToSubfolder(ImmutableMyModel session, String aFolderName) {
		session.moveCurrentImageToSubfolder(aFolderName);
	}

	public static void moveToParentFolder(ImmutableMyModel session) {
		session.moveCurrentImageToParentFolder();
	}
}
