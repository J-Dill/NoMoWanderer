package nomowanderer;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nomowanderer.tileentity.ExampleTileEntityTileEntity;
import nomowanderer.tileentity.ExampleTileEntityTileEntityRenderer;
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
        ClientRegistry.bindTileEntitySpecialRenderer(ExampleTileEntityTileEntity.class, new ExampleTileEntityTileEntityRenderer());
    }

}