libsl "1.0.0";
library SimplyMyClass;

types {
    CustomString (String);
}

typealias Int = int32;
typealias Float = float32;

automaton MyAutomat : Int{

//    var num_state: Int = -1;

    initstate created;
    state sum;
    state diff;
    state division;
    finishstate closed;

    shift created -> sum by next;
    shift sum -> self by getSum;
    shift sum -> created by toStart;
    shift sum -> diff by next;
    shift diff -> self by getDiff;
    shift diff -> created by toStart;
    shift diff -> division by next;
    shift division -> self by getDivision;
    shift division -> diff by back;
    shift division -> closed by end;

    fun getSum(a: Int, b: Int): Int {
    }
}

fun MyAutomat.getDiff(a: Int, b: Int): Int;
fun MyAutomat.next();
fun MyAutomat.toStart();
fun MyAutomat.back();
fun MyAutomat.getDivision(a: Int, b: Int): Float;
fun MyAutomat.end();