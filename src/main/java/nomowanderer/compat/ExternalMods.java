package nomowanderer.compat;

//import com.github.alexthe666.rats.RatsMod;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

public enum ExternalMods {

    CURIOS(CuriosApi.MODID),
//    RATS(RatsMod.MODID);
    ;
    private final boolean loaded;

    ExternalMods(String modid) {
        this.loaded = ModList.get() != null && ModList.get().isLoaded(modid);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

}
