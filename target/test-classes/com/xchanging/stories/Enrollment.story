Meta:
@capability customer-management
@rfc 1111
@scope (rfc 1111 or rel 3 or rel 4)
@suite sanity

Narrative:
In order to enrol a customer to enter data on the accont information screen
As a FI
I want to supply the name, mobile number, email, passcode and confirm passcode



Scenario: No 1 Successful enrolment without adding a card

Given I have launched the application
And I have accepted the terms and conditions and sms agreement
And I have entered <customer name>, <mobile number>, <email>, <passcode>, <confirm passcode>
And I have entered my HVC code
And I have entered the security questions
And I skip entering card details
When I verify email and login
Then I successfully finish enrolment process

Scenario: No 2 Login


Given I have already enroled
And I have relaunched the application
When I enter the passcode
Then I login to the app successfully

Scenario: No 3 Add Card

Given I have already enroled
And I have launched and login to the application
When I enter the card details
Then I should be able to add card successfully

