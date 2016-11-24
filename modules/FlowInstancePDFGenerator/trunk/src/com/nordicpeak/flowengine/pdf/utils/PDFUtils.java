package com.nordicpeak.flowengine.pdf.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;

public class PDFUtils {

	// http://stackoverflow.com/questions/14947829/reading-pdf-file-attachment-annotations-with-itextsharp
	public static List<com.nordicpeak.flowengine.utils.PDFByteAttachment> getAttachments(File pdfFile) throws IOException {

		List<PDFByteAttachment> attachments = new ArrayList<PDFByteAttachment>();

		RandomAccessFileOrArray inputFileRandomAccess = null;

		try {
			inputFileRandomAccess = new RandomAccessFileOrArray(pdfFile.getAbsolutePath(), false, false);

			PdfReader reader = new PdfReader(inputFileRandomAccess, null);
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

						@SuppressWarnings("unchecked")
						Set<PdfName> keys = references.getKeys();

						HashSet<String> visitedReferences = new HashSet<String>(keys.size());

						for (PdfName key : keys) {

							String keyValue = key.toString();

							if ("/UF".equals(keyValue) || "/F".equals(keyValue)) {

								PdfIndirectReference reference = references.getAsIndirectObject(key);

								if (!visitedReferences.contains(reference.toString())) {

									visitedReferences.add(reference.toString());

									String filename = fileSpec.getAsString(key).toString();

									PRStream stream = (PRStream) PdfReader.getPdfObject(references.getAsIndirectObject(key));

									attachments.add(new PDFByteAttachment(attachmentName, filename, PdfReader.getStreamBytes(stream)));
								}
							}
						}
					}
				}
			}
		} finally {

			if (inputFileRandomAccess != null) {

				try {
					inputFileRandomAccess.close();
				} catch (IOException e) {}
			}
		}

		return attachments;
	}

	// http://developers.itextpdf.com/question/how-delete-attachments-pdf-using-itext
	public static byte[] removeAttachments(File pdfFile) throws IOException, DocumentException {

		RandomAccessFileOrArray inputFileRandomAccess = null;

		try {
			inputFileRandomAccess = new RandomAccessFileOrArray(pdfFile.getAbsolutePath(), false, false);

			PdfReader reader = new PdfReader(inputFileRandomAccess, null);
			PdfDictionary root = reader.getCatalog();
			PdfDictionary names = root.getAsDict(PdfName.NAMES);

			if (names != null && names.contains(PdfName.EMBEDDEDFILES)) {

				names.remove(PdfName.EMBEDDEDFILES);
				reader.removeUnusedObjects();

				ByteArrayOutputStream buffer = new ByteArrayOutputStream(32 * BinarySizes.KiloByte);

				PdfStamper stamper = new PdfStamper(reader, buffer);
				stamper.close();

				return buffer.toByteArray();
			}
			
			return FileUtils.getRawBytes(pdfFile);
			
		} finally {

			if (inputFileRandomAccess != null) {

				try {
					inputFileRandomAccess.close();
				} catch (IOException e) {}
			}
		}
	}

}
