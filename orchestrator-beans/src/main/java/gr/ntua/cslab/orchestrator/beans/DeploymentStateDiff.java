package gr.ntua.cslab.orchestrator.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeploymentStateDiff {
	private List<DeploymentStateDiffEntry> entries;
	
	public DeploymentStateDiff() {
		
	}

	public List<DeploymentStateDiffEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DeploymentStateDiffEntry> entries) {
		this.entries = entries;
	}
	
	@Override
	public String toString() {
		return this.entries.toString();
	}
}
