package hu.emarsys.duedate

import org.scalatest.FunSpec

import hu.emarsys.duedate.Util.{calculateDueDate, nextWorkingDay, addWorkingDays, addWorkingHours}
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants._

class UtilSpec extends FunSpec {

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

  // todo: daylight saving tests
}
