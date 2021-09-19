package nomowanderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.NoMoWanderer;

@OnlyIn(Dist.CLIENT)
public class NoSolicitingSignBlockEntityRenderer implements
    BlockEntityRenderer<NoSolicitingSignBlockEntity> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation(NoMoWanderer.MODID, "textures/block/no_soliciting_sign.png");
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation(NoMoWanderer.MODID, "no_soliciting_sign"), "main");
    private final SignRenderer.SignModel model;

    public NoSolicitingSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        model = new SignRenderer.SignModel(context.bakeLayer(MODEL_LAYER));
    }

    @Override
    public void render(NoSolicitingSignBlockEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn,
        MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockstate = tileEntityIn.getBlockState();
        matrixStackIn.pushPose();
        if (blockstate.getBlock() instanceof StandingSignBlock) {
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            float f1 = -((float)(blockstate.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f1));
            this.model.stick.visible = true;
        } else {
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            float f4 = -blockstate.getValue(WallSignBlock.FACING).toYRot();
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f4));
            matrixStackIn.translate(0.0D, -0.3125D, -0.4375D);
            this.model.stick.visible = false;
        }

        matrixStackIn.pushPose();
        matrixStackIn.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer iVertexBuilder = bufferIn.getBuffer(this.model.renderType(SIGN_TEXTURE));
        this.model.root.render(matrixStackIn, iVertexBuilder, combinedLightIn, combinedOverlayIn);
        this.model.stick.render(matrixStackIn, iVertexBuilder, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();
        matrixStackIn.translate(0.0D, 0.33333334F, 0.046666667F);
        matrixStackIn.scale(0.010416667F, -0.010416667F, 0.010416667F);

        matrixStackIn.popPose();
    }
}
