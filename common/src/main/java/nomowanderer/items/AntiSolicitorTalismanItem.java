package nomowanderer.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import nomowanderer.CommonRegistry;
import nomowanderer.Config;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.util.HoverTextUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AntiSolicitorTalismanItem extends Item {

    public static final String ID = "no_mo_wanderer_totem";
    public static final ResourceKey<Item> KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, ID));

    public AntiSolicitorTalismanItem() {
        super(new Properties().setId(KEY).stacksTo(1));
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack defaultInstance = super.getDefaultInstance();
        defaultInstance.set(CommonRegistry.ENABLED.get(), Boolean.TRUE);
        return defaultInstance;
    }

    public @NotNull ItemStack getDefaultInstance(boolean enabled) {
        ItemStack defaultInstance = super.getDefaultInstance();
        defaultInstance.set(CommonRegistry.ENABLED.get(), enabled);
        return defaultInstance;
    }

    @Override
    public InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (player.isShiftKeyDown() && !level.isClientSide()) {
            ItemStack item = player.getItemInHand(hand);
            item.set(CommonRegistry.ENABLED.get(), Boolean.FALSE.equals(item.get(CommonRegistry.ENABLED.get())));
            return InteractionResult.PASS;
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
                                Consumer<Component> toolTips, TooltipFlag flag) {
        addEnabledTooltip(stack, toolTips);
        if (Screen.hasShiftDown()) {
            HoverTextUtil.addCommonText(toolTips, Config.TALISMAN_WATCH_RADIUS);
//            String totemMessage = String.format(
//                    "Can be anywhere in your inventory%s.", ExternalMods.CURIOS.isLoaded() ? " or a Curios slot" : ""
//            );
            String totemMessage = "Can be anywhere in your inventory.";
            toolTips.accept(
                    Component.literal(totemMessage).withStyle(ChatFormatting.YELLOW)
            );
            toolTips.accept(
                    Component.literal("Sneak right-click ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal("to toggle on/off").withStyle(ChatFormatting.GRAY))
            );
        } else {
            HoverTextUtil.addHoldShiftText(toolTips);
        }
        super.appendHoverText(stack, context, tooltipDisplay, toolTips, flag);
    }

    private static void addEnabledTooltip(@NotNull ItemStack stack, Consumer<Component> toolTips) {
        boolean enabled = AntiSolicitorTalismanItem.isEnabled(stack);
        toolTips.accept(
                Component.literal("Enabled: ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(enabled ? "Yes" : "No").withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED))
        );
    }

    public static boolean isEnabled(ItemStack stack) {
        if (!stack.getItem().equals(CommonRegistry.NO_SOLICITING_TALISMAN.get())) {
            return false;
        }
        // If there are no tags, assume it is enabled (for backwards compatability)
        if (stack.get(CommonRegistry.ENABLED.get()) == null) {
            return true;
        }
        return Boolean.TRUE.equals(stack.get(CommonRegistry.ENABLED.get()));
    }

}
