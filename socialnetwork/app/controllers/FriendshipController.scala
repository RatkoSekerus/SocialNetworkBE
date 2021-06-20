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

@Singleton
class FriendshipController @Inject()(val controllerComponents: ControllerComponents,
                                     friendshipService : FriendshipService,
                                     authAction: AuthAction)
  extends BaseController with Logging {

  implicit val friendRequestJson = Json.format[FriendshipFromJSON]
  implicit val friendshipJson = Json.format[Friendships]
  implicit val userJson = Json.format[User]

  def sendFriendRequest() = Action.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val friendshipRequest: Option[FriendshipFromJSON] = jsonObject.flatMap(Json.fromJson[FriendshipFromJSON](_).asOpt)

    friendshipRequest match {
      case Some(newRequest) =>
        val realRequest = Friendships(friendshipRequest.get.userSenderID, friendshipRequest.get.userReceiverID, "pending")

        friendshipService.addFriendship(realRequest) flatMap { ex: String =>
          Future {
            Ok
          }
        }
      case None =>

        Future {
          BadRequest
        }
    }
  }

  def acceptFriendRequest() = authAction.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val friendshipRequest: Option[FriendshipFromJSON] = jsonObject.flatMap(Json.fromJson[FriendshipFromJSON](_).asOpt)

    friendshipRequest match {
      case Some(newRequest) =>
        val realRequest = Friendships(friendshipRequest.get.userSenderID, friendshipRequest.get.userReceiverID, "accepted")

        friendshipService.acceptFriendship(realRequest) flatMap { ex: String =>
          Future {
            Ok
          }
        }
      case None =>
        Future {
          BadRequest
        }
    }
  }

  def rejectFriendRequest() = authAction.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val friendshipRequest: Option[FriendshipFromJSON] = jsonObject.flatMap(Json.fromJson[FriendshipFromJSON](_).asOpt)

    friendshipRequest match {
      case Some(newRequest) =>
        val realRequest = Friendships(friendshipRequest.get.userSenderID, friendshipRequest.get.userReceiverID, "")

        friendshipService.rejectFriendship(realRequest) flatMap { br: Int =>
          Future {
            Ok
          }
        }
      case None =>
        Future {
          BadRequest
        }
    }
  }

  def friendships() = authAction.async { implicit request: Request[AnyContent] =>
    friendshipService.listAllFriendships flatMap { friendships: Seq[Friendships] =>
      Future {
        if (friendships.isEmpty) NoContent
        else Ok(Json.toJson(friendships))
      }
    }
  }

  def friendshipsOfUser(userID: Long) = authAction.async { implicit request: Request[AnyContent] =>
    friendshipService.listFriendships(userID) flatMap { friendships: Seq[Friendships] =>
      Future {
        if (friendships.isEmpty) NoContent
        else Ok(Json.toJson(friendships))
      }
    }
  }

  def noFriendships(userID: Long) = authAction.async { implicit request: Request[AnyContent] =>
    friendshipService.listNoFriendships(userID) flatMap { friendships: Seq[Friendships] =>
      Future {
        if (friendships.isEmpty) NoContent
        else Ok(Json.toJson(friendships))
      }
    }
  }


}
