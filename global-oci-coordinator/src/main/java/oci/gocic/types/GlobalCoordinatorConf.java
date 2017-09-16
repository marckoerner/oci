package oci.gocic.types;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GlobalCoordinatorConf {

	List<LocalCoordinator> localCoordinators = new ArrayList<>();

	public void addLocalCoordinator(int id, InetAddress ip, String location) {
		LocalCoordinator lc = new LocalCoordinator(id, ip, location);	
		localCoordinators.add(lc);		
	}

	public boolean removeLocalCoordinator(int id) {

		Iterator<LocalCoordinator> lcListItr = localCoordinators.iterator();
		while (lcListItr.hasNext())	{
			LocalCoordinator currentLc = lcListItr.next();

			if (currentLc.getId() == id) {
				return localCoordinators.remove(currentLc);

			}
		}		
		return false;
	}

	public List<LocalCoordinator> getLocalCoordinators() {
		return localCoordinators;
	}

	public void setLocalCoordinators(List<LocalCoordinator> localCoordinators) {
		this.localCoordinators = localCoordinators;
	}

}
