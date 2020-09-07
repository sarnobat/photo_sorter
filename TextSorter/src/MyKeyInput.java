// Copyright 2012 Google Inc. All Rights Reserved.


/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 *
 */
public class MyKeyInput {

  private int keyCode;

  public MyKeyInput(int keyCode) {
    this.keyCode = keyCode;
  }

  public int keyCode() {
    return keyCode;
  }

  public void setKeyCode(char characterAt) {
    keyCode = characterAt;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    } else if (!(that instanceof MyKeyInput)) {
      return false;
    }
    MyKeyInput thatInput = (MyKeyInput) that;
    return thatInput.keyCode() == this.keyCode();
  }
  
  @Override 
  public int hashCode(){
    return keyCode;
    
  }
}
