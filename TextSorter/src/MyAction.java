import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.Modifier;

/**
 * @author sarnobat
 */
public class MyAction {
	private String aFolderName;
	TextSorter app;

	public MyAction(TextSorter app, String folderName) {
		this.app = app;
		aFolderName = folderName;
	}

	public void invoke(TextSorter app) throws IllegalAccessException {
		boolean modifierPressed = Keyboard.isPressed(Modifier.ALT) || Keyboard.isPressed(Modifier.META);
		if (modifierPressed) {
				app.transferTextBlock(aFolderName);
		}
	}

	public String getFolderName() {
		return aFolderName;
	}
}
