
// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Tobias/Documents/Git/PVT-REST-API/conf/routes
// @DATE:Mon Apr 24 18:05:42 CEST 2017

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:8
  SecurityController_3: controllers.SecurityController,
  // @LINE:11
  FacebookSecurityController_1: controllers.FacebookSecurityController,
  // @LINE:13
  SwipingSessionsController_0: controllers.SwipingSessionsController,
  // @LINE:18
  ActivityGatheringController_2: controllers.ActivityGatheringController,
  // @LINE:21
  Assets_4: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:8
    SecurityController_3: controllers.SecurityController,
    // @LINE:11
    FacebookSecurityController_1: controllers.FacebookSecurityController,
    // @LINE:13
    SwipingSessionsController_0: controllers.SwipingSessionsController,
    // @LINE:18
    ActivityGatheringController_2: controllers.ActivityGatheringController,
    // @LINE:21
    Assets_4: controllers.Assets
  ) = this(errorHandler, SecurityController_3, FacebookSecurityController_1, SwipingSessionsController_0, ActivityGatheringController_2, Assets_4, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, SecurityController_3, FacebookSecurityController_1, SwipingSessionsController_0, ActivityGatheringController_2, Assets_4, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """login""", """controllers.SecurityController.login"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """logout""", """controllers.SecurityController.logout"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """facebook/login""", """controllers.FacebookSecurityController.login"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """swipingsessions""", """controllers.SwipingSessionsController.getSwipingSessionWithExactParticipants(emailAddresses:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """swipingsessions/""" + "$" + """emailAddress<[^/]+>""", """controllers.SwipingSessionsController.getSwipingSessionWithParticipant(emailAddress:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """swipingsessions""", """controllers.SwipingSessionsController.createSwipingSession(emailAddresses:String)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """swipingsessions""", """controllers.SwipingSessionsController.chooseActivities(swipingSessionId:Long, email:String, activities:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """gather""", """controllers.ActivityGatheringController.gather()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:8
  private[this] lazy val controllers_SecurityController_login0_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("login")))
  )
  private[this] lazy val controllers_SecurityController_login0_invoker = createInvoker(
    SecurityController_3.login,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SecurityController",
      "login",
      Nil,
      "POST",
      """ Interesting tidbit which indicate POST might be better than GET for passing sensitive information:
 https://security.stackexchange.com/questions/12531/ssl-with-get-and-post
 Should be the second answer.""",
      this.prefix + """login"""
    )
  )

  // @LINE:9
  private[this] lazy val controllers_SecurityController_logout1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("logout")))
  )
  private[this] lazy val controllers_SecurityController_logout1_invoker = createInvoker(
    SecurityController_3.logout,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SecurityController",
      "logout",
      Nil,
      "POST",
      """""",
      this.prefix + """logout"""
    )
  )

  // @LINE:11
  private[this] lazy val controllers_FacebookSecurityController_login2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("facebook/login")))
  )
  private[this] lazy val controllers_FacebookSecurityController_login2_invoker = createInvoker(
    FacebookSecurityController_1.login,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.FacebookSecurityController",
      "login",
      Nil,
      "POST",
      """""",
      this.prefix + """facebook/login"""
    )
  )

  // @LINE:13
  private[this] lazy val controllers_SwipingSessionsController_getSwipingSessionWithExactParticipants3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("swipingsessions")))
  )
  private[this] lazy val controllers_SwipingSessionsController_getSwipingSessionWithExactParticipants3_invoker = createInvoker(
    SwipingSessionsController_0.getSwipingSessionWithExactParticipants(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SwipingSessionsController",
      "getSwipingSessionWithExactParticipants",
      Seq(classOf[String]),
      "GET",
      """""",
      this.prefix + """swipingsessions"""
    )
  )

  // @LINE:14
  private[this] lazy val controllers_SwipingSessionsController_getSwipingSessionWithParticipant4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("swipingsessions/"), DynamicPart("emailAddress", """[^/]+""",true)))
  )
  private[this] lazy val controllers_SwipingSessionsController_getSwipingSessionWithParticipant4_invoker = createInvoker(
    SwipingSessionsController_0.getSwipingSessionWithParticipant(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SwipingSessionsController",
      "getSwipingSessionWithParticipant",
      Seq(classOf[String]),
      "GET",
      """""",
      this.prefix + """swipingsessions/""" + "$" + """emailAddress<[^/]+>"""
    )
  )

  // @LINE:15
  private[this] lazy val controllers_SwipingSessionsController_createSwipingSession5_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("swipingsessions")))
  )
  private[this] lazy val controllers_SwipingSessionsController_createSwipingSession5_invoker = createInvoker(
    SwipingSessionsController_0.createSwipingSession(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SwipingSessionsController",
      "createSwipingSession",
      Seq(classOf[String]),
      "POST",
      """""",
      this.prefix + """swipingsessions"""
    )
  )

  // @LINE:16
  private[this] lazy val controllers_SwipingSessionsController_chooseActivities6_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("swipingsessions")))
  )
  private[this] lazy val controllers_SwipingSessionsController_chooseActivities6_invoker = createInvoker(
    SwipingSessionsController_0.chooseActivities(fakeValue[Long], fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SwipingSessionsController",
      "chooseActivities",
      Seq(classOf[Long], classOf[String], classOf[String]),
      "PUT",
      """""",
      this.prefix + """swipingsessions"""
    )
  )

  // @LINE:18
  private[this] lazy val controllers_ActivityGatheringController_gather7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("gather")))
  )
  private[this] lazy val controllers_ActivityGatheringController_gather7_invoker = createInvoker(
    ActivityGatheringController_2.gather(),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ActivityGatheringController",
      "gather",
      Nil,
      "GET",
      """""",
      this.prefix + """gather"""
    )
  )

  // @LINE:21
  private[this] lazy val controllers_Assets_versioned8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned8_invoker = createInvoker(
    Assets_4.versioned(fakeValue[String], fakeValue[Asset]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      """ Map static resources from the /public folder to the /assets URL path""",
      this.prefix + """assets/""" + "$" + """file<.+>"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:8
    case controllers_SecurityController_login0_route(params) =>
      call { 
        controllers_SecurityController_login0_invoker.call(SecurityController_3.login)
      }
  
    // @LINE:9
    case controllers_SecurityController_logout1_route(params) =>
      call { 
        controllers_SecurityController_logout1_invoker.call(SecurityController_3.logout)
      }
  
    // @LINE:11
    case controllers_FacebookSecurityController_login2_route(params) =>
      call { 
        controllers_FacebookSecurityController_login2_invoker.call(FacebookSecurityController_1.login)
      }
  
    // @LINE:13
    case controllers_SwipingSessionsController_getSwipingSessionWithExactParticipants3_route(params) =>
      call(params.fromQuery[String]("emailAddresses", None)) { (emailAddresses) =>
        controllers_SwipingSessionsController_getSwipingSessionWithExactParticipants3_invoker.call(SwipingSessionsController_0.getSwipingSessionWithExactParticipants(emailAddresses))
      }
  
    // @LINE:14
    case controllers_SwipingSessionsController_getSwipingSessionWithParticipant4_route(params) =>
      call(params.fromPath[String]("emailAddress", None)) { (emailAddress) =>
        controllers_SwipingSessionsController_getSwipingSessionWithParticipant4_invoker.call(SwipingSessionsController_0.getSwipingSessionWithParticipant(emailAddress))
      }
  
    // @LINE:15
    case controllers_SwipingSessionsController_createSwipingSession5_route(params) =>
      call(params.fromQuery[String]("emailAddresses", None)) { (emailAddresses) =>
        controllers_SwipingSessionsController_createSwipingSession5_invoker.call(SwipingSessionsController_0.createSwipingSession(emailAddresses))
      }
  
    // @LINE:16
    case controllers_SwipingSessionsController_chooseActivities6_route(params) =>
      call(params.fromQuery[Long]("swipingSessionId", None), params.fromQuery[String]("email", None), params.fromQuery[String]("activities", None)) { (swipingSessionId, email, activities) =>
        controllers_SwipingSessionsController_chooseActivities6_invoker.call(SwipingSessionsController_0.chooseActivities(swipingSessionId, email, activities))
      }
  
    // @LINE:18
    case controllers_ActivityGatheringController_gather7_route(params) =>
      call { 
        controllers_ActivityGatheringController_gather7_invoker.call(ActivityGatheringController_2.gather())
      }
  
    // @LINE:21
    case controllers_Assets_versioned8_route(params) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned8_invoker.call(Assets_4.versioned(path, file))
      }
  }
}
