package lk.pixcapsoft.diamondscanner;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The main mod class - this is the entry point of your mod
@Mod(DiamondScannerMod.MOD_ID)
public class DiamondScannerMod {
    // Your mod's ID - must match the one in mods.toml
    public static final String MOD_ID = "pixcapdiamondscanner";
    
    // Logger for debugging
    private static final Logger LOGGER = LogUtils.getLogger();

    // Constructor - this runs when your mod loads
    public DiamondScannerMod() {
        LOGGER.info("Diamond Scanner Mod is loading...");
        
        // That's it! The KeyBindingHandler will register itself automatically
        // because it uses @Mod.EventBusSubscriber annotation
        
        LOGGER.info("Diamond Scanner Mod loaded successfully!");
    }
}