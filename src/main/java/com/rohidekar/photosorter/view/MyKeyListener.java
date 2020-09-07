package com.rohidekar.photosorter.view;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener.Adapter;
import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.Modifier;
import org.apache.pivot.wtk.TextInput;

import com.rohidekar.photosorter.actions.*;
import com.rohidekar.photosorter.model.ImmutableMyModel;
import com.rohidekar.photosorter.view.MyView;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class MyKeyListener extends Adapter {

	final Actions photoSorter;
	final String FAVORITES_FOLDER;
	final String AWS_FOLDER;
	final TextInput aFilePathTextInput;
	final ImmutableMyModel session;
	final MyView gui;

	public MyKeyListener(Actions photoSorter, final String FAVORITES_FOLDER,
			final String AWS_FOLDER, TextInput aFilePathTextInput, ImmutableMyModel session, MyView gui) {
		this.photoSorter = photoSorter;
		this.session = checkNotNull(session);
		this.gui = checkNotNull(gui);
		this.aFilePathTextInput = checkNotNull(aFilePathTextInput);
		this.FAVORITES_FOLDER = FAVORITES_FOLDER;
		this.AWS_FOLDER = AWS_FOLDER;
	}

	@Override
	public boolean keyReleased(Component c, int keyCode,
			org.apache.pivot.wtk.Keyboard.KeyLocation kl) {
		boolean modifierPressed = Keyboard.isPressed(Modifier.ALT)
				|| Keyboard.isPressed(Modifier.META);
		boolean zeroPressed = keyCode == Keyboard.KeyCode.KEYPAD_0
				|| keyCode == Keyboard.KeyCode.N0;
		boolean onePressed = keyCode == Keyboard.KeyCode.KEYPAD_1 || keyCode == Keyboard.KeyCode.N1;
		boolean cPressed = keyCode == Keyboard.KeyCode.C;
		System.out.println(modifierPressed);
		System.out.println(keyCode == Keyboard.KeyCode.T);
		System.out.println("");

		if (keyCode == Keyboard.KeyCode.RIGHT || keyCode == Keyboard.KeyCode.SPACE) {
			photoSorter.showNext(aFilePathTextInput, session);
		} else if (keyCode == Keyboard.KeyCode.LEFT) {
			photoSorter.showPrevious(aFilePathTextInput, gui);
		} else if (zeroPressed) {
			MyImageAction.copyToFolder(FAVORITES_FOLDER, session);
		} else if (modifierPressed && cPressed) {
			MyImageAction.moveToParentFolder(session);
			photoSorter.showPrevious(aFilePathTextInput, gui);
			photoSorter.showNext(aFilePathTextInput, session);
		} else if (onePressed) {
			MyImageAction.copyToFolder(AWS_FOLDER, session);
		} else {
			if (modifierPressed) {
				photoSorter.invokeTagAction(keyCode, aFilePathTextInput, session);
			} else {
				if (keyCode == Keyboard.KeyCode.J || keyCode == Keyboard.KeyCode.N) {
					photoSorter.showNext(aFilePathTextInput, session);
				} else if (keyCode == Keyboard.KeyCode.F || keyCode == Keyboard.KeyCode.P) {
					photoSorter.showPrevious(aFilePathTextInput, gui);
				} else if (keyCode >= Keyboard.KeyCode.A && keyCode <= Keyboard.KeyCode.Z) {
					photoSorter.showNext(aFilePathTextInput, session);
				}
			}

		}
		return true;
	}

}
