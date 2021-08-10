package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * The {@code ImageProcessing} class is a class containing only static methods.
 * The purpose of the class is to be a simple container for some image-related
 * tasks used elsewhere in the program.
 * 
 */
class ImageProcessing {

	/**
	 * Read a image from a path and convert it to a {@code Mat}-object
	 * 
	 * @param file an {@code String} representing the location of the file to be
	 *             read
	 * @return {@code Mat}-object representing the input file
	 */
	static Mat readImage(String file) {
		return Imgcodecs.imread(file);
	}

	/**
	 * Convert a {@code Mat}-object based on a image to grayscale
	 * 
	 * @param src a {@code Mat}-object to be converted
	 * @return a new {@code Mat}-object representing a grayscale-version of the
	 *         input
	 */
	static Mat grayify(Mat src) {
		Mat dst = new Mat();
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
		return dst;
	}

	/**
	 * Resize a {@code Mat}-object based on a image
	 * 
	 * @param src  input to be resized
	 * @param size desired output size
	 * @return a new {@code Mat}-object based on {@code src} with desired size
	 */
	static Mat resize(Mat src, Size size) {
		Mat dst = new Mat();
		Imgproc.resize(src, dst, size);
		return dst;
	}

	/**
	 * Resize a {@code Mat}-object based on a image
	 * 
	 * @param src    input to be resized
	 * @param width  desired width of output
	 * @param height desired height of output
	 * @return a new {@code Mat}-object based on {@code src} with desired size
	 */
	static Mat resize(Mat src, double width, double height) {
		return resize(src, new Size(width, height));
	}

	/**
	 * Convert a greyscale based {@code Mat} into an ascii-representation.
	 * 
	 * @param grayImg image to be converted
	 * @param map     the mapping which shall be used to evaluate each pixel
	 * @return a {@code String} array where entry {@code i} is a {@code String}
	 *         representation of a row {@code i}
	 */
	static String[] toAscii(Mat grayImg, Set<Entry<Integer, Character>> map) {
		// TODO: Throw error if grayImg.channels() != 1
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

	/**
	 * Function for evaluating a pixel based of a mapping and outputting
	 * corresponding ascii-char
	 * 
	 * @param map  to be used for evaluation
	 * @param gVal value of the pixel
	 * @return the corresponding ascii-char
	 */
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

	/**
	 * Write a ascii-image to a desired path. Any exisisting file will be
	 * overwritten
	 * 
	 * @param img        ascii-image to be written to file
	 * @param outputPath filepath to write to
	 */
	static void asciiToFile(String[] img, String outputPath) {
		try (PrintWriter out = new PrintWriter(outputPath)) {
			for (String s : img) {
				out.println(s + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
