% APP 12
% Given a list L= [x1, x2, . . . . , xn] of length n and a list of integers I= [i1, i2, . . . . , ik] such that 1 ≤ i1 < i2 < . . . < ik ≤ n] 
% , we want to construct the list S= [xi1, xi2, . . . . . , xik ] (i.e. the list that is made up of the elements of L, whose positions are indicated by the elements of I).
% Here is a program in Prolog that will perform the above construction.

% | ?- pick([a,b,c,d,e,f,g,h],[2,4,7],S).
% S = [b,d,g]

pick(L,[],[]):-!. 
pick(L,[H],[S]) :- elemList(H,L,S).
pick(L,I,S) :- pickHlp(L,I,[],S).

pickHlp(L,[H|T],R,D):- elemList(H,L,G), check2([H|T]), insert(G,R,P), pickHlp(L,T,P,D).
pickHlp(L,[],R,R):-!.

check2([]).
check2([_]).
check2([H,X|T]):- H<X.

length([],X):-fail.
length([H|T],X):- length(T,Z), X is Z+1.

insert(X,[],[X]):-!.
insert(X,[H|T],[H|Z]) :- insert(X,T,Z).    

elemList(1,[H|T],H):-!.
elemList(N,[H|T],X):-M is N-1, elemList(M,T,X).
