package gr.ntua.cslab.utils;

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

public class DeploymentTopologyXML {

	private List<String> ycsb, cassandra;
	
	public DeploymentTopologyXML() {
		
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
//			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult target = new StreamResult(System.out);
			trans.transform(source, target);
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		HostsReader reader = new HostsReader();
		DeploymentTopologyXML xml = new DeploymentTopologyXML();
		xml.setYcsb(reader.getIPs(args[0]));
		xml.setCassandra(reader.getIPs(args[1]));
		
		xml.generateXML();
		
	}
}
