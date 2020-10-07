package com.avatao.tfw.sdk;

import com.avatao.tfw.sdk.api.TFWFacade;
import com.avatao.tfw.sdk.api.data.*;
import com.avatao.tfw.sdk.mock.TFWServerMock;
import kotlin.Unit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {

        TFWServerMock mock = TFWServerMock.create();
        TFWFacade api = TFW.create();

        api.onFrontendReady(frontend -> {
            frontend
                    .configureDashboard().commit()
                    .configureIDE().commit()
                    .configureSite().commit();
            return Unit.INSTANCE;
        });

        api.onReadFile(cmd -> {
            cmd.respondWith(new FileContents(
                    cmd.getFilename(),
                    "content",
                    Collections.emptyList()
            ));
            return SubscriptionCommand.keepSubscription();
        });

        api.onWriteFile(cmd -> {
            cmd.respondWith(new FileWriteResults(
                    cmd.getFilename(),
                    Collections.emptyList()
            ));
            return SubscriptionCommand.keepSubscription();
        });

        api.onDeployStart(() -> {
            api.signalDeploySuccess();
            return SubscriptionCommand.keepSubscription();
        });

        try {
            ProcessStartResult startResult = api.startProcess("webservice").get(1L, SECONDS);
            System.out.println(startResult);

            ProcessRestartResult restartResult = api.restartProcess("webservice").get(1L, SECONDS);
            System.out.println(restartResult);

            ProcessStopResult stopResult = api.stopProcess("webservice").get(1L, SECONDS);
            System.out.println(stopResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        api.configLog()
                .name("webservice")
                .tail(100)
                .commit();

        api.readLog()
                .subscribeOn(Schedulers.elastic())
                .subscribe(new Subscriber<ProcessLogEntry>() {

                    private Subscription subscription;
                    private final AtomicInteger counter = new AtomicInteger();

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ProcessLogEntry processLogEntry) {
                        System.out.println(processLogEntry);
                        counter.incrementAndGet();
                        if (counter.get() > 10) {
                            subscription.cancel();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Completed");
                    }
                });

        api.sendMessage().message("hey.").commit();
        api.queueMessages()
                .message(api.sendMessage().message("hey"))
                .message(api.sendMessage().message("other hey"))
                .commit();
        api.writeToConsole("Hello, Console!");

        api.close();
        mock.close();

    }
}
