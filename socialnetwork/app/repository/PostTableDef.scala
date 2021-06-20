package repository
import slick.jdbc.MySQLProfile.api._
import models.Post
class PostTableDef(tag: Tag) extends Table[Post](tag, "post") {
  def postID = column[Long]("postID", O.PrimaryKey,O.AutoInc)
  def text = column[String]("text")
  def userID = column[Long]("userID")
  override def * =
    (postID, text, userID) <>(Post.tupled, Post.unapply)
}