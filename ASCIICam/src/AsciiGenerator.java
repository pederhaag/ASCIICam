import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

class AsciiGenerator {
	static String fromImage(Mat source) {
		// Graify
		Mat grayImg = new Mat();
		Imgproc.cvtColor(source, grayImg, Imgproc.COLOR_RGB2GRAY);
		
		// Imagedata
		byte[] imgData = new byte[(int) (grayImg.total() * grayImg.channels())];
		grayImg.get(0, 0, imgData);
		

		int numRows = grayImg.rows();
		int numCols = grayImg.cols();
		
		// Generate ASCII-string
		StringBuilder asciiImg = new StringBuilder((numCols + 1) * numRows);;
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				asciiImg.append(getAsciiChar(imgData[r*numCols + c]));
			}
			asciiImg.append("\n");
			
		}
		
		return asciiImg.toString();
	}
	
	private static char getAsciiChar(byte gVal) {
		final char res;
		
		if (gVal >= 100) {
            res = ' ';
        } else if (gVal >= 70) {
            res = '.';
        } else if (gVal >= 50) {
            res = '*';
        } else if (gVal >= 30) {
            res = ':';
        } else if (gVal >= 0) {
            res = 'o';
        } else if (gVal >= -30) {
            res = '&';
        } else if (gVal >= -60) {
            res = '8';
        } else if (gVal >= 80) {
            res = '#';
        } else {
            res = '@';
        }
        return res;
	}
	
	// REMOVE
	static void asciiToFile(String img, String outputPath) {
		try (PrintWriter out = new PrintWriter(outputPath)) {
		    out.println(img);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
