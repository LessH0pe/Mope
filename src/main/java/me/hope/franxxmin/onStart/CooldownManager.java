package me.hope.franxxmin.onStart;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.utils.VariablesStorage.Cooldown;
import me.hope.franxxmin.utils.VariablesStorage.ServerHashmaps;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.HashMap;

public class CooldownManager {

    public static void onStartUpdate() {
        System.out.println("Importing Default cooldowns..");
        //Import Default Cooldowns (also important)

        Cooldown.def.put(TYPE.HELP, 10.0);
        Cooldown.def.put(TYPE.STATUS, 30.0);
        Cooldown.def.put(TYPE.USERS, 5.0);
        Cooldown.def.put(TYPE.OSU, 6.0);
        Cooldown.def.put(TYPE.OSU_REACT, 5.0);
        System.out.println("Done.\n");

        // Generate cooldown hashmaps for currently registered Servers TODO add extra Message Listener for new servers onRun
        System.out.println("Generating cooldown hashmap for current Guilds..");
        EmbedBuilder eb = Templates.defaultembed();
        eb.setTitle("current Guilds");


        for (Server server : Main.api.getServers()) {
            int all = server.getMemberCount();
            int botcount = 0;
            int usercount = 0;

            for (User x : server.getMembers()) {
                if (x.isBot()) {
                    botcount++;
                }


                if (!x.isBot()) {
                    usercount++;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Members [BOTS+USERS]: " + all + "\n");
            sb.append("Members [BOTS]: " + botcount + "\n");
            sb.append("Members [USERS]: " + usercount + "\n");
            eb.addField(server.getName(), "Server ID: " + server.getIdAsString() + "\n Server Owner: " + server.getOwner().getDiscriminatedName() + " (" + server.getOwner().getIdAsString() + ")\n \n" + sb.toString());
            HashMap<TYPE, Double> temp = new HashMap<>();

            temp.put(TYPE.HELP, 0.0);
            temp.put(TYPE.STATUS, 0.0);
            temp.put(TYPE.USERS, 0.0);
            temp.put(TYPE.OSU, 0.0);
            temp.put(TYPE.OSU_REACT, 0.0);
            Cooldown.cooldowntrack.put(server.getIdAsString(), temp);
            ServerHashmaps.ID.add(server.getIdAsString());
        }
        Main.api.updateActivity(ActivityType.LISTENING, Main.api.getServers().size() + " Guilds | mp> help");
        if (!Main.localmode) {

        }

        System.out.println("Done.\n");
        eb.setColor(Main.blurple);
        eb.addField("Bot-Invite", Main.api.createBotInvite());
        Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(eb).join();


    }


    public static void updateServer(Server srv) {
        String ID = srv.getIdAsString();
        System.out.println("New Guild detected! Updating for ID " + ID);

        HashMap<TYPE, Double> temp = new HashMap<>();
        temp.put(TYPE.HELP, 0.0);
        temp.put(TYPE.STATUS, 0.0);
        temp.put(TYPE.USERS, 0.0);
        temp.put(TYPE.OSU, 0.0);
        temp.put(TYPE.OSU_REACT, 0.0);
        Cooldown.cooldowntrack.put(ID, temp);
        ServerHashmaps.ID.add(ID);
        System.out.println("Done.\n");

    }

    public static void removeServer(Server srv) {
        String ID = srv.getIdAsString();
        System.out.println("Guild disconnect detected! Removing " + ID);

        ServerHashmaps.ID.remove(ID);
        Cooldown.cooldowntrack.remove(ID);

    }

    public enum TYPE {

        HELP,
        STATUS,
        USERS,
        OSU,
        OSU_REACT
        // Enumerationskonstanten


    }

    public static Double getDefault(String cooldowntype) {

        return Cooldown.def.get(cooldowntype);
    }

}
