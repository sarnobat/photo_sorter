package com.rohidekar.photosorter;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import com.google.common.collect.ImmutableMap;
import com.rohidekar.photosorter.actions.ActionsImpl;
import com.rohidekar.photosorter.actions.KeyActionsImpl;
import com.rohidekar.photosorter.actions.MyImageAction;
import com.rohidekar.photosorter.model.*;
import com.rohidekar.photosorter.view.*;

public class PhotoSorterImpl implements PhotoSorter {

	private static final String FACEBOOK_GRAPHICS = "/Users/sarnobat/Windows/Web/Facebook graphics";
	private static final String FAVORITES_FOLDER = "/Users/sarnobat/Windows/favorites/";
	private static final String IND_BTT = "/Users/sarnobat/Windows/misc/ind/btt";
	private static final String BTT = "/Users/sarnobat/Windows/misc/btt";
	private static final String IND = "/Users/sarnobat/Windows/misc/ind/";
	private static final String WWE = "/Users/sarnobat/Windows/misc/wwe/";
	private static final String TEEN = "/Users/sarnobat/Windows/misc/teen/high";
	private static final String MISC = "/Users/sarnobat/Windows/misc/";
	private static final String MISC_MISC = "/Users/sarnobat/Windows/misc/misc";
	private static final String OTHER = "/Users/sarnobat/Desktop/sshfs/e/Drive J/pictures/Other (new)/pictures";
	private static final String FRIENDS = "/Users/sarnobat/Windows/Web/Friends/";
	private static final String FRIENDS_POORVI = "/Users/sarnobat/Windows/Web/Friends/Poorvi Rohidekar/script";
	private static final String PHOTOS = "/Users/sarnobat/Desktop/sshfs/e/Sridhar/Photos/";
	private static final String ATLETICO_STADIUM = "/Users/sarnobat/Desktop/Atletico Madrid Stadiums";
	private static final String FAVORITES_SOURCE = FAVORITES_FOLDER;
	private static final String DROPBOX = "/Users/sarnobat/Desktop/Dropbox/ifttt/google reader";
	private static final String GOOGLE_DRIVE = "/Users/sarnobat/Google Drive/IFTTT/Google Reader (2)/other";
	private static final String AWS_FOLDER = "/Volumes/bollywoodbutts";
	private static final String PRIME_TEEN_JB = "/Users/sarnobat/Windows/misc/teen/primejailbat.com";
	private static final String PRIME_TEEN_JB_HIGH = PRIME_TEEN_JB + "/high";

	private static final String CONFIG_ATLETICO = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/atletico.txt";
	private static final String CONFIG_OTHER = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/other.txt";
	private static final String CONFIG_PHOTOS = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/photos.txt";
	private static final String CONFIG_FRIENDS = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/friends.txt";
	private static final String CONFIG_TEEN = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/teen.txt";
	private static final String CONFIG_TEEN_HIGH = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/teen_high.txt";
	private static final String CONFIG_WWE = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/wwe.txt";
	private static final String CONFIG_CATEGORIES = "/Users/sarnobat/Desktop/mac-sync/src/java/Photo-Sorter/resources/categories.txt";

	private static final String DEFAULT_ROOT_DIR_PATH = MISC;
	// TODO: Make this private
	public static final String TXT_FILE = "photoSorter.txt";
	private static final String DEFAULT_CONFIG_FILE_PATH = DEFAULT_ROOT_DIR_PATH + "/" + TXT_FILE;
	private static final String FALLBACK_CONFIG_FILE_PATH = CONFIG_TEEN;

	private static final boolean RECURSIVE_DEFAULT = false;
	private static final boolean SHOW_TAGGED_DEFAULT = false;

	private final Map<MyKeyInput, MyImageAction> imageActions = new HashMap<MyKeyInput, MyImageAction>();
	private final ImmutableMyModel session;

	private MyView gui;

	// TODO: add numerical key shortcuts to the most popular ones (dynamically).
	// Display the counts
	// for each tag
	PhotoSorterImpl() {
		session = new MyModelImpl();
	}

