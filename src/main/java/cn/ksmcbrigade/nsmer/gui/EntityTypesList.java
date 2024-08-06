package cn.ksmcbrigade.nsmer.gui;

import cn.ksmcbrigade.nsmer.NotSoManyEntityRendering;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class EntityTypesList extends ContainerObjectSelectionList<EntityTypesList.TypeEntry> {

    final ConfiguredScreen config;
    int maxNameWidth = 60;

    public static Collection<EntityType<?>> entityTypes = ForgeRegistries.ENTITY_TYPES.getValues();
    public static EntityType<?>[] types = entityTypes.toArray(new EntityType<?>[0]);
    
    public EntityTypesList(ConfiguredScreen p_193861_, Minecraft p_193862_) throws InstantiationException, IllegalAccessException {
        super(p_193862_,p_193861_.width + 45, p_193861_.height, 20, p_193861_.height - 32, 20);
        this.config = p_193861_;

        //Arrays.sort(types);
        
        for(EntityType<?> type:types){
            this.addEntry(new TypeEntry(type));
        }
    }

    public void refreshEntries() {
        this.children().forEach(TypeEntry::refreshEntry);
    }

    public TypeEntry create(EntityType<?> type){
        return new TypeEntry(type);
    }

    public void add(TypeEntry typeEntry){
        this.addEntry(typeEntry);
    }

    public boolean has(TypeEntry entry){
        for(TypeEntry entry1:this.children()){
            if(entry1.name.equalsIgnoreCase(entry.name)){
                return true;
            }
        }
        return false;
    }

    public class TypeEntry extends ContainerObjectSelectionList.Entry<TypeEntry> {

        public EntityType<?> type;
        public String name;

        public AbstractWidget render;

        public boolean clicked = false;

        public TypeEntry(EntityType<?> type) {
            this.type = type;
            this.name = Component.translatable(this.type.getDescriptionId()).getString();
            String register = NotSoManyEntityRendering.getRegisterName(this.type);
            boolean has = NotSoManyEntityRendering.blackList.contains(register);
            this.render = OptionInstance.createBoolean(has ? "gui.nsmer.config.render" : "gui.nsmer.config.no_render",has).createButton(EntityTypesList.this.minecraft.options,60,20,60,(e) -> {
                if(!e.booleanValue()){
                    while(NotSoManyEntityRendering.blackList.contains(register)){
                        NotSoManyEntityRendering.blackList.remove(register);
                    }
                    try {
                        NotSoManyEntityRendering.save();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    if(!NotSoManyEntityRendering.blackList.contains(register)) NotSoManyEntityRendering.blackList.add(register);
                    try {
                        NotSoManyEntityRendering.save();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                this.refreshEntry();
            });
            this.refreshEntry();
        }

        @Override
        public void render(GuiGraphics p_283112_, int p_93524_, int p_93525_, int p_93526_, int p_93527_, int p_93528_, int p_93529_, int p_93530_, boolean p_93531_, float p_93532_) {
            boolean em = EntityTypesList.this.config.editBox.getValue().isEmpty();
            if(!em && !this.name.toLowerCase().contains(EntityTypesList.this.config.editBox.getValue().toLowerCase())) {
                while (!EntityTypesList.this.removeEntry(this)){
                }
                return;
            }
            Minecraft minecraft = Minecraft.getInstance();
            int k = p_93526_ + 90 - 100;
            p_283112_.drawString(minecraft.font, this.name, k, p_93525_ + p_93528_ / 2 - 9 / 2, 16777215, false);

            this.render.setX(p_93526_+190-30);
            this.render.setY(p_93525_);

            this.render.setMessage(NotSoManyEntityRendering.blackList.contains(NotSoManyEntityRendering.getRegisterName(this.type)) ? Component.translatable("gui.nsmer.config.render") : Component.translatable("gui.nsmer.config.no_render"));
            this.render.render(p_283112_,p_93529_,p_93530_,p_93532_);
        }

        public void refreshEntry() {
            this.render.setMessage(NotSoManyEntityRendering.blackList.contains(NotSoManyEntityRendering.getRegisterName(this.type))?Component.translatable("gui.nsmer.config.render"):Component.translatable("gui.nsmer.config.no_render"));
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of();
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.render);
        }
    }
}
