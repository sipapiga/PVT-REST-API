
// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Tobias/Documents/Git/PVT-REST-API/conf/routes
// @DATE:Mon Apr 24 18:05:42 CEST 2017

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:8
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:21
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:21
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }

  // @LINE:18
  class ReverseActivityGatheringController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:18
    def gather: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ActivityGatheringController.gather",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "gather"})
        }
      """
    )
  
  }

  // @LINE:8
  class ReverseSecurityController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def logout: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SecurityController.logout",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "logout"})
        }
      """
    )
  
    // @LINE:8
    def login: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SecurityController.login",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "login"})
        }
      """
    )
  
  }

  // @LINE:13
  class ReverseSwipingSessionsController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def getSwipingSessionWithExactParticipants: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SwipingSessionsController.getSwipingSessionWithExactParticipants",
      """
        function(emailAddresses0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "swipingsessions" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("emailAddresses", emailAddresses0)])})
        }
      """
    )
  
    // @LINE:16
    def chooseActivities: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SwipingSessionsController.chooseActivities",
      """
        function(swipingSessionId0,email1,activities2) {
          return _wA({method:"PUT", url:"""" + _prefix + { _defaultPrefix } + """" + "swipingsessions" + _qS([(""" + implicitly[QueryStringBindable[Long]].javascriptUnbind + """)("swipingSessionId", swipingSessionId0), (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("email", email1), (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("activities", activities2)])})
        }
      """
    )
  
    // @LINE:15
    def createSwipingSession: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SwipingSessionsController.createSwipingSession",
      """
        function(emailAddresses0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "swipingsessions" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("emailAddresses", emailAddresses0)])})
        }
      """
    )
  
    // @LINE:14
    def getSwipingSessionWithParticipant: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SwipingSessionsController.getSwipingSessionWithParticipant",
      """
        function(emailAddress0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "swipingsessions/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("emailAddress", encodeURIComponent(emailAddress0))})
        }
      """
    )
  
  }

  // @LINE:11
  class ReverseFacebookSecurityController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def login: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.FacebookSecurityController.login",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "facebook/login"})
        }
      """
    )
  
  }


}
