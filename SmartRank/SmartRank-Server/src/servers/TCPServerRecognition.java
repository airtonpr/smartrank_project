package servers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import utils.FaceRecognitionUtils;
import utils.RabbitMQUtils;
import utils.RandomString;
import utils.RecognitionResult;
import utils.Utils;
import utils.Zipper;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class TCPServerRecognition implements Runnable {

	private static final String ROOT_PATH = "smartrank/";
	private static final String RECOG_DIR_HOME = ROOT_PATH + "facerecognition/";
	public static String RECEIVED_IMAGES_PATH = RECOG_DIR_HOME + "received_images/";
	public static String PATTERN_DATA_PATH = RECOG_DIR_HOME + "pattern_data/";
	protected String IMAGES_LIST_FOR_RECOGNITION_FILE = RECEIVED_IMAGES_PATH + "imagesList.txt";
	public static String DETECTED_IMAGES_DIR = RECOG_DIR_HOME + "detected_images/";
	public static String TRAINING_DATABASE_IMAGE_LIST = PATTERN_DATA_PATH + "facedata.xml";
	protected String ImageNameAllPath;
	FaceRecognitionUtils faceRecognition;
	public static int IMG_WIDTH = 260;
	public static int IMG_HEIGHT = 360;
	private RabbitMQUtils rabbitMQUtils = new RabbitMQUtils();
	
	private String name;
	
	public static void main(String[] args) {
		new Thread(new TCPServerRecognition("server")).start();
	}

	
	public TCPServerRecognition(String name) {
		faceRecognition = new FaceRecognitionUtils();
		this.name = name + "-recognition";
	}
	
	private void startRPCServer() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		System.out.println("SERVER_RECOG - Recognition Asynchronous Server started...");
		
		rabbitMQUtils.connect();

		rabbitMQUtils.getChannel().queueDeclare(name, false, false, false, null);

		rabbitMQUtils.getChannel().basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(rabbitMQUtils.getChannel());
		rabbitMQUtils.getChannel().basicConsume(name, false, consumer);
		
		while(true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();

		    BasicProperties props = delivery.getProperties();
		    BasicProperties replyProps = new BasicProperties
		                                     .Builder()
		                                     .correlationId(props.getCorrelationId())
		                                     .build();

		    String zipFileName = new RandomString(5).nextString() + ".zip";
			Utils.writeFileOnDisc(delivery.getBody(), RECEIVED_IMAGES_PATH, zipFileName);

			//Descompactar
			ArrayList<String> imageNames = Zipper.unzip(RECEIVED_IMAGES_PATH, zipFileName);
			
			createFileListOfImagesDetected(imageNames);

			List<RecognitionResult> results = faceRecognition.recognizeFileList(IMAGES_LIST_FOR_RECOGNITION_FILE);
			
			RecognitionResult recognitionResult = results.get(0);
			
			String response = new String("Id=" + recognitionResult.getFaceIndex()
					+ "\nLsd=" + recognitionResult.getLeast_squared_distance() + "\nConf.="
					+ recognitionResult.getConfidence() + "\n\n");
			
			rabbitMQUtils.getChannel().basicPublish( "", props.getReplyTo(), replyProps, response.getBytes());

			rabbitMQUtils.getChannel().basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
			cleanDirContent(DETECTED_IMAGES_DIR);
			cleanDirContent(RECEIVED_IMAGES_PATH);
		}
	}
	
	private static void cleanDirContent(String dir){
		File folder = new File(dir);  
		if (folder.isDirectory()) {  
		    File[] sun = folder.listFiles();  
		    for (File toDelete : sun) {  
		        toDelete.delete();  
		    }  
		}  
	}

	// For while putting only one file
	private void createFileListOfImagesDetected(ArrayList<String> imageNames)
			throws IOException, FileNotFoundException {
		File file = new File(IMAGES_LIST_FOR_RECOGNITION_FILE);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			new RandomAccessFile(file, "rws").setLength(0);
		}
		FileOutputStream out = new FileOutputStream(file, true);

		File dir = new File(DETECTED_IMAGES_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		for (String imageName : imageNames) {

			if (!imageName.isEmpty()) {

				out.write((1 + " " + imageName + " " + RECEIVED_IMAGES_PATH
						+ imageName + "\n").getBytes());

			}
		}
		out.flush();
		out.close();
	}

	@Override
	public void run() {
//		startServer();
		try {
			startRPCServer();
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
