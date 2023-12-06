# Docs
This is some of my work done in the context of the parallel programming course of my uni, in 2023. Every program was compiled using gcc v11.2.0.

## Primes.c
This is a program that finds all the prime numbers from 0 to 10000000 or the UPTO defined macro in the source code, using a serial and OpenMP parellization.
The objective is to see the time difference between the two implementations.
To compile, do:
```
gcc primes.c -fopenmp
```
To run, you can simply do:
```
./a.out <number_of_threads>
```
For example:
To run, do:
```
./a.out 4
```
in order to create and use 4 threads.
Results are printed on the terminal.

## gaussian_blur.c
This is a program that blurs a bmp image (24-bit color depth) using the gaussian blur method.
OpenMP is used here to demonstrate its ability to parallelize **for** loops, either traditionally or by using OpenMP tasks. Both methodologies have been used here.
They are both compared to the sequential gaussian blur algorithm, that does not use parellization.
To compile, use:
```
gcc gaussian_blur.c -fopenmp -lm
```
To run use:
```
./a.out <blur-radius> <filename> <no of threads>
```
Make sure the bmp images are located in the same directory with the executable files.
Status reports are printed on the terminal and new blurred images are created in the same folder.