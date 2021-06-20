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
class LikeController @Inject()(val controllerComponents: ControllerComponents, userService : UserService
                               ,postService : PostService, likeService : LikeService,
                               authAction: AuthAction)
  extends BaseController with Logging {
  implicit val likesJson = Json.format[LikeFromJSON]
  implicit val realLikesJson = Json.format[Likes]

  implicit val DislikesJson = Json.format[DislikeFromJSON]

  def likePost() = authAction.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val like: Option[LikeFromJSON] = jsonObject.flatMap(Json.fromJson[LikeFromJSON](_).asOpt)

    like match {
      case Some(newLike) =>
        println("---------------------------------------")
        val realLike = Likes(0, like.get.userID, like.get.postID)
        println(realLike)
        likeService.addLike(realLike) flatMap { ex : String =>
          Future { Ok }
        }
      case None =>
        Future { BadRequest }
    }
  }

  def dislikePost() = authAction.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val dislike: Option[DislikeFromJSON] = jsonObject.flatMap(Json.fromJson[DislikeFromJSON](_).asOpt)

    dislike match {
      case Some(newDislike) =>

        val realDislike = DislikeFromJSON(dislike.get.userID, dislike.get.postID)

        likeService.deleteLike(realDislike.userID,realDislike.postID) flatMap { int : Int =>
          Future { Ok }
        }
      case None =>
        Future { BadRequest }
    }
  }
  def likes(postID: Long) = authAction.async { implicit request: Request[AnyContent] =>
    likeService.listLikes(postID) flatMap { likes: Seq[Likes] =>
      Future {
        if (likes.isEmpty) NoContent
        else Ok(Json.toJson(likes))
      }
    }
  }
  }