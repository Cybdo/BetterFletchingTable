package me.cybdo.better_fletching_table;

import me.cybdo.better_fletching_table.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Better_fletching_table implements ModInitializer {

    public static final String MOD_ID = "better_fletching_table";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
    }
}
