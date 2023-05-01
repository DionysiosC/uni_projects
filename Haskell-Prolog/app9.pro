% APP 9
% The dipole divisor of a positive integer n is defined in the following way:
% * If n = 1 or n is a prime number, then the dipole divisor of n is itself.
% * If n is a complex number, then the dipole divisor of n is the product of the largest prime number that divides n times the smallest prime number that divides n.
% Here is a program in Prolog to calculate the dipole dihedral of a given positive integer. 
% In particular, define a predicate bipolarDivisor(N,D), which will hold true if the value of N is a positive integer n and the value of D is the bipolar divisor of n.


%Finds the maximum first divisor. must always C = N
findMax(P,C,F):- C=:=1, F is 1.
findMax(P,C,F):- 0 =:= P mod (C-1), isprime(C-1), F is (C-1).
findMax(P,C,F):- findMax(P,C-1,F).

%Finds the minimum first divisor. must always C = 2
findMin(P,C,F):- P=:=C, F is P.
findMin(P,C,F):- 0 =:= P mod C, isprime(C), F is C.
findMin(P,C,F):- findMin(P,C+1,F).

bipolarDivisor(1,D) :- D is 1.
bipolarDivisor(N,D) :- isprime(N), D is N.
bipolarDivisor(N,D) :- 	findMin(N,2,P),
						findMax(N,N,G),
						D is G*P.

%Checks if N is prime
isprime(N):- isprimeHlp(N,2).
												
isprimeHlp(P,I):- P =:= I.
isprimeHlp(P,I):- P>I, 0 =\= (P mod I), isprimeHlp(P,I+1).


%-----------------------------------------------------------------------------------------

%-- ASKHSH 2

divide(0,_,0).
divide(_,0,undefined).
divide(s(0),s(0),s(0)).

divide(X,Y,s(D)):- sum(Y,R,X), divide(R,Y,D). 

nat(0).
nat(s(X)):- nat(X).
 
sum(X,0,X):-nat(X).
sum(X,s(Y),s(Z)):-sum(X,Y,Z).


%-----------------------------------------------------------------------------------------