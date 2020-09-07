package com.rohidekar.photosorter;


@SuppressWarnings("serial")
public class NotBindingLineException extends Exception {

	public NotBindingLineException(String[] pair) {
		super("Not a binding line: " + pair);
	}

}
