package com.rohidekar.photosorter;

import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PhotoSorterControllerTest {

	@Test
	public void launch() {
		new PhotoSorterController().photoSorter.launch(new Window());
	}

	@Mock
	java.io.File file;

	@Test
	public void tag() {
		PhotoSorterController psc = new PhotoSorterController();
		psc.photoSorter.launch(new Window());
//		new MyImageTagAction(psc, "dfsafdas", new TextInput(), psc.getSession()).invoke(psc
//				.getSession());

	}
}
