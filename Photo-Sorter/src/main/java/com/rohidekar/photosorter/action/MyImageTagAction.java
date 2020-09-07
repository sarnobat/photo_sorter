package com.rohidekar.photosorter.action;

import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.Modifier;
import org.apache.pivot.wtk.TextInput;

import com.rohidekar.photosorter.actions.MyImageAction;
import com.rohidekar.photosorter.model.*;

// Copyright 2012 Google Inc. All Rights Reserved.
/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class MyImageTagAction extends MyImageAction {

	final String aFolderName;
	final ImmutableMyModel session;

	public MyImageTagAction(MyModelManipulator app, String folderName,
			TextInput aFilePathTextInput, ImmutableMyModel session) {
		super(app, folderName, aFilePathTextInput);
		aFolderName = folderName;
		this.session = session;
	}

	@Override
	public void invoke(ImmutableMyModel session) {
		boolean modifierPressed = Keyboard.isPressed(Modifier.ALT)
				|| Keyboard.isPressed(Modifier.META);
		if (modifierPressed) {
			super.moveCurrentImageToSubfolder(session, aFolderName);
		}
	}
}
