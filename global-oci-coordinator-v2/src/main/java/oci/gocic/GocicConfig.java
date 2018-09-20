
package oci.gocic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Object class of the GOCIC configuration file which contains the parsed configuration information.
 * @author Marc Koerner
 */
public class GocicConfig {

    private File            configFile  = null;
    private HashSet<Locic>  locics      = null;
    private HashSet<Locic>  gocics      = null;
    
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
            
            this.locics = new HashSet<Locic>(array.size()+10);
            
            for(int i = 0; i<array.size(); i++) {
                // better use iterator?
                
                object      = (JSONObject) array.get(i);
                        
                ip          = (String) object.get("ip");        
                subnet      = (String) object.get("subnet");      
                location    = (String) object.get("location");
                
                this.locics.add(new Locic(InetAddress.getByName(ip), subnet, location));
                
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
    
    public HashSet<Locic> getLocics() {
        return this.locics;
    }
    
} // class
