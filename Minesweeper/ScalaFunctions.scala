package minesweeperPackage

class ScalaFunctions {
  val LEFT_BUTTON = 0;
  val RIGHT_BUTTON = 1;
  
  def sort(array: Array[Int]): Array[Int] = {
    if (array.length <= 1) array
    else {
      val pivot = array(array.length / 2)
      Array.concat(
        sort(array filter (pivot >)),
        array filter (pivot ==),
        sort(array filter (pivot <)))
    }
  }
  def countLeftButtonClicked(array: Array[Int]): Int =
  {
    array.filter(_ == LEFT_BUTTON).size
  }

  def countRightButtonClicked(array: Array[Int]): Int =
  {
    array.filter(_ == RIGHT_BUTTON).size
  }
}