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
		streamPdfFileSpecification.put(PdfName.SUBTYPE, new PdfName(MimeUtils.getMimeType(filename)));
		streamPdfFileSpecification.setUnicodeFileName(filename, false);
		
		PdfStream stream;
		PdfIndirectReference ref;
		PdfIndirectReference refFileLength;
		
		try {
			refFileLength = writer.getPdfIndirectReference();

			stream = new PdfStream(inputStream, writer);

			stream.put(PdfName.TYPE, PdfName.EMBEDDEDFILE);
			stream.flateCompress();
			stream.put(PdfName.PARAMS, refFileLength);

			ref = writer.addToBody(stream).getIndirectReference();
			stream.writeLength();

			PdfDictionary params = new PdfDictionary();

			params.put(PdfName.SIZE, new PdfNumber(stream.getRawLength()));
			writer.addToBody(params, refFileLength);
			
		} finally {
			
			CloseUtils.close(inputStream);
		}
		
		PdfDictionary pdfDictionary = new PdfDictionary();
		pdfDictionary.put(PdfName.F, ref);
		pdfDictionary.put(PdfName.UF, ref);
		
		streamPdfFileSpecification.put(PdfName.EF, pdfDictionary);
		
		return streamPdfFileSpecification;
	}
}
