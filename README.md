Open Carrier Interface
==============

This implementation of the Open Carrier Interface is composed out of the following components:

- Global OCI Coordinator
- Local OCI Coordinator
- OCI Resource and Orchestration Manager
- OCI data plane
- OCI lib

It also contains a mock/dummy implementation of a client-edge application utilizing the OCI paradigm by using the OCI-lib.

- Mock Echo Client
- Mock Echo Edge

### Global OCI Coordinator
The GOCIC is the global management entity, which has knowledge about the carrier network topology and the location of the central offices (aka edges). At the moment, this information is provided by a JSON configuration file. It further implements a RESTful tenant interface, which allows the application service provider to upload her edge application and specify the regarding meta-data information, e.g. the geographical deployment location. It further communicates with the LOCIC in order to copy the tenants edge application or provide the information of a co-located edge to re-navigate clients to an alternative edge, in case of insufficient resources.

### Local OCI Coordinator
The LOCIC is the actual OCI edge management entity. It communicates with the GOCIC and the MnOM in order to start or stop edge services. It further maintains a list of all running edge services and their IP addresses. In the current development stage, it also provides the client interface for the OCI name service. This interface allows clients to send edge service queries by using a name resolution method of the OCIlib. The reply on such a query will provide the IP address of the requested edge service. This mechanism will later be outsourced to the OCI classifier.

### OCI Resource and Orchestration Manager
The RnOM is an abstraction layer for the underlying actual resource and orchestration framework. It provides a generic interface to adapt an arbitrary resource management API. This API provides methods to request resources and start or stop edge services, or just obtain their status and network address. Future versions will also deal with orchestration information for component based edge services and their scalability.

### OCI data plane
The OCI classifier is a software component which operates within ISO/OSI layer two (L2) and sits directly in packet forwarding path at the edge. Similar to a SDN data-plane, the OCI classifier processes packet header lookups for not established flows in order to identify OCI name service requests. Cached name entries where directly answered, while for un-cached entries an inquiry is send to the LOCIC. All other flows can be bypassed by instructions from the LOCIC to the resource management system, e.g. SDN controller, in order to reduce the lookup overhead. This component can be replicated for scalability. It further increases the systems security, since the LOCIC address is not exposed at all and attackers cannot flood the edge with DDoS attacks.

### OCI lib
The OCIlib contains methods and templates for application service developers to build client- edge-server (C-E-S) applications. Therefore, it provides edge service templates and methods which can be used by the client application to obtain the edge service address via service name lookup. Future versions of this library will also provide additional methods to make the developers life easier and support them with mechanisms to improve their C-E-S applications with resilience, availability and consistency features. 

### Mock Echo Client
The echo client is a dummy implementation of a client service using the OCIlib. The client serves as evaluation tool.

### Mock Echo Edge
The echo edge service is the counterpart of the echo client. It uses also the OCIlib and serves as edge mock service.
Mininet Resource and Orchestration Management Adapter
This tool is an adapter for the mininet network emulator and serves as actual resource and orchestration manager test environment.
