package gr.ntua.cslab.orchestrator.beans;

import gr.ntua.cslab.celar.server.beans.SimpleReflectiveEntity;
import gr.ntua.cslab.celar.server.beans.structured.ProvidedResourceInfo;
import gr.ntua.cslab.celar.server.beans.Spec;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This Class holds information about a resource provided by the IaaS
 * @author cmantas
 */
@XmlRootElement 
public class ResourceInfo extends SimpleReflectiveEntity {
        public int resourceId;
        public String name;
        public int resource_Type_Id;
        public List<ResourceSpec> specs;
        
        public ResourceInfo(){}
         
        public ResourceInfo(ProvidedResourceInfo pri){
            this.resourceId = pri.id;
            this.name = pri.name;
            this.resource_Type_Id = pri.resource_Type_Id;
            specs = new java.util.LinkedList<>();
//            this.mirror(pri);
            
            for(Spec s: pri.specs){
                specs.add(new ResourceSpec(s));
            }
        }
         
    /**
     * This class shows the key-value properties characterizing a resource 
     */    
    public static class ResourceSpec  extends SimpleReflectiveEntity{
        int id;
        public int provided_Resource_Id;
        public String property, value; 

        /**
         * Default Constructor
         */
        public ResourceSpec() {super();}
        
        public ResourceSpec(Spec s){
            this.id=s.id;
            this.provided_Resource_Id=s.id;
            this.property = s.property;
            this.value = s.value;
//            this.mirror(s);
            
        }
        
        @Override
        public String toString(){
            return property+":"+value;
        }

    }
}
