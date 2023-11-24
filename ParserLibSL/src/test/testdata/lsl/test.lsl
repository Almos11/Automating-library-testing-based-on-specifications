libsl "1.0.0";
library MyClass;

types {
    CustomString (String);
}

automaton MyAutomat : Int{

//    var num_state: int = -1;

    initstate created;
    state sum;
    state diff;
    state division;
    finishstate closed;

    shift created -> self by get_state;
    shift created -> sum by next();
    shift sum -> self by [get_sum, get_state];
    shift sum -> created by to_start;
    shift sum -> diff by next;
    shift diff -> self by [get_diff, get_state];
    shift diff -> created by to_start;
    shift diff -> division by next;
    shift division -> self by [get_division, get_state];
    shift division -> diff by back;
    shift division -> closed by end;
}

fun MyAutomat.get_sum(a: int, b: int): int;
fun MyAutomat.get_diff(a: int, b: int): int;
fun MyAutomat.next();
fun MyAutomat.to_start();
fun MyAutomat.back();
fun MyAutomat.get_division(a: int, b: int): float;
fun MyAutomat.get_state(): int;
fun MyAutomat.end();