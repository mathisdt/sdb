package org.zephyrsoft.util;

import java.util.*;

import org.zephyrsoft.sdb.structure.*;

/** This comparator is used to sort vectors of data.<br>Use the following code in your classes:<br>
<pre>
		...
		
		// Disable autoCreateColumnsFromModel otherwise all the
	    // column customizations and adjustments will be lost
	    // when the model data is sorted
	    table.setAutoCreateColumnsFromModel(false);
	    
	    sortAllRowsBy(mydefaulttablemodel, 0, true);
		
		...
	
	
    // Regardless of sort order (ascending or descending), "null" values always appear last.
    // colIndex specifies a column in model.
    public void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
        Vector data = model.getDataVector();
        Collections.sort(data, new ColumnSorter(colIndex, ascending));
        model.fireTableStructureChanged();
    }
</pre>
*/
public class ColumnSorter implements Comparator {

	int colIndex;
	boolean ascending;
	
	public ColumnSorter(int colIndex, boolean ascending) {
		this.colIndex = colIndex;
		this.ascending = ascending;
	}
	
	public int compare(Object a, Object b) {
		Vector v1 = (Vector)a;
		Vector v2 = (Vector)b;
		Object o1 = v1.get(colIndex);
		Object o2 = v2.get(colIndex);
		
		// Treat empty strains like nulls
		if (o1 instanceof String && ((String)o1).length() == 0) {
			o1 = null;
		}
		if (o2 instanceof String && ((String)o2).length() == 0) {
			o2 = null;
		}
		
		// Sort nulls so they appear last, regardless
		// of sort order
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 == null) {
			return 1;
		} else if (o2 == null) {
			return -1;
		} else if (o1 instanceof String) {
			String titel1 = ((String)o1).toLowerCase();
			String titel2 = ((String)o2).toLowerCase();
			titel1 = StringTools.replace(titel1, "ä", "ae"); //$NON-NLS-1$ //$NON-NLS-2$
			titel1 = StringTools.replace(titel1, "ö", "oe"); //$NON-NLS-1$ //$NON-NLS-2$
			titel1 = StringTools.replace(titel1, "ü", "ue"); //$NON-NLS-1$ //$NON-NLS-2$
			titel1 = StringTools.replace(titel1, "ß", "ss"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ä", "ae"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ö", "oe"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ü", "ue"); //$NON-NLS-1$ //$NON-NLS-2$
			titel2 = StringTools.replace(titel2, "ß", "ss"); //$NON-NLS-1$ //$NON-NLS-2$
			if (ascending) {
				return ((Comparable)titel1).compareTo(titel2);
			} else {
				return ((Comparable)titel2).compareTo(titel1);
			}
		} else if (o1 instanceof Comparable) {
			if (ascending) {
				return ((Comparable)o1).compareTo(o2);
			} else {
				return ((Comparable)o1).compareTo(o2);
			}
		} else if (o1 instanceof Song) {
			if (ascending) {
				return (new Song( -1)).compare((Song)o1, (Song)o2);
			} else {
				return (new Song( -1)).compare((Song)o2, (Song)o1);
			}
		} else {
			if (ascending) {
				return o1.toString().compareTo(o2.toString());
			} else {
				return o2.toString().compareTo(o1.toString());
			}
		}
	}
}