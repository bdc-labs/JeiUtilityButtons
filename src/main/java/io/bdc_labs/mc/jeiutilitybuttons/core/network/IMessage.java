package io.bdc_labs.mc.jeiutilitybuttons.core.network;

import net.minecraftforge.fml.network.NetworkEvent;

import java.io.Serializable;

public interface IMessage extends Serializable {
    boolean receive(NetworkEvent.Context context);
}
