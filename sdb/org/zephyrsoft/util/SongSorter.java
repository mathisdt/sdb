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
public class SongSorter implements Comparator {

	boolean byTitle;
	
	public SongSorter(boolean byTitle) {
		this.byTitle = byTitle;
	}
	
	public int compare(Object a, Object b) {
		Song s1 = (Song)a;
		Song s2 = (Song)b;
		
		if (byTitle) {
			return (new Song(-1)).compare(s1, s2);
		} else {
			String x1 = s1.getFirstLine();
			String x2 = s2.getFirstLine();
			return x1.compareTo(x2);
		}
	}
}