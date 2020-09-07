package com.rohidekar.photosorter.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.TextInput;

import com.google.common.collect.ImmutableSet;
import com.rohidekar.photosorter.action.*;
import com.rohidekar.photosorter.MyKeyInput;
import com.rohidekar.photosorter.model.*;
import com.rohidekar.photosorter.view.MyView;

public class KeyActionsImpl implements KeyActions {

	private final Map<Character, String> disabledKeyBindings = new HashMap<Character, String>();
	private final Map<MyKeyInput, MyImageAction> imageActions;
	private final ImmutableMyModel session;
	private final MyModelManipulator app;
	private final Actions actions;

	public KeyActionsImpl(Map<MyKeyInput, MyImageAction> imageActions, ImmutableMyModel session,
			MyModelManipulator app, Actions actions) {
		this.imageActions = imageActions;
		this.session = session;
		this.app = app;
		this.actions = actions;
	}

	@Override
	public void addImageActionForKey(int keyCode, String folderName, TextInput aFilePathTextInput) {
		this.imageActions.put(new MyKeyInput(keyCode), new MyImageTagAction(app, folderName,
				aFilePathTextInput, session));
	}

	@Override
	public void clearKeyBindings(MyView iGui, ImmutableMyModel iSession) {
		iGui.removeBindingsPane();
		this.imageActions.clear();
	}

	@Override
	public void addDisabledImageActionForKey(char keyCode, String folderName) {
		this.disabledKeyBindings.put(keyCode, folderName);
	}

	// TODO: demeter
	@Override
	public ImmutableSet<String> getTags() {
		Collection<String> tags = new HashSet<String>();
		for (MyImageAction tagAction : imageActions.values()) {
			if (tagAction instanceof MyImageAction) {
				tags.add(((MyImageTagAction) tagAction).getFolderName());
			}
		}
		for (String commentedTags : disabledKeyBindings.values()) {
			tags.add(commentedTags);
		}
		return ImmutableSet.copyOf(tags);
	}

	@Override
	public BoxPane buildKeyListenersFromMap(Map<MyKeyInput, MyImageAction> imageActions,
			ImmutableMyModel session) {
		return actions.buildKeyListenersFromMap(imageActions, session);
	}
}
