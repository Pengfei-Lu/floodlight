package net.floodlightcontroller.accesscontrollist.evaluation;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import net.sf.json.JSONObject;

public class EvaluationClass {

	public final static String baseURL = "http://localhost:8080";
	
	static void testAdd(int count) throws Exception {
		// clear ACL
		HttpRequest.getRequest(baseURL + "/wm/acl/clear/json");
		
		JSONObject oldJsonEntry = new JSONObject();
		oldJsonEntry.element("src-ip", "10.0.0.1/32");
		oldJsonEntry.element("dst-ip", "10.0.0.3/32");
		oldJsonEntry.element("nw-proto", "TCP");
		oldJsonEntry.element("action", "deny");
		
		for(int i=1; i<=count; i++){
			oldJsonEntry.element("tp-dst", Integer.toString(i));
			HttpRequest.postRequest(baseURL + "/wm/acl/rules/json", oldJsonEntry.toString());
		}
		
		JSONObject newJsonEntry = new JSONObject();
		newJsonEntry.element("src-ip", "10.0.0.1/32");
		newJsonEntry.element("dst-ip", "10.0.0.2/32");
		newJsonEntry.element("action", "deny");
		HttpRequest.postRequest(baseURL + "/wm/acl/rules/json", newJsonEntry.toString());
	}
	
	static void testRemove(int count) throws Exception {
		// clear ACL
		HttpRequest.getRequest(baseURL + "/wm/acl/clear/json");
		
		JSONObject oldJsonEntry = new JSONObject();
		oldJsonEntry.element("src-ip", "10.0.0.1/32");
		oldJsonEntry.element("dst-ip", "10.0.0.3/32");
		oldJsonEntry.element("nw-proto", "TCP");
		oldJsonEntry.element("action", "deny");
		
		for(int i=1; i<=count; i++){
			oldJsonEntry.element("tp-dst", Integer.toString(i));
			HttpRequest.postRequest(baseURL + "/wm/acl/rules/json", oldJsonEntry.toString());
		}
		
		JSONObject jsonEntry = new JSONObject();
		jsonEntry.element("ruleid", Integer.toString(count));
		HttpRequest.deleteRequest(baseURL + "/wm/acl/rules/json", jsonEntry.toString());
	}
	
	static double[] parseAddResult() throws Exception{
		
		Reader reader = new FileReader(new File("F:" + File.separator + "testLog.txt"));
		char data[] = new char[9999999];
		int len = reader.read(data);

		String context = new String(data, 0, len);
		String[] lines = context.split("\n");
		
//		System.out.println(lines[lines.length-3]);
//		System.out.println(lines[lines.length-2]);
		
		double addTime = Double.parseDouble(lines[lines.length-2].substring(lines[lines.length-2].indexOf(": ") + 2).trim());
		double enforceTime = Double.parseDouble(lines[lines.length-1].substring(lines[lines.length-1].indexOf(": ") + 2).trim());
		
		double[] result = new double[2];
		result[0] = addTime;
		result[1] = enforceTime;
		
		reader.close();
		
		return result;
	}
	
	static double[] parseRemoveResult() throws Exception{
		
		Reader reader = new FileReader(new File("F:" + File.separator + "testLog.txt"));
		char data[] = new char[9999999];
		int len = reader.read(data);

		String context = new String(data, 0, len);
		String[] lines = context.split("\n");
		
//		System.out.println(lines[lines.length-2]);
//		System.out.println(lines[lines.length-1]);
		
		double removeTime = Double.parseDouble(lines[lines.length-2].substring(lines[lines.length-2].indexOf(": ") + 2).trim());
		double enforceTime = Double.parseDouble(lines[lines.length-1].substring(lines[lines.length-1].indexOf(": ") + 2).trim());
		
		double[] result = new double[2];
		result[0] = removeTime;
		result[1] = enforceTime;
		
		reader.close();
		
		return result;
	}
	
	static void testApAdd(int count) throws Exception {
		// clear ACL
		HttpRequest.getRequest(baseURL + "/wm/acl/clear/json");
		
		JSONObject oldJsonEntry = new JSONObject();
		oldJsonEntry.element("src-ip", "10.0.0.1/32");
		oldJsonEntry.element("dst-ip", "10.0.0.2/32");
		oldJsonEntry.element("nw-proto", "TCP");
		oldJsonEntry.element("action", "deny");
		
		for(int i=1; i<count; i++){
			System.out.println(i);
			oldJsonEntry.element("tp-dst", Integer.toString(i));
			HttpRequest.postRequest(baseURL + "/wm/acl/rules/json", oldJsonEntry.toString());
		}
		
		JSONObject newJsonEntry = new JSONObject();
		newJsonEntry.element("src-ip", "10.0.0.1/32");
		newJsonEntry.element("dst-ip", "10.0.0.3/32");
		newJsonEntry.element("action", "deny");
		HttpRequest.postRequest(baseURL + "/wm/acl/rules/json", newJsonEntry.toString());
	}
	
	static double parseApAddResult() throws Exception{
		
		Reader reader = new FileReader(new File("F:" + File.separator + "testLog.txt"));
		char data[] = new char[9999999];
		int len = reader.read(data);

		String context = new String(data, 0, len);
		String[] lines = context.split("\n");
		
//		System.out.println(lines[lines.length-1]);
		
		double time = Double.parseDouble(lines[lines.length-1].substring(lines[lines.length-1].indexOf(": ") + 2).trim());
				
		reader.close();
		
		return time;
	}
	
	
	public static void main(String[] args) {
		int count[] = {6000, 7000, 8000, 9000, 10000};
//		int count[] = {1000, 2000, 3000, 4000, 5000};
//		int count[] = {11000, 12000, 13000, 14000, 15000};
//		int count[] = {16000, 17000, 18000, 19000, 20000};
		
//		int num = 5;
//		try {
//			for(int i : count){
//				double addSum = 0, enforceSum = 0;
//				double[] result;
//				for(int j=0; j<num; j++){
//					testAdd(i);
//					result = parseAddResult();
//					addSum += result[0];
//					enforceSum += result[1];
//				}
//				System.out.println(addSum/num + "\t" + enforceSum/num);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		int num = 5;
//		try {
//			for(int i : count){
//				double removeSum = 0, enforceSum = 0;
//				double[] result;
//				for(int j=0; j<num; j++){
//					testRemove(i);
//					parseRemoveResult();
//					result = parseRemoveResult();
//					removeSum += result[0];
//					enforceSum += result[1];
//				}
//				System.out.println(removeSum/num + "\t" + enforceSum/num);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		int num = 5;
//		try {
//			for(int i : count){
//				double sum = 0;
//				for(int j=0; j<num; j++){
//					testApAdd(i);
//					sum += parseApAddResult();
//				}
//				System.out.println(sum/num);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		try {
			testApAdd(10000);
			System.out.println("Done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}