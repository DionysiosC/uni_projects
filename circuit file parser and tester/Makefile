# Makefile

CC = gcc
CFLAGS = -g

all: main

main: main.o circuitFunctions.o
	$(CC) $(CFLAGS) -o main main.o circuitFunctions.o

main.o: main.c circuit.h
	$(CC) $(CFLAGS) -c main.c

circuitFunctions.o: circuitFunctions.c circuit.h
	$(CC) $(CFLAGS) -c circuitFunctions.c

clean:
	rm -f main main.o circuitFunctions.o
