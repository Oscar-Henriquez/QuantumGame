import socket
import qiskit as qk
from qiskit import ClassicalRegister, QuantumRegister, QuantumCircuit
from qiskit import execute, Aer
from qiskit import IBMQ
from qiskit.providers.aer import noise
import math
import numpy as np
import random

HOST = '192.168.43.135'  # Standard loopback interface address (localhost)
PORT = 65432        # Port to listen on (non-privileged ports are > 1023)


def game_run(input_string):
    '''
    creates the quantum circuit from the string of gates
    '''
    q = QuantumRegister(3)
    c = ClassicalRegister(3)
    qc = QuantumCircuit(q,c)
    i = 0
    for unitary in input_string:
        if(unitary == 'h'):
            qc.h(i%3)
        elif(unitary == 'a'):
            qc.rx(.8*math.pi, i%3)
        elif(unitary == 'b'):
            qc.ry(math.pi/3, i%3)
        elif(unitary == 'c'):
            qc.ry(.25*math.pi, i%3)
        elif(unitary == 'i'):
            qc.iden(i%3)
        i+=1
    [qc.measure(j,j) for j in range(3)]
    return qc

def end_to_end(input_string, real_device=True):
    '''
    in case we ever want to play solitaire
    '''
    circuit = game_run(input_string)
    emulator = Aer.get_backend('qasm_simulator')
    if real_device:
        IBMQ.load_account()
        provider = IBMQ.get_provider(hub='ibm-q')
        real_device = provider.get_backend('ibmq_16_melbourne')
        properties = real_device.properties()
        coupling_map = real_device.configuration().coupling_map
        noise_model = noise.device.basic_device_noise_model(properties)
        job = execute(circuit, emulator, shots=10, noise_model=noise_model,
                         coupling_map = coupling_map,
                         basis_gates = noise_model.basis_gates, memory=True)
    else:
        job = execute(circuit, emulator, shots=10, memory=True)
    sample = job.result().get_memory()
    results = ''
    for point in sample:
        results += str(int(point,2))
    return str(results)

def end_to_end_2p(input_string, real_device = True):
    '''
    args.
        input_string: 12char, space, 12char characterizing the final state of the game
        real_device: boolean, if True it accesses IBMQ melbourne and simulates its noise
    IF real_device==True, you need to have your own IBMQ credentials
    '''
    p1, p2 = str.split(input_string)
    results = ''
        
    circuit1 = game_run(p1)
    circuit2 = game_run(p2)
    
    emulator = Aer.get_backend('qasm_simulator')
    
    if real_device: 
        IBMQ.load_account()
        provider = IBMQ.get_provider(hub='ibm-q')
        real_device = provider.get_backend('ibmq_16_melbourne')
        properties= real_device.properties()
        coupling_map = real_device.configuration().coupling_map
        noise_model = noise.device.basic_device_noise_model(properties)
        job1 = execute(circuit1, emulator, shots=10, noise_model=noise_model,
                         coupling_map = coupling_map,
                         basis_gates = noise_model.basis_gates, memory=True)
        job2 = execute(circuit2, emulator, shots=10, noise_model=noise_model,
                         coupling_map = coupling_map,
                         basis_gates = noise_model.basis_gates, memory=True)
    else:
        job1 = execute(circuit1, emulator, shots=10, memory=True)
        job2 = execute(circuit2, emulator, shots=10, memory=True)
        
    sample1 = job1.result().get_memory()
    sample2 = job2.result().get_memory()

    for point in sample1:
        results += str(int(point,2))
    results += ' '
    for point in sample2:
        results += str(int(point,2))
    return results
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
   s.bind((HOST, PORT))
   s.listen()
   while True:
       conn, addr = s.accept()
       with conn:
           print('Connected by', addr)
           while True:
               data = conn.recv(1024)
               if not data:
                   break
               ret_info = end_to_end_2p(data)
               conn.sendall(ret_info.encode())