package gr.ntua.cslab.orchestrator.beans;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import gr.ntua.cslab.celar.server.beans.SimpleReflectiveEntity;


@XmlRootElement
@XmlSeeAlso({ResourceInfo.class})
@XmlAccessorType(XmlAccessType.PROPERTY)

/**
 * This is a list used to handle Orchestrator Beans
 */
public class  BeanList <E extends SimpleReflectiveEntity> extends SimpleReflectiveEntity  implements List   {
     public List<E> values;

    public BeanList() {
        values = new java.util.LinkedList();
    }

    public BeanList(List<E> values) {
        this.values = values;
    }

    @XmlAnyElement(lax=true)
    public List<E> getValues() {
        return values;
    }

    public void setValues(List<E> values) {
        this.values = values;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
       return values.iterator();
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return values.toArray(a);
    }


    @Override
    public boolean remove(Object o) {
        return values.remove(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return values.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        return values.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return values.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection c) {
       return values.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        return values.retainAll(c);
    }

    @Override
    public void clear() {
       values.clear();
    }

    @Override
    public Object get(int index) {
        return values.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return values.set(index, (E) element);
    }

    @Override
    public void add(int index, Object element) {
        values.add(index, (E) element);
    }

    @Override
    public Object remove(int index) {
       return values.remove(index);
    }

    @Override
    public int indexOf(Object o) {
       return values.indexOf((ResourceInfo)o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return values.lastIndexOf((ResourceInfo)o);
    }

    @Override
    public ListIterator listIterator() {
        return values.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return values.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return values.subList(fromIndex, toIndex);
    }

    @Override
    public boolean add(Object e) {
        return values.add((E) e); 
    }
}
