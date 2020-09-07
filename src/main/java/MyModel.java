import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds application state
 */

public class MyModel {
	// TODO: make this final
	MyImageList imageList;
	File currentImage;

	public MyModel() {
	}

	// TODO: Demeter violation
	public MyImageList getImageList() {
		return imageList;
	}

	public void setImageList(MyImageList myImageList) {
		imageList = myImageList;
	}

	public File getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(File currentImage) {
		this.currentImage = currentImage;

	}

	Map<MyTextInput, MyTextInput> keyConfigInputPairs = new HashMap<MyTextInput, MyTextInput>();

	public void eraseKeyConfigPairs() {
		keyConfigInputPairs.clear();
	}

	// TODO: demeter violation
	public Map<MyTextInput, MyTextInput> getKeyConfigInputPair() {
		return keyConfigInputPairs;
	}

	// note this can be changed
	MyConfiguration configuration;

	public void setConfiguration(MyConfiguration configuration) {
		this.configuration = checkNotNull(configuration);
	}

	public MyConfiguration getConfiguration() {
		return this.configuration;
	}

}
