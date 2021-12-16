package io.bdc_labs.mc.jeiutilitybuttons.core.network;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkHandler {
    private static final int PROTOCOL = 1;
    public final SimpleChannel channel;
    private int i = 0;

    public NetworkHandler() {
        String protocol = Integer.toString(PROTOCOL);
        channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(JeiUtilityButtons.MODID, "main"))
                .networkProtocolVersion(() -> protocol)
                .clientAcceptedVersions(protocol::equals)
                .serverAcceptedVersions(protocol::equals)
                .simpleChannel();
    }


    public <T extends IMessage> void register(Class<T> clazz, NetworkDirection dir) {
        BiConsumer<T, FriendlyByteBuf> encoder = (msg, buf) -> MessageSerializer.writeObject(msg, buf);

        Function<FriendlyByteBuf, T> decoder = (buf) -> {
            try {
                T msg = clazz.newInstance();
                MessageSerializer.readObject(msg, buf);
                return msg;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        BiConsumer<T, Supplier<NetworkEvent.Context>> consumer = (msg, supp) -> {
            NetworkEvent.Context context = supp.get();
            if (context.getDirection() != dir)
                return;

            context.setPacketHandled(msg.receive(context));
        };

        channel.registerMessage(i, clazz, encoder, decoder, consumer);
        i++;
    }

    public void sendToPlayer(IMessage msg, ServerPlayer player) {
        channel.sendTo(msg, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public void sendToServer(IMessage msg) {
        channel.sendToServer(msg);
    }
}
