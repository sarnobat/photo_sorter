package com.rohidekar.photosorter.model;

public interface ServerVisibleConfiguration {

	boolean recursive();

	String getRootDirPath();

	boolean extensionPermitted(String ext);

	boolean ignoreTaggedImages();

}
