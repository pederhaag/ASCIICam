import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

public class testing {

	public static void main(String[] args) {
		System.out.println("Hello");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		test2();
	}

	static void test2() {
		Map<Integer, Character> map = getConfig1();
		loopOverEntrySet(map.entrySet());

//		Set<Entry<Integer, Character>> set = map.entrySet();
//		Iterator<Entry<Integer, Character>> i = set.iterator();
//
//		// Traverse map and print elements
//		while (i.hasNext()) {
//			Map.Entry<Integer, Character> me = i.next();
//			System.out.print(me.getKey() + ": ");
//			System.out.println(me.getValue());
//		}
	}

	static void loopOverEntrySet(Set<Entry<Integer, Character>> set) {
		Iterator<Entry<Integer, Character>> i = set.iterator();

		// Traverse map and print elements
		while (i.hasNext()) {
			Map.Entry<Integer, Character> me = i.next();
			System.out.print(me.getKey() + ": ");
			System.out.println(me.getValue());
		}
	}

	static Map<Integer, Character> getConfig1() {
		Map<Integer, Character> map = new TreeMap<Integer, Character>(Collections.reverseOrder());

		map.put(100, ' ');
		map.put(70, '.');
		map.put(50, '*');
		map.put(30, ':');
		map.put(0, 'o');
		map.put(-30, '&');
		map.put(-60, '8');
		map.put(-80, '#');
		map.put(-1000, '@');

		return map;
	}

	static void test1() {
//		 
//
//	     
//		//Reading the Image from the file  
//		String file ="C:\\Users\\peder\\Pictures\\test.jpg"; 
//		
////		Mat src = Imgcodecs.imread(file); 
////		System.out.println(src);
////		Mat dst = new Mat();
////		Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY); 
////		System.out.println(dst);
//
//		
//		Mat orgImage = ImageProcessing.readImage(file);
//		HighGui.imshow("orgImage", orgImage);
//		HighGui.waitKey();
//		Mat greyImage = ImageProcessing.grayify(orgImage);
//		Mat resizedImage = ImageProcessing.resize(greyImage, 100, 100);
//		HighGui.imshow("resizedImage", resizedImage);
//		HighGui.waitKey();
//		
//		String asciiImg = ImageProcessing.toAscii(resizedImage);
//		
//		String outputFile = "C:\\Users\\peder\\Pictures\\ascii.txt";
//		ImageProcessing.asciiToFile(asciiImg, outputFile);
//		
//
////		String asciiImg = ImageProcessing.toAscii(Imgcodecs.imread(file, Imgcodecs.IMREAD_GRAYSCALE));
////		
////		ImageProcessing.asciiToFile(asciiImg, "C:\\Users\\peder\\Pictures\\ascii.txt");
////		
////		HighGui.imshow("Image", dst);
////		HighGui.waitKey();
//
//		JFrame mainFrame = new JFrame("My test");
//		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		// Label
//        JLabel label = new JLabel(asciiImg);
//        mainFrame.getContentPane().add(label);
//		
//
//        //Display the window.
//        mainFrame.pack();
//        mainFrame.setVisible(true);
	}

}
