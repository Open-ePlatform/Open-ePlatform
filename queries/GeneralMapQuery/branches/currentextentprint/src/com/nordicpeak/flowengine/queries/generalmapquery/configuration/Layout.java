package com.nordicpeak.flowengine.queries.generalmapquery.configuration;

import java.io.Serializable;
import java.util.List;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "general_map_query_printservice_layouts")
@XMLElement
public class Layout extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = 8884992700835940208L;

	@Key
	@DAOManaged(autoGenerated = true)
	@XMLElement
	private Integer layoutID;

	@DAOManaged
	@XMLElement
	private String name;

	@DAOManaged(columnName = "printServiceID")
	@ManyToOne
	@XMLElement
	private PrintService printService;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<MapPrint> mapPrints;

	public Integer getLayoutID() {

		return layoutID;
	}

	public void setLayoutID(Integer layoutID) {

		this.layoutID = layoutID;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public PrintService getPrintService() {

		return printService;
	}

	public void setPrintService(PrintService printService) {

		this.printService = printService;
	}

	public List<MapPrint> getMapPrints() {

		return mapPrints;
	}

	public void setMapPrints(List<MapPrint> mapPrints) {

		this.mapPrints = mapPrints;
	}
}
