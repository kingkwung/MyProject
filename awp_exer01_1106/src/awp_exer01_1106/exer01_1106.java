package awp_exer01_1106;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class exer01_1106 extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(
				"<!DOCTYPE html>\n" +
			            "<html>\n" +
			            "<head><title>Exercise 9.1</title></head>\n" +
			            "<body><H2>Registration</H2>" + 
			            "<FORM ACTION='http://localhost:8089/myServlet/readForm'>" + 
			            "<TABLE border='0'>" + 
			            "<TR><TD>" + 
			            "<LABEL>First name : </LABEL></TD><TD><INPUT TYPE='TEXT' NAME='firstName'></TD></TR>" + 
			            "<TR><TD>" + 
			            "<LABEL>Last name : </LABEL></TD><TD><INPUT TYPE='TEXT' NAME='lastName'></TD></TR>" +
			            "<TR><TD>" + 
			            "<LABEL>E-mail : </LABEL></TD><TD><INPUT TYPE='TEXT' NAME='eMail'></TD></TR>" + 
			            "<TR><TD>" + 
			            "<LABEL>Phone number : </LABEL></TD><TD><INPUT TYPE='TEXT' NAME='phoneNum'></TD></TR>" + 
			            "</TABLE>" + 
			            "<INPUT TYPE='SUBMIT' VALUE='Register'>" +
			            "</FORM>\n" + "</body></html>"
				);

	}
}
