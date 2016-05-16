package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.TextView;
import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.general.Utilities;

public class ClientThread extends Thread {
	
	private int      port;
	private String   prefix;	
	private Socket   socket;
	private TextView tv;
	
	public ClientThread(
			int port,
			String prefix,
			TextView tv) {
		this.port                    = port;
		this.prefix                    = prefix;
		this.tv = tv;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(Constants.SERVER_HOST, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(prefix);
				printWriter.flush();
				String weatherInformation;
				while ((weatherInformation = bufferedReader.readLine()) != null) {
					Log.e(Constants.TAG, "[CLIENT THREAD]" + weatherInformation);
					final String finalizedWeatherInformation = weatherInformation;
					tv.post(new Runnable() {
						@Override
						public void run() {
							tv.append(finalizedWeatherInformation + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}

}