package cn.ksmcbrigade.nsmer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class ConfiguredScreen extends Screen {

    public EntityTypesList list;

    public EditBox editBox;

    public ConfiguredScreen() {
        super(Component.translatable("gui.nsmer.config.title"));
    }

    @Override
    protected void init() {
        try {
            this.list = new EntityTypesList(this,this.minecraft);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            Minecraft.getInstance().screen = new ConfiguredScreen();
        }

        this.list.setRenderBackground(false);

        this.addWidget(this.list);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_280847_) -> this.onClose()).bounds(Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2, this.height - 29, 150, 20).build());

        this.editBox = this.addRenderableWidget(new EditBox(this.font,this.width/2 - 155,this.height-29,150,20,Component.empty()));
    }

    @Override
    public void render(@NotNull GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        try {
            this.renderBackground(p_281549_);
            this.list.render(p_281549_, p_281550_, p_282878_, p_282465_);
            p_281549_.drawCenteredString(this.font, this.title, this.width / 2, 8, 16777215);
            super.render(p_281549_, p_281550_, p_282878_, p_282465_);
        }
        catch (Exception e){
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        for(EntityType<?> type:EntityTypesList.types){
            EntityTypesList.TypeEntry entry = this.list.create(type);
            if(!this.list.has(entry)){
                boolean em = this.editBox.getValue().isEmpty();
                if(!em && !entry.name.toLowerCase().contains(this.editBox.getValue().toLowerCase())) return;
                this.list.add(entry);
            }
        }
    }
}
