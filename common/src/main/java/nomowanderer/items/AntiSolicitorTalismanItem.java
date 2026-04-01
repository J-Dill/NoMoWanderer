package nomowanderer.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
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
    
    // Add constant for the NBT key to avoid magic strings
    private static final String ENABLED_KEY = "enabled";

    public AntiSolicitorTalismanItem() {
        super(new Properties().setId(KEY).stacksTo(1));
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        return getDefaultInstance(true);
    }

    public @NotNull ItemStack getDefaultInstance(boolean enabled) {
        ItemStack defaultInstance = super.getDefaultInstance();
        setEnabled(defaultInstance, enabled);
        return defaultInstance;
    }

    @Override
    public InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (player.isShiftKeyDown() && !level.isClientSide()) {
            ItemStack item = player.getItemInHand(hand);
            boolean currentEnabled = isEnabled(item);
            setEnabled(item, !currentEnabled);
            return InteractionResult.PASS;
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
                                Consumer<Component> toolTips, TooltipFlag flag) {
        addEnabledTooltip(stack, toolTips);
        if (Minecraft.getInstance().hasShiftDown()) {
            HoverTextUtil.addCommonText(toolTips, Config.TALISMAN_WATCH_RADIUS);
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
        
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return false; // If no custom data, assume disabled
        }
        
        CompoundTag tag = customData.copyTag();
        return tag.getBoolean(ENABLED_KEY).orElse(false);
    }
    
    /**
     * Helper method to set the enabled state of a talisman item
     */
    private static void setEnabled(ItemStack stack, boolean enabled) {
        CompoundTag compoundTag = getOrCreateCompoundTag(stack);
        compoundTag.putBoolean(ENABLED_KEY, enabled);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }
    
    /**
     * Helper method to get existing compound tag or create a new one
     */
    private static CompoundTag getOrCreateCompoundTag(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : new CompoundTag();
    }

}
