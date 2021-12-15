package io.bdc_labs.mc.jeiutilitybuttons.core.network;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Created by universal on 16.09.16 16:17.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class MessageNotifyClient implements IMessage {
    public MessageNotifyClient() { }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        JeiUtilityButtons.isServerSidePresent = true;
        return true;
    }

}
