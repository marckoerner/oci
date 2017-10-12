#!/usr/bin/python

#from mininet.net import Mininet
#from mininet.cli import CLI

import socket
import sys

#net = Mininet()
#h1 = net.addHost('h1')

#h2 = net.addHost('h2')
#s1 = net.addSwitch('s1')

#net.addLink(s1,h1)
#net.addLink(s1,h2)

#net.build()
#CLI(net)  

def startService(name, socket):
    print "start service", name
    out = 'service %s started\n' % name
    socket.sendall(out)
    print out
    return True

def stopService(name, socket):
    print "stop service", name
    out = 'service %s stopped\n' % name
    socket.sendall(out)
    print out
    return True

def isRunning(name, socket):
    print "is service %s running" % name
    out = 'service %s is running\n' % name
    socket.sendall(out)
    print out
    return True

def getAddress(name, socket):
    print "service %s has IP x.y.z" % name
    out = '%s service has IP x.y.z\n' % name
    socket.sendall(out)
    print out
    return "192.168.x.y"

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
                    func(command[1], connection)

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

