import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentStateListener;
import org.apache.pivot.wtk.TextArea;

public class MyConfigTextArea extends TextArea implements
		ComponentStateListener {
	private final TextSorter textSorter;

	public MyConfigTextArea(TextSorter photoSorter) {
		this.textSorter = photoSorter;
		StringBuffer sb = new StringBuffer();
	    sb.append("recursive\n");
//	    sb.append("show_all_tagged\n");
	    sb.append("EXCLUDE not good\n");
	    sb.append("EXT png\n");
	    sb.append("EXT gif\n");
	    sb.append("EXT jpg\n");
	    sb.append("EXT jpeg\n");
	    sb.append("EXT bmp\n");
		this.setText(sb.toString());
	    this.getComponentStateListeners().add(this);
	    parseOptions();
	}

	@Override
	public void enabledChanged(Component arg0) {
		
	}

	@Override
	public void focusedChanged(Component component, Component obverse) {
	    parseOptions();
	}

	private void parseOptions() {
		String bindingsText = this.getText();
		this.textSorter.parseOptions(bindingsText);
	}
}
