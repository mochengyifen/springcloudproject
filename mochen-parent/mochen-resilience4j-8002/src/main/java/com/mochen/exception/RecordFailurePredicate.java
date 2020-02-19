package com.mochen.exception;


import java.util.function.Predicate;

/**
 * @author 董仁亮
 * @date 2020-02-19 16:36
 * @Description:
 */
public class RecordFailurePredicate implements Predicate<Throwable> {

    @Override
    public boolean test(Throwable throwable) {
        return true;
    }
}
