package gr.ntua.cslab.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApplicationXMLs {

	private List<String> ycsb, cassandra;
	
	public ApplicationXMLs() {
		
	}
	
	
	public List<String> getYcsb() {
		return ycsb;
	}


	public void setYcsb(List<String> ycsb) {
		this.ycsb = ycsb;
	}


	public List<String> getCassandra() {
		return cassandra;
	}


	public void setCassandra(List<String> cassandra) {
		this.cassandra = cassandra;
	}


	private Element createIPListTag(Document doc, String serviceUnitId, List<String> ips){
		Element root = doc.createElement("DeploymentUnit");
		root.setAttribute("serviceUnitID", serviceUnitId);
		for(String s:ips){
			Element el = doc.createElement("AssociatedVM");
			el.setAttribute("IP", s);
			root.appendChild(el);
		}
		return root;
		
	}
	
	public void generateXML(){
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement("DeploymentDescription");
			doc.setXmlStandalone(true);
			root.setAttribute("CloudServiceID", "CloudService");
			root.setAttribute("AccessIP", "localhost");
			doc.appendChild(root);
			root.appendChild(this.createIPListTag(doc, "YCSBClient", this.ycsb));
			root.appendChild(this.createIPListTag(doc, "CassandraNode", this.cassandra));
			
			
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult target = new StreamResult(new File("/tmp/deploymentDescription.xml"));
			trans.transform(source, target);
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}	
	}
	
	public String getXML(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/tmp/deploymentDescription.xml"));
			String content=reader.readLine();
			reader.close();
			File f = new File("/tmp/deploymentDescription.xml");
			f.delete();
			return content;
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getApplicationDescriptionXML(){
		String appDescriptionXML = null;
		try {
			
			FileReader reader = new FileReader("/opt/policy.xml");
			char[] buffer = new char[1024];
			StringBuffer sBuffer = new StringBuffer();
			while(reader.ready()){
				int len = reader.read(buffer);
				sBuffer.append(buffer,0,len);
			}
			reader.close();
			appDescriptionXML = sBuffer.toString();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		String appDescriptionXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//				+ "<CloudService id=\"CloudService\">"
//				+ 	"<SYBLDirective Constraints=\"Co1: CONSTRAINT cost &lt; 1.5 $;\" Strategies=\"St1:STRATEGY CASE fulfilled(Co2): minimize(cost);\"/>"
//				+ 	"<ServiceTopology id=\"MainTopology\">"
//				+ 			"<Relationship>"
//				+ 				"<Master>YCSBClient</Master>"
//				+ 				"<Slave>CassandraNode</Slave>"
//				+ 			"</Relationship>"
//				+ 		"<ServiceUnit id=\"YCSBClient\" >"
//				+ 			"<SYBLDirective Strategies=\"St2:STRATEGY CASE throughput &lt; 1000 AND latency &lt; 500 : scalein(CassandraNode);\" Constraints=\"Co2:CONSTRAINT latency &lt; 1000 ms ;\"/>"
//				+		 "</ServiceUnit>"
//				+ 		"<ServiceUnit id=\"CassandraNode\" >"
//				+ 			"<SYBLDirective Constraints=\"Co3:CONSTRAINT cpuUsage &gt; 35 % AND cpuUsage &lt; 75 %\" />"
//				+ 		"</ServiceUnit>"
//				+ "	</ServiceTopology>"
//				+ "</CloudService>";
//		
		return appDescriptionXML;
	}
	
	public static void main(String[] args) {
		HostsReader reader = new HostsReader();
		ApplicationXMLs xml = new ApplicationXMLs();
		xml.setYcsb(reader.getIPs(args[0]));
		xml.setCassandra(reader.getIPs(args[1]));
		xml.generateXML();
		System.out.println(xml.getXML());
		System.out.println(xml.getApplicationDescriptionXML());
		
	}
}
