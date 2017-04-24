
// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Tobias/Documents/Git/PVT-REST-API/conf/routes
// @DATE:Mon Apr 24 18:05:42 CEST 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
