package com.nordicpeak.flowengine.queries.generalmapquery.configuration;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "general_map_query_printconfigurations")
@XMLElement
public class PrintConfiguration extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -4577474763181281910L;

	public static final Field MAPPRINTS_RELATION = ReflectionUtils.getField(PrintConfiguration.class, "mapPrints");
	
	@Key
	@DAOManaged
	@XMLElement
	private Integer printConfigurationID;

	@DAOManaged
	@XMLElement
	private String name;

	@DAOManaged
	@XMLElement
	private String printConfigTemplate;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<MapPrint> mapPrints;

	public Integer getPrintConfigurationID() {

		return printConfigurationID;
	}

	public void setPrintConfigurationID(Integer printConfigurationID) {

		this.printConfigurationID = printConfigurationID;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getPrintConfigTemplate() {

		return printConfigTemplate;
	}

	public void setPrintConfigTemplate(String printConfigTemplate) {

		this.printConfigTemplate = printConfigTemplate;
	}

	public List<MapPrint> getMapPrints() {

		return mapPrints;
	}

	public void setMapPrints(List<MapPrint> mapPrints) {

		this.mapPrints = mapPrints;
	}

}
