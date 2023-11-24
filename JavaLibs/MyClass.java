public class MyClass {

    // states
    /*
    initstate created; - state == 0
    state sum; - state == 1
    state diff; - state == 2
    state division; - state == 3
    finishstate closed; - state == 4 - финальное состояние
     */

    // shifts
    /*
    created == 0: 0 (get_state), 1 (next)
    sum == 1: 0 (to_start), 1 (get_sum, get_state), 2 (next)
    diff == 2: 0 (to_start), 2 (diff, get_state), 3 (next)
    division == 3: 2 (back), 3 (get_division, get_state), 4 (end)
    closed == 4: - finish state
     */

    // func of shifts
    /*
    next: 1 -> 2 , 2 -> 3 , 0 -> 1
    end: 3 -> 4
    back: 3 -> 2
    to_start: 1 -> 0 , 2 -> 0
    get_state: 0 -> 0, 1 -> 1, 2 -> 2, 3 -> 3
     */

    private int state = -1;
    String name;
    MyClass(String name) {
        state = 0;
        this.name = name;
    }

    void next() {
        if (state != 4) {
            state += 1;
        } else {
            throw new RuntimeException();
        }
    }

    int get_sum(int a, int b) {
        if (state == 1) {
            return a + b;
        } else {
            throw new RuntimeException();
        }
    }

    int get_diff(int a, int b) {
        if (state == 2) {
            return a - b;
        } else {
            throw new RuntimeException();
        }
    }

    float get_division(int a, int b) {
        if (state == 3) {
            return (float) a / (float) b;
        } else {
            throw new RuntimeException();
        }
    }

    void to_start() {
        if (state == 1 || state == 2) {
            state = 0;
        } else {
            System.out.println(state);
            throw new RuntimeException();
        }
    }

    void end() {
        if (state == 3) {
            state += 1;
        } else {
            throw new RuntimeException();
        }
    }

    int get_state() {
        if (state != 4) {
            return state;
        } else {
            throw new RuntimeException();
        }
    }
}
