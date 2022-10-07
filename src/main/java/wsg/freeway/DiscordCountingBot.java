package wsg.freeway;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.entities.emoji.UnicodeEmojiImpl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

public class DiscordCountingBot extends ListenerAdapter {
    private final AdminData adminData = AdminData.loadObjFromJSON();
    private GameData gameData = GameData.loadObjFromJSON();
    private static final Properties properties = new Properties();

    public static void main(String[] args) throws Exception {

        if (!new File("countingBot.properties").exists()) {
            BufferedWriter br = new BufferedWriter(new FileWriter("countingBot.properties"));
            br.write("token=none\n");
            br.write("channel=none\n");
            br.write("guild=none\n");
            br.write("adminFile=admins.json\n");
            br.write("gameDataFile=gameData.json");
            br.flush();
            System.out.println("Please configure in countingBot.properties");
            return;
        }

        properties.load(new FileInputStream("countingBot.properties"));

        String token = properties.getProperty("token");
        String channel = properties.getProperty("channel");
        String guildId = properties.getProperty("guild");

        if (token.equals("none") || channel.equals("none") || guildId.equals("none")) {
            System.out.println("Please configure in countingBot.properties");
            return;
        }

        DiscordCountingBot bot = new DiscordCountingBot();

        int highScore = bot.gameData.getHighScore();
        int curNum = bot.gameData.getCurNum();
        int nextNum = bot.gameData.getNextNum();

        boolean valid = true;
        if (token.equals("default")) {
            System.out.println("Configure the Bot in properties.json");
            valid = false;
        } else if (channel.equals("default")) {
            System.out.println("Configure the Bot in properties.json");
            valid = false;
        } else if (guildId.equals("default")) {
            System.out.println("Configure the Bot in properties.json");
            valid = false;
        }
        if (!valid) {
            return;
        }

        JDA jda = JDABuilder.createLight(token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(bot)
                .build().awaitReady();

        Guild guild = jda.getGuildById(guildId);

        if (guild != null) {
            guild.upsertCommand("score", "Zeigt dir den Highscore").queue();
            guild.upsertCommand("resetall", "Setzt alles zurück").queue();
            guild.upsertCommand("resetrun", "Setzt den aktuellen Durchlauf zurück").queue();
        }

        assert guild != null;
        System.out.println("Bot Ready, should be ONLINE\n" +
                "\nToken: " + token +
                "\nChannel: " + channel +
                "\nGuild: " + guild.getName() +
                "\nHighScore: " + highScore +
                "\nCurNum: " + curNum +
                "\nNextNum: " + nextNum);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getChannel().getId().equals(properties.getProperty("channel"))) return;

        String messageSent = event.getMessage().getContentRaw();

        int number;
        try {
            number = Integer.parseInt(messageSent);
        } catch (NumberFormatException e) {
            event.getMessage().delete().queue();
            return;
        }


        String fail = "❌";
        if (event.getAuthor().getId().equals(gameData.getLastUser())) {
            // game failed
            addReaction(event, fail);
            reset();

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("[Fail] Nicht Zwei mal hinereinander.");
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        if (number != gameData.getNextNum()) {
            // game failed
            addReaction(event, fail);

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("[Fail] Fangt wieder bei 1 an.");
            embedBuilder.setFooter("Die erwartete Zahl war eigentlich: " + gameData.getNextNum());
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            reset();
        } else if (number == gameData.getNextNum()) {

            gameData.setNextNum(number + 1);
            gameData.setCurNum(number);
            gameData.setLastUser(event.getAuthor().getId());

            save();

            if (number > gameData.getHighScore()) {
                String trophy = "\uD83C\uDFC6";
                addReaction(event, trophy);
                gameData.setHighScore(gameData.getHighScore() + 1);
            } else if (number != 404 && number <= gameData.getHighScore()) {
                String check = "✅";
                addReaction(event, check);
            }
            save();

            if (number % 100 == 0) {
                String hundred = "\uD83D\uDCAF";
                addReaction(event, hundred);
            }

            // for switch
            String notFound = "❎";
            String hehe = "\uD83C\uDF46";
            String cookie = "\uD83C\uDF6A";
            String computer = "\uD83D\uDCBB";
            String devil = "\uD83D\uDE08";
            String e = "\uD83C\uDDEA";
            String i = "\uD83C\uDDEE";
            String star = "⭐";
            String sponge = "\uD83E\uDDFD";
            String alien = "\uD83D\uDC7D";

            switch (number) {
                case 21 -> {
                    String nine = "9️⃣";
                    addReaction(event, nine);
                    String plus = "➕";
                    addReaction(event, plus);
                    String ten = "🔟";
                    addReaction(event, ten);
                }
                case 24 -> addReaction(event, sponge);
                case 25 -> addReaction(event, star);
                case 34 -> addReaction(event, hehe);
                case 42 -> {
                    String thoughty = "4️⃣";
                    addReaction(event, thoughty);
                    String two = "2️⃣";
                    addReaction(event, two);
                }
                case 51 -> addReaction(event, alien);
                case 69 -> {
                    String n = "\uD83C\uDDF3";
                    addReaction(event, n);
                    addReaction(event, i);
                    String c = "\uD83C\uDDE8";
                    addReaction(event, c);
                    addReaction(event, e);
                }
                case 404 -> addReaction(event, notFound);
                case 420 -> {
                    String l = "\uD83C\uDDF1";
                    addReaction(event, l);
                    addReaction(event, i);
                    String f = "\uD83C\uDDEB";
                    addReaction(event, f);
                    addReaction(event, e);
                }
                case 666 -> addReaction(event, devil);
                case 727 -> addReaction(event, cookie);
                case 1337 -> addReaction(event, computer);
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String command = event.getName();

        if (command.equals("score")) {
            event.getHook().sendMessage("Der Highscore liegt aktuell bei " + gameData.getHighScore()).queue();
            return;
        }

        boolean isAdmin = false;

        for (String s : adminData.getAdmins()) {
            if (s.equals(event.getUser().getId())) {
                isAdmin = true;
            }
        }

        if (!isAdmin) {
            event.getHook().sendMessage("Your not an Admin").queue();
            return;
        }

        switch (command) {
            case "resetrun" -> {
                reset();
                event.getHook().sendMessage("done").queue();
            }
            case "resetall" -> {
                resetAll();
                event.getHook().sendMessage("done").queue();
            }
            default -> event.getHook().sendMessage("unknown command").queue();
        }
    }

    public void save() {
        GameData.saveObjToJSON(gameData);
    }

    public void reset() {
        GameData obj;
        try {
            obj = new GameData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        obj.setHighScore(gameData.getHighScore());
        GameData.saveObjToJSON(obj);
        gameData = GameData.loadObjFromJSON();
    }

    public void resetAll() {
        try {
            GameData.saveObjToJSON(new GameData());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        gameData = GameData.loadObjFromJSON();
    }

    private void addReaction(MessageReceivedEvent event, String emoji) {
        event.getMessage().addReaction(new UnicodeEmojiImpl(emoji)).queue();
    }
}