import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Window;

/**
 * Holds all physical elements
 */
public class MyView {
	FlowPane flowPaneComponent;
	BoxPane bindingsPane;
	ImageView anImageView;
	Window window;

	public MyView(Window window, FlowPane flowPaneComponent,
			BoxPane bindingsPane, ImageView anImageView) {
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

	public ImageView getImageView() {
		return anImageView;
	}

	public Window getWindow() {
		return window;
	}
}
