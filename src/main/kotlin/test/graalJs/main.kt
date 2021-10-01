package test.graalJs

import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import javax.script.ScriptEngineManager

@Suppress("unused")
class MyHttpClient(val a: String) {
    fun call() {
        println("Hello $a")
    }
}

fun main() {
    ScriptEngineManager().engineFactories.forEach {
        println("${it.languageName} ${it.engineName} ${it.languageVersion} ${it.engineVersion}")
    }

    System.setProperty("polyglot.engine.WarnInterpreterOnly", "false")

    val context = Context.newBuilder("js")
        .allowHostAccess(HostAccess.ALL)
        .allowHostClassLookup { className ->
            className == "test.graalJs.MyHttpClient" ||
                    className.startsWith("java.")
        }
        .build()

    context.eval("js",
        //language=JS
        """
            let a = 10;
            console.log(a);
            let System = Java.type("java.lang.System");
            System.out.println(a);
            let Client = Java.type("test.graalJs.MyHttpClient");
            let client = new Client("aaa");
            client.call();
            
            let array = new Uint8Array(10);
            array[9] = 11;
            console.log(array);
        """.trimIndent()
    )
}