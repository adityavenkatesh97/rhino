package org.mozilla.javascript.tests;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import static org.junit.Assert.assertEquals;

public class Issue1518Test {

    @Test
    public void arrayFromUsesCustomIterator() {
        try (Context cx = Context.enter()) {
            cx.setLanguageVersion(Context.VERSION_ES6);
            Scriptable scope = cx.initStandardObjects();

            String script =
                    "function test() {\n" +
                            "    let counter = 0;\n" +
                            "    Array.prototype[Symbol.iterator] = function* () {\n" +
                            "        for (let i = 0; i < this.length; i++) { counter++; yield this[i]; }\n" +
                            "    };\n" +
                            "    Array.from(['a','b','c']);\n" +
                            "    return counter;\n" +
                            "}\n" +
                            "test();"; // Call the function

            Object result = cx.evaluateString(scope, script, "testScript", 1, null);

            assertEquals("Array.from should prioritize Symbol.iterator over length", 3, Context.toNumber(result), 0.000001);
        }
    }

}
