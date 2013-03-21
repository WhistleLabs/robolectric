package org.robolectric.bytecode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.TestRunners;
import org.robolectric.annotation.Config;
import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.internal.Instrument;
import org.robolectric.internal.RealObject;

import java.lang.reflect.Field;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.robolectric.Robolectric.directlyOn;
import static org.robolectric.Robolectric.shadowOf_;

@RunWith(TestRunners.WithoutDefaults.class)
public class ThreadSafetyTest {
    @Test
    @Config(shadows = {InstrumentedThreadShadow.class})
    public void shadowCreationShouldBeThreadsafe() throws Exception {
        Field field = InstrumentedThread.class.getDeclaredField("shadowFromOtherThread");
        field.setAccessible(true);

        for (int i = 0; i < 100; i++) { // :-(
            InstrumentedThread instrumentedThread = new InstrumentedThread();
            instrumentedThread.start();
            Object shadowFromThisThread = shadowOf_(instrumentedThread);

            instrumentedThread.join();
            Object shadowFromOtherThread = field.get(instrumentedThread);
            assertThat(instrumentedThread.finished).isTrue();
            assertThat(shadowFromThisThread).isSameAs(shadowFromOtherThread);
        }
    }

    @Instrument
    public static class InstrumentedThread extends Thread {
        InstrumentedThreadShadow shadowFromOtherThread;
        boolean finished = false;

        @Override
        public void run() {
            shadowFromOtherThread = shadowOf_(this);
            finished = true;
        }
    }

    @Implements(InstrumentedThread.class)
    public static class InstrumentedThreadShadow {
        @RealObject InstrumentedThread realObject;
        @Implementation
        public void run() {
            directlyOn(realObject, InstrumentedThread.class).run();
        }
    }
}
