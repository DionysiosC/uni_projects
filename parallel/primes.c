#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#define UPTO 10000000

long int count,      /* number of primes */
         lastprime;  /* the last prime found */

double start ,end; /* to save the time stamps */ 
int threads;

void serial_primes(long int n) {
	long int i, num, divisor, quotient, remainder;

	if (n < 2) return;
	count = 1;                         /* 2 is the first prime */
	lastprime = 2;

	for (i = 0; i < (n-1)/2; ++i) {    /* For every odd number */
		num = 2*i + 3;

		divisor = 1;
		do 
		{
			divisor += 2;                  /* Divide by the next odd */
			quotient  = num / divisor; // phliko
			remainder = num % divisor;  // ypoloipo
		} while (remainder && divisor <= quotient);  /* Don't go past sqrt */

		if (remainder || divisor == num) /* num is prime */
		{
			count++;
			lastprime = num;
		}
	}
}

void openmp_primes(long int n) {
	long int i, num, divisor, quotient, remainder;
	if (n < 2) return;
	count = 1;                         /* 2 is the first prime */
	lastprime = 2;

	omp_set_dynamic(0);
	
	#pragma omp parallel for schedule(dynamic) private(i, num, divisor, quotient, remainder) reduction(max:lastprime) reduction(+:count) num_threads(threads)
	    for (i = 0; i < (n-1)/2; ++i) {    /* For every odd number */
		    num = 2*i + 3;

		    divisor = 1;
		    do 
		    {
			    divisor += 2;                  /* Divide by the next odd */
			    quotient  = num / divisor; 
			    remainder = num % divisor; 
		    } while (remainder && divisor <= quotient);  /* Don't go past sqrt */

		    if (remainder || divisor == num) /* num is prime */
		    {
				count++;		        				
				lastprime = num;
			    
		    }
	    }

}

int main(int argc, char **argv)
{
	threads = atoi(argv[1]);
	printf("Serial and parallel prime number calculations:\n\n");
	
	/* Time the following to compare performance 
	 */
	start = omp_get_wtime();
	serial_primes(UPTO);    
	end = omp_get_wtime(); 
	printf("[serial] count = %ld, last = %ld (time = %f)\n", count, lastprime,(double) end-start);

	start = omp_get_wtime();	
	openmp_primes(UPTO);
	end = omp_get_wtime(); 	
	printf("[parallel, dynamic] count = %ld, last = %ld (time = %f)\n", count, lastprime,(double) end-start);

	return 0;
}
