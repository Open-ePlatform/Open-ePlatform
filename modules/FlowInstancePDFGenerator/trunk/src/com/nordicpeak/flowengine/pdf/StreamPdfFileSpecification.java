package com.nordicpeak.flowengine.pdf;

import java.io.IOException;
import java.io.InputStream;

import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.mime.MimeUtils;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfStream;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;



public class StreamPdfFileSpecification extends PdfFileSpecification {

	public static PdfFileSpecification fileEmbedded(PdfWriter writer, InputStream inputStream, String filename) throws IOException {

		StreamPdfFileSpecification streamPdfFileSpecification = new StreamPdfFileSpecification();
		streamPdfFileSpecification.writer = writer;
		streamPdfFileSpecification.put(PdfName.F, new PdfString(filename));
		streamPdfFileSpecification.setUnicodeFileName(filename, false);

		String mimetype = MimeUtils.getMimeType(filename);

		if (mimetype.equals(MimeUtils.UNKNOWN_MIME_TYPE)) {
			mimetype = "application/octet-stream";
		}

		PdfStream embeddedFileStream;
		PdfIndirectReference streamRef;
		PdfIndirectReference refFileLength;

		try {
			refFileLength = writer.getPdfIndirectReference();

			embeddedFileStream = new PdfStream(inputStream, writer);

			embeddedFileStream.put(PdfName.TYPE, PdfName.EMBEDDEDFILE);
			embeddedFileStream.put(PdfName.SUBTYPE, new PdfName(mimetype));
			embeddedFileStream.flateCompress();
			embeddedFileStream.put(PdfName.PARAMS, refFileLength);

			streamRef = writer.addToBody(embeddedFileStream).getIndirectReference();
			embeddedFileStream.writeLength();

			PdfDictionary fileSpecificParams = new PdfDictionary();
			fileSpecificParams.put(PdfName.SIZE, new PdfNumber(embeddedFileStream.getRawLength()));

			writer.addToBody(fileSpecificParams, refFileLength);

		} finally {

			CloseUtils.close(inputStream);
		}

		PdfDictionary embeddedFilesDictionary = new PdfDictionary();
		embeddedFilesDictionary.put(PdfName.F, streamRef);
		embeddedFilesDictionary.put(PdfName.UF, streamRef);

		streamPdfFileSpecification.put(PdfName.EF, embeddedFilesDictionary);

		return streamPdfFileSpecification;
	}
}
