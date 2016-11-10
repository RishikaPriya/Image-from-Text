package com.servlet;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import com.asprise.ocr.Ocr;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/FileUploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*10,
				maxFileSize=1024*1024*50,
				maxRequestSize=1024*1024*100)
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIR = "uploads";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String applicationPath = request.getServletContext().getRealPath("");
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
		System.out.println(uploadFilePath);
		File fileSaveDir= new File(uploadFilePath);
		if(!fileSaveDir.exists()){
			fileSaveDir.mkdirs();
		}
		System.out.println("Upload file directory = "+fileSaveDir.getAbsolutePath());
		String fileName = null;
		for(Part part:request.getParts()){
			fileName = getFileName(part);
			System.out.println(part);
			part.write(uploadFilePath + File.separator + fileName);
			System.out.println(uploadFilePath + File.separator + fileName);
			ocr(uploadFilePath + File.separator + fileName);
		//	ocr();
		}
		System.out.println("Uploaded file" + fileName);
		request.setAttribute("message", fileName + " file uploaded successfully!");
		getServletContext().getRequestDispatcher("/response.jsp").forward(request, response);
	}
	
	private String getFileName(Part part){
		String contentDisp = part.getHeader("content-disposition");
		System.out.println("content-disposition header=" + contentDisp);
		String[] tokens = contentDisp.split(";");
		for(String token : tokens){
			if(token.trim().startsWith("filename")){
				String file = token.substring(token.indexOf("=") + 2,token.length()-1);
				String[] fileNameArray = file.split("/");
				int index = fileNameArray.length;
				return fileNameArray[index - 1];
				
			}
		}
		return "";
	}
	
	private void ocr(String filename){
		/*// Tesseract
				BytePointer outText;

				TessBaseAPI api = new TessBaseAPI();
				// Initialize tesseract-ocr with English, without specifying tessdata
				// path
				if (api.Init(".", "ENG") != 0) {
					System.err.println("Could not initialize tesseract.");
					System.exit(1);
				}

				// Open input image with leptonica library
				PIX image = pixRead(filename);
				api.SetImage(image);
				// Get OCR result
				outText = api.GetUTF8Text();
				String string = outText.getString();
				//assertTrue(!string.isEmpty());
				System.out.println("OCR output:\n" + string);

				// Destroy used object and release memory
				api.End();
				outText.deallocate();
				pixDestroy(image);*/
		 
		 	//Asprise Code
		 
		  Ocr.setUp(); 
		  Ocr ocr = new Ocr(); 
		  ocr.startEngine("eng",Ocr.SPEED_FASTEST);
		  String s = ocr.recognize(new File[] {new File(filename)}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT); 
		  System.out.println(s);
		  ocr.stopEngine();
		 
	}

}
