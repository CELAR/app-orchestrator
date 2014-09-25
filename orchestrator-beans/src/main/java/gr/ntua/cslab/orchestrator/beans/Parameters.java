/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.orchestrator.beans;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author giannis
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Parameters {

    private List<Parameter> parameters;

    public Parameters() {
        this.parameters = new LinkedList();
    }

    public Parameters(List parameters) {
        this.parameters = parameters;
    }

    public List getParameters() {
        return parameters;
    }

    public void setParameters(List parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter p) {
        this.parameters.add(p);
    }
}
