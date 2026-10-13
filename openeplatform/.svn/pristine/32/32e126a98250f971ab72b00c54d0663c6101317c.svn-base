package com.nordicpeak.flowengine.queries.generalmapquery;

import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;

import com.vividsolutions.jts.geom.Point;

public class FeatureLabel {

	private String id;

	private Point point;

	private String label;

	private String color = "#000000";

	private int width = 3;

	private String fontSize = "12px";

	public FeatureLabel(String id, Point point, String label) {

		super();
		this.id = id;
		this.point = point;
		this.label = label;
	}

	public FeatureLabel(String id, Point point, String label, String color, int width) {

		super();
		this.id = id;
		this.point = point;
		this.label = label;
		this.color = color;
		this.width = width;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public Point getPoint() {

		return point;
	}

	public void setPoint(Point point) {

		this.point = point;
	}

	public String getLabel() {

		return label;
	}

	public void setLabel(String label) {

		this.label = label;
	}

	public String getColor() {

		return color;
	}

	public void setColor(String color) {

		this.color = color;
	}

	public int getWidth() {

		return width;
	}

	public void setWidth(int width) {

		this.width = width;
	}

	public String getFontSize() {

		return fontSize;
	}

	public void setFontSize(String fontSize) {

		this.fontSize = fontSize;
	}

	public JsonObject toJson() {

		JsonObject label = new JsonObject();

		label.putField("type", point.getGeometryType());

		JsonArray coords = new JsonArray();

		coords.addNode(point.getX() + "");
		coords.addNode(point.getY() + "");

		label.putField("coordinates", coords);

		JsonObject properties = new JsonObject();
		properties.putField("_style", id);

		JsonObject featureLabel = new JsonObject();
		featureLabel.putField("type", "Feature");
		featureLabel.putField("properties", properties);
		featureLabel.putField("geometry", label);

		return featureLabel;

	}

}
