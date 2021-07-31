import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class ImageProcessing {

	static Mat readImage(String file) {
		return Imgcodecs.imread(file);
	}

	static Mat grayify(Mat src) {
		Mat dst = new Mat();
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
//		Core.normalize(dst, dst, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);
		return dst;
	}

	static Mat resize(Mat src, Size size) {
		Mat dst = new Mat();
		Imgproc.resize(src, dst, size);
		return dst;
	}

	static Mat resize(Mat src, double width, double height) {
		return resize(src, new Size(width, height));
	}

	static String[] toAscii(Mat grayImg, Set<Entry<Integer, Character>> map) {
		// Image data
		byte[] imgData = new byte[(int) (grayImg.total())];
		grayImg.get(0, 0, imgData);

		int numRows = grayImg.rows();
		int numCols = grayImg.cols();

		// Generate ASCII-string
		StringBuilder stringbuilder;
		String[] asciiImg = new String[numRows];
		for (int r = 0; r < numRows; r++) {
			stringbuilder = new StringBuilder(numCols + 1);
			for (int c = 0; c < numCols; c++) {
				stringbuilder.append(getAsciiChar(map, imgData[r * numCols + c]));
			}
			asciiImg[r] = stringbuilder.toString();

		}

		return asciiImg;
	}

	static char getAsciiChar(Set<Entry<Integer, Character>> map, byte gVal) {
		Iterator<Entry<Integer, Character>> i = map.iterator();
		Map.Entry<Integer, Character> translate;
		while (i.hasNext()) {
			translate = i.next();
			if (gVal >= translate.getKey()) {
				return translate.getValue();
			}
		}
		// Default
		return '@';
	}

	// TODO: REMOVE
	static void asciiToFile(String[] img, String outputPath) {
		try (PrintWriter out = new PrintWriter(outputPath)) {
			for (String s : img) {
				out.println(s + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
