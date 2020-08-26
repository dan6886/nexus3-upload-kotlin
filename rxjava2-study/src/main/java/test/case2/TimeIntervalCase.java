package test.case2;

import io.reactivex.Observable;
import test.BaseRunCase;

import java.util.concurrent.TimeUnit;

/**
 * 计算两个数据的时间差
 * 计算每个数据的时间戳
 */
public class TimeIntervalCase extends BaseRunCase {
    @Override
    public void runCase() {
        Observable.interval(5, TimeUnit.SECONDS)
                .timeInterval(TimeUnit.SECONDS)
                .subscribe(new StandardObserver<>());

        Observable.interval(5, TimeUnit.SECONDS)
                .timestamp()
                .blockingSubscribe(new StandardObserver<>());
    }
}