	@Override
	public FlowPane launch(final Window window) {
		final BoxPane bindingsPane;

		// ------------------------------------------------------------------------
		// Initialize model
		// ------------------------------------------------------------------------
		final Set<String> tagsToIgnore = new HashSet<String>();
		@SuppressWarnings("unused")
		final PropertiesConfiguration config = loadConfiguration("usergui.properties");

		bindingsPane = createBindingsPane(ImmutableMap.copyOf(checkNotNull(imageActions)),
				checkNotNull(session));

		// ------------------------------------------------------------------------
		// Initialize view
		// ------------------------------------------------------------------------

		final FlowPane flowPaneComponent = new FlowPane();
		final BoxPane verticalFlowPaneComponent = new BoxPane();
		final ImageView anImageView = new MyImageView();

		gui = new MyView(window, flowPaneComponent, bindingsPane, anImageView, session);

		final TextInput aFilePathTextInput = createFilePathTextInput();
		MyModelManipulator modelManipulator = new MyModelManipulatorImpl();
		ActionsImpl actions = new ActionsImpl(imageActions, session, gui, modelManipulator);
		aFilePathTextInput.getComponentKeyListeners().add(
				new MyKeyListener(actions, FAVORITES_FOLDER, AWS_FOLDER, aFilePathTextInput,
						session, gui));

		imageActions.put(new MyKeyInput(Keyboard.KeyCode.SPACE), new MyImageAction(
				modelManipulator, null, aFilePathTextInput) {
			@Override
			public void invoke(ImmutableMyModel session) throws NoImagesFoundException {
				modelManipulator.showNextUnhandled(aFilePathTextInput, session, gui);
			}
		});
		// TODO:set focus of text input, or put the key listener on something
		// else
		final OptionParser optionsParser = new OptionParser();
		final MyConfigTextArea textAreaConfig = new MyConfigTextArea(optionsParser,
				DEFAULT_ROOT_DIR_PATH, DEFAULT_CONFIG_FILE_PATH, RECURSIVE_DEFAULT,
				FAVORITES_FOLDER, SHOW_TAGGED_DEFAULT, tagsToIgnore, gui, session, imageActions,
				aFilePathTextInput, new KeyActionsImpl(imageActions, session, modelManipulator,
						actions), FALLBACK_CONFIG_FILE_PATH);

		final MyTextInput imagePathComponent = new MyTextInput(session.getRootDirPath()) {
			@Override
			public void valueChanged() {
				String text = getText();
				System.out.println(text);
				if (new File(text).exists()) {
					MyImageList.refreshImageList(text, tagsToIgnore, session);
					// TODO: update the intended config file
					textAreaConfig.updateIntendedConfigFile(text + "/" + TXT_FILE);
				}
			}
		};
		flowPaneComponent.add(checkNotNull(verticalFlowPaneComponent));
		flowPaneComponent.add(checkNotNull(textAreaConfig));
		flowPaneComponent.add(checkNotNull(((MyConfigTextArea) textAreaConfig)
				.getBindingsTextAreaComponent()));
		flowPaneComponent.add(checkNotNull(bindingsPane));

		verticalFlowPaneComponent.add(checkNotNull(imagePathComponent));
		verticalFlowPaneComponent.setOrientation(Orientation.VERTICAL);
		verticalFlowPaneComponent.add(checkNotNull(aFilePathTextInput));
		verticalFlowPaneComponent.add(checkNotNull(anImageView));

		MyImageList.refreshImageList(session.getRootDirPath(), tagsToIgnore, session);

		actions.showNext(aFilePathTextInput, session);
		anImageView.setEnabled(true);
		return flowPaneComponent;
	}

	private static TextInput createFilePathTextInput() {
		TextInput aFilePathTextInput = new TextInput();
		aFilePathTextInput.setPreferredSize(800, 20);
		return aFilePathTextInput;
	}

	// TODO: Immutable model interface?
	public static BoxPane createBindingsPane(ImmutableMap<MyKeyInput, MyImageAction> iImageActions,
			final ImmutableMyModel iModel) {
		checkNotNull(iImageActions);
		checkNotNull(iModel);
		// TODO: We should not be mutating the model in this method since we are
		// returning something
		iModel.eraseKeyConfigPairs();
		BoxPane bindingsPane = new BoxPane();
		bindingsPane.setOrientation(Orientation.VERTICAL);
		for (MyKeyInput aKeyTextInputWidget : iImageActions.keySet()) {
			try {
				MyKeyInput anInput = aKeyTextInputWidget;
				if (anInput == null) {
					// user doesn't have any more bindings
					break;
				}
				char keyCode = (char) anInput.keyCode();
				MyImageAction myImageAction = iImageActions.get(anInput);
				String folderName = myImageAction.getFolderName();
				MyTextInput keyTextInputWidget = new MyTextInput(Character.toString(keyCode)) {
					@Override
					public void valueChanged() {
					}
				};
				keyTextInputWidget.setText(Character.toString(keyCode));
				MyTextInput tagTextInputWidget = new MyTextInput(folderName, myImageAction) {
					@Override
					public void valueChanged() {
						try {
							action.invoke(iModel);
						} catch (NoImagesFoundException e) {
							e.printStackTrace();
						}
					}
				};
				if (folderName != null) {
					tagTextInputWidget.setText(folderName);
				}
				iModel.addKeyBinding(keyTextInputWidget, tagTextInputWidget);
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

	private static PropertiesConfiguration loadConfiguration(String propertiesFileName) {
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

	// For testing only
	ImmutableMyModel getSession() {
		return session;
	}

	@Override
	public void close() {
		gui.close();
	}
}
