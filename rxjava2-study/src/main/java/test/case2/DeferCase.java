package test.case2;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import test.BaseRunCase;

import java.util.concurrent.Callable;

/**
 * 只有在被订阅的时候才会创建被观察者
 */
public class DeferCase extends BaseRunCase {
    int i = 100;

    @Override
    public void runCase() {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i);
            }
        });
        i = 2000;
        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }
}
