-- APP 7
-- A function which will take as arguments a list s of any of type that supports comparison for equality, an element x of the same type as the elements of the list, and an integer n,
-- and return the list resulting from s in the following way:
-- * if x is not contained in s, then the returned list is s.
-- * if x is contained in s, then:

-- 	** if n is a positive number, then the returned list is obtained from s by moving the first occurrence of x by n positions to the right,
--	  or to the end of the list if the first occurrence of x in s is followed by fewer than n items.

--  ** if n is a negative number, then the returned list is obtained from s by moving the number of items in the list to the left or right of the list. 
--    If the number of items is less than n, the returned list is returned by moving the first occurrence of item x by |n| positions to the left, 
--	  or to the beginning of the list if there are fewer than |n| elements in s before the first occurrence of x.

--  ** if n = 0, then the returned list is obtained from s by deleting the first occurrence of element x.

--	 i.e. Main> move "0000.1111.00" ’.’ (-10)
--		  ".00001111.00"

move :: Eq u => [u]->u->Int->[u]

move s x n
	|not(check x s)
		= s
	|otherwise
		= moveHlp s x n

------		
moveHlp :: Eq u => [u]->u->Int->[u]
moveHlp s x n
	|n == 0
		= delete x s
	|n>0
		= findAndMoveRight s x n
	|otherwise
		= findAndMoveLeft s x (abs n) 
		
------
findAndMoveRight :: Eq u => [u]->u->Int->[u]
findAndMoveRight (h:t) x n
	|h == x
		= moveRight (h:t) n
	|otherwise
		= h: findAndMoveRight t x n
findAndMoveRight [] x n = []
------
moveRight :: Eq u => [u]->Int->[u]
moveRight (h:t) 0 = (h:t)
moveRight (h:t) n = insertN h t n
------
insertN :: u->[u]->Int->[u]
insertN a (h:t) n
	|n == 1	 || null t
		= h:a:t
	|otherwise
		=h : insertN a t (n-1)
insertN a [] n = [a]
-------
findAndMoveLeft :: Eq u => [u]->u->Int->[u]
findAndMoveLeft s x n
	|(findPos s x 0) - n <= 0
		= [x]++(delete x s)
	|otherwise
		= insertN x (delete x s) ((findPos s x 0) - n)
findAndMoveLeft [] x n = []

-------
findPos :: Eq u => [u]->u->Int->Int
findPos (h:t) x c
	|h == x
		= c
	|otherwise
		= findPos t x (c+1)
findPos [] x c = (-1)

-------
delete::Eq u => u->[u]->[u]
delete n (h:t)
	|n == h
		= 	t
	|otherwise
		=h:delete n t
delete n [] = []

-------
check::Eq u => u->[u]->Bool
check n (h:t)
	|n == h
		= True
	| null t
		= False
	|otherwise
		= check n t
check n [] = False

