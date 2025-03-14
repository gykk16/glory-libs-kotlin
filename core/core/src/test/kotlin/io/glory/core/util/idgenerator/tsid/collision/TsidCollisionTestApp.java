package io.glory.core.util.idgenerator.tsid.collision;

import java.util.concurrent.ThreadLocalRandom;

import io.glory.core.util.idgenerator.tsid.TsidFactory;

/**
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class TsidCollisionTestApp {

    public static void main(String[] args) throws InterruptedException {

        TestPerformer tp = TestPerformer.builder()
                .threadCount(32)
                .iterationCount(1_000)
                .repetitions(1_000)
                .build();

        tp.printTest("Create a new default TSID factory on each thread",
                "duplication present because of not enough random bits available",
                "i -> TsidFactory.newInstance1024()",
                i -> TsidFactory.newInstance1024());

        TsidFactory sharedInstance1024 = TsidFactory.newInstance1024();
        tp.printTest("Share the same default TSID factory on each thread",
                "slow because of contention accessing the TSID generator",
                "i -> sharedInstance1024",
                i -> sharedInstance1024);

        tp.printTest("Create a new node TSID factory on each thread with the same node-id",
                "duplications because generators use the same node-id",
                "i -> TsidFactory.newInstance1024(0)",
                i -> TsidFactory.newInstance1024(0));

        // THIS IS THE RIGHT STRATEGY FOR SEQUENTIAL TSID ON THE SAME NODE!
        TsidFactory sharedInstance1024Node0 = TsidFactory.newInstance1024(0);
        tp.printTest("Share the same node TSID factory on each thread",
                "slow because of contention accessing the TSID generator",
                "i -> sharedInstance1024Node0",
                i -> sharedInstance1024Node0);

        tp.printTest("Use a different TSID factory on each thread with a different node-id",
                "fast because node-ids are different so no overlapping",
                "i -> TsidFactory.newInstance1024(i)",
                i -> TsidFactory.newInstance1024(i));

        tp.printTest("Use a new thread local random TSID factory on each thread",
                "duplication present because of not enough random bits available",
                "i -> factoryCreator()",
                i -> factoryCreator());

        TsidFactory sharedFactory = factoryCreator();
        tp.printTest("Share the same thread local random TSID factory on each thread",
                "slow because of contention accessing the TSID generator",
                "i -> sharedFactory",
                i -> sharedFactory);
    }

    private static TsidFactory factoryCreator() {
        return TsidFactory.builder()
                .withRandomFunction(length -> {
                    final byte[] bytes = new byte[length];
                    ThreadLocalRandom.current().nextBytes(bytes);
                    return bytes;
                }).build();
    }

}
