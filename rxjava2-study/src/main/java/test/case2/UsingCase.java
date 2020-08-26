package test.case2;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import test.BaseRunCase;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class UsingCase extends BaseRunCase {
    @Override
    public void runCase() {
        Observable<Long> interval = Observable.interval(5, TimeUnit.SECONDS);
        Observable<Person> cxdan = Observable.using(new Callable<Person>() {
            @Override
            public Person call() throws Exception {
                int i = new Random().nextInt(20000);
                return new Person("cxdan", i);
            }
        }, new Function<Person, ObservableSource<Person>>() {
            @Override
            public ObservableSource<Person> apply(Person person) throws Exception {
                return Observable.just(person);
            }
        }, new Consumer<Person>() {
            @Override
            public void accept(Person person) throws Exception {
                person.description = "i am release";
                LogUtil.print("release:" + person);
            }
        }).map(new Function<Person, Person>() {
            @Override
            public Person apply(Person person) throws Exception {
                return person;
            }

        }).repeat(30);
        Observable.zip(interval, cxdan, new BiFunction<Long, Person, Person>() {
            @Override
            public Person apply(Long aLong, Person person) throws Exception {
                return person;
            }
        }).sorted(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.age - o2.age;
            }
        }).buffer(200)
                .blockingSubscribe(new StandardObserver<>());
        //使用缓存和排序
    }

    static class Person {
        private String name;
        private int age = 0;
        private String description = "";

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
