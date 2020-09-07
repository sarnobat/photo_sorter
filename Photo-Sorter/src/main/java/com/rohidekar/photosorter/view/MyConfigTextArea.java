package com.rohidekar.photosorter.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentStateListener;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;

import com.rohidekar.photosorter.actions.*;

import com.rohidekar.photosorter.MyKeyInput;
import com.rohidekar.photosorter.NotBindingLineException;
import com.rohidekar.photosorter.OptionParser;
import com.rohidekar.photosorter.PhotoSorterImpl;
import com.rohidekar.photosorter.view.MyKeyBindingsTextArea;

import com.rohidekar.photosorter.model.*;

public class MyConfigTextArea extends TextArea implements ComponentStateListener {

	final OptionParser photoSorter;
	final String aDefaultRootDirPath;
	final String aDefaultConfigFilePath;
	final String fallbackConfigFilePath;
	final String FAVORITES_FOLDER;
	final boolean RECURSIVE_DEFAULT;
	final boolean SHOW_TAGGED;
	final Set<String> tagsToIgnore;
	final MyView gui;
	final ImmutableMyModel session;
	final Map<MyKeyInput, MyImageAction> imageActions;
	final TextInput aFilePathTextInput;
	final MyKeyBindingsTextArea bindingsTextAreaComponent;
	final KeyActions keyActions;

	public MyConfigTextArea(OptionParser photoSorter, final String aDefaultRootDirPath,
			final String aDefaultKeybindingsFilePath, final boolean RECURSIVE_default,
			final String FAVORITES_FOLDER, final boolean SHOW_TAGGED, Set<String> tagsToIgnore,
			MyView gui, ImmutableMyModel session, final Map<MyKeyInput, MyImageAction> imageActions,
			TextInput aFilePathTextInput, KeyActions keyActions, String fallbackConfigFilePath) {
		this.keyActions = keyActions;
		this.aFilePathTextInput = aFilePathTextInput;
		this.photoSorter = photoSorter;
		this.gui = checkNotNull(gui);
		this.session = checkNotNull(session);
		this.tagsToIgnore = tagsToIgnore;
		this.SHOW_TAGGED = SHOW_TAGGED;
		this.aDefaultRootDirPath = aDefaultRootDirPath;
		this.aDefaultConfigFilePath = aDefaultKeybindingsFilePath;
		this.RECURSIVE_DEFAULT = RECURSIVE_default;
		this.FAVORITES_FOLDER = FAVORITES_FOLDER;
		this.imageActions = imageActions;
		StringBuffer sb = new StringBuffer();
		if (!RECURSIVE_default) {
			sb.append("#");
		}
		sb.append("recursive\n");
		sb.append("#show_all_tagged\n");
		sb.append("EXCLUDE not good\n");
		sb.append("EXCLUDE small\n");
		sb.append("EXCLUDE gif\n");
		sb.append("EXCLUDE png\n");
		sb.append("EXT png\n");
		sb.append("EXT gif\n");
		sb.append("EXT jpg\n");
		sb.append("EXT jpeg\n");
		sb.append("EXT bmp\n");
		this.setText(sb.toString());
		this.getComponentStateListeners().add(this);

		this.fallbackConfigFilePath = fallbackConfigFilePath;
		this.bindingsTextAreaComponent = new MyKeyBindingsTextArea(keyActions, imageActions, gui,
				session, aFilePathTextInput, aDefaultKeybindingsFilePath);
		final String keyBindingText = getTextFromFile(aDefaultKeybindingsFilePath,
				fallbackConfigFilePath);
		bindingsTextAreaComponent.setText(keyBindingText);
		bindingsTextAreaComponent.refreshKeyBindings(keyBindingText);
		// createKeyBindingsFromText(keyBindingText, keyActions, imageActions,
		// gui, session,
		// aFilePathTextInput);
		parseOptions();
	}

	private static String getTextFromFile(String iConfigFilePath, String fallbackConfigFilePath)
			throws IllegalAccessError {
		String configText = null;
		try {
			File configFile = new File(iConfigFilePath);
			File targetFilePath;
			if (configFile.exists()) {
				targetFilePath = configFile;
			} else {
				targetFilePath = new File(fallbackConfigFilePath);
				// throw new IllegalAccessError("File does not exist");
			}
			configText = FileUtils.readFileToString(targetFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		checkNotNull(configText);
		return configText;
	}

	@Override
	public void enabledChanged(Component arg0) {

	}

	@Override
	public void focusedChanged(Component component, Component obverse) {
		parseOptions();
	}

	private void parseOptions() {
		this.photoSorter.parseOptions(this.getText(), RECURSIVE_DEFAULT, SHOW_TAGGED,
				aDefaultRootDirPath, tagsToIgnore, session, keyActions);
	}

	// TODO - demeter - you can probably move this component to its own class
	public TextArea getBindingsTextAreaComponent() {
		return this.bindingsTextAreaComponent;
	}

	public void updateIntendedConfigFile(String iNewKeyBindingPath) {
		bindingsTextAreaComponent.setIntendedConfigFile(iNewKeyBindingPath);
		final String keyBindingText = getTextFromFile(iNewKeyBindingPath, fallbackConfigFilePath);
		checkNotNull(keyBindingText);
		bindingsTextAreaComponent.setText(keyBindingText);
		bindingsTextAreaComponent.refreshKeyBindings(keyBindingText);
	}
}
