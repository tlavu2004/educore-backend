package com.tlavu.educore.auth.shared.application.service;

/**
 * Service for sending emails across the application
 * Handles user notifications, activation emails, and password-related communications
 */
public interface EmailService {

    /**
     * Send activation email to a newly created user
     *
     * @param email           The recipient's email address
     * @param username        The username/full name of the user
     * @param activationToken The activation token for account activation
     * @param defaultPassword The default password assigned to the user
     */
    void sendActivationEmail(
            String email,
            String username,
            String activationToken,
            String defaultPassword
    );

    /**
     * Send password changed notification
     *
     * @param email    The recipient's email address
     * @param username The username/full name of the user
     */
    void sendPasswordChangedNotification(String email, String username);
}

