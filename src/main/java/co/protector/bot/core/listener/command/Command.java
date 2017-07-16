package co.protector.bot.core.listener.command;

import co.protector.bot.commands.CommandCategory;
import net.dv8tion.jda.core.entities.Message;

public abstract class Command {
    private CommandCategory category = CommandCategory.UNKNOWN;

    /**
     * Decides if the command is listed in the help command
     *
     * @return boolean
     */
    public boolean isListed() {
        return true;
    }

    /**
     * The trigger for this command (without prefix)
     *
     * @return trigger
     */

    public abstract String getTrigger();

    /**
     * aliases to call the command
     *
     * @return array of aliases
     */
    public abstract String[] getAliases();

    /**
     * A short discription of the method
     *
     * @return description
     */
    public abstract String getDescription();

    /**
     * How to use the command?
     * A more detailed description
     *
     * @return command usage
     */
    public abstract String[] getUsage();

    /**
     * run the command
     *
     * @param trigger the message that triggered the command
     * @param args    arguments
     */
    public abstract void execute(Message trigger, String args);

    /**
     * the category of the command (based on packagename)
     *
     * @return command category
     */
    public final CommandCategory getCategory() {
        return category;
    }

    public void setCategory(CommandCategory category) {
        this.category = category;
    }
}
