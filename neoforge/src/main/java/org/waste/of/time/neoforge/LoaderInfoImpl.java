package org.waste.of.time.neoforge;

//import net.minecraftforge.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLLoader;

public class LoaderInfoImpl {
    public static String getVersion() {
        return FMLLoader.getLoadingModList().getModFileById("worldtools").versionString();
    }
}
