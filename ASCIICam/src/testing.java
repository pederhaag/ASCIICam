import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class testing {

	public static void main(String[] args) {
		System.out.println("Hello");
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );  

     
		//Reading the Image from the file  
		String file ="C:\\Users\\peder\\Pictures\\test.jpg"; 
		Mat src = Imgcodecs.imread(file); 
		System.out.println(src);
		Mat dst = new Mat();
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY); 
		System.out.println(dst);
		
		String asciiImg = AsciiGenerator.fromImage(src);
		
		AsciiGenerator.asciiToFile(asciiImg, "C:\\Users\\peder\\Pictures\\ascii.txt");
//		int type = dst.type();
//		int channels = dst.channels();
//		Long totalNumBytes = dst.total();
//		
//		// Populate data into byte-array
//		byte[] imgData = new byte[(int) (dst.total() * dst.channels())];
//		dst.get(0, 0, imgData);
//		int numRows = dst.rows();
//		int numCols = dst.cols();
//
//		StringBuilder asciiImg = new StringBuilder((numCols + 1) * numRows);;
//		for (int r = 0; r < numRows; r++) {
//			System.out.println(String.format("row = %d/%d", r, numRows));
//			for (int c = 0; c < numCols; c++) {
//				asciiImg.append(getAsciiChar(imgData[r*numCols + c]));
//			}
//			asciiImg.append("\n");
//			
//		}
		
//		String asciiImg = "";
//		for (int r = 0; r < numRows; r++) {
//			System.out.println(String.format("row = %d/%d", r, numRows));
//			for (int c = 0; c < numCols; c++) {
//				asciiImg += getAsciiString(imgData[r*numCols + c]);
//			}
//			asciiImg += "\n";
//			
//		}
		HighGui.imshow("Image", dst);
		HighGui.waitKey();

	}
	
	private static char getAsciiChar(byte gVal) {
//		if (gVal < 0) {
//			return "@";
//		} else {
//			return ".";
//		}
		final char res;
		
		if (gVal >= 230.0) {
            res = ' ';
        } else if (gVal >= 200.0) {
            res = '.';
        } else if (gVal >= 180.0) {
            res = '*';
        } else if (gVal >= 160.0) {
            res = ':';
        } else if (gVal >= 130.0) {
            res = 'o';
        } else if (gVal >= 100.0) {
            res = '&';
        } else if (gVal >= 70.0) {
            res = '8';
        } else if (gVal >= 50.0) {
            res = '#';
        } else {
            res = '@';
        }
        return res;
	}
	
	private static void asciiToFile(String img, String outputPath) {
		try (PrintWriter out = new PrintWriter(outputPath)) {
		    out.println(img);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
