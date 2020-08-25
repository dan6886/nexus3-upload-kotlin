package test;

import test.case2.DeferCase;
import test.case2.DelayError;

public class Main {
    public static void main(String[] args) {
        BaseRunCase aCase = new DelayError();
        aCase.runCase();

    }
}
