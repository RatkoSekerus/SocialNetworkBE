package repository
import slick.jdbc.MySQLProfile.api._
import models.Friendships

class FriendshipTableDef(tag: Tag) extends Table[Friendships](tag, "friendships") {
  def userSenderID = column[Long]("userSenderID")
  def userReceiverID = column[Long]("userReceiverID")
  def status = column[String]("status",O.Default("pending"))
  def pk = primaryKey("pk", (userSenderID, userReceiverID))
  override def * =
    (userSenderID, userReceiverID, status) <>(Friendships.tupled, Friendships.unapply)

  // compiles to SQL:
  //   alter table "a" add constraint "pk_a" primary key("k1","k2")
}