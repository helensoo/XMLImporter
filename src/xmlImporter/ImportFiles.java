package xmlImporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImportFiles {

	public static List<String> loadDataFromFile(String filePath) {
		File huaweiXMLFile = new File(filePath);
		String line = null;
		List allInfos = new LinkedList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(huaweiXMLFile));
			try {
				while ((line = reader.readLine()) != null) {
					if (!line.isEmpty()) {
						allInfos.add(line.trim());
					}
				}
			} catch (IOException e) {
				System.out.println("the file is empty.");
			}

		} catch (FileNotFoundException e) {
			System.out.println("the file is NOT exist!");
		}
		return allInfos;

	}

//	public static List<String> loadDataFromTXTFile(String filePath) {
//		File nwpFile = new File(filePath);
//		String line = null;
//		List allInfos = new LinkedList<String>();
//		Map moNWPs = new HashMap<String, String>();
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(nwpFile));
//			try {
//				while ((line = reader.readLine()) != null) {
//					if (!line.isEmpty()) {
//						String[] infoArray = line.split("	", 2);
//						int arrayLength = infoArray.length;
//						if (arrayLength == 2) {
//							String moName = infoArray[0];
//							String moInfo = infoArray[1];
//							System.out.println("moName: "+ moName + " moInfo: " + moInfo);
//							moNWPs.put(moName, moInfo);
//						}
//					}
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				System.out.println("the file is empty.");
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			System.out.println("the file is NOT exist!");
//		}
//
//		return null;
//
//	}
}
