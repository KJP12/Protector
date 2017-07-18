package co.protector.bot.commands.admin;

import co.protector.bot.Config;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.util.Text;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Repulser
 * https://github.com/Repulser
 */
public class EvalCommand extends Command {
    private static final ThreadGroup EVALS = new ThreadGroup("Eval Thread Pool");
    private static final Executor POOL = Executors.newCachedThreadPool(r -> new Thread(EVALS, r,
            EVALS.getName() + EVALS.activeCount()));

    static {
        EVALS.setMaxPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public String getTrigger() {
        return "eval";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Evaluate code";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }

    @Override
    public boolean isListed() {
        return false;
    }

    @Override
    public void execute(Message trigger, String args) {
        if (!trigger.getAuthor().getId().equals(Config.owner_id)) return;
        GroovyShell shell = this.createShell(trigger);
        POOL.execute(() -> {
            try {
                Object result = shell.evaluate(args);
                if (result == null) {
                    trigger.getChannel().sendMessage("`null` **Executed successfully**").queue();
                    return;
                }
                trigger.getChannel().sendMessage("```groovy\n" + result.toString() + "```").queue();
            } catch (Exception ex) {
                String exception = ExceptionUtils.getStackTrace(ex);

                trigger.getChannel().sendMessage("\u274C **Error: **\n**" + Text.paste(exception) + "**").queue();
            }
        });

    }

    private GroovyShell createShell(Message e) {
        Binding binding = new Binding();
        binding.setVariable("api", e.getJDA());
        binding.setVariable("jda", e.getJDA());
        binding.setVariable("channel", e.getChannel());
        binding.setVariable("author", e.getAuthor());
        binding.setVariable("message", e);
        binding.setVariable("msg", e);
        binding.setVariable("guild", e.getGuild());
        return new GroovyShell(binding);
    }
}
