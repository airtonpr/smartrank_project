package com.example.smartrank_clientsender;

import java.io.IOException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {


	private ImageView iv;
	private Button b;
	private TextView tv;
	private ManageFile manageFile;
	private String ROOT = Environment.getExternalStorageDirectory().toString()
			+ "/";
	private String SMARTRANK_DIR = "smartrank/";
	private String FACE_RECOG = ROOT + SMARTRANK_DIR + "facerecognition/";
	private String PATTERN_DIR = FACE_RECOG + "patter_images/";
	private String SCANNING_DIR = ROOT + SMARTRANK_DIR + "virusscanning/";
	private String ZIP_IMAGE_NAME = "WinRAR.png";
	private String IMAGE_NAME = "18b.JPEG";
	private String ZIP_FILE_NAME = "virusFolderToScan.zip";
	private String VIRUS_FOLDER_TO_SCAN = SCANNING_DIR + "virusFolderToScan/";
	private String IMAGE_NAME_WITH_ALL_PATH = PATTERN_DIR + IMAGE_NAME;
	private String ZIP_IMAGE_NAME_WITH_ALL_PATH = SCANNING_DIR + ZIP_IMAGE_NAME;
	private String ZIP_FILE_NAME_WITH_ALL_PATH = SCANNING_DIR + ZIP_FILE_NAME;

	private long startTime;
	private long endTime;
	private long totalTime;
	private boolean ISRECOGNITION = true;
	private String HOST = "192.168.1.153";// notebook // Do not forget to
											// update the ip at the
											// CloudletTransmitterAbstract too
	// private String HOST = "172.20.10.119";//notebook // Do not forget to
	// update the ip at the CloudletTransmitterAbstract too
	// private String HOST = "localhost";
	// private String HOST = "54.221.10.143";//amazon
	static int PORT_CLOUDLET_SERVER = 6790; // versao que tem o rabbit

	// static int PORT_CLOUDLET_SERVER = 123;
	// static int PORT_RECOG_SERVER = 123;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get the ImageView
		// display the image

		b = new Button(this);
		tv =new TextView(this);
		// iv.setBackgroundDrawable(getResources().getDrawable(R.drawable.group));
		if (ISRECOGNITION) {
			manageFile = new ManageFile(null, "Log-recognition.txt");
		} else {
			manageFile = new ManageFile(null, "Log-remote-virus-scan.txt");
		}

		// manageFile.clearFile();
		 sendConstantly();
		//sendOncePerTime();

	}

	private void sendOncePerTime() {
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				b.setClickable(false);
				tv.setText("Test");
				new DownloadAndSaveTask().execute("");
			}
		});
		// b.performClick();
	}

	private void sendConstantly() {
		for (int i = 0; i < 1; i++) {
			new DownloadAndSaveTask().execute("");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class DownloadAndSaveTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "No result";
			try {

				startTime = System.currentTimeMillis();

				TCPClientAndroid tcpClient = new TCPClientAndroid(HOST,
						PORT_CLOUDLET_SERVER);

				if (ISRECOGNITION) {
				       Log.d("print", "agora foi.");
				       System.out.println("test");
					result = tcpClient.sendPicture(IMAGE_NAME_WITH_ALL_PATH);
				} else {
					result = tcpClient.sendPicture(ZIP_FILE_NAME_WITH_ALL_PATH);
					System.out.println(ZIP_FILE_NAME_WITH_ALL_PATH);
				}

				endTime = System.currentTimeMillis();

				totalTime = endTime - startTime;
				manageFile.WriteFile(String.valueOf(totalTime));

				// deleteFile(ZIP_FILE_NAME_WITH_ALL_PATH);

			} catch (UnknownHostException e) {
				e.printStackTrace();
				return e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				return e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
			return String.valueOf(result);
		}

		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);

			b.setClickable(true);

			tv.setText("Result: \nTime: " + totalTime + "Ms\n\n" + result
					+ "\n");

			System.exit(0);
		} // end postexecute method

	}
}
