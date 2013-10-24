package gr.ntua.cslab.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class used to read the /etc/hosts file located at the CELAR Orchestrator and returns the
 * IPs which are specified.
 * @author Giannis Giannakopoulos
 *
 */
public class HostsReader {

	private String fileContent;
	public HostsReader() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/etc/hosts"));
			StringBuilder strbuil = new StringBuilder();
			while(reader.ready()){
				String buffer=reader.readLine();
				if(!buffer.equals("") && buffer.trim().charAt(0)!='#')
					strbuil.append(buffer+"\n");
			}
			reader.close();
			this.fileContent = strbuil.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will return the IP address for a domain name which contains
	 * the domain parameter value.
	 * @param domain
	 * @return
	 */
	public List<String> getIPs(String domain){
		LinkedList<String> results = new LinkedList<String>();
		for(String line:this.fileContent.split("\n")){
			line=line.replace(' ', '\t');
			String[] splits= line.split("\t");
			for(String s:splits){
				if(s.contains(domain))
					results.add(splits[0]);
			}
		}
		return results;
	}
	
	public static void main(String[] args) {
		HostsReader r = new HostsReader();
		for(int i=0;i<args.length;i++)
			System.out.println(r.getIPs(args[i]));
	}
}
