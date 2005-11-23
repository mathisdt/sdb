package org.zephyrsoft.sdb.structure;

import java.util.*;

public class Options implements java.io.Serializable, java.lang.Cloneable {

	private Hashtable daten = new Hashtable();
	
	public Options() {
		super();
	}
	
	public Options(Hashtable initial) {
		super();
		this.daten = initial;
	}
	
	public void put (Object key, Object data) {
		daten.put(key, data);
	}
	
	public Object get(Object key) {
		return daten.get(key);
	}
	
	public Object clone() {
		Options ret = new Options((Hashtable)daten.clone());
		return ret;
	}
}