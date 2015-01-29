package servers.simplified_recognition_code;


import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;


/**
 * JPG File reader/writer. Uses native com.sun libraries (which
 * may deprecate at any time)
 *
 *
 * @author Konrad Rzeszutek
 * @version 1.0
 */
public class JPGFile implements xxxFile{

    private byte bytes[]=null;      // bytes which make up binary PPM image
    private double doubles[] = null;
    private String filename=null;     // filename for PPM image
    private int height = 0;
    private int width = 0;
    /**
     * Read the PPM File.
     *
     * @throws   FileNotFoundException   if the directory/image specified is wrong
     * @throws   IOException  if there are problems reading the file.
     */
    public JPGFile(String filename)  throws FileNotFoundException, IOException{
        this.filename = filename;
        readImage();
    }
    /**
     * Get the height of the PPM image.
     *
     * @return the height of the image.
     */
    public int getHeight() {
      return height;
    }
    /**
     * Get the width of the PPM image.
     *
     * @return  the width of the image.
     */
    public int getWidth() {
      return width;
    }
    /**
     * Get the data as byte array. Data is of any type that
     * has been read from the file (usually 8bit RGB)
     *
     * @return  The data of the image.
     */
    public byte[] getBytes() {
        return bytes;
    }
    /**
     * Get the data as double array. Data is of any type that
     * has been read from the file (usually 8bit RGB put into an 64bit double)
     *
     * @return  The data of the image.
     */
    public double[] getDouble() {
      return doubles;
    }

     /**
     * Write to <code>fn</code> file the <code>data</code> using the
     * <code>width, height</code> variables. Data is assumed to be 8bit RGB.
     *
     * @throws   FileNotFoundException   if the directory/image specified is wrong
     * @throws   IOException  if there are problems reading the file.
     */
    public static void writeImage(String fn, byte[] data, int width, int height)
    throws FileNotFoundException, IOException {

//        FileOutputStream fOut = new FileOutputStream(fn);
//        JPEGImageEncoder jpeg_encode = JPEGCodec.createJPEGEncoder(fOut);
//
//        int ints[] = new int[data.length];
//        for (int i = 0; i< data.length; i++)
//          ints[i] = 255 << 24 |
//            (int) (data[i] & 0xff) << 16 |
//            (int) (data[i] & 0xff) << 8 |
//            (int) (data[i] & 0xff);
//
//        BufferedImage image = new BufferedImage (width, height,BufferedImage.TYPE_INT_RGB);
//        image.setRGB(0,0,width,height,ints,0,width);
//
//        jpeg_encode.encode(image);
//        fOut.close();
    }
   /**
     * Read the image from the specified file.
     *
     * @throws  FileNotFoundException pretty obvious
     * @throws  IOException filesystem related problems
     */
    private void readImage()  throws FileNotFoundException, IOException {

//        FileInputStream fIn = new FileInputStream(filename);
//        JPEGImageDecoder jpeg_decode = JPEGCodec.createJPEGDecoder(fIn);
//        BufferedImage image = jpeg_decode.decodeAsBufferedImage();
        BufferedImage image = ImageIO.read(new File(filename));

        width = image.getWidth();
        height = image.getHeight();

        int[] rgbdata = new int[width * height];

        image.getRGB(0,0,width,height,rgbdata,0,width);


        bytes = new byte[rgbdata.length];
        doubles = new double[rgbdata.length];

        for (int i = 0; i < bytes.length; i++) {
          bytes[i]    = (byte)  (rgbdata[i] & 0xFF);
          doubles[i]  = (double)(rgbdata[i]);
        }
    }
}