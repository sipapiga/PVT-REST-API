
// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Tobias/Documents/Git/PVT-REST-API/conf/routes
// @DATE:Mon Apr 24 18:05:42 CEST 2017

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:8
package controllers {

  // @LINE:21
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:21
    def versioned(file:Asset): Call = {
      implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:18
  class ReverseActivityGatheringController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:18
    def gather(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "gather")
    }
  
  }

  // @LINE:8
  class ReverseSecurityController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def logout(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "logout")
    }
  
    // @LINE:8
    def login(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "login")
    }
  
  }

  // @LINE:13
  class ReverseSwipingSessionsController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def getSwipingSessionWithExactParticipants(emailAddresses:String): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "swipingsessions" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("emailAddresses", emailAddresses)))))
    }
  
    // @LINE:16
    def chooseActivities(swipingSessionId:Long, email:String, activities:String): Call = {
      import ReverseRouteContext.empty
      Call("PUT", _prefix + { _defaultPrefix } + "swipingsessions" + queryString(List(Some(implicitly[QueryStringBindable[Long]].unbind("swipingSessionId", swipingSessionId)), Some(implicitly[QueryStringBindable[String]].unbind("email", email)), Some(implicitly[QueryStringBindable[String]].unbind("activities", activities)))))
    }
  
    // @LINE:15
    def createSwipingSession(emailAddresses:String): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "swipingsessions" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("emailAddresses", emailAddresses)))))
    }
  
    // @LINE:14
    def getSwipingSessionWithParticipant(emailAddress:String): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "swipingsessions/" + implicitly[PathBindable[String]].unbind("emailAddress", dynamicString(emailAddress)))
    }
  
  }

  // @LINE:11
  class ReverseFacebookSecurityController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def login(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "facebook/login")
    }
  
  }


}
