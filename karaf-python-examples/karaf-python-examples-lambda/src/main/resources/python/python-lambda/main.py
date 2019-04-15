#!/usr/bin/python3
import socket, os

socket_file = "/var/run/spliter.sock"
if os.path.exists(socket_file):
    os.remove(socket_file)

server = socket.socket(socket.AF_UNIX, socket.SOCK_DGRAM)
server.bind(socket_file)

print("Listening...")
while True:
    data = server.recv(1024)
    print(data)