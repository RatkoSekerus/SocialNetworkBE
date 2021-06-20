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
class PostController @Inject()(val controllerComponents: ControllerComponents, userService : UserService
                               ,postService : PostService, likeService : LikeService,authAction : AuthAction)
  extends BaseController with Logging {

  implicit val postsJson = Json.format[PostFromJSON]
  implicit val realpostsJson = Json.format[Post]
  implicit val deletePostJson = Json.format[deletePostJSON]


  def posts() = authAction.async { implicit request: Request[AnyContent] =>
    postService.listAllPosts flatMap { posts: Seq[Post] =>
      Future {
        if (posts.isEmpty) NoContent
        else Ok(Json.toJson(posts))
      }
    }
  }

  def addPost() = authAction.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val post: Option[PostFromJSON] = jsonObject.flatMap(Json.fromJson[PostFromJSON](_).asOpt)

    post match {
      case Some(newPost) =>
        val realPost = Post(0, post.get.text, post.get.userID)
        postService.addPost(realPost) flatMap { ex : String =>
          Future { Ok }
        }
      case None =>
        Future { BadRequest }
    }
  }

  def deletePost() = authAction.async { implicit request: Request[AnyContent] =>
    val content = request.body
    val jsonObject = content.asJson

    val deletePost: Option[deletePostJSON] = jsonObject.flatMap(Json.fromJson[deletePostJSON](_).asOpt)

    deletePost match {
      case Some(newDeletePost) =>

        val realDeletePost = deletePostJSON(deletePost.get.postID, deletePost.get.userID)

        postService.deletePost(realDeletePost.postID,realDeletePost.userID) flatMap { int : Int =>
          Future { Ok }
        }
      case None =>
        Future { BadRequest }
    }
  }

  }