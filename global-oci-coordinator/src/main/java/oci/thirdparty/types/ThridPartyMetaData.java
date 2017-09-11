package oci.thirdparty.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThridPartyMetaData {

	int id;
	String serviceName;
	String fileName;
	double price;
	List<Integer> location;	

	public ThridPartyMetaData(String name, String fileName, double price) {
		super();
		this.serviceName = name;
		this.fileName = fileName;
		this.price = price;
		this.location = new ArrayList<Integer>();
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
	public String getServiceName() {
		return serviceName;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.serviceName = name;
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
	public List<Integer> getLocation() {
		return location;
	}
	
	public void addLocation(int newLocation) {
		this.location.add(newLocation);
	}

	public void printLocation() {   	
		Iterator<Integer> itr = location.iterator();
		while(itr.hasNext()){
			System.out.println(itr.next());
		}
	}
}