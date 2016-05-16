package ro.pub.cs.systems.pdsd.practicaltest02;

import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class PracticalTest02MainActivity extends Activity {

	
	private EditText serverPort;
	private EditText clientAddr;
	private Button run;
	private Button send;
	private TextView tv;
	
	private ServerThread serverThread             = null;
	private ClientThread clientThread             = null;
    
    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
	private class ConnectButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String serverP = serverPort.getText().toString();
			if (serverP == null || serverP.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Server port should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			serverThread = new ServerThread();
			serverThread.startServer(Integer.parseInt(serverP));
		}
	}
	
	private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
	private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String clientPrefix = clientAddr.getText().toString();
			String clientPort    = serverPort.getText().toString();
			if (clientPrefix == null || clientPrefix.isEmpty() ||
				clientPort == null || clientPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Client connection parameters should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			if (serverThread == null || !serverThread.isAlive()) {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}
					
			clientThread = new ClientThread(
					Integer.parseInt(clientPort),
					clientPrefix,
					tv
			);
			clientThread.start();
		}
	}
    
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        
        serverPort = (EditText)findViewById(R.id.portserver);
		clientAddr = (EditText)findViewById(R.id.prefix);
		run = (Button)findViewById(R.id.buttonrun);
		send = (Button)findViewById(R.id.send);
		run.setOnClickListener(connectButtonClickListener);
		send.setOnClickListener(getWeatherForecastButtonClickListener);
		tv = (TextView)findViewById(R.id.textView1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.practical_test02_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
