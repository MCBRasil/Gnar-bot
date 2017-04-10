package xyz.gnarbot.gnar.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public enum Scope {
    GUILD {
        @Override
        public boolean checkPermission(Message message, Member target, Permission... permissions) {
            return target.hasPermission(permissions);
        }
    },
    TEXT {
        @Override
        public boolean checkPermission(Message message, Member target, Permission... permissions) {
            return target.hasPermission(message.getTextChannel(), permissions);
        }
    },
    VOICE {
        @Override
        public boolean checkPermission(Message message, Member target, Permission... permissions) {
            return target.hasPermission(target.getVoiceState().getChannel(), permissions);
        }
    };

    abstract public boolean checkPermission(Message message, Member target, Permission... permissions);
}
