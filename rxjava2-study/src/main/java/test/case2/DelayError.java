package test.case2;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import test.BaseRunCase;

import java.util.List;

/**
 * 出现error的时候不中断，在最后执行完毕之后再error,但是对应数据源的数据从error之后就不在发送了，直接继续下一个数据源的发送，
 * 可以理解为，当出现error之后，此数据源就停止发射数据了。等到最后才抛出异常,如果有多个数据源都发射异常，这在下雨onError里面会收到一个符合异常对象，可读取里面的list异常对象
 */
public class DelayError extends BaseRunCase {
    @Override
    public void runCase() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onError(new Throwable("2 error"));
                e.onNext(2);
//                e.onComplete();
            }
        });
        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onError(new Throwable("1 error"));
                e.onComplete();
            }
        });
        Observable.concatArrayDelayError(observable, observable2).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                LogUtil.print(integer);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof CompositeException) {
                    CompositeException exception = (CompositeException) e;
                    List<Throwable> exceptions = exception.getExceptions();
                    LogUtil.printCollection(exceptions);

                }
            }

            @Override
            public void onComplete() {
                LogUtil.print("complete");
            }
        });
    }
}
