package com.entity;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.application.configuration.ConfigurationItemsBundle;
import org.richfaces.component.SortOrder;
import org.richfaces.component.UIDataTable;
import org.richfaces.model.ArrangeableModel;

@ManagedBean(name = "test")
@ViewScoped
public class Test implements Serializable {

	private TempDataModel<BusItem> dataModel;

	private boolean datatablePresent;

	private BusItem busItem;
	
	private String criteria;

	public TempDataModel<BusItem> getDataModel() {
		System.out.println("DataModel is called ");
		return dataModel;
	}

	public void setDataModel(TempDataModel<BusItem> dataModel) {
		this.dataModel = dataModel;
	}

	public void renderPage() {
		//System.out.println("Render Page");
		dataModel = new TempDataModel<BusItem>();
		dataModel.setCriteria(criteria);
	}
	public void popupPanel() {
		// this.setDataModel(null);
		String rowKey = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("rowKey");
		//System.out.println("Param Detail--------->" + rowKey);
		Long row = Long.parseLong(rowKey);
		List<BusItem> dataList = dataModel.getCachedList();
		for (BusItem bi : dataList) {
			if (bi.getRiskID().equals(row)) {
				this.setBusItem(bi);
				break;
			}
		}

	}

	public BusItem getBusItem() {
		if (busItem != null)
			System.out
					.println("l************************************Get Business Call************************************"
							+ busItem.getRiskID());
		return busItem;
	}

	public void setBusItem(BusItem busItem) {
		this.busItem = busItem;
	}

	public void selectionListener(AjaxBehaviorEvent event) {
		UIDataTable dataTable = (UIDataTable) event.getComponent();
		Object originalKey = dataTable.getRowKey();
		if (dataTable.isRowAvailable()) {
			System.out.println(dataTable.getRowData());
		}

	}

	public boolean isDatatablePresent() {
		//System.out.println("datatablePresent ---------> " + datatablePresent);
		return datatablePresent;
	}

	public void setDatatablePresent(boolean datatablePresent) {
		this.datatablePresent = datatablePresent;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

}
