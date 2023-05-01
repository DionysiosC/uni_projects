-- APP 1
-- A function that computes the final grade of a uni student. Input: <Final grade> <Lab grade>
grade :: Int->Int->Int                           

grade a b
	| (a<0 || a>100)||(b<0 || b>20)  = (-1)
	| (c>47)&&(a<=47) = 47
	| (c>47)&&(a>47)&&(c<50) = 50
	| otherwise = c
		where c = ((8 * a) `div` 10) + b

digits :: Int->Int->Int                         

-- APP 2
-- A function that calculates a prize awarded at a lucky draw (8 digit number, between 10000000 and 99999999). The prize depends on the number of the digits. 
-- Takes as arguments the lucky number and the number chosen by a player and calculates the amount won by the player.
digits x y
	|(digitCount x y)== 8 = 1000000
	|(digitCount x y)== 7 = 100000
	|(digitCount x y)== 6 = 8000
	|(digitCount x y)== 5 = 300
	|(digitCount x y)== 4 = 20
	|(digitCount x y)== 3 = 5
	|(digitCount x y)== 2 = 1
	|otherwise = 0


--A function that returns the number of the same digits of 2 numbers of the same length.
digitCount :: Int->Int->Int
digitCount x y
	| x == 0 || y == 0 = 0
	| otherwise = digitCount(x `div` 10) (y `div` 10) + q
		where q = if ((x `mod` 10)==(y `mod` 10)) then 1 else 0

