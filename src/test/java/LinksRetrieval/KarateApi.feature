Feature: End to End Api testing feature
 Background: Generating token
    * url 'https://restful-booker.herokuapp.com'
    Given path '/auth'
    And request
    """
    {
    "username" : "admin",
    "password" : "password123"
}
    """
    And header Content-Type = 'application/json'
    When method post
    Then status 200
    * def tokenValue = response.token
    * print 'value of the token is '+ tokenValue


    Scenario: Getting all the booking id's
       Given path '/booking'
       When method GET
       Then status 200
       * print response


   Scenario: Getting single booking
          Given path '/booking'
          And path '/1'
          And header Accept = 'application/json'
          When method GET
          Then status 200
          * def userFirstName = response.firstname
          * def userLastName = response.lastname
          * print userFirstName + userLastName

  Scenario: Create Booking Validation
    Given path '/booking'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    * def payload = read('createBookingPayload.json')
    And request payload

    When method post
    Then status 200
    * def BookingId = response.bookingid
    * print BookingId
    * print response
    * match response.booking.firstname == 'Pushpendra'
    * match response.booking.lastname == 'Singh'

      Scenario: Update Booking Validation

        Given path '/booking'
        And path '/1'
        And header Content-Type = 'application/json'
        And header Accept = 'application/json'
        And header Cookie = 'token='+tokenValue
        * def payload = read('updateBookingPayload.json')
        And request payload
        When method put
        Then status 200
        * print response
        * print response.additionalneeds
        * match response.additionalneeds == 'lunch'

        Scenario: Partially updating the booking details
          Given path '/booking'
          And path '/1'
          And header Content-Type = 'application/json'
          And header Accept = 'application/json'
          And header Cookie = 'token='+tokenValue
          And request
          """
          {
    "firstname" : "Rishabh",
    "lastname" : "Singh"
}
          """
          When method patch
          Then status 200
          * print response
          * match response.firstname == 'Rishabh'

Scenario: Booking deletion validation
  Given path '/booking'
  And path '/3866'
  And header Content-Type = 'application/json'
  And header Cookie = 'token='+tokenValue
  When method delete
  Then status 201










