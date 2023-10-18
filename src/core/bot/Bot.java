package core.bot;

import javafx.scene.control.Button;

public abstract class Bot {
  public static final String bot = "O";
  public static final String player = "X";

  public abstract int[] move(Button[][] b, int rl, boolean isBotFirst);
}
