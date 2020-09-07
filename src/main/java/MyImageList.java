import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class MyImageList {

	private static final boolean permitDirectories = true;
	int pointer = 0;
	List<File> imageList = new ArrayList<File>();
	@SuppressWarnings("unused")
	private Set<String> tagsToIgnore = null;

	public MyImageList(File rootDir, final boolean recursive,
			boolean showTaggedImages, final Set<String> allTags,
			final Set<String> tagsToIgnore, final MyConfiguration configuration) {
		this.tagsToIgnore = tagsToIgnore;
		// final String[] extensionsPermitted = { "jpg", "JPG", "png", "PNG",
		// "gif", "GIF", "jpeg", "JPEG" };
		Collection<File> imageFiles;
		IOFileFilter dirFilter = new IOFileFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return accept(arg0);
			}

			@Override
			public boolean accept(File iDir) {
				if (iDir.getAbsolutePath().equals(
						configuration.getRootDirPath())) {
					return true;
				}
				if (!recursive) {
					return false;
				}
				// TODO: this can be optimized
				return true;
			}
		};
		IOFileFilter fileFilter = new IOFileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (!extensionPermitted(pathname, configuration)) {
					return false;
				}
				String rootDirPath = configuration.getRootDirPath();
				File rootDir = FileUtils.getFile(rootDirPath);
				if (pathname.getParentFile().equals(rootDir)) {
					return true;
				}
				String immediateTag = pathname.getParentFile().getName();
				if (allTags.contains(immediateTag)) {
					if (configuration.ignoreTaggedImages()) {
						return false;
					}
				}
				if (tagsToIgnore.contains(immediateTag)) {
					return false;
				}
				return true;
			}

			@Override
			public boolean accept(File arg0, String arg1) {
				return accept(arg0);
			}
		};

		imageFiles = FileUtils.listFiles(rootDir, fileFilter, dirFilter);
		if (imageFiles.size() == 0) {
			System.out.println();
		}
		imageList.clear();
		imageList.addAll(imageFiles);
		Collections.shuffle(imageList);
	}

	private boolean extensionPermitted(File image,
			final MyConfiguration configuration) {
		String ext = FilenameUtils.getExtension(image.getAbsolutePath());
		boolean extensionPermitted = configuration.extensionPermitted(ext)
				|| ext.equals("") && permitDirectories;
		return extensionPermitted;
		// for (String ext : configuration) {
		// try {
		// if (image.getCanonicalPath().endsWith(ext)) {
		// return true;
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// return false;
	}

	public File advancePointer() throws NoImagesFoundException {

		if (imageList.size() < 1) {
			throw new NoImagesFoundException();
		}
		pointer++;
		if (pointer >= imageList.size()) {
			pointer = 0;
		}
		// TODO: after a really long time, this throws
		// IndexOutOfBoundsException: Index: 151, Size: 147
		return imageList.get(pointer);
	}

	public File retreatPointer() {
		if (pointer == 0) {
			pointer = imageList.size();
		}
		pointer--;
		return imageList.get(pointer);
	}

	public void replaceImage(File newFile, File currentImage) {
		int index = imageList.indexOf(currentImage);
		imageList.remove(currentImage);
		imageList.add(index, newFile);

	}

	public void remove(File currentImage) {
		imageList.remove(currentImage);

	}
}
