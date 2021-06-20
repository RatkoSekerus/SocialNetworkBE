package services
import com.google.inject.Inject
import models.User
import models.loginJSON
import repository.Users
import repository.FriendshipsQ
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import auth.AuthService
import java.time.Instant
import java.util.{Date, UUID}

import io.jsonwebtoken.{Claims, Jws, Jwts, SignatureAlgorithm}

import scala.collection.JavaConverters._


class UserService @Inject() (users: Users, friendships: FriendshipsQ,authService: AuthService) {

  def addUser(user: User): Future[String] = {
    users.check(user.userName,user.password) flatMap { check: Option[User] =>
      check match {
        case Some(f) => Future {"taken userName and password"}
        case None => users.add(user)
      }
    }
  }
  def login(userJson: loginJSON): Future[Any] = {
    users.check(userJson.userName,userJson.password) flatMap { user : Option[User] =>
      try {
        user.get.userName
        Future {authService.encode(user.get)}
      } catch {
        case _: Throwable => Future {
          1
        }
      }
    }

  }

  def deleteUser(id: Long): Future[Int] = {
    users.delete(id)
  }
  def user(userName: String,password: String): Future[Long] = {
    users.user(userName,password) flatMap { u: Option[User] =>
      try {
        u.get
        Future {u.get.userID}
      } catch {
        case _: Throwable => Future {
          0
        }
      }
    }
  }

  def getUser(id: Long): Future[Option[User]] = {
    users.get(id)
  }

  def listAllUsers: Future[Seq[User]] = {
    users.listAll
  }
  def listAllFriends(userID:Long): Future[Seq[User]] = {
    users.listAllFriends(userID)
  }

  def listAllRequests(userID:Long): Future[Seq[User]] = {
    users.listRequests(userID)
  }
}