import com.google.common.base.Preconditions;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

// Copyright 2012 Google Inc. All Rights Reserved.


/**
 * @author ssarnobat@google.com (Sridhar Sarnobat)
 */
public abstract class MyImageAction {
  // TODO: this shouldn't be here
  String folderName;
  PhotoSorter app;

  public MyImageAction(PhotoSorter app, String folderName) {
    this.app = app;
    this.folderName = folderName;
  }

  public abstract File invoke(File imageFile) throws NoImagesFoundException;

  public static File copyToFolder(File imageFile, String destinationFolderPath) {
	  verifySourceImageExists(imageFile);
	    File subfolder = getOrCreateDestinationFolder(destinationFolderPath);
	    File destinationFile = allocateFile(imageFile, subfolder);
	    try {
			FileUtils.copyFile(imageFile, destinationFile);
		} catch (IOException e) {

		      throw new IllegalAccessError("Moving did not work");
		}
	    
	    return destinationFile;
	     }
  
  public File moveImageToSubfolder(File imageFile, String folderName) {
    verifySourceImageExists(imageFile);
    if (imagePathAlreadyContainsFolder(imageFile, folderName)) {
      System.out.println("Path already contains " + folderName);
      return imageFile;
    }

    // if the subfolder exists, do nothing
    String parentDirPath = imageFile.getParent();
    String destinationFolderPath = parentDirPath + "/" + folderName;
	
    File subfolder = getOrCreateDestinationFolder(destinationFolderPath);
    File destinationFile = allocateFile(imageFile, subfolder);
    try {
      FileUtils.moveFile(imageFile, destinationFile);
    } catch (IOException e) {
      throw new IllegalAccessError("Moving did not work");
    }
    return destinationFile;
  }

private static void verifySourceImageExists(File imageFile) throws IllegalAccessError {
	Preconditions.checkNotNull(imageFile);
    if (!imageFile.exists()) {
      try {
        throw new IllegalAccessError("Developer Error. File doesn't exist:"
            + imageFile.getCanonicalPath());
      } catch (IllegalAccessError e) {
        throw new IllegalAccessError("Developer Error. File doesn't exist:");
      } catch (IOException e) {
        throw new IllegalAccessError("Developer Error. File doesn't exist:");
      }
    }
}

private static File getOrCreateDestinationFolder(String destinationFolderPath)
		throws IllegalAccessError {
	File subfolder = new File(destinationFolderPath);

    // if the subfolder does not exist, create it
    if (!subfolder.exists()) {
      boolean createdSubfolder = subfolder.mkdir();
      if (!createdSubfolder) {
        throw new IllegalAccessError("Developer Error");
      }
    }
    if (!subfolder.isDirectory()) {
      throw new IllegalAccessError("Developer Error: not a directory - "
          + subfolder.getAbsolutePath());
    }
	return subfolder;
}

private static File allocateFile(File imageFile, File subfolder)
		throws IllegalAccessError {
	// if destination file exists, rename the file to be moved(while loop)
    String destinationFilePath;
    try {
      destinationFilePath = subfolder.getCanonicalPath() + "/" + imageFile.getName();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalAccessError("Developer Error");
    }
    // TODO: make sure the extension is copied.
//    if (!(destinationFilePath.endsWith("jpg") || destinationFilePath.endsWith("JPG"))) {
//      throw new IllegalAccessError("Developer Error");
//    }

    String destinationFilePathWithoutExtension =
        destinationFilePath.substring(0, destinationFilePath.lastIndexOf('.'));
    String extension = FilenameUtils.getExtension(destinationFilePath);
    File destinationFile = new File(destinationFilePath);
    while (destinationFile.exists()) {
      destinationFilePathWithoutExtension += "1";
      destinationFilePath = destinationFilePathWithoutExtension + "." + extension;
      destinationFile = new File(destinationFilePath);
    }
    if (destinationFile.exists()) {
      throw new IllegalAccessError("an existing file will get overwritten");
    }
	return destinationFile;
}

  private boolean imagePathAlreadyContainsFolder(File imageFile, String folderName) {
    if (imageFile == null) {
      return false;
    }
    if (imageFile.getName().equals(folderName)) {
      return true;
    } else {
      return imagePathAlreadyContainsFolder(imageFile.getParentFile(), folderName);
    }
  }

  public String getFolderName() {
    return folderName;
  }
}
