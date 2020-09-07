import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.io.FileUtils;

public class Main {
	public static void main(String[] args) {
		Main m = new Main();
		m.readFile();
		m.writeFile();
	}

	private void writeFile() {
		try {
			FileUtils.writeLines(new File("/Users/sarnobat/trash/new-test-output.mwk"), _lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	List<String> _lines;
	// int i = 0;
	ListIterator<String> _lineIter;
	int _startIndex = 0;
	int _endIndex = 0;

	private void readFile() {
		try {
			_lines = FileUtils.readLines(new File("/Users/sarnobat/trash/new-test.mwk"));
			_lineIter = (ListIterator<String>) _lines.iterator();
			// for (String line : lines) {
			// System.out.println(line);
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


//	private boolean isWithinSection(String iLine, final int iHeadingLevel) {
//		int thisLinesLevel = 0;
//
//		for (int i = 1; i <= 2; i++) {
//			if (iLine.matches("^={" + i + "}[^=]")) {
//				thisLinesLevel = i;
//			}
//		}
//
//		if (thisLinesLevel < 1) {
//			return true;
//		} else if (thisLinesLevel > iHeadingLevel) {
//			return true;
//		} else if (thisLinesLevel <= iHeadingLevel && thisLinesLevel > 0) {
//			return false;// needs a new section
//		} else {
//			throw new IllegalAccessError("Developer error");
//		}
//	}

//	private void transferTextBlock() {
//		// _lines = _lines.
//		
//		// remove lines from file
//		
//		// save source file
//		
//		// save destination file
//	}
//private List<String> getNextSection() {
//List<String> aTextBlock = new LinkedList<String>();
//String theNextLine = _lineIter.next();
//
//do {
//	aTextBlock.add(theNextLine);
//	theNextLine = _lineIter.next();
//	++_endIndex;
//	// TODO: save the state of the system so that we can restore it if we
//	// chose to go backwards
//} while (isWithinSection(theNextLine, 2));
//_startIndex = _endIndex;
//return aTextBlock;
//}

}
