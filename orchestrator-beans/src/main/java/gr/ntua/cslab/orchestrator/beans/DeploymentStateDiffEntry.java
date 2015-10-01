package gr.ntua.cslab.orchestrator.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeploymentStateDiffEntry {
	private String key;
	private String beforeValue;
	private String afterValue;
	
	public DeploymentStateDiffEntry() {
		
	}
	
	public DeploymentStateDiffEntry(String key, String beforeValue, String afterValue) {
		this.key = key;
		this.beforeValue = beforeValue;
		this.afterValue = afterValue;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getBeforeValue() {
		return beforeValue;
	}
	public void setBeforeValue(String beforeValue) {
		this.beforeValue = beforeValue;
	}
	public String getAfterValue() {
		return afterValue;
	}
	public void setAfterValue(String afterValue) {
		this.afterValue = afterValue;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s -> %s", key, beforeValue, afterValue);
	}
}