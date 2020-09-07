package com.rohidekar.photosorter.actions;

import com.rohidekar.photosorter.MyKeyInput;
import com.rohidekar.photosorter.model.*;
import java.util.Map;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.TextInput;

import com.google.common.collect.ImmutableSet;
import com.rohidekar.photosorter.view.MyView;

public interface KeyActions {

	void clearKeyBindings(MyView gui, ImmutableMyModel session);

	BoxPane buildKeyListenersFromMap(Map<MyKeyInput, MyImageAction> imageActions, ImmutableMyModel session);

	void addImageActionForKey(int keyCode, String folderName, TextInput aFilePathTextInput);

	ImmutableSet<String> getTags();
	
	void addDisabledImageActionForKey(char keyCode, String folderName);

}
