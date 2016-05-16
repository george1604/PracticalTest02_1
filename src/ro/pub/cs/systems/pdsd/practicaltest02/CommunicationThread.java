package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.general.Utilities;

public class CommunicationThread extends Thread {
	
	private Socket       socket;
	
	public CommunicationThread(Socket socket) {
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String prefix            = bufferedReader.readLine();
					if (prefix != null && !prefix.isEmpty()) {
							String res = "";
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpPost = new HttpGet(Constants.WEB_SERVICE_ADDRESS 
									+ "?query=" + prefix);
							HttpResponse httpGetResponse = httpClient.execute(httpPost);
							  HttpEntity httpGetEntity = httpGetResponse.getEntity();
							  if (httpGetEntity != null) {  
								  String scriptData = EntityUtils.toString(httpGetEntity);
							    // do something with the response

							    JSONObject content = new JSONObject(scriptData);
							    JSONArray arr = content.getJSONArray("RESULTS");
							    
							    for (int i = 0; i < arr.length(); i++) {
							    	  res = res + "," + arr.getJSONObject(i).getString("name");
							    }
							    printWriter.println(res);
								printWriter.flush();
							  }
							  
							
							
//							httpPost.setEntity(urlEncodedFormEntity);
//							ResponseHandler<String> responseHandler = new BasicResponseHandler();
//							String pageSourceCode = httpClient.execute(httpPost, responseHandler);
//							if (pageSourceCode != null) {
//								Document document = Jsoup.parse(pageSourceCode);
//								Element element = document.child(0);
//								Elements scripts = element.getElementsByTag(Constants.SCRIPT_TAG);
//								for (Element script: scripts) {
//									
//									String scriptData = script.data();
//									
//									if (scriptData.contains(Constants.SEARCH_KEY)) {
//										int position = scriptData.indexOf(Constants.SEARCH_KEY) + Constants.SEARCH_KEY.length();
//										scriptData = scriptData.substring(position);
//										
//										JSONObject content = new JSONObject(scriptData);
//										
//										JSONObject currentObservation = content.getJSONObject(Constants.CURRENT_OBSERVATION);
//										String temperature = currentObservation.getString(Constants.TEMPERATURE);
//										String windSpeed = currentObservation.getString(Constants.WIND_SPEED);
//										String condition = currentObservation.getString(Constants.CONDITION);
//										String pressure = currentObservation.getString(Constants.PRESSURE);
//										String humidity = currentObservation.getString(Constants.HUMIDITY);
//										
//										weatherForecastInformation = new WeatherForecastInformation(
//												temperature,
//												windSpeed,
//												condition,
//												pressure,
//												humidity);
//
//										serverThread.setData(city, weatherForecastInformation);
//										break;
//									}
//								}
//							} else {
//								Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
//							}
						}
						
					} else {
						Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)!");
					}
				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}