package nomowanderer.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nomowanderer.NoMoWanderer;
import nomowanderer.Registry;
import nomowanderer.items.AntiSolicitorTalismanItem;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void clientOnlySetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(Registry.NO_SOLICITING_TALISMAN.get(),
                    new ResourceLocation(NoMoWanderer.MODID, "enabled"), (stack, level, living, id) -> {
                        return AntiSolicitorTalismanItem.isEnabled(stack) ? 0.0F : 1.0F;
                    });
        });
    }

}
