package models


import scala.concurrent.{ExecutionContext, Future}

case class User(userID: Long, firstName: String, lastName: String, age: Long, image: String, userName: String, password: String)

case class UserFromJSON(firstName: String, lastName: String, age: Long, userName: String, password: String)

case class UserDetails(firstName: String, lastName: String, age: Long, image: String, userName: String)

case class loginJSON(userName: String, password: String)

