import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentStateListener;
import org.apache.pivot.wtk.TextArea;

public class MyKeyBindingsTextArea extends TextArea implements ComponentStateListener {
  private PhotoSorter photoSorter;

  public MyKeyBindingsTextArea(PhotoSorter photoSorter) {
    this.photoSorter = photoSorter;
    StringBuffer sb = new StringBuffer();
    sb.append("n=not good\n");
    sb.append("3=trash\n");
    this.setText(sb.toString());
    this.getComponentStateListeners().add(this);
  }

  @Override
  public void enabledChanged(Component arg0) {}

  @Override
  public void focusedChanged(Component component, Component obverse) {
    System.out.println("Focus changed");
    if (component != null) {
      System.out.println("component" + component.getClass());
    }
    if (obverse != null) {
      System.out.println("obverse:" + obverse.getClass());
    }
    String bindingsText = this.getText();
    PhotoSorter.createKeyBindingsFromText(bindingsText, photoSorter);
  }
}
