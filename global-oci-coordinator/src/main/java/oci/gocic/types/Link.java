package oci.gocic.types;

import oci.gocic.GlobalOciCoordinator;

public class Link {

    double capacity; // should be private 
    double weight;   // should be private for good practice
    int id;
    
    public Link(double weight, double capacity) {
        this.id = GlobalOciCoordinator.linkCount + 1; // This is defined in the outer class.
        GlobalOciCoordinator.linkCount++;
        this.weight = weight;
        this.capacity = capacity;
    } 
    public String toString() {
        return "L"+id+": "+ weight;
    }
}