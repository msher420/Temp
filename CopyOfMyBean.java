package com.balusc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UICommand;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.richfaces.component.SortOrder;

import com.entity.BusItem;
import com.entity.Dataholder;

/**
 * The example backing bean for effective datatable paging and sorting.
 * 
 * @author BalusC
 * @link 
 *       http://balusc.blogspot.com/2008/10/effective-datatable-paging-and-sorting
 *       .html
 */
@ManagedBean
@ViewScoped
public class CopyOfMyBean implements Serializable {

	// Data.
	private List<BusItem> dataList;
	private int totalRows;

	// Paging.
	private int firstRow = 1;
	private int rowsPerPage;
	private int totalPages;
	private int pageRange;
	private Integer[] pages;
	private int currentPage;

	// Sorting.
	private String sortField;
	private boolean sortAscending;

	// Constructors
	// -------------------------------------------------------------------------------

	public CopyOfMyBean() {
		// Set default values somehow (properties files?).
		rowsPerPage = 2; // Default rows per page (max amount of rows to be
							// displayed at once).
		pageRange = 5; // Default page range (max amount of page links to be
						// displayed at once).
		sortField = "id"; // Default sort field.
		sortAscending = true; // Default sort direction.
	}

	// Paging actions
	// -----------------------------------------------------------------------------

	public void pageFirst() {
		page(1);
	}

	public void pageNext() {
		System.out.println("*****PageNext1**********" + firstRow);
		System.out.println("Previous -> " + (firstRow + rowsPerPage));
		page(firstRow + rowsPerPage);
	}

	public void pagePrevious() {
		System.out.println("Previous -> " + (firstRow - rowsPerPage));
		page(firstRow - rowsPerPage);
	}
	
	public void pageNext2() {
		System.out.println("*****PageNext2**********" + firstRow);
		System.out.println(rowsPerPage);
		page(firstRow + rowsPerPage * 2);
	}

	public void pagePrevious2() {
		page(firstRow - rowsPerPage * 2);
	}

	public void pageLast() {
		page(totalRows
				- ((totalRows % rowsPerPage != 0) ? totalRows % rowsPerPage
						: rowsPerPage)+1);
	}

	public void page(ActionEvent event) {
		System.out.println("After PageLink Click Value -> "
				+ ((UICommand) event.getComponent()).getValue());
		page((((Integer) ((UICommand) event.getComponent()).getValue()) -1)
				* rowsPerPage + 1);
	}

	private void page(int firstRow) {
		System.out.println("After PageLink FirstRow Value -> " + firstRow);
		this.firstRow = firstRow;
		loadDataList(); // Load requested page.
	}

	// Sorting actions
	// ----------------------------------------------------------------------------

	public void sort(ActionEvent event) {
		String sortFieldAttribute = (String) event.getComponent()
				.getAttributes().get("sortField");

		// If the same field is sorted, then reverse order, else sort the new
		// field ascending.
		//System.out.println("SortField ->  " + sortFieldAttribute);
		//System.out.println("SortOrder -> " + sortAscending);
		if (sortField.equals(sortFieldAttribute)) {
			sortAscending = !sortAscending;
		} else {
			sortField = sortFieldAttribute;
			sortAscending = true;
		}
		//System.out.println("Sort Current Page -> " + currentPage);
		page(firstRow);
		// pageFirst(); // Go to first page and load requested page.
	}

	public void valueChange(AjaxBehaviorEvent event) {

		HtmlSelectOneMenu ui = (HtmlSelectOneMenu) event.getSource();
		Integer val = (Integer) ui.getValue();
		// System.out.println("val -> " + val);
		//System.out.println("Value Change ---> " + val);
		this.setRowsPerPage(val);
		pageFirst();
	}

	// Loaders
	// ------------------------------------------------------------------------------------

	private void loadDataList() {

		Dataholder holder = new Dataholder();
		
		//Load the data from the table with the default sort condition from DB Hit
		//System.out.println("DB Hit");
		dataList = holder.getModList();
		sort(sortField, sortAscending);

		//System.out.println("DB Hit for Total Row Count");
		//Load the Total Rows in the table
		totalRows = holder.getModList().size();
		int lastRow = firstRow -1 + rowsPerPage;
		if (lastRow > totalRows) {
			lastRow = totalRows;
		}
		System.out.println("Row Query -> " + (firstRow) +" - "+ lastRow);
		
		

		//System.out.println(dataList);
		int prevLastRow = firstRow - 1;
		
		dataList = holder.getModList()
				.subList(firstRow-1, lastRow);
		
		System.out.println(dataList);
		currentPage = (totalRows / rowsPerPage)
				- ((totalRows - prevLastRow) / rowsPerPage) + 1;
		totalPages = (totalRows / rowsPerPage)
				+ ((totalRows % rowsPerPage != 0) ? 1 : 0);
		int pagesLength = Math.min(pageRange, totalPages);
		pages = new Integer[pagesLength];

		// firstPage must be greater than 0 and lesser than
		// totalPages-pageLength.
		int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)),
				totalPages - pagesLength);

		// Create pages (page numbers for page links).
		for (int i = 0; i < pagesLength; i++) {
			pages[i] = ++firstPage;
			System.out.println(pages[i]);
		}
		
		System.out.println("firstRow -> " + firstRow);
		System.out.println("rowsPerPage -> " + rowsPerPage);
		System.out.println("totalPages -> " + totalPages);
		System.out.println("pageRange -> " + pageRange);
		System.out.println("pages -> " + pages);
		System.out.println("currentPage -> " + currentPage);
		System.out.println(" pagesLength -> "+ pagesLength);
		System.out.println("Pages -> sheriff ->  " + pages);
	}

	// Getters
	// ------------------------------------------------------------------------------------

	public List<BusItem> getDataList() {
		if (dataList == null) {
			loadDataList(); // Preload page for the 1st view.
		}
		return dataList;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public Integer[] getPages() {
		return pages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	// Setters
	// ------------------------------------------------------------------------------------

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	// SortLogic

	// For the State Sort
	public void sort(String sortField, boolean sortAscending) {
		if (sortAscending == true) {
			// System.out.println("RegionName Ascending");
			Collections.sort(dataList, new Comparator<BusItem>() {
				@Override
				public int compare(BusItem o1, BusItem o2) {
					// TODO Auto-generated method stub
					return o1.getRiskID().compareTo(o2.getRiskID());
				}
			});
		} else {
			// System.out.println("RegionName Descending");
			Collections.sort(dataList, new Comparator<BusItem>() {
				@Override
				public int compare(BusItem o1, BusItem o2) {
					// TODO Auto-generated method stub
					return o2.getRiskID().compareTo(o1.getRiskID());
				}
			});
		}

	}

}