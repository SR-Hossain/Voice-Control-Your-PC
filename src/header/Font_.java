package src.header;

import java.io.File;
import java.awt.Font;

public class Font_ extends Font{
    public Font shuher;
    public Font_() throws Exception{
        super("font", Font.PLAIN, 20);
        shuher = create("src/resources/Shuher.otf").deriveFont(Font.PLAIN, 50);
    }
    public Font create(String file) throws Exception {
        return Font.createFont(Font.TRUETYPE_FONT, new File(file));
    }
}