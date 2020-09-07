import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import com.google.common.base.Joiner;

public class TextSorter implements Application {

	private static final String outputDirectory = "/Users/sarnobat/Desktop/mac-sync/cheat-sheets-plain-text/";
	private static final String inputFilePath = "/Users/sarnobat/Desktop/mac-sync/new.mwk";
	private static final String configFilePath = "/Users/sarnobat/Windows/usb/Professional/Eclipse workspace/TextSorter/resources/categories.txt";

	// Widgets
	private Window window = null;
	FlowPane flowPaneComponent;
	TextArea textAreaContent;
	TextInput aFilePathTextInput;
	// private BoxPane bindingsPane;
	Map<MyTextInput, MyTextInput> keyConfigInputPair;
	TextArea textAreaConfig;
	// Data
	List<String> _lines;
	// ListIterator<String> _lineIter;
	int _startIndex = 0;
	int _endIndex = 0;

	@Override
	public void startup(Display iDisplay,
			org.apache.pivot.collections.Map<String, String> iProperties) throws Exception {

		BoxPane verticalFlowPaneComponent = new BoxPane();
		verticalFlowPaneComponent.setOrientation(Orientation.VERTICAL);

		flowPaneComponent = new FlowPane();
		flowPaneComponent.add(verticalFlowPaneComponent);

		textAreaContent = new TextArea();
		textAreaContent.setPreferredSize(700, 700);
		flowPaneComponent.add(textAreaContent);

		textAreaConfig = new MyConfigTextArea(this);
		textAreaConfig.setPreferredSize(100, 700);
		flowPaneComponent.add(textAreaConfig);

		MyKeyBindingsTextArea bindingsTextAreaComponent = new MyKeyBindingsTextArea(this);
		String configText = getConfigTextFromFile(configFilePath);
		bindingsTextAreaComponent.setText(configText);
		createKeyBindingsFromText(configText, this);
		flowPaneComponent.add(bindingsTextAreaComponent);

		_lines = readFile(inputFilePath);
		checkNotNull(_lines);

		showNext();

		aFilePathTextInput = createFilePathTextInput(new MyKeyListener(this));

		verticalFlowPaneComponent.add(aFilePathTextInput);

		window = new Window();
		window.setContent(flowPaneComponent);
		window.setTitle("Text Sorter");
		window.setMaximized(true);
		window.open(iDisplay);

	}

	private String getConfigTextFromFile(String configFilePath) throws IllegalAccessError {
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

	public static void createKeyBindingsFromText(String bindingsText, TextSorter photoSorter) {
		String[] bindings = bindingsText.split("\\n");
		photoSorter.clearKeyBindings();
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
			photoSorter.addImageActionForKey(keyCode, folderName);
		}
		photoSorter.buildKeyListenersFromMap();
	}

	private BoxPane bindingsPane;

	public void buildKeyListenersFromMap() {
		bindingsPane = createBindingsWidgets();
	}

