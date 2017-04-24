
// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Tobias/Documents/Git/PVT-REST-API/conf/routes
// @DATE:Mon Apr 24 18:05:42 CEST 2017

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseActivityGatheringController ActivityGatheringController = new controllers.ReverseActivityGatheringController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseSecurityController SecurityController = new controllers.ReverseSecurityController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseSwipingSessionsController SwipingSessionsController = new controllers.ReverseSwipingSessionsController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseFacebookSecurityController FacebookSecurityController = new controllers.ReverseFacebookSecurityController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseActivityGatheringController ActivityGatheringController = new controllers.javascript.ReverseActivityGatheringController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseSecurityController SecurityController = new controllers.javascript.ReverseSecurityController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseSwipingSessionsController SwipingSessionsController = new controllers.javascript.ReverseSwipingSessionsController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseFacebookSecurityController FacebookSecurityController = new controllers.javascript.ReverseFacebookSecurityController(RoutesPrefix.byNamePrefix());
  }

}
