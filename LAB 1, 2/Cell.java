public class Cell {
  private int x;
  private int y;
  private int numOfNeighborBombs;

  static boolean isAnyClicked;
  static boolean isAnyBanged;

  boolean toRepaint;
  boolean isSooposedToBeBomb;
  boolean isOpen;
  boolean isBomb;

  Cell() {}

  Cell(int X, int Y) {
    x = X;
    y = Y;
    toRepaint = true;
    isAnyClicked = false;
    isAnyBanged = false;
    isSooposedToBeBomb = false;
    isBomb = false;
    isOpen = false;
    numOfNeighborBombs = 0;
  }

  public void StartNewGame() {
    isAnyClicked = false;
    isAnyBanged = false;
    isSooposedToBeBomb = false;
    isBomb = false;
    isOpen = false;
    toRepaint = true;
    numOfNeighborBombs = 0;
  }

  public boolean getToRepaint() {
    return toRepaint;
  }

  public void setToRepaint(boolean b) {
    toRepaint = b;
  }

  public boolean getIsBomb() {
    return isBomb;
  }

  public void setIsBomb(boolean b) {
    isBomb = b;
  }

  public boolean getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(boolean isOpen) {
    this.isOpen = isOpen;
  }

  public boolean getIsAnyClicked() {
    return isAnyClicked;
  }

  public void setIsAnyClicked(boolean isAnyClicked) {
    this.isAnyClicked = isAnyClicked;
  }

  public boolean getIsAnyBanged() {
    return isAnyBanged;
  }

  public void setIsAnyBanged(boolean isAnyBanged) {
    this.isAnyBanged = isAnyBanged;
  }

  public void setIsSooposedToBeBomb(boolean b) {
    isSooposedToBeBomb = b;
  }

  public boolean getIsSooposedToBeBomb() {
    return isSooposedToBeBomb;
  }

  public int getNumOfNeighborBombs() {
    return numOfNeighborBombs;
  }

  public void setNumOfNeighborBombs(int numOfNeighborBombs) {
    this.numOfNeighborBombs = numOfNeighborBombs;
  }

  public int getVariantOfImage() {
    int variantOfImage = 0;
    if (!isOpen) {
      variantOfImage = 0;
    } else if (isBomb && isOpen) {
      variantOfImage = 9;
    } else if (isOpen && !isBomb) {
      if (numOfNeighborBombs != 0) {
        variantOfImage = numOfNeighborBombs;
      } else {
        variantOfImage = 12;
      }
    }
    if (isBomb && isAnyBanged && !isOpen) {
      variantOfImage = 10;
    }
    if (isSooposedToBeBomb && !isAnyBanged) {
      variantOfImage = 11;
    }
    if (isOpen && numOfNeighborBombs == 0 && !isBomb)
      variantOfImage = 12;
    return variantOfImage;
  }
}
