package xyz.gnarbot.gnar.commands.handlers.javascript;

import com.google.inject.Inject;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

class JSMeta {
    public Object aliases;
    public String usage;
    public String description;
    public boolean inject;
    public boolean shownInHelp = true;
    public Level level = Level.USER;
}

public class JSCommandExecutor extends CommandExecutor {
    private NashornScriptEngine engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();
    private ScriptContext context = new SimpleScriptContext();

    @Inject
    private Host host;

    public JSCommandExecutor(File file) {
        context.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);

        try {
            JSMeta meta = new JSMeta();
            engine.put("meta", meta);
            engine.eval("var Level = Java.type(\"xyz.gnarbot.gnar.members.Level\");");

            engine.eval(new FileReader(file));

            setAliases(jsarray(meta.aliases, String.class));
            setDescription(meta.description);
            setUsage(meta.usage);
            setInject(meta.inject);
            setShownInHelp(meta.shownInHelp);
            setLevel(meta.level);

        } catch (ScriptException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void execute(Note note, List<String> args) {
        engine.put("host", host);
        JSObject function = (JSObject) context.getAttribute("execute", ScriptContext.ENGINE_SCOPE);
        function.call(null, note, args);
    }

    @SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
    private static <T> T[] jsarray(final Object obj, Class<T> cls) {
        if (obj instanceof ScriptObjectMirror) {
            if (((ScriptObjectMirror) obj).isArray()) {
                final Object vals = ((ScriptObjectMirror) obj).values();
                final Collection<?> coll = (Collection<?>) vals;
                return  coll.toArray((T[]) Array.newInstance(cls, 0));
            }
        }
        if (obj instanceof List<?>) {
            final List<?> list = (List<?>) obj;
            return list.toArray((T[]) Array.newInstance(cls, 0));
        }
        return null;
    }
}