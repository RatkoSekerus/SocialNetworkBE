package services
import com.google.inject.Inject
import models.Likes
import repository.LikesQ
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LikeService @Inject() (likes: LikesQ) {

  def addLike(like: Likes): Future[String] = {
    likes.check(like.userID,like.postID) flatMap { check: Option[Likes] =>
      check match {
        case Some(f) => Future {"allready liked"}
        case None => likes.add(like)
      }
    }
  }

  def deleteLike(userID: Long, postID: Long): Future[Int] = {
    likes.delete(userID,postID)
  }

  def getLike(id: Long): Future[Option[Likes]] = {
    likes.get(id)
  }

  def listAllLikes: Future[Seq[Likes]] = {
    likes.listAll
  }
  def listLikes(postID:Long): Future[Seq[Likes]] = {
    likes.listLikes(postID)
  }
}