import hu.emarsys.utils.DueDateCalc.calculateDueDate
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

// 2015.	március	29.	2 h	-	október	25.	3 h
val zone = DateTimeZone.forID("Europe/Budapest")
DateTimeZone.setDefault(zone)
val cetEnd = DateTime.parse("2015-03-29T01:59:00.000")
val cestStart = cetEnd.plusMinutes(1)
val cestEnd = DateTime.parse("2015-10-25T02:59:00.000")
val cetStart = cestEnd.plusMinutes(1)
DateTimeZone.getDefault()
//DateTimeZone.getDefault()
//DateTime.parse("2015-03-29T02:59:00.000")
//DateTime.parse("not a date")