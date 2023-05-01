-- APP 3
-- Takes as argument a positive integer n and returns a pair of integers (a, b) such that 1 ≤ a ≤ b, a - b = n and b - a is the minimum possible. 

ab :: Int->(Int,Int) 
ab n = (abmod 1 n, n `div` abmod 1 n)

abmod :: Int->Int->Int
abmod a n
	|(n `mod` a == 0) && a>=n `div` a 
		= a
	|otherwise
		= abmod (a+1) n

-- APP 4
-- A Haskell function that takes as arguments two non-negative integer values m,n and calculates the sum: s = (n+i)^m for i=m, ..., n

sum2021 :: Integer->Integer->Integer             
sum2021 m n = sum2021Hlp m n m
	
sum2021Hlp :: Integer->Integer->Integer->Integer
sum2021Hlp m n a
	|n<m
		=0
	|otherwise
		=((n+m)^a) + sum2021Hlp (m+1) n a