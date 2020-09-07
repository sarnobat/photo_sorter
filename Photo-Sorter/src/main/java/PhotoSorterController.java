import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import com.google.common.base.Preconditions;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class PhotoSorterController implements Application {
	// ---------------------- Windows --------------------------------
	// private static final String aRootDirPath =
	// "E:/Drive J/pictures/Other (new)/pictures/misc/teen";
	// private static final String configFilePath =
	// "C:/Documents and Settings/Sridhar.SRIDHAR-E5FA83A/Desktop/Macbook Pro/Professional/apache-pivot/helloworld/resources/teen.txt";
	// private static String aRootDirPath = "E:/Sridhar/Web/Friends/Poorvi";
	// String aRootDirPath =
	// "E:/Drive J/pictures/Other (new)/pictures/misc/ind";
	// "/home/ssarnobat/my/usb/Professional/apache-pivot/helloworld/resources/btt.txt";
	// public static final String FAVORITES_FOLDER =
	// "E:/Drive J/pictures/Other (new)/pictures/favorites";

	// ---------------------- Mac --------------------------------
	// /Volumes/Other/pictures/misc/misc/

	private MyModel session;
	private MyView gui;

	@Override
	public void startup(Display iDisplay,
			org.apache.pivot.collections.Map<String, String> iProperties) {

		final String aDefaultRootDirPath = "/Users/sarnobat/Windows/misc/";
		final String aDefaultConfigFilePath = "/Users/sarnobat/Desktop/usb/Professional/apache-pivot/helloworld/resources/other.txt";
		final String FAVORITES_FOLDER = "/Users/sarnobat/Desktop/usb/Other/favorites";
		final boolean RECURSIVE_DEFAULT = false;
		final boolean SHOW_TAGGED = false;
		final BoxPane bindingsPane;

		//------------------------------------------------------------------------
		// Initialize model
		//------------------------------------------------------------------------
		final Set<String> tagsToIgnore = new HashSet<String>();
		final PropertiesConfiguration config = loadConfiguration("usergui.properties");
		session = new MyModel();
		bindingsPane = createBindingsPane(checkNotNull(imageActions),
				checkNotNull(session));

		//------------------------------------------------------------------------
		// Initialize view
		//------------------------------------------------------------------------

		final FlowPane flowPaneComponent = new FlowPane();
		final BoxPane verticalFlowPaneComponent = new BoxPane();
		final ImageView anImageView = new MyImageView();
		final Window window = new Window();

		gui = new MyView(window, flowPaneComponent, bindingsPane, anImageView);

		final TextInput aFilePathTextInput = createFilePathTextInput();
		aFilePathTextInput.getComponentKeyListeners().add(
				new MyKeyListener(this, FAVORITES_FOLDER, aFilePathTextInput,
						session, gui));

		imageActions.put(new MyKeyInput(Keyboard.KeyCode.SPACE),
				new MyImageAction(getInstance(), null, aFilePathTextInput) {
					@Override
					public File invoke(File imageFile)
							throws NoImagesFoundException {
						return app.showNextUnhandled(aFilePathTextInput,
								session, gui);
					}
				});
		// TODO:set focus of text input, or put the key listener on something
		// else

		final TextArea textAreaConfig = new MyConfigTextArea(this,
				aDefaultRootDirPath, aDefaultConfigFilePath, RECURSIVE_DEFAULT,
				FAVORITES_FOLDER, SHOW_TAGGED, tagsToIgnore, gui, session);

		final String configText = getConfigTextFromFile(aDefaultConfigFilePath);
		MyKeyBindingsTextArea bindingsTextAreaComponent = new MyKeyBindingsTextArea(
				this, imageActions, gui, session, configText,
				aFilePathTextInput);
		createKeyBindingsFromText(configText, this, imageActions, gui, session,
				aFilePathTextInput);
		// final MyConfiguration configuration;
		final MyTextInput imagePathComponent = new MyTextInput(session
				.getConfiguration().getRootDirPath()) {
			@Override
			public void valueChanged() {
				refreshImageList(getText(), session.getConfiguration()
						.recursive(), SHOW_TAGGED, tagsToIgnore, session);
			}
		};
		flowPaneComponent.add(checkNotNull(verticalFlowPaneComponent));
		flowPaneComponent.add(checkNotNull(textAreaConfig));
		flowPaneComponent.add(checkNotNull(bindingsTextAreaComponent));
		flowPaneComponent.add(checkNotNull(bindingsPane));

		verticalFlowPaneComponent.add(checkNotNull(imagePathComponent));
		verticalFlowPaneComponent.setOrientation(Orientation.VERTICAL);
		verticalFlowPaneComponent.add(checkNotNull(aFilePathTextInput));
		verticalFlowPaneComponent.add(checkNotNull(anImageView));

		refreshImageList(session.getConfiguration().getRootDirPath(), session
				.getConfiguration().recursive(), SHOW_TAGGED, tagsToIgnore,
				session);

		showNext(aFilePathTextInput, session);
		anImageView.setEnabled(true);

		//------------------------------------------------------------------------
		// 
		//------------------------------------------------------------------------

		window.setContent(flowPaneComponent);
		window.setTitle("Image Sorter");
		window.setMaximized(true);
		window.open(iDisplay);
	}

	private static String getConfigTextFromFile(String configFilePath)
			throws IllegalAccessError {
		String configText = null;
		try {
			File configFile = new File(configFilePath);
			if (configFile.exists()) {
				configText = FileUtils.readFileToString(configFile);
			} else {
				throw new IllegalAccessError("File does not exist");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configText;
	}

	private final PhotoSorterController getInstance() {
		return this;
	}

	// TODO: add numerical key shortcuts to the most popular ones (dynamically).
	// Display the counts
	// for each tag
	private final Map<MyKeyInput, MyImageAction> imageActions = new HashMap<MyKeyInput, MyImageAction>();

	public static void main(String[] args) {
		DesktopApplicationContext.main(PhotoSorterController.class, args);
	}

	private void refreshImageList(String rootDirPath, boolean recursive,
			boolean SHOW_TAGGED, Set<String> tagsToIgnore, MyModel session) {
		File rootDir = new File(rootDirPath);
		if (rootDir.exists()) {
			refreshImageList(rootDir, recursive, SHOW_TAGGED, tagsToIgnore,
					session);
		}
	}

	private void refreshImageList(File rootDir, boolean recursive,
			boolean SHOW_TAGGED, Set<String> tagsToIgnore, MyModel session) {
		Set<String> tags = new HashSet<String>();
		for (MyImageAction actions : this.imageActions.values()) {
			tags.add(actions.getFolderName());
		}
		session.setImageList(new MyImageList(rootDir, session
				.getConfiguration().recursive(), SHOW_TAGGED, tags,
				tagsToIgnore, session.getConfiguration()));
	}

	private static TextInput createFilePathTextInput() {
		TextInput aFilePathTextInput = new TextInput();
		aFilePathTextInput.setPreferredSize(800, 20);
		return aFilePathTextInput;
	}

	private BoxPane createBindingsPane(
			Map<MyKeyInput, MyImageAction> imageActions, final MyModel session) {
		checkNotNull(imageActions);
		checkNotNull(session);
		Map<MyTextInput, MyTextInput> keyConfigInputPair;
		session.eraseKeyConfigPairs();
		keyConfigInputPair = session.getKeyConfigInputPair();
		BoxPane bindingsPane = new BoxPane();
		bindingsPane.setOrientation(Orientation.VERTICAL);
		for (MyKeyInput aKeyTextInputWidget : imageActions.keySet()) {
			try {
				MyKeyInput anInput = aKeyTextInputWidget;
				if (anInput == null) {
					// user doesn't have any more bindings
					break;
				}
				char keyCode = (char) anInput.keyCode();
				MyImageAction myImageAction = imageActions.get(anInput);
				String folderName = myImageAction.getFolderName();
				MyTextInput keyTextInputWidget = new MyTextInput(
						Character.toString(keyCode)) {
					@Override
					public void valueChanged() {
					}
				};
				keyTextInputWidget.setText(Character.toString(keyCode));
				MyTextInput tagTextInputWidget = new MyTextInput(folderName,
						myImageAction) {
					@Override
					public void valueChanged() {
						try {
							action.invoke(session.getCurrentImage());
						} catch (NoImagesFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				if (folderName != null) {
					tagTextInputWidget.setText(folderName);
				}
				keyConfigInputPair.put(keyTextInputWidget, tagTextInputWidget);
				FlowPane bindingEntry = new FlowPane();
				bindingEntry.add(keyTextInputWidget);
				bindingEntry.add(tagTextInputWidget);
				bindingsPane.add(bindingEntry);
			} catch (NoSuchElementException e) {
				break;
			}
		}
		return bindingsPane;
	}

	private static PropertiesConfiguration loadConfiguration(
			String propertiesFileName) {
		File configFile = new File(propertiesFileName);
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(configFile);
			config.setProperty("colors.background", "#000001");
			config.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return config;
	}

	@Override
	public boolean shutdown(boolean optional) {
		if (gui.getWindow() != null) {
			gui.getWindow().close();
		}
		return false;
	}

	@Override
	public void suspend() {
	}

	@Override
	public void resume() {
	}

	private File showNextUnhandled(TextInput aFilePathTextInput,
			MyModel session, MyView gui) throws NoImagesFoundException {
		File currentImage = session.getCurrentImage();

		if (currentImage != null) {
			if (currentImage.toURI() != null) {
				try {
					ApplicationContext.getResourceCache().remove(
							currentImage.toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		// tODO: demeter
		currentImage = session.getImageList().advancePointer();

		File ret = null;
		while (currentImage == null || !currentImage.exists()) {
			currentImage = session.getImageList().advancePointer();
		}
		try {
			ret = displayImage(currentImage, gui.getImageView(),
					aFilePathTextInput);
		} catch (IllegalArgumentException e) {
			// currentImage = null;
			return showNextUnhandled(aFilePathTextInput, session, gui);
		}
		session.setCurrentImage(currentImage);
		checkNotNull(gui.getImageView());
		checkNotNull(session.getCurrentImage());

		return ret;
	}

	public File showPrevious(TextInput aFilePathTextInput, MyView gui) {
		File currentImage = session.getImageList().retreatPointer();
		return displayImage(currentImage, gui.getImageView(),
				aFilePathTextInput);
	}

	private static File displayImage(File f, ImageView anImageView,
			TextInput aTextInput) {
		try {
			anImageView.clear();
			URL url = f.toURI().toURL();
			anImageView.setImage(url);
			// anImageView.setPreferredHeight(500);
			String decoded = URLDecoder.decode(url.toURI().toASCIIString(),
					"UTF-8");
			aTextInput.setText(decoded);
			aTextInput.setSelection(0, 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (java.awt.color.CMMException e) {
			e.printStackTrace();
		}
		return f;
	}

	public void invokeTagAction(int keyCode, TextInput aFilePathTextInput,
			MyModel session) {
		Preconditions.checkNotNull(session.getCurrentImage());
		MyImageAction anAction = null;
		for (MyTextInput aKeyInputWidget : session.getKeyConfigInputPair()
				.keySet()) {
			char[] charsInTextBox = aKeyInputWidget.getText().toCharArray();
			if (charsInTextBox.length > 1) {
				continue;
			}
			if (charsInTextBox[0] == keyCode) {
				anAction = this.imageActions.get(new MyKeyInput(keyCode));
				if (anAction == null) {
					return;
				}
				break;
			}
		}
		if (anAction == null) {
			return;
		}
		File newFile;
		try {
			newFile = anAction.invoke(session.getCurrentImage());
			// if (RECURSIVE) {
			session.getImageList().replaceImage(newFile,
					session.getCurrentImage());
			// } else {
			// imageList.remove(currentImage);
			// }
			showNext(aFilePathTextInput, session);
		} catch (NoImagesFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// showPrevious();
	}

	public void addImageActionForKey(int keyCode, String folderName,
			TextInput aFilePathTextInput) {
		this.imageActions.put(new MyKeyInput(keyCode), new MyImageTagAction(
				getInstance(), folderName, aFilePathTextInput));
	}

	public void clearKeyBindings(MyView gui, MyModel session) {
		gui.removeBindingsPane();
		this.imageActions.clear();
	}

	public BoxPane buildKeyListenersFromMap(
			Map<MyKeyInput, MyImageAction> imageActions, MyModel session) {
		// bindingsPane =
		return createBindingsPane(imageActions, session);
	}

	public static void createKeyBindingsFromText(String bindingsText,
			PhotoSorterController photoSorter,
			Map<MyKeyInput, MyImageAction> imageActions, MyView gui,
			MyModel session, TextInput aFilePathTextInput) {
		String[] bindings = bindingsText.split("\\n");
		photoSorter.clearKeyBindings(gui, session);
		for (String bindingLine : bindings) {
			// Ignore comments
			if (bindingLine.trim().startsWith("#")) {
				continue;
			}
			String[] pair = bindingLine.split("=");
			if (pair.length != 2) {
				continue;
			}
			String leftSide = pair[0];
			String folderName = pair[1];
			if (leftSide.length() != 1) {
				continue;
			}
			if (folderName.length() < 1) {
				continue;
			}
			char keyCode = leftSide.charAt(0);
			photoSorter.addImageActionForKey(keyCode, folderName,
					aFilePathTextInput);
		}
		gui.setBindingsPane(photoSorter.buildKeyListenersFromMap(imageActions,
				session));
	}

	public void parseOptions(String configText, boolean recursiveDefault,
			boolean SHOW_TAGGED, final String aDefaultRootDirPath,
			final String aDefaultConfigFilePath, final String FAVORITES_FOLDER,
			Set<String> tagsToIgnore, MyView gui, MyModel session) {
		String[] configLines = configText.split("\\n");
		setDefaultOptions(tagsToIgnore);
		boolean recursive = recursiveDefault;
		boolean showTagged = SHOW_TAGGED;
		Set<String> permittedExtensions = new HashSet<String>();
		for (String configLine : configLines) {
			// Ignore comments
			String configLineTrimmed = configLine.trim();
			if (configLineTrimmed.startsWith("#")) {
				continue;
			}
			if (configLineTrimmed.matches("recursive")) {
				recursive = true;
			}
			if (configLineTrimmed.matches("show_all_tagged")) {
				showTagged = true;
			}
			if (configLineTrimmed.startsWith("EXCLUDE")) {
				String lineRemainder = StringUtils.remove(configLineTrimmed,
						"EXCLUDE");
				String tagToIgnore = lineRemainder.trim();
				tagsToIgnore.add(tagToIgnore);
			}
			if (configLineTrimmed.startsWith("EXT")) {
				String lineRemainder = StringUtils.remove(configLineTrimmed,
						"EXT");
				String tagToIgnore = lineRemainder.trim();
				permittedExtensions.add(tagToIgnore.toLowerCase());
			}

		}
		MyConfiguration configuration = new MyConfiguration(recursive,
				showTagged, tagsToIgnore, FAVORITES_FOLDER,
				aDefaultConfigFilePath,
				session.getConfiguration() == null ? aDefaultRootDirPath : session
						.getConfiguration().getRootDirPath(),
				permittedExtensions);
		session.setConfiguration(configuration);
		refreshImageList(configuration.getRootDirPath(), recursive,
				SHOW_TAGGED, tagsToIgnore, session);
	}

	private static void setDefaultOptions(Set<String> tagsToIgnore) {
		// RECURSIVE = false;
		// SHOW_TAGGED = false;
		tagsToIgnore.clear();
	}

	public void showNext(TextInput aFilePathTextInput, MyModel session) {
		try {
			this.showNextUnhandled(aFilePathTextInput, session, gui);
		} catch (NoImagesFoundException e) {
			e.printStackTrace();
		}
	}
}
