package com.iweb2b.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

public class PDFMergeExample {

	public static String mergeFiles (List<String> inputPdfList) throws DocumentException, IOException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream("merge-pdf-result.pdf"));

        document.open();
        for (String file : inputPdfList){
            PdfReader reader = new PdfReader(file);
            copy.addDocument(reader);
            copy.freeReader(reader);
            reader.close();
        }
        document.close();
        return "merge-pdf-result.pdf";
	}

    public static void main(String... args) throws IOException, DocumentException {
    	List<String> inputPdfList = new ArrayList<>();
    	inputPdfList.add("D:\\Murugavel\\Project\\7horses\\root\\IWB2B\\Code\\Java\\B2BWrapperService\\JTE000159567827.pdf");
    	inputPdfList.add("D:\\Murugavel\\Project\\7horses\\root\\IWB2B\\Code\\Java\\B2BWrapperService\\JTE000158012161.pdf");

        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream("merge-pdf-result.pdf"));

        document.open();
        for (String file : inputPdfList){
            PdfReader reader = new PdfReader(file);
            copy.addDocument(reader);
            copy.freeReader(reader);
            reader.close();
        }
        document.close();
    }
}