/**
 * @ (#) CurrencyConverterClient.java 15th Aug 2018
 * Project 	     : Vidal Health Care
 * File          : CurrencyConverterClient.java
 * Author        : Kishor kumar S H
 * Company       : Vidal Health Care
 * Date Created  : 15th Aug 2018
 *
 * @author       :  Kishor kumar S H
 * Modified by   :  
 * Modified date :  
 * Reason        :  
 */

package com.ttk.common;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
public class CurrencyConverterClient {
	
	
	public Map<String,Object> getCurrencyData() throws JsonParseException, JsonMappingException, IOException, ParseException
		{
			Client client	=	Client.create();
			//WebResource resource		= client.resource("http://data.fixer.io/api/latest?access_key=20ba30145cd8ae4ba6bd9f691fcc2d2f");
			//WebResource resource		= client.resource("http://openexchangerates.org/api/latest.json?app_id=8bb047904a5f4386860fd3b1eb443202&base=USD");//Trial version Key
			WebResource resource		= client.resource("http://openexchangerates.org/api/latest.json?app_id=5a4711d2dd794456a69a1288b436bacf&base=QAR");//Subscription key
			String jsonData	=	resource.get(String.class);
			//System.out.println(jsonData);
			
			
			//PLAIN REST WEBSERVICE CONNECT TO RESOURCE
			//CHECK ALL NETWORK CONDITIONS - BUT IT WILL TAKE 2 HITS TO SITE
			/*URL url	=	new URL("http://data.fixer.io/api/latest?access_key=20ba30145cd8ae4ba6bd9f691fcc2d2f");//CREATE CONNECTION
			HttpURLConnection conn	=	(HttpURLConnection) url.openConnection();//OPEN CONNECTION
			conn.setRequestMethod("GET");//DECIDE WHICH METHOD TO USE GET,POST..
			int responseCode	=	conn.getResponseCode();//GET THE RESPONSE CODE
			String jsonData	=	new String();
			System.out.println(responseCode);
			if(responseCode != 200)
				throw new RuntimeException("HttpResponseCode::"+responseCode);
			else{
				java.util.Scanner sc	=	new java.util.Scanner(url.openStream());//new java.util.Scanner(url.openStream());//USING SCANNER TO READ NECT LINE FROM REST OUTPUT
				while(sc.hasNext())
					jsonData+=sc.nextLine();
				sc.close();
			}*/
	
		/*System.out.println("\nJSON data in string format");
		System.out.println(jsonData);*/
	
	
		//PARSING JSON
		JSONParser jsonParser	=	new JSONParser();//1 . CREATE JSO PARSE
		JSONObject jsonObject	=	(JSONObject) jsonParser.parse(jsonData);// 2. CONVERT TO JSON OBJECTS 
	
		Map<String,Object> result = new ObjectMapper().readValue(jsonObject.get("rates").toString(), HashMap.class);
		
		/*org.json.JSONArray jsonArray	=	(org.json.JSONArray) jsonObject.get("rates");//IDENTIFY THE ARRAY DEOM REST OUTPUT
		//GET DATA FROM RESULTS ARRAY - we are not using array in this
		for(int k=0;k<jsonArray.length() ;k++)
		{
			JSONObject object	=	(JSONObject) jsonArray.get(k);
			System.out.println("Elements are::\n");
			System.out.println("\n place id::"+object.get("place_id"));
			System.out.println("\n Types::"+object.get("types"));
		}*/
	
		return result;
	}
	
}
