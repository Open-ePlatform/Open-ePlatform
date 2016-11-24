package com.nordicpeak.flowengine.pdf;
import java.io.IOException;
import java.io.InputStream;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfStream;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;



public class StreamPdfFileSpecification extends PdfFileSpecification {

	public static PdfFileSpecification fileEmbedded(PdfWriter writer, InputStream in, String fileDisplay) throws IOException {

		StreamPdfFileSpecification fs = new StreamPdfFileSpecification();
        fs.writer = writer;
        fs.put(PdfName.F, new PdfString(fileDisplay));
        fs.setUnicodeFileName(fileDisplay, false);
        PdfStream stream;
        PdfIndirectReference ref;
        PdfIndirectReference refFileLength;
        try {
            refFileLength = writer.getPdfIndirectReference();

            stream = new PdfStream(in, writer);
            
            stream.put(PdfName.TYPE, PdfName.EMBEDDEDFILE);
            stream.flateCompress();
            stream.put(PdfName.PARAMS, refFileLength);
            
            ref = writer.addToBody(stream).getIndirectReference();
            stream.writeLength();
            
            PdfDictionary params = new PdfDictionary();

            params.put(PdfName.SIZE, new PdfNumber(stream.getRawLength()));
            writer.addToBody(params, refFileLength);
        }
        finally {
            if (in != null) {
				try{in.close();}catch(Exception e){}
			}
        }
        PdfDictionary f = new PdfDictionary();
        f.put(PdfName.F, ref);
        f.put(PdfName.UF, ref);
        fs.put(PdfName.EF, f);
        return fs;
	}
}
