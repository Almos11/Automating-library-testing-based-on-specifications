libsl "1.0.0";
library MyClass;

types {
    CustomString (String);
}

automaton MyAutomat : Int{

//    var num_state: int = -1;

    state created;
    state calculate_sum;
    state calculate_diff;
    state calculate_division;
    finishstate closed;

    shift created -> self (get_state);
    shift created -> calculate_sum (next);
    shift calculate_sum -> self (get_sum, get_state);
    shift calculate_sum -> created (to_start);
    shift calculate_sum -> calculate_diff (next);
    shift calculate_diff -> self (get_diff, get_state);
    shift calculate_diff -> created (to_start);
    shift calculate_diff -> calculate_division (next);
    shift calculate_division -> self (get_division, get_state);
    shift calculate_division -> calculate_diff (back);
    shift calculate_division -> closed (end);
}

fun MyAutomat.get_sum(a: int, b: int): int;
fun MyAutomat.get_diff(a: int, b: int): int;
fun MyAutomat.next();
fun MyAutomat.to_start();
fun MyAutomat.back();
fun MyAutomat.get_division(a: int, b: int): float;
fun MyAutomat.get_state(): int;
fun MyAutomat.end();