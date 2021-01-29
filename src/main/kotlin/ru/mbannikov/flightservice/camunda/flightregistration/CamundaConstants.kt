package ru.mbannikov.flightservice.camunda.flightregistration

object CamundaConstants {
    const val PROCESS_KEY = "FlightRegistration"

    const val ORDER_CONFIRMED_MESSAGE = "OrderConfirmedMessage"
    const val GOT_AUTHORIZE_DECISION_MESSAGE = "GotAuthorizeDecisionMessage"

    const val BOOKING_TOPIC = "Booking"
    const val SEND_AUTHORIZATION_PUSH_NOTIFICATION_TOPIC = "SendAuthorizationPushNotification"
    const val SEND_AUTHORIZATION_EMAIL_TOPIC = "SendAuthorizationEmail"
    const val CHECK_BOOKING_EXPIRATION_TOPIC = "CheckBookingExpiration"
    const val REEBOOKING_TOPIC = "Rebooking"
    const val PAYMENT_TOPIC = "Payment"

    const val ORDER_ID_CONTEXT_VARIABLE = "orderId"
    const val NEED_AUTHORIZATION_CONTEXT_VARIABLE = "needAuthorization"
    const val IS_AUTHORIZED_CONTEXT_VARIABLE = "isAuthorized"
    const val IS_BOOKING_EXPIRED_VARIABLE = "isBookingExpired"
}