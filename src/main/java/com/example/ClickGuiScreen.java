import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private String currentCategory = "Interface";
    private TextFieldWidget searchField;
    private final String[] categories = {"Interface", "Visuals", "Other", "Helper", "Settings", "Config"};

    public ClickGuiScreen() { super(Text.of("ktotovisuals")); }

    @Override
    protected void init() {
        searchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height - 35, 200, 20, Text.of(""));
        this.addSelectableChild(searchField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(30, 20, this.width - 30, this.height - 50, 0xD90A0A0A);

        int catX = 40;
        for (String cat : categories) {
            int color = cat.equals(currentCategory) ? 0xFF8A2BE2 : 0xFF4B0082;
            context.fill(catX, 25, catX + 70, 42, color);
            context.drawText(this.textRenderer, cat, catX + 6, 29, 0xFFFFFFFF, true);
            catX += 75;
        }

        List<Ktotovisuals.Module> visible = searchField.getText().isEmpty() ? 
                Ktotovisuals.getModulesByCategory(currentCategory) : Ktotovisuals.searchModules(searchField.getText());

        int modX = 45, modY = 55;
        for (Ktotovisuals.Module mod : visible) {
            int startColor = mod.enabled ? 0xFF9400D3 : 0xFF4B0082;
            int endColor = mod.enabled ? 0xFF0000FF : 0xFF00008B;
            context.fillGradient(modX, modY, modX + 110, modY + 25, startColor, endColor);
            context.drawText(this.textRenderer, mod.name, modX + 6, modY + 8, 0xFFFFFFFF, true);

            modX += 120;
            if (modX + 110 > this.width - 40) { modX = 45; modY += 32; }
        }
        searchField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        searchField.mouseClicked(mouseX, mouseY, button);
        int catX = 40;
        for (String cat : categories) {
            if (mouseX >= catX && mouseX <= catX + 70 && mouseY >= 25 && mouseY <= 42) {
                currentCategory = cat;
                searchField.setText("");
                return true;
            }
            catX += 75;
        }
        List<Ktotovisuals.Module> visible = searchField.getText().isEmpty() ? 
                Ktotovisuals.getModulesByCategory(currentCategory) : Ktotovisuals.searchModules(searchField.getText());
        int modX = 45, modY = 55;
        for (Ktotovisuals.Module mod : visible) {
            if (mouseX >= modX && mouseX <= modX + 110 && mouseY >= modY && mouseY <= modY + 25) {
                mod.toggle();
                return true;
            }
            modX += 120;
            if (modX + 110 > this.width - 40) { modX = 45; modY += 32; }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchField.keyPressed(keyCode, scanCode, modifiers)) return true;
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT || keyCode == GLFW.GLFW_KEY_ESCAPE) { this.close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPauseGame() { return false; }
}
