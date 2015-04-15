package fi.kyy.Game;

import com.badlogic.gdx.graphics.Color;

public class GameConstant {

	public static final float WIDTH_CONVERTER = 150f / 800f;
	public static final float HEIGHT_CONVERTER = 90f / 480f;
	public static final float GROUND_WIDTH  = 100f;
	public static final int MAGNET_TIME = 13;
	public static final int MAGNET_SPEED = 2;
	public static final short CATEGORY_BITS = 0x0002;
	public static final short MASK_BITS = 0x0004;
	public static final float WORLD_WIDTH = 100f;
	public static final float MIN_CAMERA_Y = 37f;
	public static final float MAGNET_OCCURANCE = 2000f;
	public static final int MAX_UPGRADE = 3;
	public static final int COIN_VALUE = 1;
	
//	public static final float FONT_COLOR = Color.toFloatBits(70 ,32, 4, 255);
	public static final Color FONT_COLOR= new Color(70/255f , 32/255f , 4/255f , 1f);
	public static final Color FONT_BLACK = Color.BLACK;
	public static final Color FONT_ALTERNATIVE = Color.BLACK;


}
