package oci.thirdparty.types;

import java.util.Iterator;
import java.util.Vector;

public class ThridPartyMetaData {

	int id;
	String name;
	String fileName;
	double price;
	Vector<String> location;	

	public ThridPartyMetaData(String name, String fileName, double price) {
		super();
		this.name = name;
		this.fileName = fileName;
		this.price = price;
		this.location = new Vector<String>();
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}


	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the location
	 */
	public Vector<String> getLocation() {
		return location;
	}
	
	/**
	 * @param location the location to set
	 */
	public void setLocation(Vector<String> location) {
		this.location = location;
	}

	public void addLocation(String newLocation) {
		this.location.addElement(newLocation);
	}

	public void printLocation() {   	
		Iterator<String> itr = location.iterator();
		while(itr.hasNext()){
			System.out.println(itr.next());
		}
	}
}