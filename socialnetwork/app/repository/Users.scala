package repository

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import repository.UserTableDef
import slick.jdbc.MySQLProfile.api._
import play.api.libs.json._
import models.User
import models.loginJSON
import models.Friendships
import scala.concurrent.{ExecutionContext, Future}
import services.FriendshipService
import scala.collection.mutable.ListBuffer

class Users @Inject()  (protected val dbConfigProvider: DatabaseConfigProvider)
                      (implicit executionContext: ExecutionContext,friendshipService:FriendshipService)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  // the HasDatabaseConfigProvider trait gives access to the
  // dbConfig object that we need to run the slick queries

  val users = TableQuery[UserTableDef]

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.userID === id).delete)
  }
  def user(userName: String,password : String): Future[Option[User]] = {
    dbConfig.db.run(users.filter(user=> {user.userName === userName && user.password === password}).result.headOption)
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.userID === id).result.headOption)
  }

  def check(userName: String, password: String): Future[Option[User]] = {
   dbConfig.db.run(users.filter
    (user=>(user.userName===userName && user.password === password)).result.headOption)

  }

  def listAll: Future[Seq[User]] = {

    dbConfig.db.run(users.result)
  }


  def listAllFriends(userID:Long): Future[Seq[User]] = {
    listAllFriendsId(userID) flatMap { ids : ListBuffer[Long] =>
      listAll flatMap { users: Seq[User] =>
        Future {users.filter(user => ids.contains(user.userID))}
      }
    }
  }



  def listAllFriendsId(userID:Long): Future[ListBuffer[Long]] = {
    var userIDs = new ListBuffer[Long]()
    friendshipService.listFriendships(userID) flatMap { friendships: Seq[Friendships] =>
      for (friendship <- friendships) {

        if (friendship.userSenderID == userID) {
          userIDs+=friendship.userReceiverID
        }
        else if (friendship.userReceiverID == userID) {
          userIDs+=friendship.userSenderID
        }
      }
      Future {userIDs}
    }
  }

  def listRequests(userID:Long): Future[Seq[User]] = {
    listAllRequestsIds(userID) flatMap { ids : ListBuffer[Long] =>
      listAll flatMap { users: Seq[User] =>
        Future {users.filter(user => ids.contains(user.userID))}
      }
    }
  }
  def listAllRequestsIds(userID:Long): Future[ListBuffer[Long]] = {
    var userIDs = new ListBuffer[Long]()
    friendshipService.listRequests(userID) flatMap { friendships: Seq[Friendships] =>
      for (friendship <- friendships) {

        if (friendship.userSenderID == userID) {
          userIDs+=friendship.userReceiverID
        }
        else if (friendship.userReceiverID == userID) {
          userIDs+=friendship.userSenderID
        }
      }
      Future {userIDs}
    }
  }
}