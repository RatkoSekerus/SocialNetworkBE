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
import models.User
import models.Friendships
import play.api.libs.json._       // JSON library
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import auth.AuthAction
@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents, userService : UserService,
                               authAction: AuthAction)
  extends BaseController with Logging {

  implicit val usersJson = Json.format[UserFromJSON]
  implicit val loginJson = Json.format[loginJSON]
  implicit val realusersJson = Json.format[User]

  implicit val userDetailsJson = Json.format[UserDetails]

  def users() = authAction.async { implicit request: Request[AnyContent] =>
    userService.listAllUsers flatMap { users: Seq[User] =>
      Future {
        if (users.isEmpty) NoContent
        else Ok(Json.toJson(users))
      }
    }
  }


  def addUser() = Action.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val user: Option[UserFromJSON] = jsonObject.flatMap(Json.fromJson[UserFromJSON](_).asOpt)
    println("---")
    println(user)
    println("---")
    user match {
      case Some(newUser) =>
        val realUser = User(0, user.get.firstName, user.get.lastName, user.get.age,"src",
          user.get.userName, user.get.password)
        userService.addUser(realUser) flatMap { s : String =>
          Future { Ok(s) }
        }
      case None =>
        Future { BadRequest }
    }
  }
  def userDetails(userID: Long) = authAction.async { implicit request: Request[AnyContent] =>
    userService.getUser(userID) flatMap { user: Option[User] =>
      user match {
        case Some(newUser) => val userJson = UserDetails (user.get.firstName, user.get.lastName, user.get.age, user.get.image, user.get.userName)
      Future {
      Ok (Json.toJson (userJson) )
      }

            case None => Future {NoContent
          }
      }
    }

  }

  def login() = Action.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val userLogin: Option[loginJSON] = jsonObject.flatMap(Json.fromJson[loginJSON](_).asOpt)
    userService.login(userLogin.get).map {
      case token: String => Ok(Json.obj("token" -> s"$token"))
      case everythingElse  => Unauthorized
    }
  }
  def userID() = authAction.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val userLogin: Option[loginJSON] = jsonObject.flatMap(Json.fromJson[loginJSON](_).asOpt)
    userService.user(userLogin.get.userName,userLogin.get.password) flatMap { userID : Long =>
      if (userID == 0) {
        Future {
          BadRequest
        }
      }
        else { Future {
          Ok(Json.toJson(userID))
          }
        }

    }
  }


  def friendsOfUser(userID: Long) = authAction.async { implicit request: Request[AnyContent] =>
    userService.listAllFriends(userID) flatMap { users: Seq[User] =>

      Future {
        if (users.isEmpty) NoContent
        else Ok(Json.toJson(users))
      }
    }
  }
  def noFriendsOfUser(userID: Long) = authAction.async { implicit request: Request[AnyContent] =>
    userService.listAllUsers flatMap { allUsers: Seq[User] =>
      userService.listAllFriends(userID) flatMap { friends: Seq[User] =>
        Future {

           Ok(Json.toJson(allUsers.diff(friends)))
        }
      }
    }
  }
  def requestsForUser(userID: Long) = Action.async { implicit request: Request[AnyContent] =>
    userService.listAllRequests(userID) flatMap { users: Seq[User] =>

      Future {
        if (users.isEmpty) NoContent
        else Ok(Json.toJson(users))
      }
    }
  }

  }




