package services
import com.google.inject.Inject
import models.Post
import repository.Posts

import scala.concurrent.Future

class PostService @Inject() (posts: Posts) {

  def addPost(post: Post): Future[String] = {
    posts.add(post)
  }

  def deletePost(postID: Long,userID: Long): Future[Int] = {
    posts.delete(postID,userID)
  }

  def getPost(id: Long): Future[Option[Post]] = {
    posts.get(id)
  }

  def listAllPosts: Future[Seq[Post]] = {
    posts.listAll
  }
}