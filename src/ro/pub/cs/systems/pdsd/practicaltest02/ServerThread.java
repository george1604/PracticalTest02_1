package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
import android.util.Log;

class ServerThread extends Thread {
		 
	    private boolean isRunning;
	    private Integer port;
	    private ServerSocket serverSocket;
	 
	    public void startServer(Integer port) {
	      isRunning = true;
	      this.port = port;
	      start();
	    }
	    
	    public void stopServer() {
	      isRunning = false;
	      new Thread(new Runnable() {
	        @Override
	        public void run() {
	          try {
	            if (serverSocket != null) {
	              serverSocket.close();
	            }
	            Log.v(Constants.TAG, "stopServer() method invoked "+serverSocket);
	          } catch(IOException ioException) {
	            Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
	            if (Constants.DEBUG) {
	              ioException.printStackTrace();
	            }
	          }
	        }
	      }).start();
	    }
	 
	    @Override
	    public void run() {
	      try {
	    	  
	    	  serverSocket = new ServerSocket(port);
	    	  Log.v(Constants.TAG, "Server running");
	        while (isRunning) {
	          Socket socket = serverSocket.accept();
	          Log.v(Constants.TAG, "Connection opened with "+socket.getInetAddress()+":"+socket.getLocalPort());
	          CommunicationThread communicationThread = new CommunicationThread(socket);
			  communicationThread.start();
	          Log.v(Constants.TAG, "Connection closed");
	        }
	      } catch (IOException ioException) {
	        Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
	        if (Constants.DEBUG) {
	          ioException.printStackTrace();
	        }
	      }
	    }
	  }