%- APP 8
%- PROBLEM:

%- A transnational counter-terrorism agency collects information on a number of suspected terrorists and records events that may be related to terrorist activities. 
%- This data is recorded as events in Prolog. For convenience, the dates and times of the events are dates are represented using natural numbers starting from an initial date.
%- Among other things, the Prolog program contains events that define the following predicates:
%- * at(X,A,B,C): suspect X was in city C from day A to day B.
%- * event(E,C,D): event E occurred in city C on day D.
%- * country(C,S): city C is located in country S.
%- * dif(T1,T2): the term T1 is different from T2 (assumes that neither T1 nor T2 is a variable).

%- To process the above data, you are asked to write rules that define the following categories:
%- * q1(C1,C2): recorded events have occurred in cities C1 and C2 on the same day (the predicate should also hold true in the case where C1=C2).
%- * q3(S): Three different events have occurred in country S within a week (we consider a week to be the interval consisting of any 7 consecutive days).
%- * q4(X): X has been found in cities in two different countries on days in which events occur in these cities.

%- The answers presuppose that the predicates at, event, country and dif are defined from the sentences contained in this file.

q1(C1,C2) :- event(_,C1,Y), event(_,C2,Y).

q3(S) :- event(A,C,X), event(B,C,Y), event(B,C,Z), dif(A,B), dif(B,C), dif(A,C),abs(X-Y)<7,abs(Y-Z)<7,abs(X-Z)<7, country(C,S).

q4(X) :- at(X,Z1,W1,A), at(X,Z2,W2,B), event(_,A,Z), event(_,B,W),(Z1<Z),(Z2<W),(W1>Z),(W2>Z), country(A,S1), country(B,S2), dif(S1,S2).


%-- Do not modify any of the code below this point !

dif(X,Y) :- X \= Y.

at(tiger,1,14,'Berlin').
at(tiger,15,15,'Hamburg').
at(tiger,16,37,'Athens').
at(tiger,38,50,'Rome').
at(wolf,1,4,'Rome').
at(wolf,5,5,'London').
at(wolf,6,7,'Rome').
at(wolf,8,8,'Rotterdam').
at(wolf,9,24,'Rome').
at(wolf,25,25,'Athens').
at(wolf,26,30,'Rome').
at(wolf,31,40,'Paris').
at(wolf,41,41,'Rome').
at(wolf,42,42,'Barcelona').
at(wolf,43,50,'Rome').
at(hawk,1,10,'Berlin').
at(hawk,11,20,'Stuttgart').
at(hawk,21,35,'Hamburg').
at(hawk,36,50,'Frankfurt').
at(shark,1,16,'Amsterdam').
at(shark,17,20,'London').
at(shark,21,29,'Paris').
at(shark,30,45,'Rome').
at(shark,43,48,'Brussels').
at(shark,49,50,'London').
at(spider,1,12,'Brussels').
at(spider,13,17,'Berlin').
at(spider,18,50,'Brussels').
at(snake,1,10,'Rome').
at(snake,11,20,'Milan').
at(snake,21,50,'Berlin').

event(e001,'Berlin',2).
event(e002,'Madrid',3).
event(e003,'London',5).
event(e004,'Rome',7).
event(e005,'Bristol',10).
event(e006,'Stuttgart',13).
event(e007,'Milan',17).
event(e008,'Amsterdam',17).
event(e009,'Rotterdam',18).
event(e010,'Hamburg',24).
event(e011,'Amsterdam',24).
event(e012,'Athens',25).
event(e013,'Groningen',25).
event(e014,'Paris',31).
event(e015,'Strasbourg',31).
event(e016,'Paris',37).
event(e017,'Brussels',40).
event(e018,'Brussels',41).
event(e019,'Barcelona',42).
event(e020,'Frankfurt',43).
event(e021,'Brussels',43).
event(e022,'London',47).

country('Amsterdam','Netherlands').
country('Athens','Greece').
country('Barcelona','Spain').
country('Berlin','Germany').
country('Bristol','United Kingdom').
country('Brussels','Belgium').
country('Frankfurt','Germany').
country('Groningen','Netherlands').
country('Hamburg','Germany').
country('London','United Kingdom').
country('Madrid','Spain').
country('Milan','Italy').
country('Paris','France').
country('Rome','Italy').
country('Rotterdam','Netherlands').
country('Strasbourg','France').
country('Stuttgart','Germany').



train(X,Y,N) :- train___(X,Y,N). 
train(X,Y,N) :- train___(Y,X,N). 

train___(astralCity,frozenTown,5).
train___(astralCity,greenTown,24).
train___(astralCity,rainyPort,15).
train___(brightCity,kindTown,21).
train___(crazyCity,vainPort,5).
train___(crazyCity,zeroTown,7).
train___(dreamCity,oldTown,3).
train___(dreamCity,newTown,4).
train___(dreamCity,timePort,6).
train___(eternalCity,mysteryTown,4).
train___(frozenTown,greenTown,27).
train___(greenTown,honeyTown,11).
train___(greenTown,icyTown,12).
train___(honeyTown,joyTown,10).
train___(honeyTown,piratesPort,9).
train___(icyTown,joyTown,15).
train___(joyTown,kindTown,18).
train___(joyTown,sunnyPort,20).
train___(kindTown,quietPort,25).
train___(luckyTown,mysteryTown,5).
train___(luckyTown,utopiaPort,7).
train___(oldTown,timePort,5).
train___(whitePort,xenonTown,7).
train___(whitePort,yellowTown,8).


boat(X,Y,N) :- boat___(X,Y,N). 
boat(X,Y,N) :- boat___(Y,X,N). 

boat___(piratesPort,whitePort,45).
boat___(quietPort,whitePort,42).
boat___(quietPort,utopiaPort,75).
boat___(rainyPort,quietPort,58).
boat___(rainyPort,vainPort,38).
boat___(rainyPort,whitePort,48).
boat___(sunnyPort,timePort,30).
boat___(sunnyPort,utopiaPort,32).
boat___(timePort,vainPort,87).
boat___(vainPort,whitePort,53).


plane(X,Y,N) :- plane___(X,Y,N). 
plane(X,Y,N) :- plane___(Y,X,N). 

plane___(astralCity,brightCity,120).
plane___(astralCity,crazyCity,80).
plane___(astralCity,dreamCity,90).
plane___(astralCity,eternalCity,150).
plane___(brightCity,crazyCity,140).
plane___(brightCity,dreamCity,110).
plane___(brightCity,eternalCity,70).
plane___(dreamCity,eternalCity,107).

