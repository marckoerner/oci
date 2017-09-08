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
The Global OCI Coordinator is used as tenant interface and forwards the tanant information to the Local OCI Coordinator.

### Local OCI Coordinator
The Local OCI Coordinator interacts with the edge resource and orchestration manager as well as with the OCI data plane.

### OCI Resource and Orchestration Manager
The OCI RnOM is an abstraction layer for the resource management.

### OCI data plane
The OCI data plane processes fast packet header lookups for the OCI name resolution and works also as edge service address cache.

### OCI lib
The OCI lib contains the required Socket libs for the implementation of Client-Edge-Server applications using the OCI. It further provides templates for creating the edge service and the regarding discovery service.   

### Mock Echo Client
A simple echo client application

### Mock Echo Edge
A simple echo edge application
