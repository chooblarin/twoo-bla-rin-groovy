package com.chooblarin.gtwooblarin.task

import android.os.Handler
import groovy.transform.CompileStatic

@CompileStatic
class Fluent<Result> {

    public static interface ResultConsumer<T> {
        void consume(T result)
    }

    private final Closure<Result> request
    private final ResultConsumer<Result> then

    private Handler handler

    public Fluent(Closure<Result> request, ResultConsumer<Result> then) {
        this.request = request
        this.then = then
    }

    public void execute() {

        handler = new Handler()
        def han = handler
        Closure<Result> req = request
        ResultConsumer<Result> thn = then

        new Thread(new Runnable() {
            @Override
            void run() {
                //Result result = req.call()
                def result = req.call()

                han.post {
                    thn.consume(result)
                }
            }
        }).start()
    }

    public static class FluentAsyncTaskBuilder<Result> {
        Closure<Result> request

        FluentAsyncTaskBuilder from(Closure<Result> request) {
            this.request = request
            return this
        }

        void then(ResultConsumer<Result> result) {
            new Fluent(request, result).execute()
        }
    }

    public static <Result> FluentAsyncTaskBuilder async(Closure<Result> request) {
        return new FluentAsyncTaskBuilder<Result>().from(request)
    }
}
