library MyClass;

automation MyAutomat {

// номер состояния
    var state: int = 0;

// состояния автомата
    state created;
    state calculate_sum;
    state calculate_diff;
    state calculate_division;
    finishstate closed;

// переходы автомата
    shift created->calculate_sum by name;
    shift calculate_sum->self by get_sum;
    shift calculate_sum->created by to_start;
    shift calculate_sum->calculate_diff by next;
    shift calculate_diff->self by get_diff;
    shift calculate_diff->created by to_start;
    shift calculate_diff->calculate_division by next;
    shift calculate_division->self by get_division;
    shift calculate_division->calculate_diff by back;
    shift calculate_division->closed by end;
}

// функции
fun MyAutomat.name(name: string): void;
fun MyAutomat.get_sum(a: int, b: int): int;
fun MyAutomat.get_diff(a: int, b: int): int;
fun MyAutomat.next(): void;
fun MyAutomat.to_start(): void;
fun MyAutomat.back(): void;
fun MyAutomat.get_division(a: int, b: int): float;

