meta.aliases = ['firstjs', 'secondjs'];
meta.description = 'Cool command made in JavaScript!';
meta.level = Level.BOT_CREATOR;
meta.usage = 'lmao usages are for nerds';
meta.shownInHelp = false;
meta.inject = true;

function execute(note, args) {
    note.replyEmbed('made from javascript', 'The guild name is ' + host.getName())
}