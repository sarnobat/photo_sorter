package com.rohidekar.photosorter.model;

import org.apache.pivot.wtk.TextInput;

import com.rohidekar.photosorter.NoImagesFoundException;
import com.rohidekar.photosorter.view.MyView;


public class MyModelManipulatorImpl implements MyModelManipulator {

	@Override
	public void showNextUnhandled(TextInput aFilePathTextInput, ImmutableMyModel session, MyView gui)
			throws NoImagesFoundException {
		System.out.println("showNextUnhandled() - free memory before: "
				+ Runtime.getRuntime().freeMemory());
		session.showNextUnhandled();
		session.advanceUntilImageFound();
		System.out.println("showNextUnhandled() - free memory 1: "
				+ Runtime.getRuntime().freeMemory());
		try {
			System.out.println("showNextUnhandled() - free memory 1.1: "
					+ Runtime.getRuntime().freeMemory());
			MyView.displayImage(aFilePathTextInput, gui);
			System.out.println("showNextUnhandled() - free memory 1.2: "
					+ Runtime.getRuntime().freeMemory());
		} catch (IllegalArgumentException e) {
			System.out.println("showNextUnhandled() - free memory 1.3: "
					+ Runtime.getRuntime().freeMemory());
			showNextUnhandled(aFilePathTextInput, session, gui);
			System.out.println("showNextUnhandled() - free memory 1.4: "
					+ Runtime.getRuntime().freeMemory());
		}
		System.out.println("showNextUnhandled() - free memory 2: "
				+ Runtime.getRuntime().freeMemory());
		gui.validate();
		session.validate();
		System.out.println("showNextUnhandled() - free memory after: "
				+ Runtime.getRuntime().freeMemory());
	}
}
