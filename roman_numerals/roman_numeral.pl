:- module(roman_numeral, []).
:- use_module(library(plunit)).

roman_numeral("I",    1).
roman_numeral("V",    5).
roman_numeral("X",   10).
roman_numeral("L",   50).
roman_numeral("C",  100).
roman_numeral("D",  500).
roman_numeral("M", 1000).

% roman_numeral(Numeral, Value)
%% Convert a Roman Numeral to a value.
roman_numeral(Numeral, Value) :-
    ground(Numeral),
    string_char_strings(Numeral, Numerals),
    to_number(Numerals, Value),
    % check for malformed numerals via to_numeral
    to_numeral(Value, Numerals), !.
roman_numeral(Numeral, Value) :-
    ground(Value),
    integer(Value),
    to_numeral(Value, Numerals),
    atomics_to_string(Numerals, Numeral).

% to_number(Numerals, Value)
%% Convert Roman Numerals to a value, do not check for malformed
%% numerals.
to_number([], 0).
to_number([N], V) :-
    roman_numeral(N, V), !.
to_number([N1, N2|Ns], Total) :-
    roman_numeral(N1, V1),
    roman_numeral(N2, V2),
    V1 >= V2, !,
    to_number([N2|Ns], SubTotal),
    Total is SubTotal + V1.
to_number([N1, N2|Ns], Total) :-
    roman_numeral(N1, V1),
    roman_numeral(N2, V2),
    V1 < V2, !,
    to_number(Ns, SubTotal),
    Total is SubTotal + V2 - V1.

% to_numeral(Value, Numerals)
%% Convert a value to a correctly formed list of Roman Numerals.
to_numeral(0, []).
to_numeral(X, Ns) :-
    next_numerals(X, As, NewX),
    to_numeral(NewX, Zs),
    append(As, Zs, Ns).

next_numerals(X, [N1, N2], NewX) :-
    roman_numeral(N2, Big),
    roman_numeral(N1, Small),
    Small < Big,
    Nvalue is Big - Small,
    Nvalue =< X,
    \+ ( roman_numeral(_, Value),
	 Value =< X,
	 Nvalue =< Value
       ), !,
    NewX is X - Nvalue.
next_numerals(X, [N], NewX) :-
    roman_numeral(N, Nvalue),
    Nvalue =< X,
    \+ ( roman_numeral(_, Value),
	 Value =< X,
	 Nvalue < Value
       ), !,
    NewX is X - Nvalue.

% string_char_string(String, StringList)
%% Converts a string into a list of single character strings.
string_char_strings(Str, Strs) :-
    string_chars(Str, Chars),
    maplist(text_to_string, Chars, Strs).


:- begin_tests(numerals).

test(all_roman_numeral_are_strings) :-
    forall(between(1, 2000, X), (roman_numeral(N, X), string(N))).

test(good_roman_numerals) :-
    roman_numeral("XXXIII", 33),
    roman_numeral("XCIV",   94),
    roman_numeral("CXXX",  130).

test(bad_roman_numerals) :-
    \+ roman_numeral("XCXL", _),
    \+ roman_numeral("IIMXCC", _).
   
:- end_tests(numerals).
