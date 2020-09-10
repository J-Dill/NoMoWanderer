package nomowanderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.NoMoWanderer;

@OnlyIn(Dist.CLIENT)
public class NoSolicitingSignTileEntityRenderer extends TileEntityRenderer<NoSolicitingSignTileEntity> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation(NoMoWanderer.MODID, "textures/block/no_soliciting_sign.png");
    private final SignTileEntityRenderer.SignModel model = new SignTileEntityRenderer.SignModel();

    public NoSolicitingSignTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(NoSolicitingSignTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockstate = tileEntityIn.getBlockState();
        matrixStackIn.push();
        if (blockstate.getBlock() instanceof StandingSignBlock) {
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            float f1 = -((float)(blockstate.get(StandingSignBlock.ROTATION) * 360) / 16.0F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
            this.model.signStick.showModel = true;
        } else {
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            float f4 = -blockstate.get(WallSignBlock.FACING).getHorizontalAngle();
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f4));
            matrixStackIn.translate(0.0D, -0.3125D, -0.4375D);
            this.model.signStick.showModel = false;
        }

        matrixStackIn.push();
        matrixStackIn.scale(0.6666667F, -0.6666667F, -0.6666667F);
        IVertexBuilder iVertexBuilder = bufferIn.getBuffer(this.model.getRenderType(SIGN_TEXTURE));
        this.model.signBoard.render(matrixStackIn, iVertexBuilder, combinedLightIn, combinedOverlayIn);
        this.model.signStick.render(matrixStackIn, iVertexBuilder, combinedLightIn, combinedOverlayIn);
        matrixStackIn.pop();
        matrixStackIn.translate(0.0D, 0.33333334F, 0.046666667F);
        matrixStackIn.scale(0.010416667F, -0.010416667F, 0.010416667F);

        matrixStackIn.pop();
    }
}
