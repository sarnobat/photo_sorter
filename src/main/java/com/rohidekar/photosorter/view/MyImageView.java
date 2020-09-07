package com.rohidekar.photosorter.view;

import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.skin.ImageViewSkin;

// Copyright 2012 Google Inc. All Rights Reserved.

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 * 
 */
public class MyImageView extends ImageView {

	public MyImageView() {
		super();
		ImageViewSkin s = (ImageViewSkin) this.getSkin();
		// s.setBackgroundColor(Color.RED);
		// s.setOpacity(0.8);
		// this.setLocation(0,0);
		this.setPreferredHeight(600);
		this.setPreferredWidth(800);
		s.setFill(true);
		s.setPreserveAspectRatio(true);
	}

	@Override
	public boolean isFocusable() {
		return true;
	}
}
