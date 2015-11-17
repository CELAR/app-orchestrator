package gr.ntua.cslab.orchestrator.beans;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.sixsq.slipstream.util.Logger;

@XmlRootElement
public class DeploymentStateDiff {
	private List<DeploymentStateDiffEntry> entries;
	
	public DeploymentStateDiff() {
		this.entries = new LinkedList<DeploymentStateDiffEntry>();
	}

	public List<DeploymentStateDiffEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DeploymentStateDiffEntry> entries) {
		this.entries = entries;
	}
	
	// Methods used to return the effect of the resizing action
	
	// scale out - scale in
	/**
	 * If the action is of type SCALE_OUT or SCALE_IN, this method returns the VM IDsof the VMs 
	 * that joined/left the deployment (respectively). In any other case, null is returned.  
	 * @return
	 */
	public List<String> getVMIDs() {
		// scale out
		List<String> vmIDs = new LinkedList<>();
		for(DeploymentStateDiffEntry e : this.entries) {
			if(e.getKey().endsWith("hostname") && e.getBeforeValue()=="") {
				vmIDs.add(e.getKey().split(":")[0]);
			}
		}
		
		// scale in
		for(DeploymentStateDiffEntry e : this.entries) {
			if(e.getKey().endsWith("vmstate") && 
					e.getBeforeValue().toLowerCase().equals("running") && 
					e.getAfterValue().toLowerCase().equals("unknown")) {
				vmIDs.add(e.getKey().split(":")[0]);
			}
		}
		
		return (vmIDs.isEmpty()?null:vmIDs);
	}
	
	// disk attach-detach
	
	/**
	 * If the actions is of type DISK_ATTACH/DISK_DETACH, this methods returns the ID of the disk that
	 * was attached/detached respectively. In any other case, null is returned.
	 * @return
	 */
	public String getDiskID() {
		String diskId=null;
		for(DeploymentStateDiffEntry e : this.entries) {
			if(e.getKey().endsWith("disk.attached.device") || e.getKey().endsWith("disk.detach.device")) {
				if(diskId !=null) {
					Logger.warning("Multiple disks where attached/detached");
				}
				diskId = e.getAfterValue();
			}
		}
		return diskId;
	}
	
	
	// Overridden methods
	@Override
	public String toString() {
		return this.entries.toString();
	}
	
	public static void main(String[] args) {
		DeploymentStateDiff diff = new DeploymentStateDiff();
		diff.getEntries().add(new DeploymentStateDiffEntry("Scheduler.1:complete","false","true"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.4:disk.attach.size","10","5"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.4:disk.attached.device","63696","63703"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.4:scale.state","operational","disk_attached"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.8:complete","false","true"));
		System.out.println(diff.getDiskID());
		
		// [Worker.7:disk.attach.size:  -> 5, Worker.7:disk.attached.device:  -> 63704, Worker.7:statecustom: Executing target 'onvmadd' -> Executing target 'postscale']
		diff = new DeploymentStateDiff();
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.7:disk.attach.size","","5"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.7:disk.attached.device","","63704"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.7:statecustom","Executing target 'onvmadd'","Executing target 'postscale'"));
		System.out.println(diff.getDiskID());
		
		// [Worker.7:disk.detach.device:  -> 63704, Scheduler.1:complete: false -> true, Worker.7:complete: false -> true]
		diff = new DeploymentStateDiff();
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.7:disk.detach.device","","63704"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Scheduler.1:complete","false","true"));
		diff.getEntries().add(new DeploymentStateDiffEntry("Worker.7:complete","false","true"));
		System.out.println(diff.getDiskID());
	}
}
