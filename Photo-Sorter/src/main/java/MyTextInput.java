import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public abstract class MyTextInput extends TextInput {

  protected MyImageAction action;

  public MyTextInput(String detail, MyImageAction action) {
    this(detail);
    this.action = action;
  }

  public MyTextInput(String detail) {
    if (detail != null) {
      setText(detail);
    }
    setPreferredSize(800, 20);
    // TODO: implement the interface instead of this/
    this.getTextInputContentListeners().add(new TextInputContentListener.Adapter() {
      @Override
      public void textChanged(TextInput textInput) {
        valueChanged();
      }
    });
  }

  public abstract void valueChanged();
}
