Open Carrier Interface
==============

This implementation of the Open Carrier Interface is composed out of the following components:

- Global OCI Coordinator
- Local OCI Coordinator
- OCI data plane
- OCI lib

### Global OCI Coordinator
The Global OCI Coordinator is used as tenant interface and forwards the tanant information to the Local OCI Coordinator.

### Local OCI Coordinator
The Local OCI Coordinator interacts with the edge resource and orchestration manager as well as with the OCI data plane

### OCI data plane
The OCI data plane processes fast packet header lookups for the OCI name resolution and works also as edge service address cache

### OCI lib
The OCI lib contains the required Socket libs for the implementation of Client-Edge-Server applications using the OCI. It further provides templates for creating the edge service and the regarding discovery service.   

