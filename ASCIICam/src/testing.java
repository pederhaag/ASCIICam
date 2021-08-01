import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class testing {

	public static void main(String[] args) {
		System.out.println("Hello");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		String input = "C:\\Users\\peder\\Pictures\\test.jpg";
		String greyed = "C:\\Users\\peder\\Pictures\\greyed.jpg";
		String output = "C:\\Users\\peder\\Pictures\\output.jpg";

		try {
			/**
			 * Convert inputimg to Mat
			 */
			BufferedImage inputImg = ImageIO.read(new File(input));
			byte[] inputData = ((DataBufferByte) inputImg.getRaster().getDataBuffer()).getData();
			Mat inputMat = new Mat();
			inputMat.put(0, 0, inputData);
			
			/**
			 * Greify
			 */
			Mat grayMat = ImageProcessing.grayify(inputMat);
			
			/**
			 * Create gray BufferedImage
			 */
			BufferedImage GrayBuff = Mat2BufferedImage(grayMat);
			
			File outputfile = new File(greyed);
			ImageIO.write(GrayBuff, "jpg", outputfile);

//
//			/**
//			 * Create gray BufferedImage
//			 */
//			BufferedImage GrayBuff = new BufferedImage(inputImg.getWidth(), inputImg.getHeight(),
//					BufferedImage.TYPE_BYTE_GRAY);
//			byte[] graydata = ((DataBufferByte) GrayBuff.getRaster().getDataBuffer()).getData();
//			Mat Gray = new Mat();
//			rgba.get(0, 0, graydata);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	static BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {        
	    MatOfByte mob=new MatOfByte();
	    Imgcodecs.imencode(".jpg", matrix, mob);
	    byte ba[]=mob.toArray();

	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
	    return bi;
	}
}
