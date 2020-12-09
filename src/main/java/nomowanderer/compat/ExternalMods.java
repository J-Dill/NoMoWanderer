package nomowanderer.compat;

import com.lazy.baubles.api.BaublesAPI;
import net.minecraftforge.fml.ModList;

public enum ExternalMods {

    CURIOS("curios"),
    BAUBLES(BaublesAPI.MOD_ID);

    private final boolean loaded;

    ExternalMods(String modid) {
        this.loaded = ModList.get() != null && ModList.get().isLoaded(modid);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

}
