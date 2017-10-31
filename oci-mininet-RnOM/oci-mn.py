
# imports
from mininet.net import Mininet
from mininet.cli import CLI

import socket
import sys

# method definitions
def startService(name, socket, m_net, switch):
    print "start service", name
    host = m_net.addHost(name)
    net.addLink(switch,host)

    interfaces = switch.intfs
    #print "Interfaces: ", interfaces
    #print "len: ", len(interfaces)

    if_nr, if_name = interfaces.items()[len(interfaces)-1]
    #print "interface name: ", if_name
    
    switch.attach(if_name)
    host.configDefault(defaultRoute = host.defaultIntf())

    # print network configuration information
    print "net: ", net
    print "m_net", m_net
    print host.cmd('ifconfig')

    # Test network function
    h1 = net.getNodeByName('h1')
    print host.cmd( 'ping -c4 ', h1.IP() )

    # start ssh server
    host.cmd('/usr/sbin/sshd -D &')

    out = 'service %s started\n' % name
    socket.sendall(out)
    print out
    return True

def stopService(name, socket, m_net, switch):
    print "stop service", name

    host = m_net.get(name)
    print "get host: ", host
    m_net.delHost(host)
    print "host deleted"

    out = 'service %s stopped\n' % name
    socket.sendall(out)
    print out
    return True

def isRunning(name, socket, m_net, switch):
    print "is service %s running" % name
    out = 'service %s is running\n' % name

    # if host with name NAME exist?

    socket.sendall(out)
    print out
    return True

def getAddress(name, socket, m_net, switch):
    # this method blocks if no host with name exists
    host = net.get(name)
    #print "host: ", host
    host_ip = host.IP()
    #print "host.IP: ", host_ip
    out = name + ' service has IP: ' + host_ip + '\n'
    socket.sendall(out)
    print out
    return host_ip

def disconnect(name, socket):
    print "disconnect"
    out = 'disconnected\n'
    socket.sendall(out)
    print out
    return True

def error(name, socket):
    print "No command found!"
    out = 'command not found\n'
    socket.sendall(out)
    return False

def string_to_function(argument):
    switcher = {
        "start": startService,
        "stop": stopService,
        "isRunning": isRunning,
        "disconnect": disconnect,
        "getAddress": getAddress
    }
    func = switcher.get(argument, error)
    return func
    
# main
print "#######################################"
print "### Mininet Resource Manager Daemon ###"
print "#######################################"

# set up daemon connection
port = 8383
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = ('', port)
sock.bind(server_address)
sock.listen(1)

# build initial mininet topology
net = Mininet()
c0 = net.addController(name='c0',
#                     controller=Controller,
                     port=6633)
h1 = net.addHost('h1')
h2 = net.addHost('h2')
s1 = net.addSwitch('s1')
net.addLink(s1,h1)
net.addLink(s1,h2)
for controller in net.controllers:
    controller.start()
net.get('s1').start([c0])
#net.build()
net.start()
#CLI(net)

# main programm loop / wait for instructions from OCI RnOM
while True:
    # Wait for a connection
    print "waiting for a connection on port ", port
    connection, client_address = sock.accept()

    try:
        print 'connection from', client_address

        # Receive the data in small chunks and retransmit it
        while True:
            data = connection.recv(32)
            print 'received command "%s"' % data
            if data:
                command = data.split( )
                length = len(command)
                #func = string_to_command()

                if length == 2:
                    
                    func = string_to_function(command[0])
                    func(command[1], connection, net, s1)

                elif length == 1:

                    func = string_to_function(command[0])
                    func("", connection)

                else:
                    error("error", connection)

            else:
                print 'no more data from', client_address
                break
            
    finally:
        # Clean up the connection
        connection.close()
	net.stop()

