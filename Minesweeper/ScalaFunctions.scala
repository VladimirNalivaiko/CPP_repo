package minesweeperPackage

class ScalaFunctions {
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
}