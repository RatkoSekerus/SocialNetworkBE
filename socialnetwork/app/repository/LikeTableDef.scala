package repository
import slick.jdbc.MySQLProfile.api._
import models.Likes

class LikeTableDef(tag: Tag) extends Table[Likes](tag, "likes") {
  def likeID = column[Long]("likeID", O.PrimaryKey,O.AutoInc)
  def userID = column[Long]("userID")
  def postID = column[Long]("postID")
  override def * =
    (likeID, userID, postID) <>(Likes.tupled, Likes.unapply)
}