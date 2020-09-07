package com.rohidekar.photosorter.model;

public interface ClientVisibleConfiguration {

	String getRootDirPath();

	boolean recursive();

	boolean extensionPermitted(String ext);

}
