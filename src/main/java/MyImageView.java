import org.apache.pivot.wtk.ImageView;

// Copyright 2012 Google Inc. All Rights Reserved.


/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 *
 */
public class MyImageView extends ImageView {
  public MyImageView (){
//    ((ImageViewSkin)getSkin()).setFill(true);
  }
  @Override
  public boolean isFocusable() {
    return true;
  }
}
