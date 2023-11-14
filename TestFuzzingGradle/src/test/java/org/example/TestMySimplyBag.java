package org.example;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

class TestMySimplyBag {
    @FuzzTest
    void myFuzzTest(FuzzedDataProvider data) {
    int a = data.consumeInt();
    int b = data.consumeInt();
    MySimpleBag bag = new MySimpleBag();
    bag.mySimpleBag(a, b);
    }
}
