libsl "1.0.0";
library MyClass;

types {
    CustomString (String);
}

automaton MyAutomat : Int{

//    var num_state: int = 0;

    state created;
    state calculate_sum;
    state calculate_diff;
    state calculate_division;
    finishstate closed;

    shift created -> calculate_sum (name);
    shift calculate_sum -> self (get_sum);
    shift calculate_sum -> created (to_start);
    shift calculate_sum -> calculate_diff (next);
    shift calculate_diff -> self (get_diff);
    shift calculate_diff -> created (to_start);
    shift calculate_diff -> calculate_division (next);
    shift calculate_division -> self (get_division);
    shift calculate_division -> calculate_diff (back);
    shift calculate_division -> closed (end);
}

fun MyAutomat.name(name: string);
fun MyAutomat.get_sum(a: int, b: int): int;
fun MyAutomat.get_diff(a: int, b: int): int;
fun MyAutomat.next();
fun MyAutomat.to_start();
fun MyAutomat.back();
fun MyAutomat.get_division(a: int, b: int): float;
fun MyAutomat.end();