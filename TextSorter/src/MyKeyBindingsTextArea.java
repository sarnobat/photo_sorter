import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentStateListener;
import org.apache.pivot.wtk.TextArea;

public class MyKeyBindingsTextArea extends TextArea implements ComponentStateListener {
  private TextSorter textSorter;

  public MyKeyBindingsTextArea(TextSorter photoSorter) {
    this.textSorter = photoSorter;
    StringBuffer sb = new StringBuffer();
//    sb.append("D=docs\n");
    sb.append("P=productivity\n");
    this.setText(sb.toString());
    this.getComponentStateListeners().add(this);
    this.setPreferredSize(300, 800);
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
    TextSorter.createKeyBindingsFromText(bindingsText, textSorter);
  }
}
