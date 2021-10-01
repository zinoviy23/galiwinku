package test.graalJs

import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import javax.script.ScriptEngineManager

fun main() {
    ScriptEngineManager().engineFactories.forEach {
        println("${it.languageName} ${it.engineName} ${it.languageVersion} ${it.engineVersion}")
    }

    System.setProperty("polyglot.engine.WarnInterpreterOnly", "false")

    val context = Context.newBuilder("js")
        .allowHostAccess(HostAccess.ALL)
        .allowHostClassLookup { className -> className.startsWith("java.") }
        .build()

    context.eval("js",
        //language=JS
        """
            let a = 10;
            console.log(a);
            let System = Java.type("java.lang.System");
            System.out.println(a);
        """.trimIndent()
    )
}