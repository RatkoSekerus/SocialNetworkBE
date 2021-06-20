package repository

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import repository.PostTableDef
import slick.jdbc.MySQLProfile.api._
import play.api.libs.json._
import slick.jdbc.MySQLProfile.ColumnOptions
import repository.LikeTableDef
import models.Friendships
import models.User
import scala.concurrent.{ExecutionContext, Future}



class FriendshipsQ @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  // the HasDatabaseConfigProvider trait gives access to the
  // dbConfig object that we need to run the slick queries

  val friendships = TableQuery[FriendshipTableDef]

  def add(friendship: Friendships): Future[String] = {
    dbConfig.db.run(friendships += friendship).map(res => "Like successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
  def check(userSenderID: Long, userReceiverID: Long): Future[Option[Friendships]] = {
    dbConfig.db.run(friendships.filter
    (friendships=>
      (friendships.userSenderID===userSenderID && friendships.userReceiverID === userReceiverID))
      .result.headOption)

  }

  def delete(friendship: Friendships): Future[Int] = {
    dbConfig.db.run(friendships.filter(friendshipDB => (friendshipDB.userSenderID === friendship.userSenderID
      && friendshipDB.userReceiverID === friendship.userReceiverID)).delete)
  }

  def listAll: Future[Seq[Friendships]] = {
    dbConfig.db.run(friendships.result)
  }

  def listFriendships(userID:Long): Future[Seq[Friendships]] = {
    dbConfig.db.run(friendships.filter(friendshipDB => ((friendshipDB.userSenderID === userID
      || friendshipDB.userReceiverID === userID) && (friendshipDB.status==="accepted"))).result)
  }

  def listRequests(userID:Long): Future[Seq[Friendships]] = {
    dbConfig.db.run(friendships.filter(friendshipDB => ((friendshipDB.userSenderID === userID
      || friendshipDB.userReceiverID === userID) && (friendshipDB.status==="pending"))).result)
  }

  def listNoFriendships(userID:Long): Future[Seq[Friendships]] = {
    dbConfig.db.run(friendships.filter(friendshipDB => (!(friendshipDB.userSenderID === userID
      || friendshipDB.userReceiverID === userID))).result)
  }



}