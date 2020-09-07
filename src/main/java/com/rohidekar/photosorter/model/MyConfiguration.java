package com.rohidekar.photosorter.model;

import java.util.Collection;
import java.util.Set;

public class MyConfiguration implements ClientVisibleConfiguration, ServerVisibleConfiguration {

	final boolean recursive;
	final boolean showTagged;
	final String rootDirPath;
	final Set<String> permittedExtensions;

	public MyConfiguration(boolean recursive, boolean showTagged, Set<String> tagsToIgnore,
			String rootDirPath, Set<String> permittedExtensions,
			Collection<String> tagsAssignedToKeys) {
		if (!showTagged) {
			tagsToIgnore.addAll(tagsAssignedToKeys);
		}
		this.rootDirPath = rootDirPath;
		this.recursive = recursive;
		this.showTagged = showTagged;
		this.permittedExtensions = permittedExtensions;
	}

	public boolean ignoreTaggedImages() {
		return !showTagged;
	}

	public boolean recursive() {
		return recursive;
	}

	@Override public String getRootDirPath() {
		return rootDirPath;
	}

	public boolean extensionPermitted(String ext) {
		return this.permittedExtensions.contains(ext.toLowerCase());
	}
}
