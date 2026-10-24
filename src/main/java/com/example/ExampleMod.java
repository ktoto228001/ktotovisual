import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ktotovisuals implements ModInitializer, HudRenderCallback {
    public static final String MOD_ID = "ktotovisuals";
    private static KeyBinding menuKeyBind;
    private static final List<Module> modules = new ArrayList<>();

    @Override
    public void onInitialize() {
        initModules();
        HudRenderCallback.EVENT.register(this);

        menuKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ktotovisuals.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.ktotovisuals"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (menuKeyBind.wasPressed() && client.currentScreen == null) {
                client.setScreen(new ClickGuiScreen());
            }
        });
    }

    private void initModules() {
        // INTERFACE
        modules.add(new Module("Watermark", "Interface"));
        modules.add(new Module("Armor Helper", "Interface"));
        modules.add(new Module("Potions Helper", "Interface"));
        modules.add(new Module("Cooldowns", "Interface"));
        modules.add(new Module("Inventory HUD", "Interface"));
        modules.add(new Module("Friends List", "Interface"));
        modules.add(new Module("Coordinates", "Interface"));
        modules.add(new Module("Keystrokes", "Interface"));
        modules.add(new Module("Totem Display", "Interface"));
        modules.add(new Module("Smooth Bar", "Interface"));
        modules.add(new Module("Smooth Chat", "Interface"));
        modules.add(new Module("Smooth Exp Bar", "Interface"));
        modules.add(new Module("Smooth Buttons", "Interface"));
        modules.add(new Module("Smooth Menus", "Interface"));
        modules.add(new Module("Smooth Tab List", "Interface"));
        // VISUALS
        modules.add(new Module("Hit Particles", "Visuals"));
        modules.add(new Module("Snow", "Visuals"));
        modules.add(new Module("Hit Bubbles", "Visuals"));
        modules.add(new Module("Chin Hat", "Visuals"));
        modules.add(new Module("Prediction", "Visuals"));
        modules.add(new Module("Fog Color", "Visuals"));
        modules.add(new Module("Sky Color", "Visuals"));
        modules.add(new Module("Target ESP", "Visuals"));
        modules.add(new Module("Time Changer", "Visuals"));
        modules.add(new Module("Custom Cape", "Visuals"));
        modules.add(new Module("Pearl Particles", "Visuals"));
        modules.add(new Module("Trails", "Visuals"));
        modules.add(new Module("Nimb", "Visuals"));
        modules.add(new Module("Motion Blur", "Visuals"));
        modules.add(new Module("See Own Name", "Visuals"));
        modules.add(new Module("Friend Armor", "Visuals"));
        modules.add(new Module("Custom Hitbox", "Visuals"));
        modules.add(new Module("Hit Color", "Visuals"));
        modules.add(new Module("Loot Beams", "Visuals"));
        // OTHER
        modules.add(new Module("Small Player", "Other"));
        modules.add(new Module("Big Lol", "Other"));
        modules.add(new Module("No Hurt Cam", "Other"));
        modules.add(new Module("Crosshair", "Other"));
        modules.add(new Module("Freelook", "Other"));
        modules.add(new Module("No Fire Overlay", "Other"));
        modules.add(new Module("Saturation", "Other"));
        modules.add(new Module("Fullbright", "Other"));
        modules.add(new Module("No Rain", "Other"));
        modules.add(new Module("ViewModel", "Other"));
        modules.add(new Module("Swing Animation", "Other"));
        modules.add(new Module("Screen Stretch", "Other"));
        modules.add(new Module("TNT Timer", "Other"));
        modules.add(new Module("Player Clone", "Other"));
        // HELPER / SETTINGS / CONFIG
        modules.add(new Module("Auto Fix", "Helper"));
        modules.add(new Module("Keybind", "Helper"));
        modules.add(new Module("Better Near", "Helper"));
        modules.add(new Module("Smaller Totem", "Helper"));
        modules.add(new Module("Health Warning", "Helper"));
        modules.add(new Module("Safety", "Helper"));
        modules.add(new Module("Custom Color", "Settings"));
        modules.add(new Module("Config Manager", "Config"));
    }

    public static List<Module> getModulesByCategory(String category) {
        return modules.stream().filter(m -> m.category.equals(category)).collect(Collectors.toList());
    }

    public static List<Module> searchModules(String query) {
        return modules.stream().filter(m -> m.name.toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
    }

    public static boolean isEnabled(String name) {
        return modules.stream().filter(m -> m.name.equals(name)).findFirst().map(m -> m.enabled).orElse(false);
    }

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        if (isEnabled("Watermark")) {
            context.fill(5, 5, 110, 22, 0xD90A0A0A);
            context.fillGradient(5, 5, 110, 7, 0xFF8A2BE2, 0xFF0000FF);
            context.drawText(client.textRenderer, "ktotovisuals | FPS: " + client.getCurrentFps(), 10, 10, 0xFFFFFFFF, true);
        }

        if (isEnabled("Armor Helper")) {
            int y = 40;
            context.fill(5, y, 90, y + 65, 0xD90A0A0A);
            context.drawText(client.textRenderer, "Броня:", 10, y + 5, 0xFF8A2BE2, true);
            int i = 0;
            for (ItemStack armor : client.player.getArmorItems()) {
                if (!armor.isEmpty()) {
                    int dmg = armor.getMaxDamage() - armor.getDamage();
                    context.drawText(client.textRenderer, armor.getItem().getName().getString().substring(0, Math.min(6, armor.getItem().getName().getString().length())) + ": " + dmg, 10, y + 18 + (i * 10), 0xFFFFFFFF, true);
                }
                i++;
            }
        }

        if (isEnabled("Coordinates")) {
            String coords = String.format("XYZ: %d %d %d", client.player.getBlockX(), client.player.getBlockY(), client.player.getBlockZ());
            context.fill(5, client.getWindow().getScaledHeight() - 20, 110, client.getWindow().getScaledHeight() - 5, 0xD90A0A0A);
            context.drawText(client.textRenderer, coords, 10, client.getWindow().getScaledHeight() - 15, 0xFFFFFFFF, true);
        }
    }

    public static class Module {
        public String name;
        public String category;
        public boolean enabled = false;
        public Module(String name, String category) { this.name = name; this.category = category; }
        public void toggle() { this.enabled = !this.enabled; }
    }
}

