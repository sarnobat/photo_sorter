package com.rohidekar.photosorter;

import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.Window;

public interface PhotoSorter {

	FlowPane launch(Window window);

	void close();
}
