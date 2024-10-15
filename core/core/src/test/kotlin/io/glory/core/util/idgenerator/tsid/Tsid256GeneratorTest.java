package io.glory.core.util.idgenerator.tsid;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Tsid256GeneratorTest {

    private static final Logger LOGGER = Logger.getLogger("Tsid256GeneratorTest");

    private final TsidFactory tsidFactory = TsidFactory.newInstance256(1);

    @Disabled("heavy test")
    @DisplayName("TsidJ 256 Generator 32 thread 테스트 - 동시 요청자 수: 256, 1024, 4096, 100,000, 500,000 일 때")
    @ParameterizedTest
    @ValueSource(ints = {256, 256, 1024, 4096, 100000, 500000})
    void tsid_fast(int numberOfUsers) throws Exception {
        // given
        int threadPoolSize = 32;
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch endLatch = new CountDownLatch(threadPoolSize);

        // when
        long[][] tsids = new long[threadPoolSize][numberOfUsers];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadPoolSize; i++) {

            int threadI = i;
            executorService.execute(() -> {
                try {
                    for (int j = 0; j < numberOfUsers; j++) {
                        Tsid tsid = tsidFactory.create();
                        tsids[threadI][j] = tsid.toLong();
                    }

                } finally {
                    endLatch.countDown();
                }
            });
        }
        endLatch.await();

        long endTime = System.currentTimeMillis();
        LOGGER.info(() -> "[동시 요청자 수: " + numberOfUsers + "명] " + (endTime - startTime) + "ms");

        // Set<Long> set = new HashSet<>();
        // for (long[] ids : tsids) {
        //     Arrays.stream(ids).forEach(set::add);
        // }
        List<Object> list = new ArrayList<>();
        for (long[] ids : tsids) {
            for (long id : ids) {
                list.add(id);
            }
        }

        // then
        executorService.shutdownNow();

        LOGGER.info("==> set.size() = " + list.size());
        assertThat(list).hasSize(numberOfUsers * threadPoolSize);
    }

}
