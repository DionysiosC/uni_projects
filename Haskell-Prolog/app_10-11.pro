% APP 10
% In a vote for the best footballer of the year, n journalists vote and the winner is the one who gets more than n/2 votes. If no candidate does not get more than n/2 votes then there is no winner
% Here is a program in Prolog which, given a list of journalists' votes, will find the winner of the vote, if any. In particular, define a predicate majority(L,X),
% which will hold true if X appears in more positions than half of the length of the list L.

% i.e.:
% | ?- majority(['Ronaldo', 'Ronaldo', 'Messi', 'Messi', 'Ronaldo', 'Ronaldo', 'Ronaldo', 'Salah', 'Mbappe'], X).
% X = 'Ronaldo'  

majority([],C) :- fail.
majority([H|T],H) :- majorityHlp([H|T],H,D), length([H|T],L), D > (L//2).
majority([H|T],C) :- majority(T,C),majorityHlp([H|T],H,D), length([H|T],L), D < (L//2).

%Counts the occurences of an element in a list.
majorityHlp([],X,0).
majorityHlp([X|T],X,Y):- majorityHlp(T,X,Z), Y is 1+Z.
majorityHlp([Q|T],X,Z):- Q\==X, majorityHlp(T,X,Z).

%Finds the length of a list -- Used for both exersices.
length([],X):-fail.
length([H|T],X):- length(T,Z), X is Z+1.

% APP 11
% We can add the element 0 to the end of a list L iteratively,
% until we get a list whose length is a power of 2. We call the resulting list by this procedure a 2^n-extension of L.
% If the length of L is a power of 2, then the 2^n-extension is L itself.
% Here is a program in Prolog which calculates the 2^n-expansion of a given list.
%i.e.
% | ?- expand([a1,a2,a3,b1,b2,b3,c1,c2,c3],E).
% E = [a1,a2,a3,b1,b2,b3,c1,c2,c3,0,0,0,0,0,0,0] 

% | ?- expand([.,:,...,::,:.:,:::],E).
% E = [’.’,:,...,::,:.:,:::,0,0]

expand([],[0]).
expand([X],[X]).
expand(L,L) :- length(L,D), check(D).
expand(L,D) :- insert(0,L,E), expand(E,D).


%Insert at the end of a list the element X.
insert(X,[],[X]).
insert(X,[H|T],[H|Z]) :- insert(X,T,Z).    

%Check if for int n is true that: 2^n = L.
check(0).
check(2).
check(L):- L mod 2 =:= 0, N is L//2, check(N).



%-----------------------------------------------------------------------------------------

