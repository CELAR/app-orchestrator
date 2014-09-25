/*
 * Copyright 2014 CELAR.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.ntua.cslab.orchestrator.rest;

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
class Test {
    private List<SimpleKeyValueStuff> kvs;

    public Test() {
        this.kvs = new LinkedList<>();
    }

    public List<SimpleKeyValueStuff> getKv() {
        return kvs;
    }

    public void setKv(List<SimpleKeyValueStuff> kv) {
        this.kvs = kv;
    }
    
    public void addKv(SimpleKeyValueStuff kv) {
        this.kvs.add(kv);
    }

}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class SimpleKeyValueStuff {
    private String key, value;

    public SimpleKeyValueStuff(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SimpleKeyValueStuff() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}