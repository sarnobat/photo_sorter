package com.rohidekar.photosorter.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.rohidekar.photosorter.NoImagesFoundException;

/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public class MyImageList {

	final List<File> imageList = new ArrayList<File>();
	int pointer = 0;

	private MyImageList(File rootDir, final IOFileFilter fileFilter, final IOFileFilter dirFilter) {
		final List<File> imageFiles = new LinkedList<File>(FileUtils.listFiles(rootDir, fileFilter,
				dirFilter));
		if (imageFiles.size() == 0) {
			System.out.println();
		}
		imageList.clear();
		// TODO: make 200 a configurable param
		imageList.addAll(imageFiles);// imageFiles.subList(0, 200)
		Collections.shuffle(imageList);
	}

	public static void refreshImageList(String rootDirPath, Set<String> tagsToIgnore,
			ImmutableMyModel session) {
		File rootDir = new File(rootDirPath);
		if (rootDir.exists()) {
			MyImageList.refreshImageList(rootDir, tagsToIgnore, session);
		}
	}

	static void refreshImageList(File rootDir, final Set<String> tagsToIgnore,
			final ImmutableMyModel session) {
		if (tagsToIgnore.size() < 1) {
			throw new IllegalAccessError("Developer error.");
		}
		final IOFileFilter dirFilter = new IOFileFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return accept(arg0);
			}

			@Override
			public boolean accept(File iDir) {
				if (iDir.getAbsolutePath().equals(session.getRootDirPath())) {
					return true;
				}
				if (!session.recursive()) {
					return false;
				}
				// TODO: this can be optimized
				return true;
			}
		};

		final IOFileFilter fileFilter = new IOFileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (!extensionPermitted(pathname, session)) {
					return false;
				}
				String rootDirPath = session.getRootDirPath();
				File rootDir = FileUtils.getFile(rootDirPath);
				if (pathname.getParentFile().equals(rootDir)) {
					return true;
				}
				String immediateTag = pathname.getParentFile().getName();
				if (tagsToIgnore.contains(immediateTag)) {
					if (session.ignoreTaggedImages()) {
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
		MyImageList imageList = new MyImageList(rootDir, fileFilter, dirFilter);
		session.setImageList(imageList);
	}

	static final boolean permitDirectories = true;

	static boolean extensionPermitted(File image, final ImmutableMyModel session) {
		String ext = FilenameUtils.getExtension(image.getAbsolutePath());
		boolean extensionPermitted = session.extensionPermitted(ext) || ext.equals("")
				&& permitDirectories;
		return extensionPermitted;
	}

	void advancePointer() throws NoImagesFoundException {
		if (imageList.size() < 1) {
			throw new NoImagesFoundException();
		}
		pointer++;
		if (pointer >= imageList.size()) {
			pointer = 0;
		}
	}

	void retreatPointer() {
		if (pointer == 0) {
			pointer = imageList.size();
		}
		pointer--;
	}

	void replaceImage(File newFile, File currentImage) {
		if (newFile.equals(currentImage)) {
			throw new IllegalAccessError("This is pointless");
		}
		int index = imageList.indexOf(currentImage);
		imageList.remove(currentImage);
		imageList.add(index, newFile);
		System.out.println("Removed: " + currentImage.getAbsolutePath());
		System.out.println("Added: " + newFile.getAbsolutePath());
	}

	void remove(File currentImage) {
		imageList.remove(currentImage);
	}

	File getCurrentImage() {
		return imageList.get(pointer);
	}
}
