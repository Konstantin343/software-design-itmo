package ru.kkrukhmalev;

import ru.kkrukhmalev.app.SortsRunner;
import ru.kkrukhmalev.profiler.Profiler;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                Profiler.use("ru.kkrukhmalev.app", () -> {
                    new SortsRunner().run();
                })
        );
    }
}
