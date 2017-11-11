
# imports
from mininet.net import Mininet
from mininet.cli import CLI
from mininet.node import Node, OVSBridge

import socket
import sys

gateway_ip = '10.100.100.1'

# method definitions
def startService(name, socket, m_net, switch):
    print "start service", name

    # add host and default route
    route = 'via ' + gateway_ip
    host = m_net.addHost(name, defaultRoute=route)

    m_net.addLink(switch, host)
    
    # attach added interface
    interfaces = switch.intfs
    if_nr, if_name = interfaces.items()[len(interfaces)-1]
    switch.attach(if_name)
    #host.configDefault(defaultRoute = host.defaultIntf())
    host.configDefault()

    # print network configuration information
    print host.cmd('ifconfig')

    # test network function
    #print h1.cmd('ping -c4 ', host.IP())

    # start ssh server
    host.cmd('/usr/sbin/sshd -D &')

    # start java edge service
    cmd_string = 'java -jar /home/mininet/oci/%s.jar &' %name
    print cmd_string
    print host.cmd(cmd_string)

    out = 'service %s started\n' % name
    socket.sendall(out)
    print out
    return True

def stopService(name, socket, m_net, switch):
    print "stop service", name

    host = m_net.get(name)
    #print "get host: ", host

    #switch.detach()
    # kill java 
    cmd = "ps -ef | grep " + name + " | grep -v grep | awk '{ print $2 }'"
    pid = host.cmd(cmd)
    host.cmd('kill ' + pid)

    # kill sshd 
    cmd = '/usr/sbin/sshd'
    host.cmd( 'kill %' + cmd )

    # delete links and host
    m_net.delLinkBetween(switch, host)
    m_net.delHost(host)
    host.stop()
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

# get cmd arguments
if len(sys.argv) != 2:
    print ""
    print "Usage: python oci-mn.py [NUMBER]"
    print "Starts the oci mininet resource manager daemon and mininet network "
    print "with an OVSBridge star topology and NUMBER client hosts."
    print ""
    exit()

item_2 = sys.argv[1]
number = int(item_2)

# set up daemon connection
port = 8383
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = ('', port)
sock.bind(server_address)
sock.listen(1)

# build initial mininet topology
net = Mininet()
s1 = net.addSwitch('s1', cls=OVSBridge)

# Create a gateway node in root namespace
ip = gateway_ip + '/8'
root = Node('root', inNamespace=False)
intf = net.addLink(root, s1).intf1
root.setIP(ip, intf=intf)

# create client nodes
for n in xrange(1, number+1):
    tmp_name = 'h' + str(n)
    print "add client host ", tmp_name
    tmp_route = 'via ' + gateway_ip
    tmp_host = net.addHost(tmp_name, defaultRoute=tmp_route)
    net.addLink(s1,tmp_host)
    # start sshd on all hosts
    tmp_host.cmd('/usr/sbin/sshd -D &')


#net.build()
net.start()
#CLI(net)
#net.stop()

# main programm loop / wait for instructions from OCI RnOM
try:
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
	    #print "finally"
            connection.close()
	    #root.stop()
	    #net.stop()
	    print "connection closed"

finally:
    # terminate sshd processes
    print "kill all sshd"
    cmd = '/usr/sbin/sshd'
    for host in net.hosts:
        host.cmd('kill %' + cmd)
        host.cmd('killall java')

    print "close mn network"
    #root.stop()
    net.stop()
    print "EXIT"

