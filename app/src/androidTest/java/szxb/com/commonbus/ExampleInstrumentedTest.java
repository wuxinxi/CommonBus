package szxb.com.commonbus;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("ExampleInstrumentedTest",
                "useAppContext(ExampleInstrumentedTest.java:27)" + format.format(new Date()));
        Log.d("ExampleInstrumentedTest",
                "useAppContext(ExampleInstrumentedTest.java:27)" + format.format(new Date()));
        Log.d("ExampleInstrumentedTest",
                "useAppContext(ExampleInstrumentedTest.java:27)" + format.format(new Date()));
        Log.d("ExampleInstrumentedTest",
                "useAppContext(ExampleInstrumentedTest.java:27)" + format.format(new Date()));
        Log.d("ExampleInstrumentedTest",
                "useAppContext(ExampleInstrumentedTest.java:27)" + format.format(new Date()));
        assertEquals("szxb.com.commonbus", appContext.getPackageName());
    }
}
