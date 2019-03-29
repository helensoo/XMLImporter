package xmlImporter;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class huaweiXMLImpoter {

	public static void main(String[] args) {

		/*
		 * load the xmlfile data into map moList
		 */
		String huaweiXMLFilePath = "src/xmlImporter/huaweiXMLTest.xml";
		List allInfos = ImportFiles.loadDataFromFile(huaweiXMLFilePath);

		// insert xml file data into HashMap
		String moName = null;
		Map<Integer, ManagedObject> moList = new HashMap();
		boolean isMoBegin = false;
		boolean isMoEnd = false;
		int fileLength = allInfos.size();
		int moIndex = 0;
		ManagedObject mo = null;
		Map<String, String> parameters = null;

		String moPrefix = "<moi xsi:type=\"";
		String moPostfix = "</moi>";
		String attrPrefix = "<attributes>";
		String attrPostfix = "</attributes>";
		//interate every entry in the file
		for (int i = 0; i <= fileLength - 1; i++) {
			// get the MO name

			String temp1 = (String) allInfos.get(i);

			if (temp1.contains(moPrefix)) {
				// create a new MO
				isMoBegin = true;
				isMoEnd = false;
				mo = new ManagedObject();
				parameters = new HashMap<String, String>();
				//cut <moi xsi:type=" at the beginning
				String temp2 = temp1.replace(moPrefix, "");
				//cut "> at the end
				moName = temp2.replace("\">", "").trim();
			} else if (temp1.contains(moPostfix)) {
				isMoBegin = false;
				isMoEnd = true;
			}

			if (isMoBegin && !isMoEnd && !(temp1.contains(moPrefix)) && !(temp1.contains(attrPrefix))
					&& !(temp1.contains(attrPostfix))) {

				// get the parameters Map
				String regex1 = "^<[0-9a-zA-Z]*>[0-9a-zA-Z\\.\\s]*</[0-9a-zA-Z]*>";
				Pattern pattern = Pattern.compile(regex1);
				Matcher matcher = pattern.matcher(temp1);

				// the line is parameter
				if (matcher.matches()) {
					int tempIndex1 = temp1.indexOf("</");
					String tempString = temp1.substring(1, tempIndex1);
					int tempIndex2 = tempString.indexOf(">");
					String paramName = tempString.substring(0, tempIndex2);
					String paramValue = tempString.substring(tempIndex2 + 1, tempString.length());
					parameters.put(paramName, paramValue);
				}

			} else if (!isMoBegin && isMoEnd) {
				//do this for own method
				mo.setMoName(moName);
				mo.setParameters(parameters);
				//do this for hashmap method
				mo.put(moName, parameters);
				moList.put(moIndex, mo);
				moIndex++;
				isMoBegin = false;
				isMoEnd = false;
//				System.out.print(mo.getMoName()+ " ");
//				System.out.println(mo.getParameters());
			}
		}
		ExportResult.exportIndexResult((HashMap<Integer, ManagedObject>) moList);



		/*
		 * load the spec NWP file data into map moNWP
		 */
		String huaweiNWPFilePath =  "src/xmlImporter/H4GNWP.txt";
		List huaweiNWP = ImportFiles.loadDataFromFile(huaweiNWPFilePath);
		Iterator it = huaweiNWP.iterator();
		Map <String, ManagedObject> moNWPs = new HashMap();
		ManagedObject moNWPFromFile = null;
		Map <String, String> paramNWP = null;
		while (it.hasNext()){
			String line = (String) it.next();
			//split the data into 3 entries
			String[] nwpArray = line.split("	",3);
			String moNWPName = null;
			String moParamNWPName = null;
			String moParamNWPVlaue = null;
			if (nwpArray.length ==3){
				moNWPName = nwpArray[0];
				moParamNWPName = nwpArray[1];
				moParamNWPVlaue = nwpArray[2];

			}else{
				//for further developing: how to handle the null value?
				moNWPName = nwpArray[0];
				moParamNWPName = nwpArray[1];
				moParamNWPVlaue = "null";
			}

			if (moNWPFromFile==null ||!moNWPFromFile.containsKey(moNWPName)){
				// here means !moNWPFromFile.containsKey(moNWPName)
				if (moNWPFromFile != null){
					moNWPs.put(moNWPFromFile.getMoName(), moNWPFromFile);
				}
				moNWPFromFile = new ManagedObject();
				paramNWP = new HashMap<String, String>();
				paramNWP.put(moParamNWPName, moParamNWPVlaue);
				moNWPFromFile.setMoName(moNWPName);
				moNWPFromFile.setParameters(paramNWP);
				moNWPFromFile.put(moNWPName, paramNWP);
			}else if(moNWPFromFile.containsKey(moNWPName)){
				paramNWP.put(moParamNWPName, moParamNWPVlaue);
				moNWPFromFile.setParameters(parameters);
			}
		}
		ExportResult.exportStrResult((HashMap<String, ManagedObject>) moNWPs);


		/*
		 * analyze the data in moList and in moNWP
		 */
		Map<Integer, ManagedObject> passMos = new HashMap<Integer, ManagedObject>();
		Map<Integer, ManagedObject> falseMos = new HashMap<Integer, ManagedObject>();
		Map<Integer, ManagedObject> openMos = new HashMap<Integer, ManagedObject>();
		for (Map.Entry<Integer, ManagedObject> entry : moList.entrySet()){
			int index = entry.getKey();
			ManagedObject moXML = entry.getValue();
			String moXMLName = moXML.getMoName();
			Map moXMLParam = moXML.getParameters();
			ManagedObject moNWP = null;
			if(moNWPs.containsKey(moXMLName)){
				moNWP = moNWPs.get(moXMLName);
				Map moNWPParam = moNWP.getParameters();
				//1. Analyse if the parameters sum of xml less than spec is false
				if (moXMLParam.size()< moNWPParam.size()){
					if(!moNWPParam.values().contains("null")){
						falseMos.put(index, moXML);
					}else{
						openMos.put(index, moXML);
					}

				}else if (moXML.equals(moNWP)){
					// 2. step of analyse: filter all same entries
					passMos.put(index, moXML);
					}else{
						openMos.put(index, moXML);
					}


			}else{
				openMos.put(index,moXML);
			}
//			System.out.println("moXML:   " + moXML);
//			System.out.println("moNWP:   "  + moNWP);
		}
		System.out.println(passMos.size());
		System.out.println("passMos: " + passMos);
		System.out.println(falseMos.size());
		System.out.println("falseMos: " + falseMos);
		System.out.println(openMos.size());
//		System.out.println("opemMos: " + openMos);


//version 9


	}
}
