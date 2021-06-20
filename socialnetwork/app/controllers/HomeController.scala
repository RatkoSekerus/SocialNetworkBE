package controllers

import services._
import models._
import javax.inject._
import play.api._
import play.api.mvc._
import services._
import scala.concurrent._
import play.api.Logging
import scala.collection.mutable
import play.api.libs.json._       // JSON library
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import auth.AuthAction
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, userService : UserService
                              ,authAction: AuthAction)
                               extends BaseController with Logging {


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */



  implicit val realusersJson = Json.format[User]


  def index() = Action.async { implicit request: Request[AnyContent] =>
    userService.listAllUsers flatMap { users: Seq[User] =>
      Future {
        if (users.isEmpty) NoContent
        else Ok
      }
    }
  }

}



