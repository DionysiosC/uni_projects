-- APP 5
--  The results of a football team's matches during the league, can be represented as a list of pairs of integers. Each pair represents the score 
--  result of a match, where the first element of the pair corresponds to the goals scored in the match scored by the team 
--  and the second element to the goals scored by the opposing team in that particular match.

-- By giving a list of a team's results in the league, we want to
-- construct a set of five numbers describing the following team statistics:
--  * the total number of matches played
--  * the total number of points won, assuming that for every win the team wins three points and one point for each draw
--  * the total number of goals scored
--  * the total number of goals conceded
--  * the goal difference in the best result achieved. This difference will be positive if the team has achieved at least one win, zero if it has not achieved a win but has 
--    at least one draw and negative if the team has lost all its matches.
--  The function takes as an argument the list of results of a team and return a quintuple of integers with the statistics described in described above.
-- i.e. Main> statistics [(0,1),(1,3),(1,2)]
--      (3,0,2,6,-1)

statistics :: [(Int,Int)]->(Int,Int,Int,Int,Int)

statistics s
	|null s = (0,0,0,0,0)
	|otherwise = (length s, score s, goals s, goalsTaken s, dif s)

--
score :: [(Int,Int)]->Int
score (h:t) = (scoreHlp h) + score t
score [] = 0

scoreHlp :: (Int,Int)->Int
scoreHlp (a,b)
	|a>b = 3
	|a==b = 1
	|otherwise = 0
--
goals :: [(Int,Int)]->Int
goals ((a,b):t) = a + goals t 
goals [] = 0

--
goalsTaken :: [(Int,Int)]->Int
goalsTaken ((a,b):t) = b + goalsTaken t 
goalsTaken [] = 0

--
dif :: [(Int,Int)]->Int
dif s = (maxIntList (difHlp s))

difHlp :: [(Int,Int)]->[Int]
difHlp ((a,b):[]) = [a-b]
difHlp ((a,b):t) = (a-b):(difHlp t)

maxIntList :: [Int]->Int
maxIntList (h:[]) = h
maxIntList (h:t) = max h (maxIntList t)

-- APP 6
-- The function takes as an argument a string w and returns a basic partition of w in a list.
-- i.e. Main> partition "sun"
--      [["s"],["u"],["n"]]

partition :: String->[[String]]
partition w
	|length w == 1 || null w
		= []
	|otherwise
		= partitionHlp w (power 2 (length w))

partitionHlp :: String -> Int -> [[[Char]]]
partitionHlp s 0 = []
partitionHlp [] n = []
partitionHlp s n = [headString s]: partitionHlp (tail s) (n-1)

headString :: String -> [Char]
headString a
	|length a == 1
		= a
	|otherwise
		= headString (init a)
		
power :: Int -> Int -> Int
power n 0 = 1
power n k
	|k `mod` 2 == 0
		=p*p
	|otherwise 
		=p*p*n
	where p = power n (k `div` 2)
