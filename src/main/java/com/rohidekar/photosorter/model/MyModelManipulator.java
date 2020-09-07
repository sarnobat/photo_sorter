package com.rohidekar.photosorter.model;

import org.apache.pivot.wtk.TextInput;

import com.rohidekar.photosorter.NoImagesFoundException;
import com.rohidekar.photosorter.view.MyView;

public interface MyModelManipulator {
	
	public void showNextUnhandled(TextInput aFilePathTextInput, ImmutableMyModel session,
			MyView gui) throws NoImagesFoundException;

}
