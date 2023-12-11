package data

data class SimplyMyClass(private var name: String, private var state: Int = 0) {
    fun next() {
        if (state != 4) {
            state += 1
        } else {
            throw RuntimeException()
        }
    }

    fun getSum(a: Int, b: Int): Int {
        if (state == 1) {
            return a + b
        } else {
            throw RuntimeException()
        }
    }

    fun getDiff(a: Int, b: Int): Int {
        if (state == 2) {
            return a - b
        } else {
            throw RuntimeException()
        }
    }

    fun getDivision(a: Int, b: Int): Float {
        if (state == 3) {
            return a.toFloat() / b.toFloat()
        } else {
            throw RuntimeException()
        }
    }

    fun back() {
        if (state == 3) {
            state -= 1
        } else {
            throw RuntimeException()
        }
    }

    fun toStart() {
        if (state == 1 || state == 2) {
            state = 0
        } else {
            println(state)
            throw RuntimeException()
        }
    }

    fun end() {
        if (state == 3) {
            state += 1
        } else {
            throw RuntimeException()
        }
    }
    fun getState(): Int {
        return state
    }
}