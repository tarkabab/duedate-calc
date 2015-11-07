package hu.emarsys.utils

import org.scalatest.FunSpec

import hu.emarsys.utils.DueDateCalc.{calculateDueDate, nextWorkingDay, addWorkingDays, addWorkingHours, parseDate, formatDate}
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.DateTimeConstants._

class DueDateCalcSpec extends FunSpec {

  describe("The CalculateDueDate method") {
    it("should take the submit date and turnaround time as an input") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      val turnaroundTime = 16
      calculateDueDate(submitDate, turnaroundTime)
    }
    it("should return the date and time when the issue is to be resolved") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      assert(WEDNESDAY === DateTime.parse(submitDate).getDayOfWeek)
      val turnaroundTime = 16
      val dueDate = "2015-10-30T16:30:00.000+02:00"
      assert(calculateDueDate(submitDate, turnaroundTime) === dueDate)
    }
    it("should handle workinghour overflow correctly") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      assert(WEDNESDAY === DateTime.parse(submitDate).getDayOfWeek)
      val turnaroundTime = 1
      val dueDate = "2015-10-29T09:30:00.000+02:00"
      assert(calculateDueDate(submitDate, turnaroundTime) === dueDate)
    }
    it("should handle workday overflow correctly") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      assert(WEDNESDAY === DateTime.parse(submitDate).getDayOfWeek)
      val turnaroundTime = 17
      val dueDate = "2015-11-02T09:30:00.000+02:00"
      assert(calculateDueDate(submitDate, turnaroundTime) === dueDate)
    }
  }

  describe("The nextWorkingDay method") {
    it("should return Tuesday when Monday is entered") {
      val start = DateTime.parse("2015-10-26T16:30:00.000+02:00")
      assert(MONDAY === start.getDayOfWeek)
      val nextDay = nextWorkingDay(start)
      assert(nextDay.getDayOfWeek === TUESDAY)
    }
    it("should return Monday when Friday is entered") {
      val start = DateTime.parse("2015-10-30T16:30:00.000+02:00")
      assert(FRIDAY === start.getDayOfWeek)
      val nextDay = nextWorkingDay(start)
      assert(nextDay.getDayOfWeek === MONDAY)
    }
  }

  describe("The addWorkingDays method") {
    it("should add the number of working days to the start date") {
      val start = DateTime.parse("2015-10-26T16:30:00.000+02:00")
      assert(MONDAY === start.getDayOfWeek)
      val twoDaysLater = addWorkingDays(start, 2)
      assert(twoDaysLater.getDayOfWeek === WEDNESDAY)
    }
    it("should be aware of weekends") {
      val start = DateTime.parse("2015-10-30T16:30:00.000+02:00")
      assert(FRIDAY === start.getDayOfWeek)
      val oneDaysLater = addWorkingDays(start, 1)
      assert(oneDaysLater.getDayOfWeek === MONDAY)
      val seveDaysLater = addWorkingDays(start, 7)
      assert(seveDaysLater.getDayOfWeek === TUESDAY)
    }
  }

  describe("The addHours method") {
    it("should add the number of hours to the start date") {
      val start = DateTime.parse("2015-10-26T09:30:00.000+02:00")
      assert(MONDAY === start.getDayOfWeek)
      val twoHoursLater = addWorkingHours(start, 2)
      val expectedDateTime = DateTime.parse("2015-10-26T11:30:00.000+02:00")
      assert(twoHoursLater === expectedDateTime)
    }
    it("should be aware of end of work") {
      val start = DateTime.parse("2015-10-26T16:30:00.000+02:00")
      assert(MONDAY === start.getDayOfWeek)
      val twoHoursLater = addWorkingHours(start, 2)
      val expectedDateTime = DateTime.parse("2015-10-27T10:30:00.000+02:00")
      assert(twoHoursLater === expectedDateTime)
    }
    it("should be aware of end of week") {
      val start = DateTime.parse("2015-10-30T16:30:00.000+02:00")
      assert(FRIDAY === start.getDayOfWeek)
      val twoHoursLater = addWorkingHours(start, 2)
      val expectedDateTime = DateTime.parse("2015-11-02T10:30:00.000+02:00")
      assert(twoHoursLater === expectedDateTime)
    }
  }

  describe("The parseDate method") {
    it("should accept date in an ISO format") {
      val date = "2015-10-26T09:30:00.000+02:00"
      parseDate(date)
    }
    it("should throw IllegalArgument exception in case of an invalid date format") {
      val invalidDate: String = "not a date"
      val exception = intercept[IllegalArgumentException] {
        parseDate(invalidDate)
      }
      assert(exception.getMessage === """Invalid format: "not a date"""")
    }
    it("should throw IllegalArgument exception in case of an invalid date") {
      val zone = DateTimeZone.forID("Europe/Budapest")
      DateTimeZone.setDefault(zone)
      val invalidDate: String = "2015-03-29T02:59:00.000"
      val exception = intercept[IllegalArgumentException] {
        parseDate(invalidDate)
      }
      assert(exception.getMessage ===
        """Cannot parse "2015-03-29T02:59:00.000": Illegal instant due to time zone offset transition (Europe/Budapest)""")
    }
  }

  describe("The formatDate method") {
    it("should output yyyy-MM-ddThh:mm:ss.SSSZ format") {
      val date = parseDate("2015-10-26T09:30:00.000+02:00")
      val formatted = formatDate(date)
      assert(formatted === "2015-10-26T09:30:00.000+02:00")
    }
  }
}
