package com.example.dlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.google.cloud.ServiceOptions;
import com.google.cloud.dlp.v2.DlpServiceClient;
import com.google.privacy.dlp.v2.ByteContentItem;
import com.google.privacy.dlp.v2.ContentItem;
import com.google.privacy.dlp.v2.Finding;
import com.google.privacy.dlp.v2.InfoType;
import com.google.privacy.dlp.v2.InspectConfig;
import com.google.privacy.dlp.v2.InspectConfig.FindingLimits;
import com.google.privacy.dlp.v2.InspectContentRequest;
import com.google.privacy.dlp.v2.InspectContentResponse;
import com.google.privacy.dlp.v2.Likelihood;
import com.google.privacy.dlp.v2.ProjectName;
import com.google.protobuf.ByteString;

@WebServlet(name = "Scan Data", urlPatterns = { "/inspect","/MyP/scanDlp" })
public class InspectServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String ALL_BASIC = "ALL_BASIC";
	private static final String LIKELY= "LIKELY";
	private static final String VERY_LIKELY= "VERY_LIKELY";
	private static final String ZERO = "0";

	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp)
	      throws IOException, ServletException {
		
			List<String> sensitiveList = new ArrayList<String>();
	    	 String string =req.getParameter("message");
	         Likelihood minLikelihood =   Likelihood.valueOf(Likelihood.LIKELIHOOD_UNSPECIFIED.name());
	         int maxFindings =Integer.parseInt(ZERO);
	         boolean includeQuote = true;
	         List<InfoType> infoTypes = new ArrayList<InfoType>();
	         infoTypes.add(InfoType.newBuilder().setName(ALL_BASIC).build());
	         String projectId = ServiceOptions.getDefaultProjectId();
	    	
	          PrintWriter out = resp.getWriter();
	    	  
	    		  try (DlpServiceClient dlpServiceClient = DlpServiceClient.create()) {
	    		      FindingLimits findingLimits = FindingLimits.newBuilder().setMaxFindingsPerRequest(maxFindings).build();
	    		   
					InspectConfig inspectConfig = InspectConfig.newBuilder() .addAllInfoTypes(infoTypes).setMinLikelihood(minLikelihood).setLimits(findingLimits).setIncludeQuote(includeQuote).build();

	    		    ByteContentItem byteContentItem = ByteContentItem.newBuilder() .setType(ByteContentItem.BytesType.TEXT_UTF8).setData(ByteString.copyFromUtf8(string)).build();

	    		    ContentItem contentItem = ContentItem.newBuilder().setByteItem(byteContentItem).build();

	    		    InspectContentRequest request = InspectContentRequest.newBuilder().setParent(ProjectName.of(projectId).toString()).setInspectConfig(inspectConfig).setItem(contentItem).build();
	    		    InspectContentResponse response = dlpServiceClient.inspectContent(request);
	    		    
	    		   // Finding finding = response.getResult().getFindingsList().get(0);
	    		    List<Finding> findingList = response.getResult().getFindingsList();
			if (response.getResult().getFindingsCount() > 0) {

				for (Finding finding : findingList) {
					if (finding.getLikelihood().toString().equals(LIKELY)
							|| finding.getLikelihood().toString().equals(VERY_LIKELY)) {
						System.out.println("Quote: " + finding.getQuote());
						System.out.println("Info type: " + finding.getInfoType().getName());
						System.out.println("Likelihood: " + finding.getLikelihood());
						sensitiveList.add(finding.getQuote().toString());
					
					}
				}
				
				System.out.println("Sensitive List: " + sensitiveList);
				//req.setAttribute("sensitiveList", sensitiveList);
				//req.getRequestDispatcher("/deidentify").forward(req, resp);
				String deidentifiedRes =DeIdentification.deIdentifyWithMask(string, dlpServiceClient, infoTypes, projectId);
				resp.setContentType("application/json");
				out.println("{\"response\":\" " +deidentifiedRes +"\"}");
				
			}

			else {
				out.println("No Finding");
			}
		}
	    		  
	    catch(Exception e)
	{
		resp.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}

}}