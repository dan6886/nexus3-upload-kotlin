package test.case2;

import io.reactivex.Observable;
import test.BaseRunCase;

/**
 * concatEager:
 * 多个Observable可以同时开始发射数据，
 * 如果后一个Observable发射完成后，前一个Observable还有发射完数据，
 * 那么它会将后一个Observable的数据先缓存起来，等到前一个Observable发射完毕后，才将缓存的数据发射出去。
 */
public class ContactCase extends BaseRunCase {
    @Override
    public void runCase() {
    }
}
