package minesweeperPackage
class ScalaFunctions {

  val LEFT_BUTTON = 0;
  val RIGHT_BUTTON = 1;

  def sort(array: Array[Int], low: Int, high: Int) {
    val pivot = array((low + high) / 2)
    var i = low
    var j = high
    while (i <= j) {
      while (array(i) > pivot)
        i += 1
      while (array(j) < pivot)
        j -= 1
      if (i <= j) {
        val temp = array(i)
        array(i) = array(j)
        array(j) = temp
        i += 1
        j -= 1
      }
    }
    if (low < j)
      sort(array, low, j)
    if (j < high)
      sort(array, i, high)
  }
  def getSeqOfClicks(xs: Array[Int], toFind: Int): IndexedSeq[Int] =
    for (i <- 0 until xs.length if xs(i) == toFind) yield xs(i)

  def countLeftButtonClicked(array: Array[Int]): Int =
  {
    getSeqOfClicks(array, LEFT_BUTTON).length
  }
  def countRightButtonClicked(array: Array[Int]): Int =
  {
     getSeqOfClicks(array, RIGHT_BUTTON).length
  }

  def findMaxRepeatedReplaySeq(xs: Array[String]): String = {
    var min = 0
    for (elem <- xs) {
      if (elem.length < xs(min).length) min = xs.indexOf(elem)
    }
    var maxString = ""
    for (i <- 0 until xs(min).length)
      for (j <- i + 1 until xs(min).length) {
        var fl = true
        for (elem <- xs) {
          if (!elem.contains(xs(min).substring(i, j)))
            fl = false
        }
        if (fl && (maxString.length < xs(min).substring(i, j).length))
          maxString = xs(min).substring(i , j)
      }
    maxString
  }
}