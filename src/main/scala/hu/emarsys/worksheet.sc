import hu.emarsys.Util.calculateDueDate
import org.joda.time._
import org.joda.time.DateTimeConstants.{SATURDAY, SUNDAY}

val submitDate = DateTime.parse("2015-10-30T16:45:00.000+02:00")
submitDate.plusHours(1).withTime(17,0,0,0)
val submitDay = submitDate.getDayOfWeek
val turnaroundTime = 71

val workHourStart = 9
require(workHourStart >= 0 && workHourStart < 24)
val workHourEnd = 17
require(workHourEnd >= 0 && workHourEnd < 24 && workHourEnd > workHourStart)
val workHoursPerDay = workHourEnd - workHourStart
val completeWorkingDays = turnaroundTime / workHoursPerDay
val remainingWorkHours = turnaroundTime % workHoursPerDay


