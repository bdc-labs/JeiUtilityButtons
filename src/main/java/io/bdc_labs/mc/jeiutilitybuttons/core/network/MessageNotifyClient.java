package io.bdc_labs.mc.jeiutilitybuttons.core.network;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import net.minecraftforge.network.NetworkEvent;

public class MessageNotifyClient implements IMessage {
    public MessageNotifyClient() { }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        JeiUtilityButtons.isServerSidePresent = true;
        return true;
    }

}
