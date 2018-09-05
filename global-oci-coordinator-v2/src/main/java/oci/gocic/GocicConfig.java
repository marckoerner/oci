
package oci.gocic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Object class of the GOCIC configuration file which contains the parsed configuration information.
 * @author Marc Koerner
 */
public class GocicConfig {

    private File    configFile  = null;
    private Vector  locics      = new Vector<Locic>();
    private Vector  gocics      = new Vector<Locic>();
    
    public GocicConfig(File configFile) {
        this.configFile = configFile;
    }
    
    public boolean parseConfig() {
        if(configFile == null) return false;
        
        try {
            FileReader  fileReader  = new FileReader(this.configFile);
            JSONParser  parser      = new JSONParser();
            
            JSONArray   array       = (JSONArray) parser.parse(fileReader);
            
            JSONObject  object      = null;
            String      ip          = null;
            String      subnet      = null;
            String      location    = null;
            
            for(int i = 0; i<array.size(); i++) {
                // better use iterator?
                
                object      = (JSONObject) array.get(i);
                        
                ip          = (String) object.get("ip");        
                subnet      = (String) object.get("subnet");      
                location    = (String) object.get("location");
                
                this.locics.addElement(new Locic(InetAddress.getByName(ip), subnet, location));
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public boolean parseConfig(File configFile) {
        this.configFile = configFile;
        return this.parseConfig();
    }
    
    public Vector<Locic> getLocics() {
        return this.locics;
    }
    
} // class
