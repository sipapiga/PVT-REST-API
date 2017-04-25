package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.ActivityMuseum;

/**
 * Gathers data from API's and saves them to the database.
 */
public class ActivityGatherer {
	
	private final String API_KEY="key=AIzaSyDSNr7q3oRHvttkSfK85MYQnN3DSoRg_tg";
	private final String API_URL="https://maps.googleapis.com/maps/api/place/textsearch/json?";
	private final String PLACE_TYPE="type=museum";
	private final String QUERY="query=\"Stockholm\"";
    /**
     * Executes gathering of data.
     */
    public void gather() {
    	jsonParseJackson(gatherAPIData());
    }
    
    private String gatherAPIData(){
		System.out.println("In gatherAPIData");
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(API_URL+QUERY+"&"+API_KEY+"&"+PLACE_TYPE);//takes the urlstring and creates an URL with the APIkey appended to it
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream())); //reads all data from the url to a reader
			String line;
			while ((line = bufferedReader.readLine()) != null) //appending every line of data that the reader has gathered to a string.
				content.append(line);
			bufferedReader.close();
		} catch (Exception e){}
		return content.toString(); 
	}
    
    
    
    private void jsonParseJackson(String jsonString){
    	ObjectMapper mapper = new ObjectMapper();
    	
    	try{
            ActivityMuseum activityMuseum= mapper.readValue(jsonString, ActivityMuseum.class);
            System.out.println(activityMuseum);
            
            activityMuseum.save();
         }
         catch (JsonParseException e) { e.printStackTrace();}
         catch (JsonMappingException e) { e.printStackTrace(); }
         catch (IOException e) { e.printStackTrace(); }
    	}
    }
