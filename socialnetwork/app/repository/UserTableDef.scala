package repository
import slick.jdbc.MySQLProfile.api._
import models.User
class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

  def userID = column[Long]("userID", O.PrimaryKey,O.AutoInc)
  def firstName = column[String]("firstName")
  def lastName = column[String]("lastName")
  def age = column[Long]("age")
  def image = column[String]("image",O.Default("src"))
  def userName = column[String]("userName")
  def password = column[String]("password")
  override def * =
    (userID, firstName, lastName, age, image, userName, password) <>(User.tupled, User.unapply)
}