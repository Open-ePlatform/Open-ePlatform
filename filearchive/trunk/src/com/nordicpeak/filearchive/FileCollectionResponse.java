package com.nordicpeak.filearchive;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.filearchive.beans.FileCollection;


public class FileCollectionResponse implements Elementable {

	private List<FileCollection> fileCollections;

	public FileCollectionResponse(List<FileCollection> fileCollections) {

		super();
		this.fileCollections = fileCollections;
	}

	@Override
	public Element toXML(Document doc) {

		Element fileCollectionsElement = doc.createElement("FileCollections");

		XMLUtils.append(doc, fileCollectionsElement, fileCollections);

		return fileCollectionsElement;
	}
}
