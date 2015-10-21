<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@page import="org.jsoup.Jsoup"%>
<%@page import="org.jsoup.nodes.Document"%>
<%@page import="org.jsoup.nodes.Element"%>
<%@page import="org.jsoup.select.Elements"%>
<%@page import="java.lang.*"%>
<%@ page import="java.io.IOException"%>
<%@page import="java.util.ArrayList"%>

<%@page import=" java.util.HashMap"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Term project EAST SOFT !</title>
</head>
<body>
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0-wip/css/bootstrap.min.css">
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0-wip/js/bootstrap.min.js"></script>
	<%
		Document doc;
	String str = "<h1>-EAST SOFT-</h1><table class='table'>";
	String newContent = "";
		ArrayList<String> data = new ArrayList<String>();
		try {
			doc = Jsoup.connect(
					"http://www.estsoft.co.kr/Default.aspx?wbs=5.0.3").get();
	
			Elements recruit = doc.select(".tbl_recruit tr");
			recruit.select("span").remove();
			recruit.select("a").removeAttr("href");
			recruit.select("td").removeAttr("class");
			recruit.select("div").removeAttr("style");
			//System.out.println(recruit);
			for(Element ele : recruit){
				str += "<tr>";
				newContent += ele;
				for(int i = 1; i < 4; i++){
					str += ele.child(i);
				}
				str += "</tr>";
			}
			str.replaceAll("class='subj'", "");
			str.replaceAll("amp;", "");
			str.replaceAll("./Default.","http://www.estsoft.co.kr/Default.");

			
			   if(!prevContent.equals(newContent)){
			      sendMail();
			      prevContent = newContent;
			   }
			
			
	str += "</table>";
		} catch (IOException e) {
			e.printStackTrace();
		}
	%>
	<%= str %>

	<!-- send email -->
	<%@page
		import="java.util.*, java.io.*, javax.mail.*, javax.mail.internet.*, javax.activation.*"%>
	<%!private static void sendMail() throws AddressException, MessagingException {
         Properties mailServerProperties;
         Session getMailSession;
         MimeMessage generateMailMessage;

         System.out.println("\n 1st ===> setup Mail Server Properties..");
         mailServerProperties = System.getProperties();
         mailServerProperties.put("mail.smtp.port", "587");
         mailServerProperties.put("mail.smtp.auth", "true");
         mailServerProperties.put("mail.smtp.starttls.enable", "true");
         System.out
               .println("Mail Server Properties have been setup successfully..");

         System.out.println("\n\n 2nd ===> get Mail Session..");
         getMailSession = Session.getDefaultInstance(mailServerProperties,
               null);
         generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO,
                  new InternetAddress("dndudska89@naver.com"));
         generateMailMessage.setSubject("Å×½ºÆ®");
         String emailBody = "The new recruitment in uploaded! please checking ! ";
         generateMailMessage.setContent(emailBody, "text/html");
         System.out.println("Mail Session has been created successfully..");

         System.out.println("\n\n 3rd ===> Get Session and Send mail");
         Transport transport = getMailSession.getTransport("smtp");

         // Enter your correct gmail UserID and Password (XXXarpitshah@gmail.com)
         transport.connect("smtp.gmail.com", "dndudska89@gmail.com",
               "qlqjsdi898");
         transport.sendMessage(generateMailMessage,
               generateMailMessage.getAllRecipients());
         transport.close();
   }%>

	<%! private String prevContent = ""; %>



</body>
</html>