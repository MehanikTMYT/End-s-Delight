package cn.foggyhillside.ends_delight.qa;

import cn.foggyhillside.ends_delight.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import java.util.*;

@EventBusSubscriber(modid="ends_delight_qa", value=Dist.CLIENT)
public class ClientQa {
    private static int ticks;
    private static boolean done;
    @SubscribeEvent public static void tick(ClientTickEvent.Post e) {
        var mc = Minecraft.getInstance();
        if (done || mc.level == null || mc.player == null || mc.screen != null || ++ticks < 100) return;
        done = true;
        try { probe(mc); mc.setScreen(new Catalogue()); }
        catch (Throwable t) { EndsDelightQa.check("client-probe", false, t); t.printStackTrace(); }
    }
    static boolean valid(BakedQuad q) { return !q.materialInfo().sprite().contents().name().getPath().equals("missingno"); }
    static void probe(Minecraft mc) throws Exception {
        int count=0;
        for (var entry : ModItems.ITEMS.getEntries()) {
            var stack = new ItemStack(entry.get());
            var state = new ItemStackRenderState();
            mc.getItemModelResolver().updateForTopItem(state, stack, ItemDisplayContext.GUI, mc.level, mc.player, 42);
            boolean ok = !state.isEmpty();
            var particle = state.pickParticleMaterial(mc.level.getRandom());
            ok &= particle != null && !particle.sprite().contents().name().getPath().equals("missingno");
            // Inspect every baked item layer, including textures other than the particle texture.
            var layersField = ItemStackRenderState.class.getDeclaredField("layers"); layersField.setAccessible(true);
            var countField = ItemStackRenderState.class.getDeclaredField("activeLayerCount"); countField.setAccessible(true);
            Object[] layers = (Object[])layersField.get(state);
            for (int i=0; i<countField.getInt(state); i++) {
                var field = layers[i].getClass().getDeclaredField("quads"); field.setAccessible(true);
                for (Object q : (List<?>)field.get(layers[i])) ok &= valid((BakedQuad)q);
            }
            EndsDelightQa.check("client-item-model/"+entry.getId(), ok, particle == null ? "no particle" : particle.sprite().contents().name());
            count++;
        }
        int states=0;
        var models = mc.getModelManager().getBlockStateModelSet();
        for (var entry : ModBlocks.BLOCKS.getEntries()) for (var state : entry.get().getStateDefinition().getPossibleStates()) {
            var model = models.get(state);
            List<BlockStateModelPart> parts = new ArrayList<>();
            model.collectParts(mc.level.getRandom(), parts);
            boolean ok = model != models.missingModel() && !parts.isEmpty();
            for (var part : parts) {
                for (var q : part.getQuads(null)) ok &= valid(q);
                for (var direction : Direction.values()) for (var q : part.getQuads(direction)) ok &= valid(q);
            }
            EndsDelightQa.check("client-block-model/"+state, ok, "parts="+parts.size());
            states++;
        }
        EndsDelightQa.log("CLIENT_MODELS items="+count+" blockstates="+states);
    }
    static class Catalogue extends Screen {
        private final List<ItemStack> items = ModItems.ITEMS.getEntries().stream().map(x -> new ItemStack(x.get())).toList();
        Catalogue() { super(Component.literal("End's Delight QA catalogue")); }
        @Override public boolean isPauseScreen() { return false; }
        @Override public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partial) {
            g.fill(0,0,width,height,0xF0181424);
            g.text(font, "End's Delight | 63 items | MC 26.1.2 / NeoForge 26.1.2.122", 12, 10, 0xFFE8D6FF);
            int cellW=(width-20)/9, cellH=(height-48)/7;
            for(int i=0;i<items.size();i++) {
                int x=10+(i%9)*cellW, y=28+(i/9)*cellH;
                g.fill(x,y,x+cellW-2,y+cellH-2,0xFF302940);
                g.item(items.get(i),x+cellW/2-8,y+2);
                String label = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(items.get(i).getItem()).getPath();
                g.text(font,font.plainSubstrByWidth(label,cellW-4),x+2,y+21,0xFFDFD5EA);
            }
            g.text(font,"Actual in-game item rendering. Esc: inspect the isolated End test area.",12,height-13,0xFFB9ADCA);
        }
    }
}
