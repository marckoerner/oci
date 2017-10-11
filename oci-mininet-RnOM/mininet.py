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

def startService(name):
    print "start service", name
    return True

def stopService(name):
    print "stop service", name
    return True

def isRunning(name):
    print "is service %s running" % name
    return True

def getAddress(name):
    print "service %s has IP x.y.z" % name
    return "192.168.x.y"

def disconnect(name):
    print "disconnect"
    return True

def error(name):
    print "No command found!"
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
                    func(command[1])

                elif length == 1:

                    func = string_to_function(command[0])
                    func("")

                else:
                    error("error")

                #print 'command: ', command 
                #print 'command[0]: "%s"' % command[0]
                #print 'command[1]: "%s"' % command[1]

                #func = string_to_function(command[0])
                #func(command[1])
                #if data == "start\n":
                #    print "startService"
                #elif data == "stop\n":
                #    print "stopService"
                #else:
                #    print "ELSE"

                print 'sending data and command back to the client'
                connection.sendall(data)
                
            else:
                print 'no more data from', client_address
                break
            
    finally:
        # Clean up the connection
        connection.close()

