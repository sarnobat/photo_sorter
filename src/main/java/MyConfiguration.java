import java.util.Set;

public class MyConfiguration {
	private boolean recursive;
	private boolean showTagged;
	private String rootDirPath;
	private Set<String> permittedExtensions;
	public MyConfiguration(boolean recursive, boolean showTagged,
			Set<String> tagsToIgnore, String favoritesFolder, String configFilePath, String rootDirPath, Set<String> permittedExtensions) {
		if (!showTagged){
			System.out.println();
		}
		this.rootDirPath = rootDirPath;
		this.recursive= recursive;
		this.showTagged=showTagged;
		this.permittedExtensions = permittedExtensions;
	}

	public boolean ignoreTaggedImages() {
		return !showTagged;
	}

	public boolean recursive() {
		return recursive;
	}

	public String getRootDirPath() {
		return rootDirPath;
	}
	
	public boolean extensionPermitted(String ext) {
		return this.permittedExtensions.contains(ext.toLowerCase());
	}
}
