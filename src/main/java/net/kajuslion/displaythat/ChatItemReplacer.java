package net.kajuslion.displaythat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

@EventBusSubscriber(modid = DisplayThat.MODID)
public class ChatItemReplacer {
    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        String message = event.getMessage().getString();
        ServerPlayer player = event.getPlayer();

        if (message.contains("[item]")) {
            ItemStack heldItem = player.getMainHandItem();
            if (!heldItem.isEmpty()) {
                Component itemComponent = heldItem.getDisplayName();

                String[] parts = message.split("\\[item\\]", 2);
                MutableComponent finalMessage = Component.empty();

                for (int i = 0; i < parts.length; i++) {
                    finalMessage = finalMessage.append(Component.literal(parts[i]));
                    if (i < parts.length - 1) {
                        finalMessage = finalMessage.append(itemComponent);
                    }
                }

                event.setMessage(finalMessage);
            }
        }
    }
}