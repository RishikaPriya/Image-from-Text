package com.ocr.tessOcr;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		/* 
		 * Asprise Code
		 * 
		 * Ocr.setUp(); Ocr ocr = new Ocr(); ocr.startEngine("eng",
		 * Ocr.SPEED_FASTEST); String s = ocr.recognize(new File[] {new
		 * File("SSN1.jpg")}, Ocr.RECOGNIZE_TYPE_ALL,
		 * Ocr.OUTPUT_FORMAT_PLAINTEXT); System.out.println(s);
		 * ocr.stopEngine();
		 */

		
		// Tesseract
		BytePointer outText;

		TessBaseAPI api = new TessBaseAPI();
		// Initialize tesseract-ocr with English, without specifying tessdata
		// path
		if (api.Init(".", "ENG") != 0) {
			System.err.println("Could not initialize tesseract.");
			System.exit(1);
		}

		// Open input image with leptonica library
		PIX image = pixRead("rishika_pancard.jpg");
		api.SetImage(image);
		// Get OCR result
		outText = api.GetUTF8Text();
		String string = outText.getString();
		//assertTrue(!string.isEmpty());
		System.out.println("OCR output:\n" + string);

		// Destroy used object and release memory
		api.End();
		outText.deallocate();
		pixDestroy(image);
    }
}
