import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener.Adapter;
import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.Modifier;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class MyKeyListener extends Adapter {
	TextSorter photoSorter;

	public MyKeyListener(TextSorter photoSorter) {
		this.photoSorter = photoSorter;
	}

	@Override
	public boolean keyReleased(Component c, int keyCode, org.apache.pivot.wtk.Keyboard.KeyLocation kl) {
		boolean modifierPressed = Keyboard.isPressed(Modifier.ALT) || Keyboard.isPressed(Modifier.META);
		// boolean zeroPressed = keyCode == Keyboard.KeyCode.KEYPAD_0 || keyCode ==
		// Keyboard.KeyCode.N0;
		System.out.println(modifierPressed);
		System.out.println(keyCode == Keyboard.KeyCode.T);
		System.out.println("");
		if (keyCode == Keyboard.KeyCode.DOWN || keyCode == Keyboard.KeyCode.SPACE || keyCode == Keyboard.KeyCode.J) {
			photoSorter.skipSection();
			photoSorter.showNext();

		}
		if (keyCode == Keyboard.KeyCode.N && !modifierPressed) {
			for (int i = 0; i < 20; i++) {

				photoSorter.skipSection();
				photoSorter.showNext();
			}
		} else if (keyCode == Keyboard.KeyCode.P  && !modifierPressed) {
			for (int i = 0; i < 20; i++) {
				photoSorter.rewindSection();
			}
		} else if (keyCode == Keyboard.KeyCode.UP || keyCode == Keyboard.KeyCode.K) {
			photoSorter.rewindSection();
		} else {
			try {
				photoSorter.invokeTagAction(keyCode);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
