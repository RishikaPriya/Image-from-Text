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

/**
 * Servlet implementation class FileUploadServlet
 */
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
			System.out.println(fileName);
			part.write(uploadFilePath + File.separator + fileName);
			System.out.println(uploadFilePath + File.separator + fileName);
		}
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

}
