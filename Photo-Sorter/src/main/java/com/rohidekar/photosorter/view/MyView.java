package com.rohidekar.photosorter.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.color.CMMException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import com.rohidekar.photosorter.model.*;
/**
 * Holds all physical elements
 */
public class MyView {

	private final FlowPane flowPaneComponent;
	private final ImageView anImageView;
	private final Window window;
	private final ImmutableMyModel session;

	private BoxPane bindingsPane;

	public MyView(Window window, FlowPane flowPaneComponent, BoxPane bindingsPane,
			ImageView anImageView, ImmutableMyModel session) {
		this.session = checkNotNull(session);
		this.flowPaneComponent = checkNotNull(flowPaneComponent);
		this.bindingsPane = checkNotNull(bindingsPane);
		this.anImageView = checkNotNull(anImageView);
		this.window = window;
	}

	public void removeBindingsPane() {
		flowPaneComponent.remove(bindingsPane);
	}

	public void setBindingsPane(BoxPane buildKeyListenersFromMap) {
		this.bindingsPane = checkNotNull(buildKeyListenersFromMap);
	}

	public void setNewImage(URL url) {

		System.out.println("setNewImage() - free memory 1.1: " + Runtime.getRuntime().freeMemory());
		anImageView.clear();
		// Runtime.getRuntime().gc();
		// Runtime.getRuntime().runFinalization();
		System.out.println("setNewImage() - free memory 1.2: " + Runtime.getRuntime().freeMemory());
		anImageView.setImage(url);

		System.out.println("setNewImage() - free memory 1.3: " + Runtime.getRuntime().freeMemory());
	}

	public void close() {
		if (window != null) {
			window.close();
		}
	}

	public void validate() {
		checkNotNull(anImageView);
	}

	public static File displayImage(TextInput aTextInput, MyView gui) {
		System.out
				.println("displayImage() - free memory 1.1: " + Runtime.getRuntime().freeMemory());
		File f = gui.getCurrentImage();
		System.out
				.println("displayImage() - free memory 1.2: " + Runtime.getRuntime().freeMemory());
		try {
			URL url = f.toURI().toURL();
			System.out.println("displayImage() - free memory 1.3: "
					+ Runtime.getRuntime().freeMemory());
			gui.setNewImage(url);
			System.out.println("displayImage() - free memory 1.31: "
					+ Runtime.getRuntime().freeMemory());
			String decoded = URLDecoder.decode(url.toURI().toASCIIString(), "UTF-8");

			System.out.println("displayImage() - free memory 1.4: "
					+ Runtime.getRuntime().freeMemory());
			aTextInput.setText(decoded);
			aTextInput.setSelection(0, 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (CMMException e) {
			e.printStackTrace();
		}
		System.out
				.println("displayImage() - free memory 1.5: " + Runtime.getRuntime().freeMemory());
		return f;
	}

	private File getCurrentImage() {
		return session.getCurrentImage();
	}
}
