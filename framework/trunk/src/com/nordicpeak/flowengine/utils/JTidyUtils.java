package com.nordicpeak.flowengine.utils;

import java.io.StringReader;
import java.io.StringWriter;

import org.w3c.tidy.Tidy;


public class JTidyUtils {

	public static String getXHTML(String html, String encoding){
		
		Tidy tidy = new Tidy();

		tidy.setXHTML(true);
		tidy.setMakeClean(true);
		tidy.setShowWarnings(false);
		tidy.setShowErrors(0);
		tidy.setQuiet(true);
		tidy.setPrintBodyOnly(true);
		tidy.setOutputEncoding(encoding);
		tidy.setQuoteNbsp(false);
		
		StringWriter stringWriter = new StringWriter();
		
		tidy.parse(new StringReader(html), stringWriter);
		
		return stringWriter.toString();
	}
}
