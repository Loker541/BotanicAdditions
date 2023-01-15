package org.zeith.botanicadds.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.zeith.botanicadds.init.BlocksBA;
import vazkii.botania.client.render.block_entity.TEISR;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ISTER
{
	public static final Map<Block, Function<Block, TEISR>> BE_ITEM_RENDERER_FACTORIES = Map.of(
			BlocksBA.ELVEN_BREWERY, TEISR::new
	);
	
	// Nulls in ctor call are fine, we don't use those fields
	private static final BlockEntityWithoutLevelRenderer RENDERER = new BlockEntityWithoutLevelRenderer(null, null)
	{
		private final Map<Item, TEISR> renderers = new IdentityHashMap<>();
		
		@Override
		public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform,
								 PoseStack ps, MultiBufferSource buffers, int light, int overlay)
		{
			renderers.computeIfAbsent(stack.getItem(), i ->
			{
				var block = Block.byItem(i);
				return BE_ITEM_RENDERER_FACTORIES.get(block).apply(block);
			}).render(stack, transform, ps, buffers, light, overlay);
		}
	};
	
	private static final IClientItemExtensions PROPS = new IClientItemExtensions()
	{
		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer()
		{
			return ISTER.RENDERER;
		}
	};
	
	public static void initItem(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(PROPS);
	}
}