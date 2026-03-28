package nomowanderer.compat;

import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosResources;

public enum ExternalMods {

    CURIOS(CuriosResources.MOD_ID);
    private final boolean loaded;

    ExternalMods(String modid) {
        this.loaded = ModList.get() != null && ModList.get().isLoaded(modid);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

}
