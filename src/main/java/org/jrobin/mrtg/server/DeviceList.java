/* ============================================================
 * JRobin : Pure java implementation of RRDTool's functionality
 * ============================================================
 *
 * Project Info:  http://www.jrobin.org
 * Project Lead:  Sasa Markovic (saxon@jrobin.org);
 *
 * (C) Copyright 2003, by Sasa Markovic.
 *
 * Developers:    Sasa Markovic (saxon@jrobin.org)
 *                Arne Vandamme (cobralord@jrobin.org)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jrobin.mrtg.server;

import java.util.Vector;

class DeviceList {
	private Vector routers = new Vector();

	DeviceList() {	}

	Vector getRouters() {
		return routers;
	}

	void setRouters(Vector routers) {
		this.routers = routers;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		for(int i = 0; i < routers.size(); i++) {
			buff.append(routers.get(i));
		}
		return buff.toString();
	}

	Device getRouterByHost(String host) {
        for(int i = 0; i < routers.size(); i++) {
			Device router = (Device) routers.get(i);
			if(router.getHost().equalsIgnoreCase(host)) {
				return router;
			}
		}
		return null;
	}

	int addRouter(String host, String community, String descr, boolean active) {
		Device router = getRouterByHost(host);
		if(router == null) {
			// not found
			Device newRouter = new Device();
            newRouter.setHost(host);
			newRouter.setCommunity(community);
			newRouter.setDescr(descr);
			newRouter.setActive(active);
			routers.add(newRouter);
			// added
			return 0;
		}
		// error, already exists
		return -1;
	}

    int updateRouter(String host, String community, String descr, boolean active) {
        Device router = getRouterByHost(host);
		if(router != null) {
			router.setCommunity(community);
			router.setDescr(descr);
			router.setActive(active);
			return 0;
		}
		// not found, not updated
		return -1;
	}

	int removeRouter(String host) {
		Device router = getRouterByHost(host);
		if(router == null) {
			// not found, cannot remove
			return -1;
		}
		// remove router only if no links are attached
		if(router.getLinkCount() > 0) {
			return -2;
		}
		routers.remove(router);
		return 0;
	}
	/**
	 * @deprecated user have to specify SNMP-ver (now v.2 is dafault) 
	 * 
	 * @author vipup
	 * @param host
	 * @param ifDescr
	 * @param descr
	 * @param samplingInterval
	 * @param active
	 * @return
	 */
	int addLink(String host, String ifDescr, String descr, int samplingInterval, boolean active) {
		return addLink(host, ifDescr, 2, descr, samplingInterval, active); 
	}
	int addLink(String hostPar, String ifDescrPar, int snmpVersionPar, String descrPar, int samplingIntervalPar, boolean isActivePar) {
        Device router = getRouterByHost(hostPar);
		if(router == null) {
			// router not found, link cannot be added
            return -1;
		}
        Port link = null;
        try{
        	link = router.getLinkByIfDescr(ifDescrPar);
        }catch(NullPointerException e){
        	
        }
		if(link != null && link.getDescr().equals(descrPar) ) {
			// such link already exists, link cannot be added
			return -2;
		}else if(link != null  ){// there are ling with path BUT! diff OID
			return -255;
		}
        Port newLink = new Port();
		newLink.setDescr(descrPar);
		newLink.setIfAlias(descrPar);
		newLink.setIfDescr(ifDescrPar);
		newLink.setSamplingInterval(samplingIntervalPar);
		newLink.setActive(isActivePar);
		newLink.setSnmpVersion(snmpVersionPar); 		
		router.addLink(newLink);
		return 0;
	}

	int updateLink(String host, String ifDescr, String descr, int samplingInterval, boolean active) {
		Device router = getRouterByHost(host);
		if(router == null) {
			// router not found, link cannot be updated
            return -1;
		}
        Port link = router.getLinkByIfDescr(ifDescr);
		if(link == null) {
			// such link cannot be found and updated
			return -2;
		}
		link.setDescr(descr);
		link.setSamplingInterval(samplingInterval);
		link.setActive(active);
		return 0;
	}

	int removeLink(String host, String ifDescr) {
		Device router = getRouterByHost(host);
		if(router == null) {
			// router not found, link cannot be removed
            return -1;
		}
        Port link = router.getLinkByIfDescr(ifDescr);
		if(link == null) {
			// such link cannot be found and removed
			return -2;
		}
		router.removeLink(link);
		return 0;
	}
}

