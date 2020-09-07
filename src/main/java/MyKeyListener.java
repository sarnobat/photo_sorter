import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener.Adapter;
import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.Modifier;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class MyKeyListener extends Adapter {
	PhotoSorter photoSorter;

	public MyKeyListener(PhotoSorter photoSorter) {
		this.photoSorter = photoSorter;
	}

	@Override
	public boolean keyReleased(Component c, int keyCode,
			org.apache.pivot.wtk.Keyboard.KeyLocation kl) {
		boolean modifierPressed = Keyboard.isPressed(Modifier.ALT) || Keyboard.isPressed(Modifier.META);
		boolean zeroPressed = keyCode == Keyboard.KeyCode.KEYPAD_0 || keyCode == Keyboard.KeyCode.N0;
		System.out.println(modifierPressed);
		System.out.println(keyCode == Keyboard.KeyCode.T);
		System.out.println("");
		if (keyCode == Keyboard.KeyCode.RIGHT || keyCode == Keyboard.KeyCode.SPACE) {
			try {
				photoSorter.showNext();
			} catch (NoImagesFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (keyCode == Keyboard.KeyCode.LEFT) {
			photoSorter.showPrevious();
		} else if (zeroPressed) {
			MyImageAction.copyToFolder(photoSorter.getCurrentImage(), PhotoSorter.FAVORITES_FOLDER);
			
		} else{
			photoSorter.invokeTagAction(keyCode);
		}
		return true;
	}

}