	private BoxPane createBindingsWidgets() {
		keyConfigInputPair = new HashMap<MyTextInput, MyTextInput>();
		BoxPane bindingsPane = new BoxPane();
		bindingsPane.setOrientation(Orientation.VERTICAL);
		for (MyKeyInput aKeyTextInputWidget : imageActions.keySet()) {
			try {
				MyKeyInput anInput = aKeyTextInputWidget;
				if (anInput == null) {
					// user doesn't have any more bindings
					break;
				}
				char keyCode = (char) anInput.keyCode();// TODO: capitalize,
														// otherwise
														// you need the bindings
														// file to
														// give uppercase
														// letters
				MyAction myImageAction = imageActions.get(anInput);
				final String folderName = myImageAction.getFolderName();
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
							action.invoke(getInstance());
						} catch (IllegalAccessException e) {
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

	public void addImageActionForKey(int keyCode, String folderName) {
		this.imageActions.put(new MyKeyInput(keyCode), new MyAction(getInstance(), folderName));
	}

	private final TextSorter getInstance() {
		return this;
	}

	public void clearKeyBindings() {
		flowPaneComponent.remove(bindingsPane);
		this.imageActions.clear();
	}

	private static TextInput createFilePathTextInput(ComponentKeyListener.Adapter keyListener) {
		TextInput aFilePathTextInput = new TextInput();
		aFilePathTextInput.setPreferredSize(200, 20);
		aFilePathTextInput.getComponentKeyListeners().add(keyListener);
		return aFilePathTextInput;
	}

	public void showNext() {
		_lines = readFile(inputFilePath);
		// add the text to the text area
		textAreaContent.setText(Joiner.on("\n").join(locateNextSection()));
	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(TextSorter.class, args);
	}

	private static List<String> readFile(String inputFilePath) {
		List<String> theLines = null;
		try {
			theLines = FileUtils.readLines(new File(inputFilePath));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return new CopyOnWriteArrayList<String>(theLines);
	}

	private List<String> locateNextSection() {
		if (_startIndex != _endIndex) {
			throw new IllegalAccessError("Developer error");
		}
		List<String> aNextSection = new LinkedList<String>();

		// The first line is the heading and should not be disqualified for
		// being
		// equal to the heading level at which we will terminate
		String aCurrentLine = _lines.get(_endIndex++);
		aNextSection.add(aCurrentLine);

		aCurrentLine = _lines.get(_endIndex++);
		// 1) forward the end index to the end of this section
		// 2) accumulate the text to be displayed
		while (isWithinSection(aCurrentLine, 2)) {
			aNextSection.add(aCurrentLine);
			aCurrentLine = _lines.get(_endIndex++);
		}
		--_endIndex;
		if (_endIndex == _startIndex) {
			throw new IllegalAccessError("Developer Error");
		}
		return aNextSection;
	}

	public void rewindSection() {
		_lines = readFile(inputFilePath);
		textAreaContent.setText(Joiner.on("\n").join(locatePreviousSection()));
	}

	private List<String> locatePreviousSection() {

		if (_endIndex == _startIndex) {
			throw new IllegalAccessError("Developer Error");
		}

		if (_startIndex == 0) {
			// There is no previous section
			return _lines.subList(_startIndex, _endIndex);
		}

		_endIndex = _startIndex;
		if (_startIndex > 0) {
			--_startIndex;
		}
		String aCurrentLine = null;
		try {
			aCurrentLine = _lines.get(_startIndex);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println();
		}
		List<String> sectionBefore = new LinkedList<String>();

		while (isWithinSection(aCurrentLine, 2)) {
			sectionBefore.add(0, aCurrentLine);
			if (_startIndex > 0) {
				--_startIndex;
			} else {
				break;
			}
			try {
				aCurrentLine = _lines.get(_startIndex);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println();
			}
		}
		sectionBefore.add(0, aCurrentLine);

		if (_endIndex == _startIndex) {
			throw new IllegalAccessError("Developer Error");
		}
		return sectionBefore;
	}

	// private List<String> getNextSectionOld() {
	// checkNotNull(_lineIter);
	// // System.out.println(_lineIter);
	// _startIndex = _endIndex;
	// List<String> aTextBlock = new LinkedList<String>();
	// String theNextLine = _lineIter.next();
	//
	// do {
	// aTextBlock.add(theNextLine == null ? null : theNextLine);
	// theNextLine = _lineIter.next();
	// // System.out.println(theNextLine);
	// ++_endIndex;
	// // TODO: save the state of the system so that we can restore it if we
	// // chose to go backwards
	// } while (isWithinSection(theNextLine, 2));
	// _lineIter.previous();
	// return aTextBlock;
	// }

	private boolean isWithinSection(String iLine, final int iHeadingLevel) {
		int thisLinesLevel = 0;
		if (iLine == null || iLine.equals("")) {

		} else {
			for (int i = 1; i <= 6; i++) {
				String pattern = "^={" + i + "}[^=].*";
				if (iLine.matches(pattern)) {
					thisLinesLevel = i;
				}
			}
		}
		if (thisLinesLevel < 1) {
			return true;
		} else if (thisLinesLevel > iHeadingLevel) {
			return true;
		} else if (thisLinesLevel <= iHeadingLevel && thisLinesLevel > 0) {
			return false;// needs a new section
		} else {
			throw new IllegalAccessError("Developer error");
		}
	}

	public void transferTextBlock(String aFolderName) throws IllegalAccessException {
		// back up old source file
		File inputBackupFile = new File(inputFilePath + ".bak");
		File inputFile = new File(inputFilePath);
		try {
			FileUtils.copyFile(inputFile, inputBackupFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// back up destination file
		File outputBackupFile = new File(outputDirectory + aFolderName + ".mwk.bak");
		File outputFile = new File(outputDirectory + aFolderName + ".mwk");
		if (!outputFile.exists()) {
			throw new IllegalAccessException("Tag name doesn't correspond to an existing file"
					+ outputFile.getAbsolutePath());
		}
		try {
			FileUtils.copyFile(outputFile, outputBackupFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// remove lines from file
		List<String> newDestLines = new LinkedList<String>();
		String lastLine;
		while (_startIndex < _endIndex) {
			lastLine = _lines.remove(--_endIndex);
			newDestLines.add(0, lastLine);
		}

		// save source file
		try {
			List<String> linesToWrite = new LinkedList<String>();
			linesToWrite.addAll(_lines);
			CollectionUtils.filter(linesToWrite, new Predicate() {

				@Override
				public boolean evaluate(Object element) {
					return element != null;
				}
			});
			FileUtils.writeLines(inputFile, linesToWrite);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// read dest file lines
		List<String> existingDestLines = null;
		try {
			existingDestLines = FileUtils.readLines(outputFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// prepend new lines to dest file lines
		newDestLines.addAll(existingDestLines);
		// save all lines to dest file
		try {
			FileUtils.writeLines(outputFile, newDestLines);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// delete backups
		FileUtils.deleteQuietly(inputBackupFile);
		FileUtils.deleteQuietly(outputBackupFile);
		// Re-read file from disk (since we're editing it in parallel)
		showNext();
	}

	// public void transferTextBlockOld(String aFolderName) throws
	// IllegalAccessException {
	// // remove lines from file
	// List<String> newDestLines = new LinkedList<String>();
	// while (_startIndex < _endIndex) {
	// newDestLines.add(_lines.remove(_startIndex));
	// _lines.add(_startIndex, null);
	// ++_startIndex;
	// }
	//
	// // back up old source file
	// File inputBackupFile = new
	// File("/Users/sarnobat/trash/new-test.mwk.bak");
	// File inputFile = new File("/Users/sarnobat/trash/new-test.mwk");
	// try {
	// FileUtils.copyFile(inputFile, inputBackupFile);
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// }
	// // back up destination file
	// File outputBackupFile = new File("/Users/sarnobat/trash/" + aFolderName +
	// "-test.mwk.bak");
	// File outputFile = new File("/Users/sarnobat/trash/" + aFolderName +
	// "-test.mwk");
	// if (!outputFile.exists()) {
	// throw new
	// IllegalAccessException("Tag name doesn't correspond to an existing file");
	// }
	// try {
	// FileUtils.copyFile(outputFile, outputBackupFile);
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// }
	// // save source file
	// try {
	// List<String> linesToWrite = new LinkedList<String>();
	// linesToWrite.addAll(_lines);
	// CollectionUtils.filter(linesToWrite, new Predicate() {
	//
	// @Override
	// public boolean evaluate(Object element) {
	// return element != null;
	// }
	// });
	// FileUtils.writeLines(inputFile, linesToWrite);
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// }
	// // read dest file lines
	// List<String> existingDestLines = null;
	// try {
	// existingDestLines = FileUtils.readLines(outputFile);
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// }
	// // prepend new lines to dest file lines
	// newDestLines.addAll(existingDestLines);
	// // save all lines to dest file
	// try {
	// FileUtils.writeLines(outputFile, newDestLines);
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// }
	// // delete backups
	// FileUtils.deleteQuietly(inputBackupFile);
	// FileUtils.deleteQuietly(outputBackupFile);
	// showNext();
	//
	// }

	private java.util.Map<MyKeyInput, MyAction> imageActions = new HashMap<MyKeyInput, MyAction>();

	public void invokeTagAction(int keyCode) throws IllegalAccessException {
		MyAction anAction = null;
		anAction = this.imageActions.get(new MyKeyInput(keyCode));

		if (anAction == null) {

		} else {
			anAction.invoke(this);
		}
	}

	@Override
	public void resume() throws Exception {
	}

	@Override
	public boolean shutdown(boolean arg0) throws Exception {
		return false;
	}

	@Override
	public void suspend() throws Exception {

	}

	public void parseOptions(String bindingsText) {
	}

	public void skipSection() {
		_startIndex = _endIndex;
	}
}
