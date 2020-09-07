package com.rohidekar.photosorter.actions;

import java.util.Map;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.TextInput;

import com.rohidekar.photosorter.MyKeyInput;
import com.rohidekar.photosorter.view.MyView;
import com.rohidekar.photosorter.model.*;

public interface Actions {

	void showNext(TextInput aFilePathTextInput, ImmutableMyModel session);

	void showPrevious(TextInput aFilePathTextInput, MyView gui);

	void invokeTagAction(int keyCode, TextInput aFilePathTextInput, ImmutableMyModel session);

	BoxPane buildKeyListenersFromMap(Map<MyKeyInput, MyImageAction> imageActions, ImmutableMyModel session);

}
