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
		new PhotoSorterController().run(new Window());
	}
@Mock java.io.File file;
	@Test
	public void tag() {
		PhotoSorterController psc = new PhotoSorterController();
		psc.run(new Window());
		TextInput aFilePathTextInput = new TextInput();
		new MyImageTagAction(psc, "dfsafdas", aFilePathTextInput).invoke(file);
		
	}
}
