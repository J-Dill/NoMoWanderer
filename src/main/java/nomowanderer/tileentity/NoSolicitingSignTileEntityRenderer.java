package nomowanderer.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.SignModel;
import net.minecraft.util.ResourceLocation;
import nomowanderer.NoMoWanderer;

public class NoSolicitingSignTileEntityRenderer extends TileEntityRenderer<NoSolicitingSignTileEntity> {
    private static final ResourceLocation signTexture = new ResourceLocation(NoMoWanderer.MODID, "textures/block/no_soliciting_sign.png");
    private final SignModel model = new SignModel();

    @Override
    public void render(NoSolicitingSignTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        BlockState blockstate = tileEntityIn.getBlockState();
        GlStateManager.pushMatrix();
        float f = 0.6666667F;
        if (blockstate.getBlock() instanceof StandingSignBlock) {
            GlStateManager.translatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
            GlStateManager.rotatef(-((float)(blockstate.get(StandingSignBlock.ROTATION) * 360) / 16.0F), 0.0F, 1.0F, 0.0F);
            this.model.getSignStick().showModel = true;
        } else {
            GlStateManager.translatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
            GlStateManager.rotatef(-blockstate.get(WallSignBlock.FACING).getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(0.0F, -0.3125F, -0.4375F);
            this.model.getSignStick().showModel = false;
        }

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 2.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(this.getTexture(blockstate.getBlock()));
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scalef(0.6666667F, -0.6666667F, -0.6666667F);
        this.model.renderSign();
        GlStateManager.popMatrix();
        GlStateManager.translatef(0.0F, 0.33333334F, 0.046666667F);
        GlStateManager.scalef(0.010416667F, -0.010416667F, 0.010416667F);
        GlStateManager.normal3f(0.0F, 0.0F, -0.010416667F);
        GlStateManager.depthMask(false);

        GlStateManager.depthMask(true);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private ResourceLocation getTexture(Block p_217658_1_) {
        return signTexture;
    }
}
