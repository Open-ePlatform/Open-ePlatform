package com.nordicpeak.flowengine.pdf.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.nordicpeak.flowengine.utils.PDFByteAttachment;

public class PDFUtils {

	// http://stackoverflow.com/questions/14947829/reading-pdf-file-attachment-annotations-with-itextsharp
	public static List<com.nordicpeak.flowengine.utils.PDFByteAttachment> getAttachments(File pdfFile, boolean getData) throws IOException {

		List<PDFByteAttachment> attachments = new ArrayList<PDFByteAttachment>();

		RandomAccessFileOrArray inputFileRandomAccess = null;
		PdfReader reader = null;

		try {
			inputFileRandomAccess = new RandomAccessFileOrArray(pdfFile.getAbsolutePath(), false, false);

			reader = new PdfReader(inputFileRandomAccess, null);
			PdfDictionary catalog = reader.getCatalog();

			PdfDictionary documentNames = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES));

			if (documentNames != null) {

				PdfDictionary embeddedFiles = (PdfDictionary) PdfReader.getPdfObject(documentNames.get(PdfName.EMBEDDEDFILES));

				if (embeddedFiles != null) {

					PdfArray fileSpecs = embeddedFiles.getAsArray(PdfName.NAMES);

					for (int i = 0; i < fileSpecs.size(); i += 2) {

						String attachmentName = fileSpecs.getAsString(i).toString();

						PdfDictionary fileSpec = fileSpecs.getAsDict(i + 1);
						PdfDictionary references = fileSpec.getAsDict(PdfName.EF);

						Set<PdfName> keys = references.getKeys();

						HashSet<String> visitedReferences = new HashSet<String>(keys.size());

						for (PdfName key : keys) {

							String keyValue = key.toString();

							if ("/UF".equals(keyValue) || "/F".equals(keyValue)) {

								PdfIndirectReference reference = references.getAsIndirectObject(key);

								if (!visitedReferences.contains(reference.toString())) {

									visitedReferences.add(reference.toString());

									String filename = fileSpec.getAsString(key).toString();

									if(getData) {
										
										PRStream stream = (PRStream) PdfReader.getPdfObject(references.getAsIndirectObject(key));
										
										attachments.add(new PDFByteAttachment(attachmentName, filename, PdfReader.getStreamBytes(stream)));
										
									}else {
										
										attachments.add(new PDFByteAttachment(attachmentName, filename, null));
									}
								}
							}
						}
					}
				}
			}
		} finally {

			CloseUtils.close(inputFileRandomAccess);
			CloseUtils.close(reader);
		}

		return attachments;
	}

	public static byte[] removeAttachments(File pdfFile) throws DocumentException, IOException {
		
		RandomAccessFileOrArray inputFileRandomAccess = null;
		PdfReader reader = null;

		try {
			
			inputFileRandomAccess = new RandomAccessFileOrArray(pdfFile.getAbsolutePath(), false, false);

			reader = new PdfReader(inputFileRandomAccess, null);
			PdfDictionary catalog = reader.getCatalog();

			PdfDictionary documentNames = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES));

			if (documentNames != null) {

				PdfDictionary embeddedFiles = (PdfDictionary) PdfReader.getPdfObject(documentNames.get(PdfName.EMBEDDEDFILES));

				if (embeddedFiles != null) {

					PdfArray namesArray = embeddedFiles.getAsArray(PdfName.NAMES);

					for (int i = 0; i < namesArray.size(); i += 2) {

						PdfDictionary names = namesArray.getAsDict(i + 1);
						PdfDictionary references = names.getAsDict(PdfName.EF);

						if (references != null && (references.contains(PdfName.UF) || references.contains(PdfName.F))) {
							references.remove(PdfName.UF);
							references.remove(PdfName.F);
						}
						
						if (names != null && (names.contains(PdfName.UF) || names.contains(PdfName.F))) {
							names.remove(PdfName.UF);
							names.remove(PdfName.F);
						}						
						
					}
					
					reader.removeUnusedObjects();
					
					ByteArrayOutputStream buffer = new ByteArrayOutputStream(32 * BinarySizes.KiloByte);

					PdfStamper stamper = new PdfStamper(reader, buffer);
					stamper.close();

					return buffer.toByteArray();
					
				}
			}
			
			return FileUtils.getRawBytes(pdfFile);
			
		} finally {
			
			CloseUtils.close(inputFileRandomAccess);
			CloseUtils.close(reader);
		}
		
	}

}
