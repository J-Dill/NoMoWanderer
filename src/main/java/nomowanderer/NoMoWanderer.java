package nomowanderer;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;
import nomowanderer.tileentity.NoSolicitingSignTileEntityRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(NoMoWanderer.MODID)
public class NoMoWanderer {

    public static final String MODID = "nomowanderer";

    private static final Logger LOGGER = LogManager.getLogger(NoMoWanderer.MODID + " Mod Event Subscriber");

    public NoMoWanderer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
//        ScreenManager.registerFactory(PedestalContainer.TYPE, PedestalScreen::new);
        ClientRegistry.bindTileEntitySpecialRenderer(NoSolicitingSignTileEntity.class, new NoSolicitingSignTileEntityRenderer());
    }

}