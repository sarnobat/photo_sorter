package com.rohidekar.photosorter.actions;

import java.util.Map;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.TextInput;

import com.google.common.collect.ImmutableMap;
import com.rohidekar.photosorter.MyKeyInput;
import com.rohidekar.photosorter.NoImagesFoundException;
import com.rohidekar.photosorter.PhotoSorterImpl;
import com.rohidekar.photosorter.model.*;
import com.rohidekar.photosorter.view.MyView;

public class ActionsImpl implements Actions {
	private final Map<MyKeyInput, MyImageAction> imageActions;
	private final ImmutableMyModel session;
	private final MyView gui;
	private final MyModelManipulator modelManipulator;

	public ActionsImpl(Map<MyKeyInput, MyImageAction> imageActions, ImmutableMyModel session, MyView gui,
			MyModelManipulator modelManipulator) {
		this.imageActions = imageActions;
		this.gui = gui;
		this.session = session;
		this.modelManipulator = modelManipulator;
	}

	@Override
	public void showPrevious(TextInput aFilePathTextInput, MyView gui) {
		session.retreatPointer();
		MyView.displayImage(aFilePathTextInput, gui);
	}

	@Override
	public void invokeTagAction(int iKeyCode, TextInput iFilePathTextInput, ImmutableMyModel iSession) {
		iSession.validate();
		MyImageAction anAction = getActionForKey(iKeyCode, iSession);
		if (anAction == null) {
			return;
		}
		invokeAction(iFilePathTextInput, iSession, anAction);
	}

	private void invokeAction(TextInput iFilePathTextInput, ImmutableMyModel iSession, MyImageAction anAction) {
		try {
			anAction.invoke(iSession);
			// if (RECURSIVE) {
			// } else {
			// imageList.remove(currentImage);
			// }
			showNext(iFilePathTextInput, iSession);
		} catch (NoImagesFoundException e) {
			e.printStackTrace();
		}
		// showPrevious();
	}

	// Nullable
	private MyImageAction getActionForKey(int iKeyCode, ImmutableMyModel iSession) {
		MyImageAction anAction = null;
		for (char actionChar : iSession.getActionChars()) {
			if (actionChar == iKeyCode) {
				anAction = this.imageActions.get(new MyKeyInput(iKeyCode));
				break;
			}
		}
		return anAction;
	}

	// TODO: ImmutableMap (be careful - this introduced a regression last time)
	// TODO: demeter
	@Override
	public BoxPane buildKeyListenersFromMap(Map<MyKeyInput, MyImageAction> iImageActions,
			ImmutableMyModel iSession) {
		return PhotoSorterImpl.createBindingsPane(ImmutableMap.copyOf(iImageActions), iSession);
	}

	@Override
	public void showNext(TextInput aFilePathTextInput, ImmutableMyModel session) {
		System.out.println("showNext() - free memory before: " + Runtime.getRuntime().freeMemory());
		try {
			modelManipulator.showNextUnhandled(aFilePathTextInput, session, gui);
		} catch (NoImagesFoundException e) {
			e.printStackTrace();
		}
		Runtime.getRuntime().gc();
		Runtime.getRuntime().runFinalization();
		System.out.println("showNext() - free memory after: " + Runtime.getRuntime().freeMemory());
	}

}
