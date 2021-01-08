package nomowanderer.compat;

import com.github.alexthe666.rats.RatsMod;
import com.lazy.baubles.api.BaublesAPI;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

public enum ExternalMods {

    CURIOS(CuriosApi.MODID),
    BAUBLES(BaublesAPI.MOD_ID),
    RATS(RatsMod.MODID);

    private final boolean loaded;

    ExternalMods(String modid) {
        this.loaded = ModList.get() != null && ModList.get().isLoaded(modid);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

}
