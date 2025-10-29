package lk.pixcapsoft.diamondscanner;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = DiamondScannerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindingHandler {
    public static KeyMapping scanKey;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        scanKey = new KeyMapping(
                "key.pixcapdiamondscanner.scan",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "key.categories.pixcapdiamondscanner"
        );
        event.register(scanKey);
    }

    @Mod.EventBusSubscriber(modid = DiamondScannerMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft client = Minecraft.getInstance();
                while (scanKey.consumeClick()) {
                    scanForDiamonds(client);
                }
            }
        }
    }

    private static void scanForDiamonds(Minecraft client) {
        if (client.player == null || client.level == null) return;
        
        // Only allow in singleplayer
        if (!client.hasSingleplayerServer()) {
            client.player.sendSystemMessage(Component.literal("§cDiamond Scanner only works in singleplayer worlds."));
            return;
        }
        
        client.player.sendSystemMessage(Component.literal("Starting scanning engine..."));

        BlockPos playerPos = client.player.blockPosition();
        int radius = 32;
        int found = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos scanPos = playerPos.offset(x, y, z);
                    if (client.level.getBlockState(scanPos).is(Blocks.DIAMOND_ORE) ||
                            client.level.getBlockState(scanPos).is(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                        client.player.sendSystemMessage(Component.literal("💎 Found diamond at: " + scanPos.toShortString()));
                        found++;
                    }
                }
            }
        }

        if (found == 0) {
            client.player.sendSystemMessage(Component.literal("No diamonds found nearby."));
        } else {
            client.player.sendSystemMessage(Component.literal("Scan complete. Found " + found + " diamond ores."));
            client.player.sendSystemMessage(Component.literal("Open chat if you only seeing few..."));
        }
    }
}