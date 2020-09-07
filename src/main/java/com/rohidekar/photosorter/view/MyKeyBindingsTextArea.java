package com.rohidekar.photosorter.view;

import static com.google.common.base.Preconditions.checkNotNull;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentStateListener;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;

import com.google.common.base.Preconditions;
import com.rohidekar.photosorter.*;
import com.rohidekar.photosorter.actions.*;
import com.rohidekar.photosorter.model.*;
public class MyKeyBindingsTextArea extends TextArea implements ComponentStateListener {

	final KeyActions photoSorter;
	final Map<MyKeyInput, MyImageAction> imageActions;
	final MyView gui;
	final ImmutableMyModel session;
	final TextInput aFilePathTextInput;
	private String intendedConfigFilePath;

	public MyKeyBindingsTextArea(KeyActions photoSorter,
			java.util.Map<MyKeyInput, MyImageAction> imageActions, MyView gui, ImmutableMyModel session,
			TextInput aFilePathTextInput, String iIntendedConfigFilePath) {
		this.intendedConfigFilePath = checkNotNull(iIntendedConfigFilePath);
		this.aFilePathTextInput = checkNotNull(aFilePathTextInput);
		this.gui = checkNotNull(gui);
		this.session = checkNotNull(session);
		this.photoSorter = photoSorter;
		this.imageActions = Preconditions.checkNotNull(imageActions);
		StringBuffer sb = new StringBuffer();
		sb.append("n=not good\n");
		sb.append("3=trash\n");
		this.getComponentStateListeners().add(this);
	}

	@Override
	public void enabledChanged(Component arg0) {
	}

	@Override
	public void focusedChanged(Component component, Component obverse) {
		System.out.println("Focus changed");
		if (component != null) {
			System.out.println("component" + component.getClass());
		}
		if (obverse != null) {
			System.out.println("obverse:" + obverse.getClass());
		}
		String bindingsText = this.getText();
		refreshKeyBindings(bindingsText);
	}

	public void refreshKeyBindings(String bindingsText) {
		createKeyBindingsFromText(bindingsText, photoSorter, imageActions, gui, session,
				aFilePathTextInput);
		// TODO: Write it out to disk (even if the file doesn't exist)
		writeToFile(bindingsText, intendedConfigFilePath);
	}

	public static void createKeyBindingsFromText(String iBindingsText, KeyActions iPhotoSorter,
			Map<MyKeyInput, MyImageAction> iImageActions, MyView iGui, ImmutableMyModel iSession,
			TextInput iFilePathTextInput) {
		String[] allBindings = iBindingsText.split("\\n");
		iPhotoSorter.clearKeyBindings(iGui, iSession);
		for (String aBindingLine : allBindings) {
			// Ignore comments
			if (aBindingLine.trim().startsWith("#")) {
				String uncommentedBindingLine = aBindingLine.substring(1);
				char keyCode;
				try {
					keyCode = getKeyCode(uncommentedBindingLine);
					String folderName = getFolderName(uncommentedBindingLine);
					iPhotoSorter.addDisabledImageActionForKey(keyCode, folderName);
				} catch (NotBindingLineException e) {
					System.err.println(e.getMessage());
				}
				continue;
			}
			try {
				char keyCode = getKeyCode(aBindingLine);
				String folderName = getFolderName(aBindingLine);
				iPhotoSorter.addImageActionForKey(keyCode, folderName, iFilePathTextInput);
			} catch (NotBindingLineException e) {
				System.err.println(e.getMessage());
			}
		}
		iGui.setBindingsPane(iPhotoSorter.buildKeyListenersFromMap(iImageActions, iSession));
	}

	private static String getFolderName(String uncommentedBindingLine)
			throws NotBindingLineException {
		String rightSide = parseBindingLine(uncommentedBindingLine)[1];
		if (rightSide.length() < 1) {
			throw new IllegalAccessError("Developer error");
		}
		return rightSide;
	}

	private static char getKeyCode(String uncommentedBindingLine) throws NotBindingLineException {

		String leftSide = parseBindingLine(uncommentedBindingLine)[0];
		if (leftSide.length() != 1) {
			throw new IllegalAccessError("Developer error");
		}
		char keyCode = leftSide.charAt(0);
		return keyCode;
	}

	private static String[] parseBindingLine(String aBindingLine) throws NotBindingLineException {
		if (aBindingLine.trim().startsWith("#")) {
			throw new IllegalAccessError("Developer error");
		}
		String[] pair = aBindingLine.split("=");
		if (pair.length != 2) {
			throw new NotBindingLineException(pair);
		}
		return pair;
	}

	private void writeToFile(String bindingsText, String intendedConfigFilePath2) {
		try {
			File configFile = new File(intendedConfigFilePath2);
			FileUtils.writeStringToFile(configFile, bindingsText);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setIntendedConfigFile(String string) {
		intendedConfigFilePath = string;
	}
}
