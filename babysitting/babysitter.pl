:- module(babysitter, []).
:- use_module(library(plunit)).

% babysitter(StartTime, BedTime, EndTime, Pay).
%% Pay is the amount of money in whole dollars that a babysitter
%% should receive.  Times are military format as integers.
babysitter(Start, Bed, End, _) :-
    % fail if times are wrong
    Start > 2400, !, fail;
    Start < 1700, !, fail;
    Bed > 2400,   !, fail;
    Bed < 1700,   !, fail;
    End > 2400,   !, fail;
    End < 0,      !, fail;
    % fail if times are misordered
    Start > Bed,  !, fail.

babysitter(StartTime, BedTime, EndTime, Pay) :-
    % past-midnight pay
    EndTime =< 400, !,
    hours_between(0, EndTime, LateHours),
    babysitter(StartTime, BedTime, 2400, Pay2),
    Pay is Pay2 + LateHours * 16.

babysitter(StartTime, BedTime, EndTime, Pay) :-
    % until-midnight pay
    EndTime > 400, !,
    BedTime =< EndTime,
    hours_between(StartTime, BedTime, Hrs1),
    hours_between(  BedTime, EndTime, Hrs2),
    Pay is Hrs1 * 12 + Hrs2 * 8.

full_hours(MilitaryTime, Hours) :-
    FracTime is MilitaryTime mod 100,
    Time is MilitaryTime - FracTime,
    Hours is Time / 100.

hours_between(Start, Stop, Hours) :-
    Start =< Stop,
    Time is Stop - Start,
    full_hours(Time, Hours).

:- begin_tests(babysitter).

test(good_babysitter) :-
    babysitter(1700, 2200, 2300, 68),
    babysitter(1700, 2200, 2400, 76),
    babysitter(1700, 2200,    0, 76),
    babysitter(1700, 2200, 300, 124).

test(bad_start_time) :-
    \+ babysitter(1600, 2500, 2400, _).

test(bad_bed_times) :-
    \+ babysitter(1700, 2500, 2400, _),
    \+ babysitter(1700, -100, 2400, _).

test(bad_end_times) :-
    \+ babysitter(1700, 2200, 2500, _),
    \+ babysitter(1700, 2200, -100, _).

test(bad_time_order) :-
    \+ babysitter(1800, 1700, 2400, _),
    \+ babysitter(1800, 2000, 1900, _).

test(full_hours) :-
    full_hours(800, 8),
    full_hours(850, 8),
    full_hours(  0, 0).

test(hours_between) :-
    hours_between(100, 500, 4),
    hours_between(130, 520, 3),

    \+ hours_between(100, 0, _).

:- end_tests(babysitter).
