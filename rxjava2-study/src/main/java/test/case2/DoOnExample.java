package test.case2;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import test.BaseRunCase;

public class DoOnExample extends BaseRunCase {
    @Override
    public void runCase() {
        /**
         * doOnEach 处理onNext，onError，onComplete
         */
        Observable.just(1, 2, 3)
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        LogUtil.print(integerNotification.getValue());
                    }
                }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

                LogUtil.print("doOnNext:" + integer);
            }
        })
                .subscribe(new StandardObserver<>());
        LogUtil.print("=========== doOnEach End =============");
/**
 *doOnNext只会在下游调用onNext之前，先访问一下数据
 */
        Observable.just(1, 2, 3)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.print(integer);
                    }
                })
                .subscribe(new StandardObserver<>());

        LogUtil.print("=========== doOnNext End =============");
        /**
         * doOnTerminate 触发结束之前响应
         * doAfterTerminate 最后响应 晚于finally
         * doFinally
         */
        Observable.just(1, 2, 3)
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.print("doOnTerminate");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.print("doAfterTerminate");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.print("doFinally");

                    }
                })
                .subscribe(new StandardObserver<>());
        LogUtil.print("=========== doAfterTerminate End =============");
    }


}
