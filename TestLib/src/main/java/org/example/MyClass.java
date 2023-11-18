package org.example;

public class MyClass {
    private int state;

    public MyClass() {
        state = 0;
    }

    public void name(String name) {
        state = 1;
    }

    public int get_sum(int a, int b) {
        if (state == 2) {
            return a + b;
        } else {
            throw new RuntimeException();
        }
    }

    public int get_diff(int a, int b) {
        if (state == 3) {
            return a - b;
        } else {
            throw new RuntimeException();
        }
    }

    public void next() {
        if (state == 2 || state == 3) {
            state += 1;
        } else {
            throw new RuntimeException();
        }
    }

    public void to_start() {
        if (state == 2 || state == 3) {
            state = 1;
        } else {
            throw new RuntimeException();
        }
    }

    public void back() {
        if (state == 4) {
            state -= 1;
        } else {
            throw new RuntimeException();
        }
    }

    public float get_division(int a, int b) {
        if (state == 4) {
            return (float) a / (float) b;
        } else {
            throw new RuntimeException();
        }
    }
}
