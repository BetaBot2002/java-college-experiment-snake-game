import java.awt.Color;

public class ColorPicker {
    private static final Color[] COLORS={Color.BLUE,Color.CYAN,Color.YELLOW,Color.ORANGE,Color.PINK,Color.WHITE,Color.GRAY,Color.BLACK};
    private static int nextColor=0;

    public static Color getNextColor(){
        Color color=COLORS[nextColor];
        nextColor=(nextColor+1)%COLORS.length;
        return color;
    }
}
