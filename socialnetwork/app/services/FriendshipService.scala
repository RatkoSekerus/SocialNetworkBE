package services
import com.google.inject.Inject
import models.Friendships
import models.User
import repository.FriendshipsQ
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class FriendshipService @Inject() (friendships: FriendshipsQ) {
  def addFriendship(friendship: Friendships): Future[String] = {
    friendships.check(friendship.userSenderID,friendship.userReceiverID) flatMap { check: Option[Friendships] =>
      check match {
        case Some(f) => Future {"allready sent request"}
        case None => friendships.add(friendship)
      }
    }
    friendships.add(friendship)
  }
  def acceptFriendship(friendship: Friendships): Future[String] = {
    friendships.delete(friendship)
    friendships.add(friendship)
  }
  def rejectFriendship(friendship: Friendships): Future[Int] = {
    friendships.delete(friendship)
  }
  def listAllFriendships: Future[Seq[Friendships]] = {
    friendships.listAll
  }
  def listFriendships(userID:Long): Future[Seq[Friendships]] = {
    friendships.listFriendships(userID)
  }

  def listRequests(userID:Long): Future[Seq[Friendships]] = {
    friendships.listRequests(userID)
  }
  def listNoFriendships(userID:Long): Future[Seq[Friendships]] = {
    friendships.listNoFriendships(userID)
  }
}